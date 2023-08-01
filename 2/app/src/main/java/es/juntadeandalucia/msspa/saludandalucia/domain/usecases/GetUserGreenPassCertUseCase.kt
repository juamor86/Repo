package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import android.content.Context
import es.juntadeandalucia.msspa.saludandalucia.data.factory.UserRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.data.utils.ApiConstants
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.GreenPassCertEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.Session
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.GreenPassMapper
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils

class GetUserGreenPassCertUseCase(
    private val context: Context,
    private val session: Session,
    private val greenPassRepositoryFactory: UserRepositoryFactory
) : SingleUseCase<GreenPassCertEntity>() {

    private lateinit var type: String
    private var accessToken: String? = null

    fun params(type: String, accessToken: String?) {
        this.type = type
        this.accessToken = accessToken
    }

    override fun buildUseCase() =
        greenPassRepositoryFactory.create(Strategy.NETWORK).getGreenPass(
            type,
            accessToken?.run { if (this.contains(ApiConstants.Common.BEARER_TOKEN)) this else "${ApiConstants.Common.BEARER_TOKEN} $this" })
            .map { data ->
                val user = GreenPassMapper.convert(data, type).apply {
                    nuhsa = session.msspaAuthenticationEntity.msspaAuthenticationUser!!.nuhsa
                    val bitmap = Utils.generateQR(context, qr)
                    bitmap.let {
                        qrBitmap = it!!
                    }
                }
                user
            }
}
