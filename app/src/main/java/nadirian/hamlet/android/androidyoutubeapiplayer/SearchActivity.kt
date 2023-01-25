package nadirian.hamlet.android.androidyoutubeapiplayer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import nadirian.hamlet.android.androidyoutubeapiplayer.adapter.RecyclerviewItemAdapterSearch
import nadirian.hamlet.android.androidyoutubeapiplayer.model.Items
import org.json.JSONException
import org.json.JSONObject


class SearchActivity : AppCompatActivity() {

    private lateinit var recyclerViewAdapterSearch: RecyclerviewItemAdapterSearch
    lateinit var recycleViewSearch: RecyclerView
    private var listSearch: ArrayList<Items> = arrayListOf()
    lateinit var editText: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        editText = findViewById<TextView>(R.id.edit_text_search) as EditText

        val intent = intent
        var str = intent.getStringExtra("message_key")

        recycleViewSearch = findViewById<View>(R.id.recycleViewSearch) as RecyclerView
        recyclerViewAdapterSearch = RecyclerviewItemAdapterSearch(listSearch, this)
        val layoutManagerSearch: RecyclerView.LayoutManager = GridLayoutManager(this, 3)
        recycleViewSearch.layoutManager = layoutManagerSearch
        recycleViewSearch.adapter = recyclerViewAdapterSearch

        if (str != null) {
            fetchData(recyclerViewAdapterSearch, listSearch, str)
        }

        editText.onDone {
            if (str != null) {
                var listSearch: ArrayList<Items> = arrayListOf()
                var recycleViewSearch = findViewById<View>(R.id.recycleViewSearch) as RecyclerView
                var recyclerViewAdapterSearch = RecyclerviewItemAdapterSearch(listSearch, this)
                val layoutManagerFooter: RecyclerView.LayoutManager = GridLayoutManager(this, 3)
                recycleViewSearch.layoutManager = layoutManagerFooter
                recycleViewSearch.adapter = recyclerViewAdapterSearch
                var textForSearch = editText.text.toString()

                fetchData(recyclerViewAdapterSearch,listSearch,textForSearch)
            }
/*

            editText.onDone {
                var str = editText.text.toString()

                intent.putExtra("message_key", str)
                startActivity(intent) }

            */

        }
        /*button = findViewById(R.id.buttonS)
        var str2 = editText.text.toString()

        button.setOnClickListener {
            val listFooter: ArrayList<Items> = arrayListOf()
            val recycleViewFooter = findViewById<View>(R.id.recycleViewSearch) as RecyclerView
            val recyclerViewAdapterSearch = RecyclerviewItemAdapterSearch(listFooter, this)
            val layoutManagerSearch: RecyclerView.LayoutManager = GridLayoutManager(this, 3)
            recycleViewFooter.layoutManager = layoutManagerSearch
            recycleViewFooter.adapter = recyclerViewAdapterSearch
            fetchData(recyclerViewAdapterSearch, listFooter, str2)
        }*/

  }
    fun EditText.onDone(callback: () -> Unit) {
        setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                callback.invoke()
                return@setOnEditorActionListener true
            }
            false
        }
    }


  @SuppressLint("NotifyDataSetChanged", "SuspiciousIndentation")
  fun fetchData(
      recyclerViewAdapterSearch: RecyclerviewItemAdapterSearch,
      list: ArrayList<Items>,
      string: String
  ) {
      val URL =
          "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=9&q=$string&type=video&key=AIzaSyDoVdfda3x50zuLxU9n-zZunBn_elMKddc"
      val requestQueue = Volley.newRequestQueue(applicationContext)
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
                      recyclerViewAdapterSearch.notifyDataSetChanged()

              } catch (e: JSONException) {
                  e.printStackTrace()
              }
          },
          { error ->
              Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
          })
      requestQueue.add(stringRequest)
  }
}