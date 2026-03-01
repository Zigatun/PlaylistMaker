package ussr.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ussr.playlistmaker.databinding.ActivityTrackCardBinding
import ussr.playlistmaker.search.models.Track

class ItunesTrackAdapter(private val onItemClick: (Track) -> Unit
) : RecyclerView.Adapter<ItunesTrackViewHolder>() {

    private val tracks = mutableListOf<Track>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItunesTrackViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ActivityTrackCardBinding.inflate(inflater, parent, false)
        return ItunesTrackViewHolder(binding)
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

    fun setList(items: List<Track>){
        tracks.clear()
        tracks.addAll(items)
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return tracks.size
    }
}