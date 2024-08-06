package com.msaggik.playlistmaker.search.presentation.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.msaggik.playlistmaker.R
import com.msaggik.playlistmaker.databinding.FragmentSearchBinding
import com.msaggik.playlistmaker.player.presentation.ui.PlayerFragment
import com.msaggik.playlistmaker.search.domain.models.Track
import com.msaggik.playlistmaker.search.presentation.ui.adapter.TrackListAdapter
import com.msaggik.playlistmaker.search.presentation.ui.state.TracksState
import com.msaggik.playlistmaker.search.presentation.view_model.SearchViewModel
import com.msaggik.playlistmaker.util.Utils
import com.msaggik.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private companion object {
        const val KEY_TEXT_SEARCH = "KEY_SEARCH"
        const val TEXT_SEARCH_DEFAULT = ""
        const val DELAY_CLICK_TRACK = 1000L
    }

    // view
    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding get() = _binding!!

    // visible views
    private var viewArray: Array<View>? = null

    // view-model
    private val searchViewModel: SearchViewModel by viewModel()

    // data
    private var searchTrack = ""
    private var searchTrackResult = ""

    private lateinit var onTrackClickDebounce: (Track) -> Unit

    private var trackList: MutableList<Track> = mutableListOf()

    private val trackListAdapter: TrackListAdapter by lazy {
        TrackListAdapter(trackList) {
            onTrackClickDebounce(it)
        }
    }

    // shared preferences data MutableList<Track>
    private var trackListHistory: MutableList<Track> = mutableListOf()

    private val trackListHistoryAdapter: TrackListAdapter by lazy {
        TrackListAdapter(trackListHistory) {
            onTrackClickDebounce(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        viewArray = arrayOf(
            binding.loadingTime,
            binding.layoutSearchHistory,
            binding.trackList,
            binding.layoutNothingFound,
            binding.layoutCommunicationProblems
        )
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onTrackClickDebounce = debounce<Track>(
            DELAY_CLICK_TRACK,
            viewLifecycleOwner.lifecycleScope,
            false,
            true
        ) { track -> trackSelection(track) }

        binding.trackList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.searchHistoryTrackList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.trackList.adapter = trackListAdapter

        searchViewModel.readTrackListHistory()

        searchViewModel.getTrackListHistoryLiveData().observe(viewLifecycleOwner) { list ->
            trackListHistory.clear()
            trackListHistory.addAll(list)
            trackListHistoryAdapter.notifyDataSetChanged()
        }

        searchViewModel.getStateLiveData().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.searchHistoryTrackList.adapter = trackListHistoryAdapter

        binding.inputSearch.onFocusChangeListener = focusChangeListener
        binding.inputSearch.addTextChangedListener(inputSearchWatcher)
        binding.buttonClear.setOnClickListener(listener)
        binding.buttonUpdate.setOnClickListener(listener)
        binding.buttonClearSearchHistory.setOnClickListener(listener)
    }

    private fun render(state: TracksState) {
        when (state) {
            is TracksState.Loading -> Utils.visibilityView(viewArray, binding.loadingTime)
            is TracksState.Content -> showContent(state.tracks)
            is TracksState.Error -> {
                Utils.visibilityView(viewArray, binding.layoutCommunicationProblems)
                Toast.makeText(requireContext(), state.errorMessage, Toast.LENGTH_SHORT).show()
            }
            is TracksState.Empty -> Utils.visibilityView(viewArray, binding.layoutNothingFound)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showContent(tracks: List<Track>) {
        Utils.visibilityView(viewArray, binding.trackList)
        trackList.clear()
        trackList.addAll(tracks)
        trackListAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun trackSelection(track: Track) {
        searchViewModel.addTrackListHistory(track)
        findNavController().navigate(
            R.id.action_searchFragment_to_playerFragment,
            PlayerFragment.createArgs(track)
        )
    }

    private val focusChangeListener = object : OnFocusChangeListener {
        override fun onFocusChange(p0: View?, p1: Boolean) {
            visibleLayoutSearchHistory(p1)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun visibleLayoutSearchHistory(flag: Boolean) {
        if (flag && binding.inputSearch.text.isEmpty() && binding.inputSearch.hasFocus() && trackListHistory.isNotEmpty()) {
            Utils.visibilityView(viewArray, binding.layoutSearchHistory)
            trackListHistoryAdapter.setTrackList(trackListHistory)
            trackListHistoryAdapter.notifyDataSetChanged()
        } else {
            binding.layoutSearchHistory.visibility = View.GONE
        }
    }

    private val listener: View.OnClickListener = object : View.OnClickListener {
        @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
        override fun onClick(p0: View?) {
            when (p0?.id) {
                R.id.button_clear -> {
                    binding.inputSearch.setText("")
                    val keyboardOnOff =
                        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    keyboardOnOff?.hideSoftInputFromWindow(binding.inputSearch.windowToken, 0)
                    trackList.clear()
                    trackListAdapter.notifyDataSetChanged()
                    searchViewModel.clearSearchLiveData()
                    visibleLayoutSearchHistory(true)
                    Utils.visibilityView(viewArray, binding.layoutSearchHistory)
                }

                R.id.button_update -> {
                    searchViewModel.searchTracks(searchTrack)
                }

                R.id.button_clear_search_history -> {
                    searchViewModel.clearTrackListHistory()
                    trackListHistory.clear()
                    trackListHistoryAdapter.notifyDataSetChanged()
                    visibleLayoutSearchHistory(true)
                }
            }
        }
    }

    private val inputSearchWatcher = object : TextWatcher {
        override fun beforeTextChanged(oldText: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(inputText: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val isInputText = !inputText.isNullOrEmpty()
            binding.buttonClear.isVisible = isInputText
            if (isInputText) {
                searchTrackResult = inputText.toString()
                searchViewModel.searchDebounce(searchTrackResult)
            }
            visibleLayoutSearchHistory(!isInputText)
        }

        override fun afterTextChanged(resultText: Editable?) {
            searchTrack = resultText.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        inputSearchWatcher.let { binding.inputSearch.removeTextChangedListener(it) }
        _binding = null
        viewArray = emptyArray()
        viewArray = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_TEXT_SEARCH, searchTrack)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            searchTrack = savedInstanceState.getString(KEY_TEXT_SEARCH, TEXT_SEARCH_DEFAULT)
        }
    }
}