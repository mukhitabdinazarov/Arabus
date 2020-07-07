package com.jibergroup.arabus.di

import com.jibergroup.arabus.data.repositories.WordRepository
import com.jibergroup.arabus.ui.details.WordDetailViewModel
import com.jibergroup.arabus.ui.favorites.FavoritesViewModel
import com.jibergroup.arabus.ui.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {

    single {
        WordRepository(get())
    }

    viewModel {
        SearchViewModel(get())
    }
    viewModel {
        WordDetailViewModel(get())
    }
    viewModel {
        FavoritesViewModel(get())
    }

}