package com.pam.Dictionary.feature_dictionary.di

import android.app.Application
import androidx.room.Room
import com.pam.Dictionary.feature_dictionary.data.local.Converters
import com.pam.Dictionary.feature_dictionary.data.local.DictionaryDatabase
import com.pam.Dictionary.feature_dictionary.data.local.FavoriteWordDAO
import com.pam.Dictionary.feature_dictionary.data.local.WordInfoDao
import com.pam.Dictionary.feature_dictionary.data.remote.DictionaryApi
import com.pam.Dictionary.feature_dictionary.data.repository.FavoriteWordRepositoryImpl
import com.pam.Dictionary.feature_dictionary.data.repository.WordInfoRepositoryImpl
import com.pam.Dictionary.feature_dictionary.data.util.GsonParser
import com.pam.Dictionary.feature_dictionary.domain.repository.FavoriteWordRepository
import com.pam.Dictionary.feature_dictionary.domain.repository.WordInfoRepository
import com.pam.Dictionary.feature_dictionary.domain.usecase.GetWordInfoUseCase
import com.pam.Dictionary.feature_dictionary.domain.usecase.getAllWordInfoUseCase
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGetWordInfoUseCase(repository: WordInfoRepository): GetWordInfoUseCase {
        return GetWordInfoUseCase(repository)
    }

    @Provides
    @Singleton
    fun providegetAllWordInfoUseCase(repository: WordInfoRepository): getAllWordInfoUseCase {
        return getAllWordInfoUseCase(repository)
    }


    @Provides
    @Singleton
    fun provideWordInfoRepository(
        api: DictionaryApi,
        db: DictionaryDatabase
    ): WordInfoRepository {
        return WordInfoRepositoryImpl(api, db.wordInfoDao)
    }

    @Provides
    @Singleton
    fun provideFavoriteWordRepository(
        db: DictionaryDatabase
    ) : FavoriteWordRepository{
        return FavoriteWordRepositoryImpl(db.favoriteWordDAO)
    }

    @Provides
    @Singleton
    fun provideDictionaryDatabase(app: Application): DictionaryDatabase {
        return Room.databaseBuilder(
            app,
            DictionaryDatabase::class.java,
            "dictionary_db"
        ).addTypeConverter(Converters(GsonParser(Gson())))
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideWordInfoDao(db: DictionaryDatabase) : WordInfoDao {
        return db.wordInfoDao
    }

    @Provides
    @Singleton
    fun provideFavoriteWordDAO(db: DictionaryDatabase) : FavoriteWordDAO {
        return db.favoriteWordDAO
    }

    @Provides
    @Singleton
    fun provideDictionaryApi(): DictionaryApi {
        return Retrofit.Builder()
            .baseUrl(DictionaryApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DictionaryApi::class.java)
    }
}