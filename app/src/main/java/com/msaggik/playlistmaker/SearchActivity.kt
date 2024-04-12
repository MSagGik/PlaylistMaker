package com.msaggik.playlistmaker

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SearchActivity : AppCompatActivity() {
    private var textSearch = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val inputSearch = findViewById<EditText>(R.id.input_search)
        val buttonBack = findViewById<ImageView>(R.id.button_back)
        val buttonClear = findViewById<Button>(R.id.button_clear)

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
                        inputSearch.isFocusable = false
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
}