package ussr.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

class SearchActivity : AppCompatActivity() {
    private var searchBarValue: CharSequence? = SEARCHBAR_VALUE_DEFAULT

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesSearchApiService = retrofit.create<ItunesSearchApiService>()

    //    private var tracks: List<Track> = listOf(
//        Track("Smells Like Teen Spirit", "Nirvana", "5:01", "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"),
//        Track("Billie Jean", "Michael Jackson", "4:35", "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"),
//        Track("Stayin' Alive", "Bee Gees", "4:10", "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"),
//        Track("Whole Lotta Love", "Led Zeppelin", "5:33", "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"),
//        Track("Sweet Child O'Mine", "Guns N' Roses", "5:03", "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"),
//        )
    private fun doSearch(request: String) {
        itunesSearchApiService.search(request)
            .enqueue(object : Callback<ItunesSearchResult> {
                override fun onResponse(
                    call: Call<ItunesSearchResult?>,
                    response: Response<ItunesSearchResult?>
                ) {
                    if (response.isSuccessful) {
                        val respObjects = response.body()?.results
                        if (respObjects != null) {
                            findViewById<RecyclerView>(R.id.tracksRecyclerView).adapter =
                                ItunesTrackAdapter(respObjects)
                        }
                    }
                }

                override fun onFailure(
                    call: Call<ItunesSearchResult?>,
                    t: Throwable
                ) {
                    val toast =
                        Toast.makeText(
                            applicationContext,
                            t.message.toString(),
                            Toast.LENGTH_LONG
                        )
                    toast.show()
                }

            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchBar = findViewById<EditText>(R.id.search_bar)
        val clearButton = findViewById<ImageView>(R.id.search_bar_clear_text)

        if (savedInstanceState != null) {
            searchBarValue = savedInstanceState.getCharSequence(SEARCHBAR, SEARCHBAR_VALUE_DEFAULT)
            searchBar.setText(searchBarValue)
        }

        findViewById<android.widget.Toolbar>(R.id.main_toolbar).setNavigationOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            searchBar.setText("")
            doSearch("")
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchBar.windowToken, 0)
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