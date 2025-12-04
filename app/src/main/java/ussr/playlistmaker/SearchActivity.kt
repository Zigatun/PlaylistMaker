package ussr.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ussr.playlistmaker.adapters.ItunesTrackAdapter
import ussr.playlistmaker.api.ItunesSearchApiService
import ussr.playlistmaker.models.ItunesSearchResult
import ussr.playlistmaker.storages.SearchHistory

class SearchActivity : AppCompatActivity() {
    private var searchBarValue: CharSequence? = SEARCHBAR_VALUE_DEFAULT
    private lateinit var history: SearchHistory
    private lateinit var historyAdapter: ItunesTrackAdapter
    private lateinit var resultsAdapter: ItunesTrackAdapter
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesSearchApiService = retrofit.create<ItunesSearchApiService>()

    private fun setPlaceholderMessage(message: String = "", isError: Boolean = false) {
        val placeholderView = findViewById<LinearLayout>(R.id.error_placeholder)
        val tracksView = findViewById<RecyclerView>(R.id.tracksRecyclerView)
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
        itunesSearchApiService.search(request)
            .enqueue(object : Callback<ItunesSearchResult> {
                override fun onResponse(
                    call: Call<ItunesSearchResult?>,
                    response: Response<ItunesSearchResult?>
                ) {
                    if (response.isSuccessful) {
                        setPlaceholderMessage("")
                        if (wasBeenCleared) {
                            findViewById<RecyclerView>(R.id.tracksRecyclerView).isVisible = false
                            return
                        }
                        val respObjects = response.body()?.results
                        if (respObjects != null && respObjects.isNotEmpty()) {
                            resultsAdapter.updateTracks(respObjects.toMutableList())
                        } else {
                            setPlaceholderMessage(getString(R.string.any_not_found))
                        }
                    }
                }

                override fun onFailure(
                    call: Call<ItunesSearchResult?>,
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
        }

        resultsAdapter = ItunesTrackAdapter(mutableListOf()) { track ->
            history.add(track)
            historyAdapter.updateTracks(history.get().toMutableList())
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
        searchBar.setOnFocusChangeListener { view, hasFocus ->
            val historyIsVisible = hasFocus && searchBar.text.isEmpty()
            findViewById<ScrollView>(R.id.tracksSearchHistory).isVisible = historyIsVisible
            if (historyIsVisible)
                refreshHistory()
        }
        searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
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

    companion object {
        const val SEARCHBAR = "SEARCHBAR"
        const val SEARCHBAR_VALUE_DEFAULT = ""
    }
}