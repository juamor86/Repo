package es.juntadeandalucia.msspa.saludandalucia.domain.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class UseCaseDisposable {

    private val disposable = CompositeDisposable()

    fun addDispose(disposable: Disposable) = this.disposable.add(disposable)

    open fun clear() {
        if (!disposable.isDisposed) {
            disposable.clear()
        }
    }
}
