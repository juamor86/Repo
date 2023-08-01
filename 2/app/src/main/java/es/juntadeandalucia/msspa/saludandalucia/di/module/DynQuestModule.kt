package es.juntadeandalucia.msspa.saludandalucia.di.module

import dagger.Module
import dagger.Provides
import es.juntadeandalucia.msspa.saludandalucia.data.factory.DynamicQuestionnairesRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.di.scope.PerFragment
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetDynQuestListUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetNewDynQuestUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.SendDynQuestAnswersUseCase

@Module
class DynQuestModule {

    //region - Use cases
    @PerFragment
    @Provides
    fun provideGetNewDynQuestUseCase(dynQuestRepositoryFactory: DynamicQuestionnairesRepositoryFactory): GetNewDynQuestUseCase =
        GetNewDynQuestUseCase(dynQuestRepositoryFactory)

    @PerFragment
    @Provides
    fun provideGetDynQuestListUseCase(dynQuestRepositoryFactory: DynamicQuestionnairesRepositoryFactory): GetDynQuestListUseCase =
        GetDynQuestListUseCase(dynQuestRepositoryFactory)

    @PerFragment
    @Provides
    fun provideSendDynQuestAnswersUseCase(dynQuestRepositoryFactory: DynamicQuestionnairesRepositoryFactory): SendDynQuestAnswersUseCase =
        SendDynQuestAnswersUseCase(dynQuestRepositoryFactory)

    //endregion
}