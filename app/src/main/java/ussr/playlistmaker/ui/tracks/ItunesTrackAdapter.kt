package ussr.playlistmaker.ui.tracks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ussr.playlistmaker.R
import ussr.playlistmaker.data.dto.TrackDto
import ussr.playlistmaker.domain.models.Track

class ItunesTrackAdapter(private var tracks: MutableList<Track>, private val onItemClick: (Track) -> Unit
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
    fun updateTracks(newTracks: MutableList<Track>) {
        tracks = newTracks
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return tracks.size
    }
}