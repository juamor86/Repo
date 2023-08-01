package es.inteco.conanmobile.domain.base

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Params single use case
 *
 * @param T
 * @param Params
 * @constructor Create empty Params single use case
 */
abstract class ParamsSingleUseCase<T, Params> : UseCaseDisposable() {

    /**
     * Execute
     *
     * @param params
     * @param onSuccess
     * @param onError
     * @receiver
     * @receiver
     */
    fun execute(
        params: Params,
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
    ) = addDispose(createSingle(params).subscribe(onSuccess, onError))

    private fun createSingle(params: Params) =
        buildUseCase(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    /**
     * Build use case
     *
     * @param params
     * @return
     */
    abstract fun buildUseCase(params: Params): Single<T>
}
