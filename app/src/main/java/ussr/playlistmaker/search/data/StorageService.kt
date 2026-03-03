package ussr.playlistmaker.search.data

interface StorageService {
    fun get(key: String): String
    fun put(key: String, payload: String)
}