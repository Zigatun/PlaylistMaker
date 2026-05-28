package ussr.playlistmaker.media.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ussr.playlistmaker.databinding.ListItemPlaylistCardBinding
import ussr.playlistmaker.playlist.data.models.PlaylistModel

class PlaylistAdapter: RecyclerView.Adapter<PlaylistViewHolder>() {
    private val playlists = mutableListOf<PlaylistModel>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemPlaylistCardBinding.inflate(inflater, parent,false)
        return PlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PlaylistViewHolder,
        position: Int
    ) {
        holder.bind(playlists[position])
    }

    override fun getItemCount(): Int = playlists.size

    fun setList(items: List<PlaylistModel>){
        playlists.clear()
        playlists.addAll(items)
        notifyDataSetChanged()
    }
}