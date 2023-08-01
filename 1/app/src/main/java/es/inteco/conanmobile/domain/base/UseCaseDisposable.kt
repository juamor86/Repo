package es.inteco.conanmobile.domain.base

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable


/**
 * Use case disposable
 *
 * @constructor Create empty Use case disposable
 */
abstract class UseCaseDisposable {

    private val disposable = CompositeDisposable()

    /**
     * Add dispose
     *
     * @param disposable
     */
    fun addDispose(disposable: Disposable) = this.disposable.add(disposable)

    /**
     * Clear
     *
     */
    open fun clear() {
        if (!disposable.isDisposed) {
            disposable.clear()
        }
    }
}
