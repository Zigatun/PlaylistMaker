package ussr.playlistmaker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ussr.playlistmaker.R
import ussr.playlistmaker.models.ItunesTrack
import ussr.playlistmaker.viewholders.ItunesTrackViewHolder

class ItunesTrackAdapter(private val tracks:List<ItunesTrack>) : RecyclerView.Adapter<ItunesTrackViewHolder>() {
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
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}