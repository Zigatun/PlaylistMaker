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
import ussr.playlistmaker.storages.SearchHistory
import ussr.playlistmaker.ui.media.PlayerActivity
import java.time.Instant

class SearchActivity : AppCompatActivity() {
    private var searchBarValue: CharSequence? = SEARCHBAR_VALUE_DEFAULT
    private val handler = Handler(Looper.getMainLooper())
    private var searchItemClickAllowed = true
    private lateinit var history: SearchHistory
    private lateinit var historyAdapter: ItunesTrackAdapter
    private lateinit var resultsAdapter: ItunesTrackAdapter
    private val searchRunnable = Runnable {
        doSearch(searchBarValue.toString(), false)
    }


    private val itunesSearchApiService = retrofit.create<ItunesSearchApiService>()

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
        val tracksView = findViewById<RecyclerView>(R.id.tracksRecyclerView)
        findViewById<ProgressBar>(R.id.searchProgressBar).isVisible = false
        if (message == "") {
            placeholderView.isVisible = false
            tracksView.isVisible = true
            return
        } else {
            placeholderView.isVisible = true
            tracksView.isVisible = false
        }
        findViewById<TextView>(R.id.error_description).text = message
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
        findViewById<ProgressBar>(R.id.searchProgressBar).isVisible = true
        findViewById<RecyclerView>(R.id.tracksRecyclerView).isVisible = false
        itunesSearchApiService.search(request)
            .enqueue(object : Callback<ItunesSearchResponse> {
                override fun onResponse(
                    call: Call<ItunesSearchResponse?>,
                    response: Response<ItunesSearchResponse?>
                ) {
                    if (response.isSuccessful) {
                        if (wasBeenCleared) {
                            findViewById<RecyclerView>(R.id.tracksRecyclerView).isVisible = false
                            findViewById<ProgressBar>(R.id.searchProgressBar).isVisible = false
                            return
                        }
                        val respObjects = response.body()?.results
                        if (respObjects != null && respObjects.isNotEmpty()) {
                            resultsAdapter.updateTracks(respObjects.toMutableList())
                            findViewById<ProgressBar>(R.id.searchProgressBar).isVisible = false
                            findViewById<RecyclerView>(R.id.tracksRecyclerView).isVisible = true
                        } else {
                            setPlaceholderMessage(getString(R.string.any_not_found))
                        }
                    }
                }

                override fun onFailure(
                    call: Call<ItunesSearchResponse?>,
                    t: Throwable
                ) {
                    if (wasBeenCleared) {
                        setPlaceholderMessage("")
                        return
                    }
                    setPlaceholderMessage(
                        getString(R.string.connection_troubles) + "\n\n" + getString(
                            R.string.trouble_no_internet
                        ), true
                    )
                }

            })
    }
    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCHBAR_DEBOUNCE_DELAY)
    }
    private fun refreshHistory() {
        val items = history.get()
        val root = findViewById<ScrollView>(R.id.tracksSearchHistory)
        root.isVisible = items.isNotEmpty()
        historyAdapter.updateTracks(items.toMutableList())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchBar = findViewById<EditText>(R.id.search_bar)
        val clearButton = findViewById<ImageView>(R.id.search_bar_clear_text)
        val refreshButton = findViewById<Button>(R.id.placeholder_refresh_button)
        history = SearchHistory((applicationContext as PlaylistMakerApp).sharedPreferences)

        historyAdapter = ItunesTrackAdapter(history.get().toMutableList()) { track ->
            history.add(track)
            historyAdapter.updateTracks(history.get().toMutableList())

            if(isSearchItemClickAllowed()) {
                val playerIntent = Intent(this, PlayerActivity::class.java)
                playerIntent.putExtra("track", track)
                startActivity(playerIntent)
            }
        }

        resultsAdapter = ItunesTrackAdapter(mutableListOf()) { track ->
            history.add(track)
            historyAdapter.updateTracks(history.get().toMutableList())

            if(isSearchItemClickAllowed()) {
                val playerIntent = Intent(this, PlayerActivity::class.java)
                playerIntent.putExtra("track", track)
                startActivity(playerIntent)
            }
        }

        findViewById<RecyclerView>(R.id.tracksHistoryRecyclerView).adapter = historyAdapter
        findViewById<RecyclerView>(R.id.tracksRecyclerView).adapter = resultsAdapter

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
            findViewById<ScrollView>(R.id.tracksSearchHistory).isVisible = historyIsVisible
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
                findViewById<ScrollView>(R.id.tracksSearchHistory).isVisible = historyIsVisible
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