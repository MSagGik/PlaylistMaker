package com.msaggik.playlistmaker.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.msaggik.playlistmaker.R
import com.msaggik.playlistmaker.entity.Track
import com.msaggik.playlistmaker.util.TrackListAdapter

class SearchActivity : AppCompatActivity() {
    private var textSearch = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val inputSearch = findViewById<EditText>(R.id.input_search)
        val buttonBack = findViewById<ImageView>(R.id.button_back)
        val buttonClear = findViewById<ImageView>(R.id.button_clear)
        val trackListView = findViewById<RecyclerView>(R.id.track_list)

        trackListView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // инициализация списка треков
        var trackList: MutableList<Track> = mutableListOf()
        trackList = mockPlayList(trackList)

        val trackListAdapter = TrackListAdapter(trackList)
        trackListView.adapter = trackListAdapter


        inputSearch.setText(textSearch)

        val listener: View.OnClickListener = object: View.OnClickListener {
            override fun onClick(p0: View?) {
                when(p0?.id) {
                    R.id.button_back -> {
                        val backIntent = Intent(this@SearchActivity, MainActivity::class.java)
                        startActivity(backIntent)
                    }
                    R.id.button_clear -> {
                        inputSearch.setText("")
                        val keyboardOnOff = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                        keyboardOnOff?.hideSoftInputFromWindow(inputSearch.windowToken, 0)
                    }
                }
            }
        }

        val inputSearchWatcher = object : TextWatcher {
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

        inputSearch.addTextChangedListener(inputSearchWatcher)
        buttonBack.setOnClickListener(listener)
        buttonClear.setOnClickListener(listener)
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

    private fun mockPlayList(list: MutableList<Track>): MutableList<Track> {
        list.add(Track("Smells Like Teen Spirit", "Nirvana", "5:01", "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"))
        list.add(Track("Billie Jean", "Michael Jackson", "4:35", "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"))
        list.add(Track("Stayin' Alive", "Bee Gees", "4:10", "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"))
        list.add(Track("Whole Lotta Love", "Led Zeppelin", "5:33", "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"))
        list.add(Track("Sweet Child O'Mine", "Guns N' Roses", "5:03", "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"))
        list.add(Track("Sweet Child O'Mine Sweet Child O'Mine Sweet Child O'Mine", "Guns N' Roses Sweet Child O'Mine Sweet Child O'Mine Sweet Child O'Mine", "5:03", "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"))
        return list
    }
}