package ussr.playlistmaker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ussr.playlistmaker.databinding.ListItemPlaylistCardHorizontalBinding
import ussr.playlistmaker.playlist.data.models.PlaylistModel

class PlaylistHorizontalCardAdapter(private val onItemClick: (PlaylistModel) -> Unit
) : RecyclerView.Adapter<PlaylistHorizontalCardViewHolder>() {

    private val playlists = mutableListOf<PlaylistModel>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistHorizontalCardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemPlaylistCardHorizontalBinding.inflate(inflater, parent, false)
        return PlaylistHorizontalCardViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PlaylistHorizontalCardViewHolder,
        position: Int
    ) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
             onItemClick(playlists[position])
        }
    }

    fun setList(items: List<PlaylistModel>){
        playlists.clear()
        playlists.addAll(items)
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return playlists.size
    }
}