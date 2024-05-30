package com.msaggik.playlistmaker.presentation.ui.activity

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
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.msaggik.playlistmaker.R
import com.msaggik.playlistmaker.presentation.ui.adapters.TrackListAdapter
import com.msaggik.playlistmaker.util.Creator
import com.msaggik.playlistmaker.domain.api.network.TracksInteractor
import com.msaggik.playlistmaker.domain.api.sp.SpInteractor
import com.msaggik.playlistmaker.domain.models.Track
import com.msaggik.playlistmaker.util.Utils


class SearchActivity : AppCompatActivity(R.layout.activity_search) {

    // param view
    private val inputSearch: EditText by lazy {
        findViewById(R.id.input_search)
    }
    private val buttonBack: ImageView by lazy {
        findViewById(R.id.button_back)
    }
    private val buttonClear: ImageView by lazy {
        findViewById(R.id.button_clear)
    }
    private val trackListView: RecyclerView by lazy {
        findViewById(R.id.track_list)
    }
    private val trackListHistoryView: RecyclerView by lazy {
        findViewById(R.id.search_history_track_list)
    }
    private val layoutNothingFound: LinearLayout by lazy {
        findViewById(R.id.layout_nothing_found)
    }
    private val layoutCommunicationProblems: LinearLayout by lazy {
        findViewById(R.id.layout_communication_problems)
    }
    private val layoutSearchHistory: LinearLayout by lazy {
        findViewById(R.id.layout_search_history)
    }
    private val buttonUpdate: Button by lazy {
        findViewById(R.id.button_update)
    }
    private val buttonClearSearchHistory: Button by lazy {
        findViewById(R.id.button_clear_search_history)
    }
    private val loadingTime: ProgressBar by lazy {
        findViewById(R.id.loading_time)
    }

    // visible views
    private val viewArray: Array<View> by lazy {
        arrayOf(loadingTime, layoutSearchHistory, trackListView, layoutNothingFound, layoutCommunicationProblems)
    }

    // data
    private var textSearch = ""
    private var trackList: MutableList<Track> = mutableListOf()
    private val trackListAdapter: TrackListAdapter by lazy {
        TrackListAdapter(trackList) {
            // implementing an adapter interface method with Debounce
            if (clickTracksDebounce()) {
                trackSelection(it)
            }
        }
    }
    private val trackListHistoryAdapter: TrackListAdapter by lazy {
        TrackListAdapter(trackListHistory) {
            // implementing an adapter interface method with Debounce
            if (clickTracksDebounce()) {
                trackSelection(it)
            }
        }
    }

    // shared preferences data MutableList<Track>
    private var trackListHistory: MutableList<Track> = mutableListOf()

    // search tracks
    private val handlerSearchTrack = Handler(Looper.getMainLooper())
    private var searchTrack = ""
    private val searchTracksRunnable = Runnable { searchTracks(searchTrack) }

    // click tracks debounce
    private val handlerClickTrack = Handler(Looper.getMainLooper())
    private var isClickTrackAllowed = true

    // communication with domain
    private val tracksInteractor = Creator.provideTracksInteractor()
    private val spInteractor by lazy {
        Creator.provideSpInteractor(applicationContext)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        trackListView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        trackListHistoryView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // output list tracks in RecyclerView trackListView
        trackListView.adapter = trackListAdapter

        // output list tracks in RecyclerView trackListHistoryView
        readTrackListHistory()
        trackListHistoryAdapter.notifyDataSetChanged()
        trackListHistoryView.adapter = trackListHistoryAdapter

        // listeners
        inputSearch.setOnFocusChangeListener(focusChangeListener)
        inputSearch.addTextChangedListener(inputSearchWatcher)
        buttonBack.setOnClickListener(listener)
        buttonClear.setOnClickListener(listener)
        buttonUpdate.setOnClickListener(listener)
        buttonClearSearchHistory.setOnClickListener(listener)
    }

