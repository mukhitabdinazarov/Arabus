package com.jibergroup.arabus.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jibergroup.arabus.R
import com.jibergroup.arabus.data.db.entities.Word
import kotlinx.android.synthetic.main.item_words_layout.view.*

class SimpleWordAdapter(

) : RecyclerView.Adapter<SimpleWordAdapter.SimpleWordViewHolder>() {

    private var wordsList: ArrayList<Word> = ArrayList()

    override fun getItemCount() = wordsList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleWordViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_simple_word_layout, parent, false)
        return SimpleWordViewHolder(view)
    }

    override fun onBindViewHolder(holder: SimpleWordViewHolder, position: Int) {
        val word = wordsList[position]
        holder.itemView.arabic_text.text = word.arabic
        holder.itemView.kazakh_text.text = word.kazakh
    }

    inner class SimpleWordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    fun addList(list: List<Word>) {
        wordsList.clear()
        wordsList.addAll(list)
        notifyDataSetChanged()
    }
}

