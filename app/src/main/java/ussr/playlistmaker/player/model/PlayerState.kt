package ussr.playlistmaker.player.model

sealed class PlayerState(val isInPlayMode: Boolean, val progress: String, var isInFavorites: Boolean){
    class Default(isInFavorites: Boolean) : PlayerState(false, "00:00", isInFavorites)
    class Prepared(isInFavorites: Boolean) : PlayerState(false, "00:00", isInFavorites)
    class Playing(progress: String, isInFavorites: Boolean) : PlayerState(true, progress, isInFavorites)
    class Paused(progress: String, isInFavorites: Boolean) : PlayerState(false, progress, isInFavorites)
}