package ussr.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ussr.playlistmaker.R
import ussr.playlistmaker.databinding.FragmentSearchBinding
import ussr.playlistmaker.search.models.TracksState
import ussr.playlistmaker.search.ui.viewmodel.SearchActivityViewModel
import ussr.playlistmaker.player.ui.PlayerFragment

class SearchFragment : Fragment() {
    private val handler = Handler(Looper.getMainLooper())

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchActivityViewModel by viewModel()

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        viewModel.observableTrackNavigationEvent().observe(viewLifecycleOwner) { event ->
            event.get()?.let { track ->
                findNavController().navigate(R.id.action_searchFragment_to_playerFragment,
                    PlayerFragment.createArgs(track))
            }
        }

        binding.tracksHistoryClear.setOnClickListener { viewModel.onHistoryClearClicked() }

        binding.searchBarClearText.setOnClickListener {
            viewModel.onClearClicked()
            val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
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

        viewModel.observableClearTextIsAvailable().observe(viewLifecycleOwner) {isAvailable ->
            binding.searchBarClearText.isVisible = isAvailable
        }

        viewModel.observableSearchBarText().observe(viewLifecycleOwner) { text ->
            if (binding.searchBar.text.toString() != text) {
                binding.searchBar.setText(text)
            }
        }

        viewModel.observableTrackViewState().observe(viewLifecycleOwner) { state ->
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val SEARCHLIST_DEBOUNCE_DELAY = 1000L
    }

}