package es.juntadeandalucia.msspa.authentication.domain.base

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class ParamsSingleUseCase<T, Params> : UseCaseDisposable() {

    fun execute(
        params: Params,
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
    ) = addDispose(createSingle(params).subscribe(onSuccess, onError))

    private fun createSingle(params: Params) =
        buildUseCase(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    abstract fun buildUseCase(params: Params): Single<T>
}
