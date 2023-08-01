package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.UserRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.data.utils.ApiConstants
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleUseCase
import io.reactivex.Single
import javax.inject.Inject

class DownloadUserCovidCertPDFUseCase @Inject constructor(private val userRepositoryFactory: UserRepositoryFactory) :
    SingleUseCase<String?>() {

    private var accessToken: String? = null

    fun params(accessToken: String?) = this.apply {
        this.accessToken = accessToken
    }

    override fun buildUseCase(): Single<String?> =
        userRepositoryFactory.create(Strategy.NETWORK).getUserCovidCertPdf(
            accessToken?.run { if (this.contains(ApiConstants.Common.BEARER_TOKEN)) this else "${ApiConstants.Common.BEARER_TOKEN} $this" })
            .map {
                with(it) {
                    if (content.isNotEmpty()) {
                        content[0].attachment.data
                    } else {
                        null
                    }
                }
            }
}
