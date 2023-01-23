package nadirian.hamlet.android.androidyoutubeapiplayer
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    val url1 = "https://www.googleapis.com/youtube/v3/search?part=snippet&channelId=UCe-XYtlVB58YcMCNnaq0nvA&maxResults=30&key=AIzaSyAWrQKBw0cZ8z9p3yLDbaEpCoogD9EkLKI"
  //  CHANNELShttps://youtube.googleapis.com/youtube/v3/channels?part=snippet&id=UC_x5XG1OV2P6uZZ5FSM9Ttw&key=AIzaSyAWrQKBw0cZ8z9p3yLDbaEpCoogD9EkLKI
    // val url = "https://www.googleapis.com/youtube/v3/search?part=snippet&channelId=UCe-XYtlVB58YcMCNnaq0nvA&maxResults=30&key=AIzaSyCUNXyBXNoNYgTcLC7jSVHugOKIfHj7mJU"
    //var url = "https://youtube.googleapis.com/youtube/v3/playlistItems?part=snippet%2CcontentDetails&maxResults=25&playlistId=PLBCF2DAC6FFB574DE&key=AIzaSyCUNXyBXNoNYgTcLC7jSVHugOKIfHj7mJU"//playlist
  // val url = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=20&q=Eminem&type=video&key=AIzaSyCUNXyBXNoNYgTcLC7jSVHugOKIfHj7mJU"
    val url = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=20&q=&type=video&key=AIzaSyAWrQKBw0cZ8z9p3yLDbaEpCoogD9EkLKI"

    //val url = "https://youtube.googleapis.com/youtube/v3/playlistItems?part=snippet%2CcontentDetails&maxResults=25&playlistId=PLBCF2DAC6FFB574DE&key=AIzaSyCUNXyBXNoNYgTcLC7jSVHugOKIfHj7mJU"//playlist



    private lateinit var recyclerViewAdapter:RecyclerviewItemAdapter
    lateinit var recyclerView: RecyclerView
    private var list: ArrayList<Items> = arrayListOf()

    private var listFooter: ArrayList<Items> = arrayListOf()
    private lateinit var recyclerViewAdapterFooter:RecyclerviewItemAdapterFooter
    lateinit var recycleViewFooter: RecyclerView


    private lateinit var recyclerViewAdapterSearch:RecyclerviewItemAdapterFooter
    lateinit var recycleViewSearch: RecyclerView
    private var listSearch: ArrayList<Items> = arrayListOf()

    lateinit var editText: EditText

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById<View>(R.id.recycleView) as RecyclerView
        recyclerViewAdapter = RecyclerviewItemAdapter(list,this)
        val layoutManager:RecyclerView.LayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = recyclerViewAdapter
        fetchData(recyclerViewAdapter,url1,list)

     recycleViewFooter = findViewById<View>(R.id.recycleViewFooter) as RecyclerView
        recyclerViewAdapterFooter = RecyclerviewItemAdapterFooter(listFooter,this)
        val layoutManagerFooter:RecyclerView.LayoutManager =  GridLayoutManager(this, 3)
        recycleViewFooter.layoutManager = layoutManagerFooter
        recycleViewFooter.adapter = recyclerViewAdapterFooter
        fetchData(recyclerViewAdapterFooter,url,listFooter)
       editText = findViewById(R.id.edit_text)
        editText.setOnClickListener {
            recycleViewSearch = findViewById<View>(R.id.recycleViewFooter) as RecyclerView
            recyclerViewAdapterSearch = RecyclerviewItemAdapterFooter(listSearch,this)
             var layoutManagerSearch:RecyclerView.LayoutManager =  GridLayoutManager(this, 3)
            recycleViewSearch.layoutManager = layoutManagerSearch
            recycleViewSearch.adapter = recyclerViewAdapterSearch
            var string = editText.text.toString()
            fetchData(recyclerViewAdapterSearch,listSearch,string)
        }

    }

    @SuppressLint("NotifyDataSetChanged", "SuspiciousIndentation")
    fun fetchData(recyclerViewAdapter:RecyclerviewItemAdapter,URL:String, list: ArrayList<Items> ) {
        val requestQueue = Volley.newRequestQueue(applicationContext)
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
                        if (i!=1&&i!=2&&jsonId.has("videoId")){
                            items.videoId = jsonId.getString("videoId")
                            items.title = jsonSnippet.getString("title")
                            items.url = jsonThumbnail.getString("url")
                            if (jsonSnippet.has("channelTitle")){
                                items.channelTitle = jsonSnippet.getString("channelTitle")
                            }

                            list.add(items)
                        }
                    }
                    if (list.size>0)
                        recyclerViewAdapter.notifyDataSetChanged()

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { /*error ->
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()*/
            })
        requestQueue.add(stringRequest)
    }
    @SuppressLint("NotifyDataSetChanged", "SuspiciousIndentation")
    fun fetchData(recyclerviewItemAdapterFooter: RecyclerviewItemAdapterFooter,URL:String, list: ArrayList<Items>) {
        val requestQueue = Volley.newRequestQueue(applicationContext)
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
                        if (i!=1&&i!=2&&jsonId.has("videoId")){
                            items.videoId = jsonId.getString("videoId")
                            items.title = jsonSnippet.getString("title")
                            items.url = jsonThumbnail.getString("url")
                            if (jsonSnippet.has("channelTitle")){
                                items.channelTitle = jsonSnippet.getString("channelTitle")
                            }
                            list.add(items)
                        }
                    }
                    if (list.size>0)
                        recyclerviewItemAdapterFooter.notifyDataSetChanged()

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            {
                   error -> Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()

            })
        requestQueue.add(stringRequest)
    }

    @SuppressLint("NotifyDataSetChanged", "SuspiciousIndentation")
    fun fetchData(recyclerviewItemAdapterFooter: RecyclerviewItemAdapterFooter, list: ArrayList<Items>,string:String) {
        val URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=20&q=$string&type=video&key=AIzaSyAWrQKBw0cZ8z9p3yLDbaEpCoogD9EkLKI"

        val requestQueue = Volley.newRequestQueue(applicationContext)
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
                        if (i!=1&&i!=2&&jsonId.has("videoId")){
                            items.videoId = jsonId.getString("videoId")
                            items.title = jsonSnippet.getString("title")
                            items.url = jsonThumbnail.getString("url")
                            if (jsonSnippet.has("channelTitle")){
                                items.channelTitle = jsonSnippet.getString("channelTitle")
                            }
                            list.add(items)
                        }
                    }
                    if (list.size>0)
                        recyclerviewItemAdapterFooter.notifyDataSetChanged()

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