    private fun readTrackListHistory() {
        spInteractor.readTrackListHistory(object : SpInteractor.SpTracksHistoryConsumer {
            @SuppressLint("NotifyDataSetChanged")
            override fun consume(listHistoryTracks: List<Track>) {
                trackListHistory.clear()
                trackListHistory.addAll(listHistoryTracks)
            }
        })
    }

    private fun addTrackListHistory(track: Track) {
        spInteractor.addTrackListHistory(track, object : SpInteractor.SpTracksHistoryConsumer {
            @SuppressLint("NotifyDataSetChanged")
            override fun consume(listHistoryTracks: List<Track>) {
                trackListHistory.clear()
                trackListHistory.addAll(listHistoryTracks)
            }
        })
    }

    private fun clearTrackListHistory() {
        spInteractor.clearTrackListHistory()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun trackSelection(track: Track) {
        addTrackListHistory(track)
        trackListHistoryAdapter.notifyDataSetChanged()
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
        readTrackListHistory()
        if (flag && inputSearch.text.isEmpty() && inputSearch.hasFocus() && trackListHistory.isNotEmpty()) {
            Utils.visibilityView(viewArray, layoutSearchHistory)
            trackListHistoryAdapter.setTrackList(trackListHistory)
            trackListHistoryAdapter.notifyDataSetChanged()
        } else {
            layoutSearchHistory.visibility = View.GONE
        }
    }

    private fun searchTracks(searchNameTracks: String) {
        if (searchNameTracks.isNotEmpty()) {
            Utils.visibilityView(viewArray, loadingTime)
            tracksInteractor.searchTracks(
                searchNameTracks,
                object : TracksInteractor.TracksConsumer {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun consume(listTracks: List<Track>) {
                        handlerSearchTrack.post {
                            if (listTracks.isNotEmpty()) {
                                Utils.visibilityView(viewArray, trackListView)
                                trackList.clear()
                                trackList.addAll(listTracks)
                                trackListAdapter.notifyDataSetChanged()
                            } else {
                                Utils.visibilityView(viewArray, layoutCommunicationProblems)
                            }
                            if (listTracks.isEmpty()) {
                                Utils.visibilityView(viewArray, layoutNothingFound)
                            }
                        }
                    }
                })
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
                    inputSearch.setText("")
                    val keyboardOnOff =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    keyboardOnOff?.hideSoftInputFromWindow(inputSearch.windowToken, 0)
                    trackList.clear()
                    trackListAdapter.notifyDataSetChanged()
                    visibleLayoutSearchHistory(true)
                    Utils.visibilityView(viewArray, layoutSearchHistory)
                }

                R.id.button_update -> {
                    searchTracks(textSearch)
                }

                R.id.button_clear_search_history -> {
                    clearTrackListHistory()
                    trackListHistory.clear()
                    visibleLayoutSearchHistory(true)
                }
            }
        }
    }

    private val inputSearchWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            // просмотр старого текста
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            // просмотр введённого текста
            val isInputText = !p0.isNullOrEmpty()
            buttonClear.isVisible = isInputText
            if (isInputText) {
                searchTrack = p0.toString()
                searchTracksDebounce()
            }
            visibleLayoutSearchHistory(!isInputText)
        }

        override fun afterTextChanged(p0: Editable?) {
            // просмотр отредактированного текста
            textSearch = p0.toString()
        }
    }

    private fun searchTracksDebounce() {
        handlerSearchTrack.removeCallbacks(searchTracksRunnable)
        handlerSearchTrack.postDelayed(searchTracksRunnable, DELAY_SEARCH_TRACKS)
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
        outState.putString(KEY_TEXT_SEARCH, textSearch)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        textSearch = savedInstanceState.getString(KEY_TEXT_SEARCH, TEXT_SEARCH_DEFAULT)
    }

    private companion object {
        const val KEY_TEXT_SEARCH = "KEY_SEARCH"
        const val TEXT_SEARCH_DEFAULT = ""
        const val DELAY_SEARCH_TRACKS = 2000L
        const val DELAY_CLICK_TRACK = 1000L
    }
}