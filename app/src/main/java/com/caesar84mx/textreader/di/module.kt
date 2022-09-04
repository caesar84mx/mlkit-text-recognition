package com.caesar84mx.textreader.di

import com.caesar84mx.textreader.ui.home.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val module = module {
    viewModel { HomeViewModel() }
}