package es.inteco.conanmobile.domain.base.scheduler

import io.reactivex.rxjava3.schedulers.Schedulers


/**
 * Trampoline scheduler provider
 *
 * @constructor Create empty Trampoline scheduler provider
 */
class TrampolineSchedulerProvider : BaseSchedulerProvider {
    override fun computation() = Schedulers.trampoline()
    override fun ui() = Schedulers.trampoline()
    override fun io() = Schedulers.trampoline()
}
