package ussr.playlistmaker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ussr.playlistmaker.R
import ussr.playlistmaker.models.ItunesTrack
import ussr.playlistmaker.viewholders.ItunesTrackViewHolder

class ItunesTrackAdapter(private var tracks: MutableList<ItunesTrack>, private val onItemClick: (ItunesTrack) -> Unit
) : RecyclerView.Adapter<ItunesTrackViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItunesTrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_track_card, parent, false)
        return ItunesTrackViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ItunesTrackViewHolder,
        position: Int
    ) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
             onItemClick(tracks[position])
        }
    }
    fun updateTracks(newTracks: MutableList<ItunesTrack>) {
        tracks = newTracks
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return tracks.size
    }
}