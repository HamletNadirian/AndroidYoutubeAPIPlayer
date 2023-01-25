package nadirian.hamlet.android.androidyoutubeapiplayer.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import nadirian.hamlet.android.androidyoutubeapiplayer.PlayVideoOfYoutube
import nadirian.hamlet.android.androidyoutubeapiplayer.R
import nadirian.hamlet.android.androidyoutubeapiplayer.model.Items

class RecyclerviewItemAdapter internal constructor(
    private val itemsList: ArrayList<Items>,
    var context: Context
) :
    RecyclerView.Adapter<RecyclerviewItemAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_header, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemsList[position]
        holder.name.text = item.title
        holder.channelTitle.text = item.channelTitle
        Picasso.get().load(item.url).into(holder.imageView)
        holder.name.setOnClickListener {
            val intent = Intent(context, PlayVideoOfYoutube::class.java)
            intent.putExtra("videoid", item.videoId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.tvName)
        var channelTitle: TextView = itemView.findViewById(R.id.channelTitle)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }
}
