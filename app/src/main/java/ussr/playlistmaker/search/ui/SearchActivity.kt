package ussr.playlistmaker.search.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import ussr.playlistmaker.main.Creator
import ussr.playlistmaker.PlaylistMakerApp
import ussr.playlistmaker.R
import ussr.playlistmaker.databinding.ActivitySearchBinding
import ussr.playlistmaker.search.models.TracksState
import ussr.playlistmaker.search.ui.viewmodel.SearchActivityViewModel
import ussr.playlistmaker.player.ui.PlayerActivity

class SearchActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())

    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchActivityViewModel

    private var searchItemClickAllowed = true

    private lateinit var historyAdapter: ItunesTrackAdapter
    private lateinit var resultsAdapter: ItunesTrackAdapter


    private fun isSearchItemClickAllowed(): Boolean {
        val current = searchItemClickAllowed
        if (searchItemClickAllowed) {
            searchItemClickAllowed = false
            handler.postDelayed({ searchItemClickAllowed = true }, SEARCHLIST_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun setPlaceholderMessage(message: String = "", isError: Boolean = false) {
        binding.searchProgressBar.isVisible = false
        if (message == "") {
            binding.errorPlaceholder.isVisible = false
            binding.tracksRecyclerView.isVisible = false
            binding.tracksSearchHistory.isVisible = false
            return
        } else {
            binding.errorPlaceholder.isVisible = true
            binding.tracksRecyclerView.isVisible = false
        }
        binding.errorDescription.text = message
        binding.placeholderRefreshButton.isVisible = isError
        if (isError) {
            binding.placeholderIcon.setImageResource(R.drawable.no_connection_icon)
        } else {
            binding.placeholderIcon.setImageResource(R.drawable.no_items_icon)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this, SearchActivityViewModel.getFactory(
                Creator.provideTracksInteractor(applicationContext as PlaylistMakerApp),
                Creator.provideSearchHistoryInteractor(applicationContext as PlaylistMakerApp)
            )
        ).get(SearchActivityViewModel::class.java)

        historyAdapter = ItunesTrackAdapter { track ->
            if(isSearchItemClickAllowed())
                viewModel.onTrackClicked(track)
        }

        resultsAdapter = ItunesTrackAdapter { track ->
            if(isSearchItemClickAllowed())
                viewModel.onTrackClicked(track)
        }

        binding.tracksHistoryRecyclerView.adapter = historyAdapter
        binding.tracksRecyclerView.adapter = resultsAdapter
        viewModel.observableTrackNavigationEvent().observe(this) { track ->
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra("track", track)
            startActivity(intent)
        }
//        if (savedInstanceState != null) {
//            searchBarValue = savedInstanceState.getCharSequence(SEARCHBAR, SEARCHBAR_VALUE_DEFAULT)
//            searchBar.setText(searchBarValue)
//        }

        binding.mainToolbar.setNavigationOnClickListener { finish() }

        binding.tracksHistoryClear.setOnClickListener { viewModel.onHistoryClearClicked() }

        binding.searchBarClearText.setOnClickListener {
            viewModel.onClearClicked()
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.searchBarClearText.windowToken, 0)
        }

        binding.placeholderRefreshButton.setOnClickListener { viewModel.onSearchSubmitted(binding.searchBar.text.toString()) }

        binding.searchBar.setOnFocusChangeListener { _, hasFocus ->
            viewModel.onSearchFocusChanged(hasFocus)
        }
        binding.searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.onSearchSubmitted(binding.searchBar.text.toString())
                true
            } else {
                false
            }
        }

        viewModel.observableClearTextIsAvailable().observe(this) {isAvailable ->
            binding.searchBarClearText.isVisible = isAvailable
        }

        viewModel.observableSearchBarText().observe(this) { text ->
            if (binding.searchBar.text.toString() != text) {
                binding.searchBar.setText(text)
            }
        }

        viewModel.observableTrackViewState().observe(this) { state ->
            when (state) {

                is TracksState.Content -> {
                    binding.searchProgressBar.isVisible = false
                    binding.errorPlaceholder.isVisible = false
                    if (state.isHistory) {
                        binding.tracksSearchHistory.isVisible = true
                        binding.tracksRecyclerView.isVisible = false
                        historyAdapter.setList(state.tracks)
                    } else {
                        binding.tracksSearchHistory.isVisible = false
                        binding.tracksRecyclerView.isVisible = true
                        resultsAdapter.setList(state.tracks)
                    }
                }

                is TracksState.Empty -> {
                    setPlaceholderMessage(state.message, false)
                }

                is TracksState.Error -> {
                    setPlaceholderMessage(state.message, true)
                }

                TracksState.Loading -> {
                    binding.tracksSearchHistory.isVisible = false
                    binding.tracksRecyclerView.isVisible = false
                    binding.errorPlaceholder.isVisible = false
                    binding.searchProgressBar.isVisible = true
                }

                else -> {
                    binding.tracksSearchHistory.isVisible = false
                }
            }
        }


        val searchBarTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onSearchTextChanged(s?.toString().orEmpty())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        binding.searchBar.addTextChangedListener(searchBarTextWatcher)

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        const val SEARCHLIST_DEBOUNCE_DELAY = 1000L
    }

}