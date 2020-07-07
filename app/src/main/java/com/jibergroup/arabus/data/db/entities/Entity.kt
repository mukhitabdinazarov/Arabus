package com.jibergroup.arabus.data.db.entities

import androidx.room.*
import com.google.gson.annotations.SerializedName
import java.io.Serializable


@Entity(tableName = "words")
data class Word(
    @PrimaryKey
    val id: Int,
    val arabic: String,
    val kazakh: String,
    val description: String
) : Serializable{
    @Ignore
    @SerializedName("root_words")
    var rootWords: List<Int>? = null

    var isFavorite: Boolean = false
}

@Entity(tableName = "root_words")
data class RootWord(
    var root_word_id: Int,
    val main_word_id: Int
){
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}

class WordModel {

    @Embedded
    var word: Word? = null

    @Relation(parentColumn = "id", entityColumn = "root_word_id", entity = RootWord::class)
    var rootWords: List<RootWord?>? = null
}