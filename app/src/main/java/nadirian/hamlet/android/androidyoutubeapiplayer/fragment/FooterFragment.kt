package nadirian.hamlet.android.androidyoutubeapiplayer.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.NonDisposableHandle.parent
import nadirian.hamlet.android.androidyoutubeapiplayer.R
import nadirian.hamlet.android.androidyoutubeapiplayer.adapter.RecyclerviewItemAdapter
import nadirian.hamlet.android.androidyoutubeapiplayer.adapter.RecyclerviewItemAdapterFooter
import nadirian.hamlet.android.androidyoutubeapiplayer.model.Items
import org.json.JSONException
import org.json.JSONObject


class FooterFragment : Fragment() {

    private var listFooter: ArrayList<Items> = arrayListOf()
    private lateinit var recyclerViewAdapterFooter: RecyclerviewItemAdapterFooter
    lateinit var recycleViewFooter: RecyclerView
    val url =
        "https://www.googleapis.com/youtube/v3/search?part=snippet&channelId=UCxrxwFTBU3DTJ9Y5TKeW7KA&maxResults=6&key=YOUR_KEY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_footer, container, false)
        recycleViewFooter = view?.findViewById<View>(R.id.recycleViewFooter) as RecyclerView
        recyclerViewAdapterFooter = context?.let { RecyclerviewItemAdapterFooter(listFooter, it) }!!
        val layoutManagerFooter: RecyclerView.LayoutManager = GridLayoutManager(context, 3)
        recycleViewFooter.layoutManager = layoutManagerFooter
        recycleViewFooter.adapter = recyclerViewAdapterFooter
        fetchData(recyclerViewAdapterFooter, url, listFooter)
        return view
    }

    @SuppressLint("NotifyDataSetChanged", "SuspiciousIndentation")
    fun fetchData(
        recyclerviewItemAdapterFooter: RecyclerviewItemAdapterFooter,
        URL: String,
        list: ArrayList<Items>
    ) {
        val requestQueue = Volley.newRequestQueue(requireActivity().applicationContext)
        val stringRequest = StringRequest(Request.Method.GET, URL,
            { response ->
                try {
                    val obj = JSONObject(response)
                    val jsonArray = obj.getJSONArray("items")
                    for (i in 0 until jsonArray.length()) {
                        val jsonObjectArr = jsonArray.getJSONObject(i)
                        val jsonId = jsonObjectArr.getJSONObject("id")
                        val jsonSnippet = jsonObjectArr.getJSONObject("snippet")
                        val jsonThumbnail = jsonSnippet.getJSONObject("thumbnails")
                            .getJSONObject("medium")
                        val items = Items()
                        if (i != 1 && i != 2 && jsonId.has("videoId")) {
                            items.videoId = jsonId.getString("videoId")
                            items.title = jsonSnippet.getString("title")
                            items.url = jsonThumbnail.getString("url")
                            if (jsonSnippet.has("channelTitle")) {
                                items.channelTitle = jsonSnippet.getString("channelTitle")
                            }
                            list.add(items)
                        }
                    }
                    if (list.size > 0)
                        recyclerviewItemAdapterFooter.notifyDataSetChanged()

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                Toast.makeText(
                    requireActivity().applicationContext,
                    error.message,
                    Toast.LENGTH_SHORT
                ).show()

            })
        requestQueue.add(stringRequest)
    }

}
