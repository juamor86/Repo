package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.UserRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.data.utils.ApiConstants
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleUseCase
import io.reactivex.Single

class DownloadGreenPassPdfUseCase(private val userRepositoryFactory: UserRepositoryFactory) :
    SingleUseCase<String?>() {

    private lateinit var type: String
    private lateinit var format: String
    private var accessToken: String? = null

    fun params(format: String, type: String, accessToken: String?) {
        this.type = type
        this.format = format
        this.accessToken = accessToken
    }

    override fun buildUseCase(): Single<String?> =
        userRepositoryFactory.create(Strategy.NETWORK).getGreenPassPdf(
            format,
            type,
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
