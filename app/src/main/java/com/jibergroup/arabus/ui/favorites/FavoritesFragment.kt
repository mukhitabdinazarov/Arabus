package com.jibergroup.arabus.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.jibergroup.arabus.R
import com.jibergroup.arabus.data.db.entities.Word
import com.jibergroup.arabus.databinding.FragmentFavoritesBinding
import com.jibergroup.arabus.ui.adapters.WordAdapter
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_word_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule


class FavoritesFragment : Fragment() {

    private var binding: FragmentFavoritesBinding? = null
    private val mViewModel: FavoritesViewModel by viewModel()

    private val adapter = WordAdapter(wordsList = ArrayList(),
        wordChangeStateListener = { word -> onWordStateChangeListener(word) })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_favorites, container, false
        )
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView.adapter = adapter
        mViewModel.onLoadFavoriteWords()

        mViewModel.wordModelList.observe(viewLifecycleOwner, Observer { list ->
            if (list.isNullOrEmpty()) {
                binding?.dataIsEmpty = true
            } else {
                binding?.dataIsEmpty = false
                adapter.setList(list)
            }
        })

        backBtn.setOnClickListener {
            Timer().schedule(100) {
                findNavController().navigateUp()
            }
        }

    }

    private fun onWordStateChangeListener(word: Word) {
        mViewModel.onChangeWord(word)
    }
}