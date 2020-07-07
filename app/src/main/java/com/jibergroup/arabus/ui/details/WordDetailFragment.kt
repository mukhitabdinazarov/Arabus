package com.jibergroup.arabus.ui.details

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jibergroup.arabus.BuildConfig
import com.jibergroup.arabus.R
import com.jibergroup.arabus.data.db.entities.Word
import com.jibergroup.arabus.ui.adapters.SimpleWordAdapter
import com.jibergroup.arabus.utils.copyToClipboard
import kotlinx.android.synthetic.main.fragment_word_detail.*
import kotlinx.android.synthetic.main.item_words_layout.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.concurrent.schedule


class WordDetailFragment : Fragment(R.layout.fragment_word_detail) {

    private val mWordDetailViewModel: WordDetailViewModel by viewModel()
    private val mRootWordsAdapter: SimpleWordAdapter = SimpleWordAdapter()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.getSerializable("word")?.let {
            val word = it as Word
            toolbar_title.text = word.arabic
            arabic_text.text = word.arabic
            kazakh_text.text = word.kazakh
            description_text.text = word.description

            mWordDetailViewModel.setWord(word)
            switchView.isChecked = word.isFavorite
        }

        rootWordRecyclerView.isNestedScrollingEnabled = true
        rootWordRecyclerView.adapter = mRootWordsAdapter

        listeners()
        initObservables()

    }

    private fun listeners() {
        backBtn.setOnClickListener {
            Timer().schedule(100) {
                findNavController().navigateUp()
            }
        }

        copyBtn.setOnClickListener {
            activity?.copyToClipboard(mWordDetailViewModel.getWord()?.arabic)
            Toast.makeText(
                activity,
                getString(R.string.text_successfully_copied),
                Toast.LENGTH_SHORT
            ).show()
        }

        shareBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
            val message = mWordDetailViewModel.getWord()?.arabic.toString() +
                    "\n\nАрабша - қазақша мобильдік қосымшаны жүктеп алыңыз.\n" +
                    "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n"
            intent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(Intent.createChooser(intent, getString(R.string.intent_choser_title)))
        }

        switchView.setOnCheckedChangeListener { _ , isChecked ->
            mWordDetailViewModel.onChangeWordFavoriteState(isChecked)
        }

    }

    private fun initObservables() {
        mWordDetailViewModel.wordList.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { list ->
                if (!list.isNullOrEmpty()) {
                    mRootWordsAdapter.addList(list)
                    if (rootWordLayout.visibility == View.GONE) {
                        rootWordLayout.visibility = View.VISIBLE
                    }
                } else {
                    rootWordLayout.visibility = View.GONE
                }
            })
    }
}