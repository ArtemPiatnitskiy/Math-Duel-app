package com.example.math_duel.di.modules

import com.example.math_duel.data.GameRepositoryImpl
import com.example.math_duel.domain.api.GameRepository
import com.example.math_duel.domain.usecases.CheckAnswerUseCase
import com.example.math_duel.domain.usecases.GetQuestionUseCase
import com.example.math_duel.presentation.viewmodel.GameViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin модуль для настройки зависимостей
 */
val appModule = module {
    single<GameRepository> { GameRepositoryImpl() }

    factory { GetQuestionUseCase(get()) }
    factory { CheckAnswerUseCase() }

    viewModel { GameViewModel(get(), get()) }
}


