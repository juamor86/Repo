package es.juntadeandalucia.msspa.saludandalucia.data.factory

import es.juntadeandalucia.msspa.saludandalucia.data.factory.base.BaseRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.data.repository.mock.LikeItRepositoryMockImpl
import es.juntadeandalucia.msspa.saludandalucia.data.repository.network.LikeItRepositoryNetworkImpl
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.FeedbackRepository

class FeedbackRepositoryFactory(
    private val likeItRepositoryMockImpl: LikeItRepositoryMockImpl,
    private val likeItRepositoryNetworkImpl: LikeItRepositoryNetworkImpl
) : BaseRepositoryFactory<FeedbackRepository>() {

    override fun create(strategy: Strategy): FeedbackRepository =
        when (strategy) {
            Strategy.NETWORK -> likeItRepositoryNetworkImpl
            else -> likeItRepositoryMockImpl
        }

}