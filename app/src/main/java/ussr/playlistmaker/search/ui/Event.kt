package ussr.playlistmaker.search.ui

class Event<out T>(private val content: T) {
    private var handled = false

    fun get(): T? {
        return if (handled) null
        else {
            handled = true
            content
        }
    }
}