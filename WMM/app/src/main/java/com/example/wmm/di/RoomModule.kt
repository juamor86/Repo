package com.example.wmm.di

import android.content.Context
import androidx.room.Room
import com.example.wmm.data.persistence.AppDataBase
import com.example.wmm.utils.Consts.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, AppDataBase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun providesExpenseDao(db: AppDataBase) = db.getExpenseDao()

}