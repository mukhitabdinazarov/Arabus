package com.jibergroup.arabus.ui.favorites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jibergroup.arabus.data.db.entities.Word
import com.jibergroup.arabus.data.repositories.WordRepository
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val wordRepository: WordRepository
) : ViewModel() {

    private var _wordModelList = MutableLiveData<List<Word>>()
    val wordModelList = _wordModelList
    fun onLoadFavoriteWords(){
        viewModelScope.launch {
            _wordModelList.value = wordRepository.onLoadFavoriteWords()
        }
    }

    fun onChangeWord(word: Word){
        viewModelScope.launch {
            wordRepository.onUpdateWordFavorite(word)
        }
    }

}