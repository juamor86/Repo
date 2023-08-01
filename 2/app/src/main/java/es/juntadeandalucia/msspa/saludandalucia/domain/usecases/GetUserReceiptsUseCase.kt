package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.UserRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.BeneficiaryEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.BeneficiaryMapper
import io.reactivex.Single

class GetUserReceiptsUseCase(
    private val userRepositoryFactory: UserRepositoryFactory
) :
    SingleUseCase<List<BeneficiaryEntity>>() {

    override fun buildUseCase(): Single<List<BeneficiaryEntity>> {
        return userRepositoryFactory.create(strategy = Strategy.NETWORK).run {
            getUserBeneficiaries().map {
                BeneficiaryMapper.convert(it)
            }
        }
    }
}
