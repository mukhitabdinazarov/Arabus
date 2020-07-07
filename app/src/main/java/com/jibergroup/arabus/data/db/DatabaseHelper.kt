package com.jibergroup.arabus.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jibergroup.arabus.data.db.entities.RootWord
import com.jibergroup.arabus.data.db.entities.Word
import java.io.IOException
import java.util.concurrent.Executors

const val DATABASE_NAME = "arabus.db"

@Database(
    entities = [Word::class, RootWord::class],
    version = 7,
    exportSchema = false
)
abstract class DatabaseHelper : RoomDatabase() {

    abstract fun wordsDao(): WordsDao

    companion object {
        @Volatile
        private var INSTANCE: DatabaseHelper? = null
        private val IO_EXECUTOR = Executors.newSingleThreadExecutor()

        fun getInstance(context: Context): DatabaseHelper {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                DatabaseHelper::class.java, DATABASE_NAME
            )
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        val gson = Gson()

                        val database = getInstance(context)
                        val dao = database.wordsDao()
                        // insert the data on the IO Thread
                        IO_EXECUTOR.execute {
                            insertWords(context, dao, gson)
                        }
                    }
                })
                .build()

        private fun insertWords(context: Context, dao: WordsDao, gson: Gson) {
            val assets = loadDir(context) ?: return
            val type = object : TypeToken<List<Word>>() {}.type
            val words = gson.fromJson<List<Word>>(assets, type)
            words.forEach { word ->
                word.rootWords?.let {
                    dao.insertWords(word, word.rootWords)
                }
            }
        }

        private fun loadDir(context: Context, fileName: String = "words.json"): String? {
            val json: String?
            try {
                val inputStream = context.assets.open(fileName)
                val size = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                inputStream.close()
                json = String(buffer, Charsets.UTF_8)
            } catch (ex: IOException) {
                ex.printStackTrace()
                return null
            }

            return json
        }
    }
}