package ussr.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SearchActivity : AppCompatActivity() {
    private var searchBarValue: CharSequence? = SEARCHBAR_VALUE_DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchBar = findViewById<EditText>(R.id.search_bar)
        val clearButton = findViewById<ImageView>(R.id.search_bar_clear_text)

        if(savedInstanceState != null) {
            searchBarValue = savedInstanceState.getCharSequence(SEARCHBAR, SEARCHBAR_VALUE_DEFAULT)
            searchBar.setText(searchBarValue)
        }

        findViewById<android.widget.Toolbar>(R.id.main_toolbar).setNavigationOnClickListener{
            finish()
        }

        clearButton.setOnClickListener{
            searchBar.setText("")
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchBar.windowToken, 0)
        }

        val searchBarTextWatcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchBarValue = s

                clearButton.visibility = when(s.isNullOrEmpty()){
                    true -> View.GONE
                    false -> View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }

        searchBar.addTextChangedListener(searchBarTextWatcher)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(SEARCHBAR, searchBarValue.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchBarValue = savedInstanceState.getString(SEARCHBAR, SEARCHBAR_VALUE_DEFAULT)
    }

    companion object{
        const val SEARCHBAR= "SEARCHBAR"
        const val SEARCHBAR_VALUE_DEFAULT = ""
    }
}