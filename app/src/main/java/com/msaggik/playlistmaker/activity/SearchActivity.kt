package com.msaggik.playlistmaker.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
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

    private var textSearch = ""
    private var trackList: MutableList<Track> = mutableListOf()
    private lateinit var searchHistory: SearchHistory
    private lateinit var trackListAdapter: TrackListAdapter
    private lateinit var trackListHistoryAdapter: TrackListAdapter

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

        trackListView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        trackListHistoryView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // вывод списка треков в RecyclerView trackListView
        trackListAdapter = TrackListAdapter(trackList)
        trackListView.adapter = trackListAdapter

        sharedPreferences = getSharedPreferences(TRACK_LIST_PREFERENCES, MODE_PRIVATE)
        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener)
        // вывод списка истории треков в RecyclerView trackListHistoryView
        searchHistory = SearchHistory()
        trackListHistoryAdapter = TrackListAdapter(searchHistory.readTrackListHistorySharedPreferences(sharedPreferences))
        trackListHistoryView.adapter = trackListHistoryAdapter

        inputSearch.setOnEditorActionListener(editorActionListener)
        inputSearch.setOnFocusChangeListener(focusChangeListener)
        inputSearch.addTextChangedListener(inputSearchWatcher)
        buttonBack.setOnClickListener(listener)
        buttonClear.setOnClickListener(listener)
        buttonUpdate.setOnClickListener(listener)
        buttonClearSearchHistory.setOnClickListener(listener)
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

    private val editorActionListener = object: OnEditorActionListener {
        override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
            // поиск треков с помощью REST API
            textSearch = inputSearch.text.toString()
            searchTracks(textSearch)
            return true
        }
    }

    private fun searchTracks(searchNameTracks: String) {
        // поиск треков с помощью REST API
        if (searchNameTracks.isNotEmpty()) {
            itunesRestService.search(searchNameTracks).enqueue(object :
                Callback<TrackResponse> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>
                ) {
                    if (response.code() == 200) {
                        trackList.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            trackListView.visibility = View.VISIBLE
                            layoutNothingFound.visibility = View.GONE
                            layoutCommunicationProblems.visibility = View.GONE
                            trackList.addAll(response.body()?.results!!)
                            trackListAdapter.notifyDataSetChanged()
                        }
                        if (trackList.isEmpty()) {
                            trackListView.visibility = View.GONE
                            layoutNothingFound.visibility = View.VISIBLE
                            layoutCommunicationProblems.visibility = View.GONE
                        }
                    } else {
                        trackListView.visibility = View.GONE
                        layoutNothingFound.visibility = View.GONE
                        layoutCommunicationProblems.visibility = View.VISIBLE
                        Toast.makeText(applicationContext, response.code().toString(), Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    trackListView.visibility = View.GONE
                    layoutNothingFound.visibility = View.GONE
                    layoutCommunicationProblems.visibility = View.VISIBLE
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
                    trackListView.visibility = View.VISIBLE
                    visibleLayoutSearchHistory(true)
                    layoutNothingFound.visibility = View.GONE
                    layoutCommunicationProblems.visibility = View.GONE
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
            }

            visibleLayoutSearchHistory(p0?.isEmpty() == true)
        }

        override fun afterTextChanged(p0: Editable?) {
            // просмотр отредактированного текста
            textSearch = p0.toString()
        }
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
            layoutSearchHistory.visibility = View.VISIBLE
            trackListView.visibility = View.GONE
            trackListHistoryAdapter.setTrackList(trackListHistory)
            trackListHistoryAdapter.notifyDataSetChanged()
        } else {
            layoutSearchHistory.visibility = View.GONE
        }
    }

    companion object {
        const val KEY_TEXT_SEARCH = "KEY_SEARCH"
        const val TEXT_SEARCH_DEFAULT = ""
    }
}