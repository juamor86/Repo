package es.inteco.conanmobile.domain.base.scheduler

import io.reactivex.rxjava3.schedulers.TestScheduler


/**
 * Test scheduler provider
 *
 * @property scheduler
 * @constructor Create empty Test scheduler provider
 */
class TestSchedulerProvider(private val scheduler: TestScheduler) : BaseSchedulerProvider {
    override fun computation() = scheduler
    override fun ui() = scheduler
    override fun io() = scheduler
}
