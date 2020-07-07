package com.jibergroup.arabus.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.jibergroup.arabus.data.db.entities.RootWord
import com.jibergroup.arabus.data.db.entities.Word
import com.jibergroup.arabus.data.db.entities.WordModel

@Dao
interface WordsDao {

    fun insertWords(word: Word, rootWords: List<Int>?) {
        val rootWordList = ArrayList<RootWord>()
        if (rootWords != null) {
            for (mainWordId in rootWords) {
                val rootWord = RootWord(root_word_id = word.id, main_word_id = mainWordId)
                rootWordList.add(rootWord)
            }
            insertWord(word)
            insertRootWords(rootWordList)
        } else {

            insertWord(word)
        }
    }

    @Insert
    fun insertRootWords(rootWords: List<RootWord>?)

    @Insert
    fun insertWord(user: Word?)

    @Query("SELECT * FROM words")
    suspend fun loadAllWords(): List<WordModel>

    @Query("SELECT * FROM words WHERE id = :wordId")
    suspend fun loadWordModelById(wordId: Int): WordModel

    @Query("SELECT * FROM words WHERE id in (:wordList)")
    suspend fun loadWordById(wordList: List<Int?>): List<Word>

    @Query("SELECT * FROM words WHERE arabic LIKE :word OR kazakh LIKE :word OR description LIKE :word order by id LIMIT 30")
    suspend fun loadWords(word: String): List<Word>

    @Query("SELECT * FROM words WHERE isFavorite = 1 ORDER BY id")
    suspend fun loadFavoriteWords(): List<Word>

    @Query("UPDATE words SET isFavorite=:favorite WHERE id = :wordId")
    suspend fun updateWordFavorite(wordId: Int, favorite: Boolean)
}