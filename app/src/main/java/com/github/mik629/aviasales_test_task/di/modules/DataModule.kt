package com.github.mik629.aviasales_test_task.di.modules

import com.github.mik629.aviasales_test_task.data.repositories.DestinationsRepositoryImpl
import com.github.mik629.aviasales_test_task.domain.DestinationsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class DataModule {
    @Provides
    @Singleton
    fun provideDestinationsRepository(repository: DestinationsRepositoryImpl): DestinationsRepository =
        repository
}
