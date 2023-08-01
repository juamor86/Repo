package es.inteco.conanmobile.domain.base.scheduler

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers


/**
 * Scheduler provider
 *
 * @constructor Create empty Scheduler provider
 */
class SchedulerProvider : BaseSchedulerProvider {
    override fun computation() = Schedulers.computation()
    override fun ui() = AndroidSchedulers.mainThread()
    override fun io() = Schedulers.io()
}
