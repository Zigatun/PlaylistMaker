package ussr.playlistmaker.storages

import android.content.SharedPreferences
import com.google.gson.Gson
import ussr.playlistmaker.models.ItunesTrack

private const val PLAYLISTMAKER_SEARCH_HISTORY = "playlistmaker_search_history"
private const val PLAYLISTMAKER_HISTORY_LIMIT = 10
class SearchHistory(val storage: SharedPreferences) {
    private fun commit(target: ArrayDeque<ItunesTrack>){
        val json = Gson().toJson(target)

        storage.edit().putString(PLAYLISTMAKER_SEARCH_HISTORY, json).apply()
    }
    fun get() : ArrayDeque<ItunesTrack>{
        val out = ArrayDeque<ItunesTrack>()
        val json = storage.getString(PLAYLISTMAKER_SEARCH_HISTORY, "")
        val memory = Gson().fromJson(json, Array<ItunesTrack>::class.java)?:emptyArray()
        out.addAll(memory)
        return out
    }
    fun add(track: ItunesTrack){
        val tracks = get()
        if (!tracks.any{t->t.trackId == track.trackId}){
            tracks.addFirst(track)
        }
        else
        {
            tracks.removeIf { t -> t.trackId == track.trackId }
            tracks.addFirst(track)
        }
        if(tracks.count() > PLAYLISTMAKER_HISTORY_LIMIT)
            tracks.removeLast()
        commit(tracks)
    }

    public fun clear(){
        commit(ArrayDeque())
    }
}