package com.jibergroup.arabus.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.jibergroup.arabus.R
import com.jibergroup.arabus.data.db.entities.Word
import kotlinx.android.synthetic.main.item_words_layout.view.*

class WordAdapter(
    private var wordsList: List<Word>,
    private val wordDetailClickListener: ((item: Word) -> Unit)? = null,
    private val wordChangeStateListener: ((word: Word) -> Unit)? = null
) : RecyclerView.Adapter<WordAdapter.WordViewHolder>() {

    override fun getItemCount() = wordsList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_words_layout, parent, false)
        return WordViewHolder(view)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word = wordsList[position]
        holder.itemView.arabic_text.text = word.arabic
        holder.itemView.kazakh_text.text = word.kazakh

        holder.itemView.switchView.isChecked = word.isFavorite
        holder.itemView.switchView.setOnCheckedChangeListener { buttonView, isChecked ->
            wordsList[holder.adapterPosition].isFavorite = isChecked
            wordChangeStateListener?.invoke(wordsList[holder.adapterPosition])
        }

        holder.itemView.setOnClickListener {
            wordDetailClickListener?.invoke(wordsList[holder.adapterPosition])
        }
    }

    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    fun setList(list: List<Word>) {
        wordsList = list
        notifyDataSetChanged()
    }

}