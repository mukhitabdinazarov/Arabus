package com.jibergroup.arabus.ui.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jibergroup.arabus.data.db.entities.Word
import com.jibergroup.arabus.data.db.entities.WordModel
import com.jibergroup.arabus.data.repositories.WordRepository
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: WordRepository
) : ViewModel() {

    private var searchMutableLiveData = MutableLiveData<SearchState>()
    val searchLiveData = searchMutableLiveData

    init {
        searchLiveData.value = SearchState.NONE
    }

    fun onSearch(similarWord: String) {
        if (similarWord.isBlank()) {
            searchMutableLiveData.value = SearchState.EMPTY
        } else {
            searchMutableLiveData.value = SearchState.SEARCHABLE
            loadWords(similarWord)
        }
    }

    private var _wordModelList = MutableLiveData<List<Word>>()
    val wordModelList = _wordModelList
    fun loadWords(similarWord: String) {
        viewModelScope.launch {
            _wordModelList.value = repository.loadWords(similarWord)

        }
    }

    fun onChangeWord(word: Word){
        viewModelScope.launch {
            repository.onUpdateWordFavorite(word)
        }
    }
}