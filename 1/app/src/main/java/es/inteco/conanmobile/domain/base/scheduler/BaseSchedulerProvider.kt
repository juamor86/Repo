package es.inteco.conanmobile.domain.base.scheduler

import io.reactivex.rxjava3.core.Scheduler


/**
 * Base scheduler provider
 *
 * @constructor Create empty Base scheduler provider
 */
interface BaseSchedulerProvider {
    /**
     * Io
     *
     * @return
     */
    fun io(): Scheduler

    /**
     * Computation
     *
     * @return
     */
    fun computation(): Scheduler

    /**
     * Ui
     *
     * @return
     */
    fun ui(): Scheduler
}
