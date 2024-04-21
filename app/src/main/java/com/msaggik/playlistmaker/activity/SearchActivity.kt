package com.msaggik.playlistmaker.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
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
import com.msaggik.playlistmaker.util.network.RestItunes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var inputSearch: EditText
    private lateinit var buttonBack: ImageView
    private lateinit var buttonClear: ImageView
    private lateinit var trackListView: RecyclerView
    private lateinit var layoutNothingFound: LinearLayout
    private lateinit var layoutCommunicationProblems: LinearLayout
    private lateinit var buttonUpdate: Button

    private var textSearch = ""
    private var trackList: MutableList<Track> = mutableListOf()
    private lateinit var trackListAdapter: TrackListAdapter

    private val itunesBaseURL = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val itunesRestService = retrofit.create(RestItunes::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        inputSearch = findViewById(R.id.input_search)
        buttonBack = findViewById(R.id.button_back)
        buttonClear = findViewById(R.id.button_clear)
        trackListView = findViewById(R.id.track_list)
        layoutNothingFound = findViewById(R.id.layout_nothing_found)
        layoutCommunicationProblems = findViewById(R.id.layout_communication_problems)
        buttonUpdate = findViewById(R.id.button_update)

        trackListView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // вывод списка треков на экран в RecyclerView
        trackListAdapter = TrackListAdapter(trackList)
        trackListView.adapter = trackListAdapter

        inputSearch.setOnEditorActionListener(editorActionListener)
        inputSearch.addTextChangedListener(inputSearchWatcher)
        buttonBack.setOnClickListener(listener)
        buttonClear.setOnClickListener(listener)
        buttonUpdate.setOnClickListener(listener)
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
                }
                R.id.button_update -> {
                    searchTracks(textSearch)
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

    companion object {
        const val KEY_TEXT_SEARCH = "KEY_SEARCH"
        const val TEXT_SEARCH_DEFAULT = ""
    }
}