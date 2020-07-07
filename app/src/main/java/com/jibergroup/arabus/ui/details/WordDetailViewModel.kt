package com.jibergroup.arabus.ui.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jibergroup.arabus.data.db.entities.Word
import com.jibergroup.arabus.data.repositories.WordRepository
import kotlinx.coroutines.launch

class WordDetailViewModel(
    private val wordRepository: WordRepository
): ViewModel(){

    private var word: Word? = null
    fun setWord(word: Word){
        this.word = word
        onLoadRootWords(word.id)
    }

    fun getWord() : Word? = word

    private var _wordList = MutableLiveData<List<Word>>()
    val wordList = _wordList
    private fun onLoadRootWords(wordId: Int){
        viewModelScope.launch {
            _wordList.value = wordRepository.loadRootWords(wordId)
        }
    }

    fun onChangeWordFavoriteState(isFavorite: Boolean){
        word?.isFavorite = isFavorite
        viewModelScope.launch {
            word?.let{
                wordRepository.onUpdateWordFavorite(it)
            }
        }
    }

}