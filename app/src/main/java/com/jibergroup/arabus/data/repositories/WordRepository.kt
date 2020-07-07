package com.jibergroup.arabus.data.repositories

import com.jibergroup.arabus.data.db.WordsDao
import com.jibergroup.arabus.data.db.entities.Word

class WordRepository(
    private val wordsDao: WordsDao
) {

    suspend fun loadWords(similarWord: String): List<Word>? {
        if(similarWord.isNullOrBlank()){
            return null
        }
        return wordsDao.loadWords("%$similarWord%")
    }

    suspend fun loadRootWords(wordId: Int): List<Word>?{
        val rootWords = wordsDao.loadWordModelById(wordId).rootWords
        val listOfRootIds = ArrayList<Int?>()
        val l = if (rootWords != null) {
            for(rootWord in rootWords){
                listOfRootIds.add(rootWord?.main_word_id)
            }
            wordsDao.loadWordById(listOfRootIds)
        } else{
            null
        }

        return l
    }

    suspend fun onUpdateWordFavorite(word: Word){
        wordsDao.updateWordFavorite(wordId = word.id, favorite = word.isFavorite)
    }

    suspend fun onLoadFavoriteWords(): List<Word>{
        return wordsDao.loadFavoriteWords()
    }
}