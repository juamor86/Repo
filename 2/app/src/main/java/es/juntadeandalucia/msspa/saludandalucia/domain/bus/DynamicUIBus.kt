package es.juntadeandalucia.msspa.saludandalucia.domain.bus

import android.content.Context
import es.juntadeandalucia.msspa.saludandalucia.data.factory.DynamicUIRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleSubjectUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicHomeEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicMenuEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicReleasesSectionEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicSectionEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.DynamicReleasesMapper
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.DynamicUIMapper
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.FileUtils
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DynamicUIBus @Inject constructor(
    private val context: Context,
    private val dynamicUIRepositoryFactory: DynamicUIRepositoryFactory
) {
    private var getMenuUseCase: SingleSubjectUseCase<DynamicMenuEntity> =
        object : SingleSubjectUseCase<DynamicMenuEntity>() {
            override fun buildUseCase(strategy: Strategy): Single<DynamicMenuEntity> {
                return dynamicUIRepositoryFactory.create(strategy).getMenu()
                    .map {
                        if(strategy == Strategy.NETWORK) FileUtils.writeToFile(it, Consts.FILE_MENU, context)
                        DynamicUIMapper.convertTo(it)
                    }
            }
        }

    private var getHomeUseCase: SingleSubjectUseCase<DynamicHomeEntity> =
        object : SingleSubjectUseCase<DynamicHomeEntity>() {
            override fun buildUseCase(strategy: Strategy): Single<DynamicHomeEntity> {
                return dynamicUIRepositoryFactory.create(strategy).getHome()
                    .map {
                        if(strategy == Strategy.NETWORK) FileUtils.writeToFile(it, Consts.FILE_HOME, context)
                        DynamicUIMapper.convertTo(it) }
                    }
        }

    private var getScreenUseCase: SingleSubjectUseCase<DynamicSectionEntity> =
        object : SingleSubjectUseCase<DynamicSectionEntity>() {
            override fun buildUseCase(strategy: Strategy): Single<DynamicSectionEntity> {
                return dynamicUIRepositoryFactory.create(strategy).getScreen()
                    .map {
                        if(strategy == Strategy.NETWORK) FileUtils.writeToFile(it, Consts.FILE_SCREEN, context)
                        DynamicUIMapper.convert(it)
                    }
            }
        }

    private var getReleasesUseCase: SingleSubjectUseCase<DynamicReleasesSectionEntity> =
        object : SingleSubjectUseCase<DynamicReleasesSectionEntity>() {
            override fun buildUseCase(strategy: Strategy): Single<DynamicReleasesSectionEntity> {
                return dynamicUIRepositoryFactory.create(strategy).getReleases()
                    .map {
                        if(strategy == Strategy.NETWORK) FileUtils.writeToFile(it, Consts.FILE_RELEASES, context)
                        DynamicReleasesMapper.convert(it)
                    }
            }
        }

    fun getMenu(onSuccess: (DynamicMenuEntity) -> Unit, onError: (Throwable) -> Unit, strategy: Strategy = Strategy.NETWORK) {
        getMenuUseCase.execute(onSuccess, onError, strategy)
    }

    fun getHome(onSuccess: (DynamicHomeEntity) -> Unit, onError: (Throwable) -> Unit, strategy: Strategy = Strategy.NETWORK) {
        getHomeUseCase.execute(onSuccess, onError, strategy)
    }

    fun getScreen(onSuccess: (DynamicSectionEntity) -> Unit, onError: (Throwable) -> Unit, strategy: Strategy = Strategy.NETWORK) {
        getScreenUseCase.execute(onSuccess, onError, strategy)
    }

    fun getReleases(onSuccess: (DynamicReleasesSectionEntity) -> Unit, onError: (Throwable) -> Unit, strategy: Strategy = Strategy.NETWORK) {
        getReleasesUseCase.execute(onSuccess, onError, strategy)
    }
}
