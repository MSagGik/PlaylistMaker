package com.msaggik.playlistmaker.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.msaggik.playlistmaker.R
import com.msaggik.playlistmaker.entity.Track
import com.msaggik.playlistmaker.entity.TrackResponse
import com.msaggik.playlistmaker.util.adapters.TrackListAdapter
import com.msaggik.playlistmaker.util.additionally.SearchHistory
import com.msaggik.playlistmaker.util.network.RestItunes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TRACK_LIST_PREFERENCES = "track_list_preferences"
private const val TRACK_LIST_HISTORY_KEY = "track_list_history_key"
private const val DELAY_SEARCH_TRACKS = 2000L
private const val DELAY_CLICK_TRACK = 1000L

class SearchActivity : AppCompatActivity() {

    private lateinit var inputSearch: EditText
    private lateinit var buttonBack: ImageView
    private lateinit var buttonClear: ImageView
    private lateinit var trackListView: RecyclerView
    private lateinit var trackListHistoryView: RecyclerView
    private lateinit var layoutNothingFound: LinearLayout
    private lateinit var layoutCommunicationProblems: LinearLayout
    private lateinit var layoutSearchHistory: LinearLayout
    private lateinit var buttonUpdate: Button
    private lateinit var buttonClearSearchHistory: Button
    private lateinit var loadingTime: ProgressBar

    private var textSearch = ""
    private var trackList: MutableList<Track> = mutableListOf()
    private lateinit var searchHistory: SearchHistory
    private lateinit var trackListAdapter: TrackListAdapter
    private lateinit var trackListHistoryAdapter: TrackListAdapter

    private val handlerSearchTrack = Handler(Looper.getMainLooper())
    private var searchTrack = ""
    private val searchTracksRunnable = Runnable { searchTracks(searchTrack) }

    private val handlerClickTrack = Handler(Looper.getMainLooper())
    private var isClickTrackAllowed = true

    private val itunesBaseURL = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val itunesRestService = retrofit.create(RestItunes::class.java)

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        inputSearch = findViewById(R.id.input_search)
        buttonBack = findViewById(R.id.button_back)
        buttonClear = findViewById(R.id.button_clear)
        trackListView = findViewById(R.id.track_list)
        trackListHistoryView = findViewById(R.id.search_history_track_list)
        layoutNothingFound = findViewById(R.id.layout_nothing_found)
        layoutCommunicationProblems = findViewById(R.id.layout_communication_problems)
        layoutSearchHistory = findViewById(R.id.layout_search_history)
        buttonUpdate = findViewById(R.id.button_update)
        buttonClearSearchHistory = findViewById(R.id.button_clear_search_history)
        loadingTime = findViewById(R.id.loading_time)

        trackListView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        trackListHistoryView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // вывод списка треков в RecyclerView trackListView
        trackListAdapter = TrackListAdapter(trackList) {
            // реализация метода интерфейса адаптера с Debounce
            if(clickTracksDebounce()) {
                trackSelection(it)
            }
        }
        trackListView.adapter = trackListAdapter

