package com.jibergroup.arabus.ui.search

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.jibergroup.arabus.R
import com.jibergroup.arabus.data.db.entities.Word
import com.jibergroup.arabus.ui.adapters.WordAdapter
import kotlinx.android.synthetic.main.fragment_search.*
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val REQUEST_WRITE_PERMISSION = 123

class SearchFragment : Fragment(R.layout.fragment_search),
    ActivityCompat.OnRequestPermissionsResultCallback {

    private val searchViewModel: SearchViewModel by viewModel()
    private val adapter =
        WordAdapter(wordsList = ArrayList(),
            wordDetailClickListener = { word -> onWordClickListener(word) },
            wordChangeStateListener = {word -> onWordStateChangeListener(word)})

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requestPermission()


        recyclerView.adapter = adapter

        listeners()
        initObservables()

    }

    private fun listeners() {
        clear_btn.setOnClickListener {
            search_view.setText("")
            searchViewModel.searchLiveData.value = SearchState.EMPTY
        }
    }

    private fun initObservables() {

        search_view.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchViewModel.onSearch(search_view.text.toString().trim().toLowerCase())
            }
        })

        searchViewModel.searchLiveData.observe(viewLifecycleOwner, Observer { searchState ->
            when (searchState) {
                SearchState.SEARCHABLE -> {
                    showClearButton()
                }
                SearchState.EMPTY -> {
                    hideClearButton()
                    adapter.setList(ArrayList())
                }
                SearchState.NONE -> {
                    hideClearButton()
                }
            }
        })

        searchViewModel.wordModelList.observe(viewLifecycleOwner, Observer { list ->
            list?.let {
                adapter.setList(list)
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            searchViewModel.loadWords("")
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                REQUEST_WRITE_PERMISSION
            )
        } else {
            searchViewModel.loadWords("")
        }
    }

    private fun hideClearButton() {
        clear_btn.visibility = View.INVISIBLE
    }

    private fun showClearButton() {
        if (clear_btn.visibility == View.INVISIBLE) {
            clear_btn.visibility = View.VISIBLE
        }
    }

    private fun onWordClickListener(word: Word) {
        val args = Bundle()
        args.putSerializable("word", word)
        findNavController().navigate(R.id.action_searchFragment_to_wordDetailFragment, args)
    }

    private fun onWordStateChangeListener(word: Word){
        searchViewModel.onChangeWord(word)
    }

}