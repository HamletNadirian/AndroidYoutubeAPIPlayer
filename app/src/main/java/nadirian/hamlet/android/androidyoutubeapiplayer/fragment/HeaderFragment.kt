package nadirian.hamlet.android.androidyoutubeapiplayer.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import nadirian.hamlet.android.androidyoutubeapiplayer.R
import nadirian.hamlet.android.androidyoutubeapiplayer.adapter.RecyclerviewItemAdapter
import nadirian.hamlet.android.androidyoutubeapiplayer.model.Items
import org.json.JSONException
import org.json.JSONObject

class HeaderFragment : Fragment() {

    private lateinit var recyclerViewAdapter: RecyclerviewItemAdapter
    lateinit var recyclerView: RecyclerView
    private var list: ArrayList<Items> = arrayListOf()
    val url1 =
        "https://www.googleapis.com/youtube/v3/search?part=snippet&channelId=UCh-4c1ZoBPNP1C8c-7iVvUQ&maxResults=6&key=AIzaSyCSMatZ9pufDtGa6mULtmfEZG-b30MKy88"
    //val url1 = "https://www.googleapis.com/youtube/v3/search?part=snippet&channelId=UCe-XYtlVB58YcMCNnaq0nvA&maxResults=9&key=AIzaSyDoVdfda3x50zuLxU9n-zZunBn_elMKddc"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_header, container, false)
        recyclerView = view?.findViewById<View>(R.id.recycleView) as RecyclerView
        recyclerViewAdapter = RecyclerviewItemAdapter(list, requireContext())
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = recyclerViewAdapter
        fetchData(recyclerViewAdapter, url1, list)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    @SuppressLint("NotifyDataSetChanged", "SuspiciousIndentation")
    fun fetchData(
        recyclerViewAdapter: RecyclerviewItemAdapter,
        URL: String,
        list: ArrayList<Items>
    ) {
        val requestQueue = Volley.newRequestQueue(context)
        val stringRequest = StringRequest(
            Request.Method.GET, URL,
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
                        recyclerViewAdapter.notifyDataSetChanged()

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