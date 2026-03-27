package com.seenu.dev.android.smartstep.ai_coach.di

import com.seenu.dev.android.smartstep.ai_coach.data.AiInsightsConfig
import com.seenu.dev.android.smartstep.ai_coach.data.AiInsightsRepository
import com.seenu.dev.android.smartstep.ai_coach.data.GeminiRepository
import com.seenu.dev.android.smartstep.ai_coach.data.InMemoryAiInsightsRepository
import com.seenu.dev.android.smartstep.ai_coach.presentation.AiCoachViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val aiCoachModule = module {
    single { GeminiRepository() }
    // Inject ApplicationScope into the in-memory repo for structured, app-wide in-flight de-duplication
    single<AiInsightsRepository> { InMemoryAiInsightsRepository(get(), AiInsightsConfig(), appScope = get()) }
    viewModel { params ->
        AiCoachViewModel(
            geminiRepository = get(),
            connectivityObserver = get(),
            currentSteps = params.get(),
            stepGoal = params.get()
        )
    }
}
