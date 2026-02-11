package ussr.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ussr.playlistmaker.ui.tracks.ItunesTrackAdapter
import ussr.playlistmaker.data.network.ItunesSearchApiService
import ussr.playlistmaker.data.dto.ItunesSearchResponse
import ussr.playlistmaker.domain.api.SearchHistoryInteractor
import ussr.playlistmaker.domain.api.TracksInteractor
import ussr.playlistmaker.domain.models.Track
import ussr.playlistmaker.storages.SearchHistory
import ussr.playlistmaker.ui.media.PlayerActivity
import java.time.Instant

class SearchActivity : AppCompatActivity() {
    private var searchBarValue: CharSequence? = SEARCHBAR_VALUE_DEFAULT
    private val handler = Handler(Looper.getMainLooper())
    private var searchItemClickAllowed = true

    private lateinit var historyAdapter: ItunesTrackAdapter
    private lateinit var resultsAdapter: ItunesTrackAdapter

    private lateinit var searchProgressBar: ProgressBar
    private lateinit var tracksView: RecyclerView
    private lateinit var rootView: ScrollView
    private lateinit var placeholderTextView: TextView
    private lateinit var searchBar: EditText
    private lateinit var clearButton: ImageView
    private lateinit var refreshButton: Button

    private val searchRunnable = Runnable {
        doSearch(searchBarValue.toString(), false)
    }

    private fun isSearchItemClickAllowed() : Boolean {
        val current = searchItemClickAllowed
        if (searchItemClickAllowed) {
            searchItemClickAllowed = false
            handler.postDelayed({ searchItemClickAllowed = true }, SEARCHLIST_DEBOUNCE_DELAY)
        }
        return current
    }
    private fun setPlaceholderMessage(message: String = "", isError: Boolean = false) {
        val placeholderView = findViewById<LinearLayout>(R.id.error_placeholder)
        searchProgressBar.isVisible = false
        if (message == "") {
            placeholderView.isVisible = false
            tracksView.isVisible = true
            return
        } else {
            placeholderView.isVisible = true
            tracksView.isVisible = false
        }
        placeholderTextView.text = message
        val icon = findViewById<ImageView>(R.id.placeholder_icon)
        val btn = findViewById<Button>(R.id.placeholder_refresh_button)
        btn.isVisible = isError
        if (isError) {
            icon.setImageResource(R.drawable.no_connection_icon)
        } else {
            icon.setImageResource(R.drawable.no_items_icon)
        }
    }

    private fun doSearch(request: String, wasBeenCleared: Boolean = false) {
        setPlaceholderMessage("")
        searchProgressBar.isVisible = true
        tracksView.isVisible = false

        tracksInteractor.searchTracks(request, object: TracksInteractor.TracksConsumer {
            override fun consume(foundTracks: List<Track>) {
                runOnUiThread {

                    if (foundTracks.isEmpty()) {
                        setPlaceholderMessage(getString(R.string.any_not_found))
                        return@runOnUiThread
                    }

                    if (wasBeenCleared) {
                        tracksView.isVisible = false
                        searchProgressBar.isVisible = false
                        return@runOnUiThread
                    }

                    resultsAdapter.updateTracks(foundTracks.toMutableList())
                    searchProgressBar.isVisible = false
                    tracksView.isVisible = true
                }
            }
        })
    }
    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCHBAR_DEBOUNCE_DELAY)
    }
    private fun refreshHistory() {
        val items = history.get()
        rootView.isVisible = items.isNotEmpty()
        //historyAdapter.updateTracks(items.toMutableList())
    }

    private lateinit var tracksInteractor: TracksInteractor
    private lateinit var searchHistoryInteractor: SearchHistoryInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        tracksInteractor = Creator.provideTracksInteractor()
        searchHistoryInteractor = Creator.provideSearchHistoryInteractor((applicationContext as PlaylistMakerApp).sharedPreferences)

        searchProgressBar = findViewById<ProgressBar>(R.id.searchProgressBar)
        tracksView = findViewById<RecyclerView>(R.id.tracksRecyclerView)
        rootView = findViewById<ScrollView>(R.id.tracksSearchHistory)
        placeholderTextView = findViewById<TextView>(R.id.error_description)
        searchBar = findViewById<EditText>(R.id.search_bar)
        clearButton = findViewById<ImageView>(R.id.search_bar_clear_text)
        refreshButton = findViewById<Button>(R.id.placeholder_refresh_button)

//        historyAdapter = ItunesTrackAdapter(history.get().toMutableList()) { track ->
//            history.add(track)
//            historyAdapter.updateTracks(history.get().toMutableList())
//
//            if(isSearchItemClickAllowed()) {
//                val playerIntent = Intent(this, PlayerActivity::class.java)
//                playerIntentputExtra("track", track)
//                startActivity(playerIntent)
//            }
//        }

        resultsAdapter = ItunesTrackAdapter(mutableListOf()) { track ->
            //history.add(track)
            //historyAdapter.updateTracks(history.get().toMutableList())

            if(isSearchItemClickAllowed()) {
                val playerIntent = Intent(this, PlayerActivity::class.java)
                //playerIntent.putExtra("track", track)
                startActivity(playerIntent)
            }
        }

        //findViewById<RecyclerView>(R.id.tracksHistoryRecyclerView).adapter = historyAdapter
        tracksView.adapter = resultsAdapter

        if (savedInstanceState != null) {
            searchBarValue = savedInstanceState.getCharSequence(SEARCHBAR, SEARCHBAR_VALUE_DEFAULT)
            searchBar.setText(searchBarValue)
        }

        findViewById<android.widget.Toolbar>(R.id.main_toolbar).setNavigationOnClickListener {
            finish()
        }
        findViewById<Button>(R.id.tracksHistoryClear).setOnClickListener {
            history.clear()
            refreshHistory()
        }

        clearButton.setOnClickListener {
            searchBar.setText("")
            doSearch("", true)
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchBar.windowToken, 0)
        }

        refreshButton.setOnClickListener {
            doSearch(searchBar.text.toString())
        }
        searchBar.setOnFocusChangeListener { _, hasFocus ->
            val historyIsVisible = hasFocus && searchBar.text.isEmpty()
            rootView.isVisible = historyIsVisible
            if (historyIsVisible)
                refreshHistory()
        }
        searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                handler.removeCallbacks(searchRunnable)
                doSearch(searchBar.text.toString())
            }
            false
        }

        val searchBarTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchBarValue = s
                clearButton.isVisible = !s.isNullOrEmpty()

                val historyIsVisible = searchBar.hasFocus() && s?.isEmpty() == true
                rootView.isVisible = historyIsVisible
                if (historyIsVisible) {
                    refreshHistory()
                }
                if (s?.isEmpty() == true) {
                    doSearch("", true)
                }else{
                    searchDebounce()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }

        searchBar.addTextChangedListener(searchBarTextWatcher)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(SEARCHBAR, searchBarValue.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchBarValue = savedInstanceState.getString(SEARCHBAR, SEARCHBAR_VALUE_DEFAULT)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(searchRunnable)
    }

    companion object {
        const val SEARCHBAR = "SEARCHBAR"
        const val SEARCHBAR_VALUE_DEFAULT = ""
        const val SEARCHBAR_DEBOUNCE_DELAY = 2000L
        const val SEARCHLIST_DEBOUNCE_DELAY = 1000L
    }

}