package com.msaggik.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.msaggik.playlistmaker.R
import com.msaggik.playlistmaker.main.ui.MainActivity
import com.msaggik.playlistmaker.player.ui.PlayerActivity
import com.msaggik.playlistmaker.search.ui.adapter.TrackListAdapter
import com.msaggik.playlistmaker.databinding.ActivitySearchBinding
import com.msaggik.playlistmaker.search.domain.models.Track
import com.msaggik.playlistmaker.search.ui.state.TracksState
import com.msaggik.playlistmaker.search.view_model.SearchViewModel
import com.msaggik.playlistmaker.util.Utils
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {

    // view
    private val binding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }

    // visible views
    private val viewArray: Array<View> by lazy {
        arrayOf(
            binding.loadingTime,
            binding.layoutSearchHistory,
            binding.trackList,
            binding.layoutNothingFound,
            binding.layoutCommunicationProblems
        )
    }

    // view-model
    private val searchViewModel: SearchViewModel by viewModel()

    // data
    private var searchTrack = ""
    private var searchTrackResult = ""
    private var trackList: MutableList<Track> = mutableListOf()
    private val trackListAdapter: TrackListAdapter by lazy {
        TrackListAdapter(trackList) {
            if (clickTracksDebounce()) {
                trackSelection(it)
            }
        }
    }
    private val trackListHistoryAdapter: TrackListAdapter by lazy {
        TrackListAdapter(trackListHistory) {
            if (clickTracksDebounce()) {
                trackSelection(it)
            }
        }
    }

    // shared preferences data MutableList<Track>
    private var trackListHistory: MutableList<Track> = mutableListOf()

    // click tracks debounce
    private val handlerClickTrack = Handler(Looper.getMainLooper())
    private var isClickTrackAllowed = true

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.trackList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.searchHistoryTrackList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // output list tracks in RecyclerView trackListView
        binding.trackList.adapter = trackListAdapter

        // subscription to TrackListHistoryLiveData
        searchViewModel.getTrackListHistoryLiveData().observe(this) { list ->
            trackListHistory.clear()
            trackListHistory.addAll(list)
            trackListHistoryAdapter.notifyDataSetChanged()
        }

        // subscription to StateLiveData
        searchViewModel.getStateLiveData().observe(this) {
            render(it)
        }

        binding.searchHistoryTrackList.adapter = trackListHistoryAdapter

        // listeners
        binding.inputSearch.setOnFocusChangeListener(focusChangeListener)
        binding.inputSearch.addTextChangedListener(inputSearchWatcher)
        binding.buttonBack.setOnClickListener(listener)
        binding.buttonClear.setOnClickListener(listener)
        binding.buttonUpdate.setOnClickListener(listener)
        binding.buttonClearSearchHistory.setOnClickListener(listener)
    }

    // methods visible state screen
    private fun render(state: TracksState) {
        when (state) {
            is TracksState.Loading -> Utils.visibilityView(viewArray, binding.loadingTime)
            is TracksState.Content -> showContent(state.tracks)
            is TracksState.Error -> {
                Utils.visibilityView(viewArray, binding.layoutCommunicationProblems)
                Toast.makeText(this, state.errorMessage, Toast.LENGTH_SHORT).show()
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
        val intent = Intent(applicationContext, PlayerActivity::class.java)
        intent.putExtra(Track::class.java.simpleName, track)
        startActivity(intent)
    }

    private val focusChangeListener = object : OnFocusChangeListener {
        override fun onFocusChange(p0: View?, p1: Boolean) {
            visibleLayoutSearchHistory(p1)
        }
    }

    // show search history
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
        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(p0: View?) {
            when (p0?.id) {
                R.id.button_back -> {
                    val backIntent = Intent(this@SearchActivity, MainActivity::class.java)
                    startActivity(backIntent)
                }

                R.id.button_clear -> {
                    binding.inputSearch.setText("")
                    val keyboardOnOff =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    keyboardOnOff?.hideSoftInputFromWindow(binding.inputSearch.windowToken, 0)
                    trackList.clear()
                    trackListAdapter.notifyDataSetChanged()
                    visibleLayoutSearchHistory(true)
                    Utils.visibilityView(viewArray, binding.layoutSearchHistory)
                }

                R.id.button_update -> {
                    searchViewModel.searchTracks(searchTrack)
                }

                R.id.button_clear_search_history -> {
                    searchViewModel.clearTrackListHistory()
                    trackListHistory.clear()
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

    override fun onDestroy() {
        super.onDestroy()
        inputSearchWatcher.let { binding.inputSearch.removeTextChangedListener(it) }
    }

    private fun clickTracksDebounce(): Boolean {
        val current = isClickTrackAllowed
        if (isClickTrackAllowed) {
            isClickTrackAllowed = false
            handlerClickTrack.postDelayed({ isClickTrackAllowed = true }, DELAY_CLICK_TRACK)
        }
        return current
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_TEXT_SEARCH, searchTrack)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchTrack = savedInstanceState.getString(KEY_TEXT_SEARCH, TEXT_SEARCH_DEFAULT)
    }

    private companion object {
        const val KEY_TEXT_SEARCH = "KEY_SEARCH"
        const val TEXT_SEARCH_DEFAULT = ""
        const val DELAY_CLICK_TRACK = 1000L
    }
}