        sharedPreferences = getSharedPreferences(TRACK_LIST_PREFERENCES, MODE_PRIVATE)
        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener)
        // вывод списка истории треков в RecyclerView trackListHistoryView
        searchHistory = SearchHistory()
        trackListHistoryAdapter = TrackListAdapter(searchHistory.readTrackListHistorySharedPreferences(sharedPreferences)) {
            // реализация метода интерфейса адаптера с Debounce
            if(clickTracksDebounce()) {
                trackSelection(it)
            }
        }
        trackListHistoryView.adapter = trackListHistoryAdapter

        inputSearch.setOnFocusChangeListener(focusChangeListener)
        inputSearch.addTextChangedListener(inputSearchWatcher)
        buttonBack.setOnClickListener(listener)
        buttonClear.setOnClickListener(listener)
        buttonUpdate.setOnClickListener(listener)
        buttonClearSearchHistory.setOnClickListener(listener)
    }

    private fun trackSelection(track: Track) {
        // получение объекта настроек и их обновление новым треком
        val sharedPreferences = applicationContext.getSharedPreferences(TRACK_LIST_PREFERENCES, Context.MODE_PRIVATE)
        val searchHistory = SearchHistory()
        searchHistory.addTrackListHistorySharedPreferences(sharedPreferences, track)
        // переход в активность аудиоплеера
        val intent = Intent(applicationContext, PlayerActivity::class.java)
        intent.putExtra(Track::class.java.simpleName, track)
        startActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    private var sharedPreferenceChangeListener: OnSharedPreferenceChangeListener =
        OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if(key == TRACK_LIST_HISTORY_KEY) {
                val trackListHistory = searchHistory.readTrackListHistorySharedPreferences(sharedPreferences)
                trackListHistoryAdapter.setTrackList(trackListHistory)
                trackListHistoryAdapter.notifyDataSetChanged()
            }
        }

    private val focusChangeListener = object: OnFocusChangeListener {
        override fun onFocusChange(p0: View?, p1: Boolean) {
            visibleLayoutSearchHistory(p1)
        }
    }

    private fun searchTracks(searchNameTracks: String) {
        // поиск треков с помощью REST API
        if (searchNameTracks.isNotEmpty()) {
            visibilityView(loadingTime)
            itunesRestService.search(searchNameTracks).enqueue(object :
                Callback<TrackResponse> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>
                ) {
                    if (response.code() == 200) {
                        trackList.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            visibilityView(trackListView)
                            trackList.addAll(response.body()?.results!!)
                            trackListAdapter.notifyDataSetChanged()
                        }
                        if (trackList.isEmpty()) {
                            visibilityView(layoutNothingFound)
                        }
                    } else {
                        visibilityView(layoutCommunicationProblems)
                        Toast.makeText(applicationContext, response.code().toString(), Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    visibilityView(layoutCommunicationProblems)
                    Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    private val listener: View.OnClickListener = object: View.OnClickListener {
        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(p0: View?) {
            when(p0?.id) {
                R.id.button_back -> {
                    val backIntent = Intent(this@SearchActivity, MainActivity::class.java)
                    startActivity(backIntent)
                }
                R.id.button_clear -> {
                    inputSearch.setText("")
                    // убирание клавиатуры с экрана
                    val keyboardOnOff = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    keyboardOnOff?.hideSoftInputFromWindow(inputSearch.windowToken, 0)
                    trackList.clear()
                    trackListAdapter.notifyDataSetChanged()
                    visibleLayoutSearchHistory(true)
                    visibilityView(layoutSearchHistory)
                }
                R.id.button_update -> {
                    searchTracks(textSearch)
                }
                R.id.button_clear_search_history -> {
                    searchHistory.clearTrackListHistorySharedPreferences(sharedPreferences)
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
            if(p0.isNullOrEmpty()) {
                buttonClear.visibility = View.GONE
            } else {
                buttonClear.visibility = View.VISIBLE
                searchTrack = p0.toString()
                searchTracksDebounce()
            }

            visibleLayoutSearchHistory(p0?.isEmpty() == true)
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
        if(isClickTrackAllowed) {
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

    @SuppressLint("NotifyDataSetChanged")
    private fun visibleLayoutSearchHistory(flag: Boolean) {
        val trackListHistory = searchHistory.readTrackListHistorySharedPreferences(sharedPreferences)
        if (flag && inputSearch.text.isEmpty() && inputSearch.hasFocus() && trackListHistory.isNotEmpty()) {
            visibilityView(layoutSearchHistory)
            trackListHistoryAdapter.setTrackList(trackListHistory)
            trackListHistoryAdapter.notifyDataSetChanged()
        } else {
            layoutSearchHistory.visibility = View.GONE
        }
    }

    private fun visibilityView(v: View? = null) {
        loadingTime.visibility = View.GONE
        layoutSearchHistory.visibility = View.GONE
        trackListView.visibility = View.GONE
        layoutNothingFound.visibility = View.GONE
        layoutCommunicationProblems.visibility = View.GONE

        when(v) {
            loadingTime -> loadingTime.visibility = View.VISIBLE
            layoutSearchHistory -> layoutSearchHistory.visibility = View.VISIBLE
            trackListView -> trackListView.visibility = View.VISIBLE
            layoutNothingFound -> layoutNothingFound.visibility = View.VISIBLE
            layoutCommunicationProblems -> layoutCommunicationProblems.visibility = View.VISIBLE
        }
    }

    companion object {
        const val KEY_TEXT_SEARCH = "KEY_SEARCH"
        const val TEXT_SEARCH_DEFAULT = ""
    }
}