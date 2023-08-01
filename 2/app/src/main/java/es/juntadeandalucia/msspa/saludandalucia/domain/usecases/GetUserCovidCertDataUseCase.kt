package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import android.content.Context
import es.juntadeandalucia.msspa.saludandalucia.data.factory.UserRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.data.utils.ApiConstants
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.Session
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.UserCovidCertEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.UserCovidCertMapper
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils
import io.reactivex.Single
import javax.inject.Inject

class GetUserCovidCertDataUseCase @Inject constructor(
    private val context: Context,
    private val session: Session,
    private val userRepositoryFactory: UserRepositoryFactory
) :
    SingleUseCase<UserCovidCertEntity>() {
    private var accessToken: String? = null

    fun params(  accessToken: String?) = this.apply {
        this.accessToken = accessToken
    }

    override fun buildUseCase(): Single<UserCovidCertEntity> =
        userRepositoryFactory.create(Strategy.NETWORK).getUserCovidCert(
            accessToken?.run { if (this.contains(ApiConstants.Common.BEARER_TOKEN)) this else "${ApiConstants.Common.BEARER_TOKEN} $this" })
            .map { data ->
                val user = UserCovidCertMapper.convert(data).apply {
                    nuhsa = session.msspaAuthenticationEntity!!.msspaAuthenticationUser!!.nuhsa
                    val bitmap = Utils.generateQR(context, qr)
                    bitmap?.let {
                        qrBitmap = it
                    }
                }
                user
            }
}
