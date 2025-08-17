package com.droidnest.tech.ziya.di

import android.content.Context
import com.droidnest.tech.ziya.data.dataStore.PreferencesManager
import com.droidnest.tech.ziya.data.local.AssetsLoader
import com.droidnest.tech.ziya.data.repository.DuaRepositoryImpl
import com.droidnest.tech.ziya.data.repository.PreferencesRepositoryImpl
import com.droidnest.tech.ziya.data.repository.HomeRepositoryImpl
import com.droidnest.tech.ziya.data.repository.SurahRepositoryImpl
import com.droidnest.tech.ziya.domain.repository.DuaRepository
import com.droidnest.tech.ziya.domain.repository.PreferencesRepository
import com.droidnest.tech.ziya.domain.repository.HomeRepository
import com.droidnest.tech.ziya.domain.repository.SurahRepository
import com.droidnest.tech.ziya.domain.use_cases.ZiyaUseCases
import com.droidnest.tech.ziya.domain.use_cases.datastore.GetNameUseCase
import com.droidnest.tech.ziya.domain.use_cases.datastore.GetSelectedDistrictUseCase
import com.droidnest.tech.ziya.domain.use_cases.datastore.GetThemeUseCase
import com.droidnest.tech.ziya.domain.use_cases.datastore.SaveNameUseCase
import com.droidnest.tech.ziya.domain.use_cases.datastore.SaveSelectedDistrictUseCase
import com.droidnest.tech.ziya.domain.use_cases.datastore.SaveThemeUseCase
import com.droidnest.tech.ziya.domain.use_cases.dua.GetDailyDuasUseCase
import com.droidnest.tech.ziya.domain.use_cases.dua.GetNamazDuasUseCase
import com.droidnest.tech.ziya.domain.use_cases.hadith.GetRandomHadithUseCase
import com.droidnest.tech.ziya.domain.use_cases.prayer_time.GetTodayPrayerTimeUseCase
import com.droidnest.tech.ziya.domain.use_cases.surah.GetSurahDetailsUseCase
import com.droidnest.tech.ziya.domain.use_cases.surah.GetSurahListUseCase
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePreferencesManager(
        @ApplicationContext context: Context
    ): PreferencesManager {
        return PreferencesManager(context)
    }


    @Provides
    @Singleton
    fun provideFirebaseAnalytics(
        @ApplicationContext context: Context
    ): FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    @Provides
    @Singleton
    fun providePreferencesRepository(
        preferencesManager: PreferencesManager
    ): PreferencesRepository {
        return PreferencesRepositoryImpl(preferencesManager)
    }

    @Provides
    @Singleton
    fun providesAssetsLoader(
        @ApplicationContext context: Context
    ): AssetsLoader {
        return AssetsLoader(context = context)
    }

    @Provides
    @Singleton
    fun providesZiyaRepository(
        assetsLoader: AssetsLoader
    ): HomeRepository {
        return HomeRepositoryImpl(
            assetsLoader = assetsLoader
        )
    }

    @Provides
    @Singleton
    fun providesDuaRepository(
        assetsLoader: AssetsLoader
    ): DuaRepository {
        return DuaRepositoryImpl(
            assetsLoader = assetsLoader
        )
    }

    @Provides
    @Singleton
    fun providesSurahRepository(
        assetsLoader: AssetsLoader
    ): SurahRepository {
        return SurahRepositoryImpl(
            assetsLoader = assetsLoader
        )
    }

    @Provides
    @Singleton
    fun providesZiyaUseCases(
        homeRepository: HomeRepository,
        dataStoreRepository: PreferencesRepository,
        surahRepository: SurahRepository,
        duaRepository: DuaRepository
    ): ZiyaUseCases {
        return ZiyaUseCases(
            todayPrayerTimeUseCase = GetTodayPrayerTimeUseCase(repository = homeRepository),
            saveDistrictUseCase = SaveSelectedDistrictUseCase(repository = dataStoreRepository),
            readDistrictUseCase = GetSelectedDistrictUseCase(repository = dataStoreRepository),
            saveNameUseCase = SaveNameUseCase(repository = dataStoreRepository),
            readNameUseCase = GetNameUseCase(repository = dataStoreRepository),
            getRandomHadithUseCase = GetRandomHadithUseCase(repository = homeRepository),
            getSurahListUseCase = GetSurahListUseCase(repository = surahRepository),
            getSurahDetailsUseCase = GetSurahDetailsUseCase(repository = surahRepository),
            getDailyDuasUseCase = GetDailyDuasUseCase(duaRepository = duaRepository),
            getNamazDuasUseCase = GetNamazDuasUseCase(duaRepository = duaRepository),
            saveThemeUseCase = SaveThemeUseCase(repository = dataStoreRepository),
            getThemeUseCase = GetThemeUseCase(repository = dataStoreRepository)
        )
    }


}