package com.example.guitar_share.di

import com.example.guitar_share.data.repository.AuthRepositoryImpl
import com.example.guitar_share.domain.repository.AuthRepository
import com.example.guitar_share.domain.usecase.GetCurrentUserIdUseCase
import com.example.guitar_share.domain.usecase.GetUserUseCase
import com.example.guitar_share.domain.usecase.IsUserLoggedInUseCase
import com.example.guitar_share.domain.usecase.LoginUseCase
import com.example.guitar_share.domain.usecase.LogoutUseCase
import com.example.guitar_share.domain.usecase.RegisterUseCase
import com.example.guitar_share.presentation.screens.login.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Repository
    single<AuthRepository> { AuthRepositoryImpl() }

    // Use Cases
    factory { LoginUseCase(get()) }
    factory { RegisterUseCase(get()) }
    factory { GetUserUseCase(get()) }
    factory { LogoutUseCase(get()) }
    factory { GetCurrentUserIdUseCase(get()) }
    factory { IsUserLoggedInUseCase(get()) }
    factory { com.example.guitar_share.domain.usecase.UpdateUserUseCase(get()) }
    factory { com.example.guitar_share.domain.usecase.CheckStreakUseCase(get()) }
    factory { com.example.guitar_share.domain.usecase.MarkLessonCompletedUseCase(get()) }

    single { com.example.guitar_share.data.repository.LessonRepository(get()) }
    
    // ViewModel
    single { AuthViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { com.example.guitar_share.presentation.screens.lesson.LessonViewModel(get()) }

    // Network
    single<retrofit2.Retrofit> {
        retrofit2.Retrofit.Builder()
            .baseUrl("https://api.uberchord.com/v1/")
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .build()
    }

    single {
        get<retrofit2.Retrofit>().create(com.example.guitar_share.data.remote.UberChordApi::class.java)
    }
}
