package teach.meskills.lastfm

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AudioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val nameView = view.findViewById<TextView>(R.id.name)
    val artist = view.findViewById<TextView>(R.id.artist)
    val imageView = view.findViewById<ImageView>(R.id.image)


    fun input(audio: AudioEntity) {
        nameView.text = audio.name
        artist.text = audio.artist
        Glide.with(imageView.context)
            .load(audio.image)
            .into(imageView)
    }
}