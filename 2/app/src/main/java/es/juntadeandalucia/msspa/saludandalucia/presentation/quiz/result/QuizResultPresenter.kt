package es.juntadeandalucia.msspa.saludandalucia.presentation.quiz.result

import com.google.android.gms.common.SupportErrorDialogFragment
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AppointmentEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizResultEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.CancelAppointmentUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.CreateDelayedNotificationUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetNotificationsPhoneNumberUseCase
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import timber.log.Timber

class QuizResultPresenter(
    private val createNotificationUseCase: CreateDelayedNotificationUseCase,
    private val cancelAppointmentUseCase: CancelAppointmentUseCase,
    private val getNotificationsPhoneNumberUseCase: GetNotificationsPhoneNumberUseCase
) : BasePresenter<QuizResultContract.View>(), QuizResultContract.Presenter {

    private lateinit var resultAppointment: AppointmentEntity

    override fun setupView(quizResultEntity: QuizResultEntity?) {
        view.setupView()
        quizResultEntity?.apply {
            checkNotificationsEnabled(this)
            view.showResult(quizResultEntity)
            quizResultEntity.appointment?.let {
                resultAppointment = it
                view.showAppointment(it)
            }
        }
    }

    override fun getScreenNameTracking(): String? = Consts.Analytics.QUIZ_COVID_RESULT_SCREEN_ACCESS

    private fun checkNotificationsEnabled(quizResultEntity: QuizResultEntity) {
        getNotificationsPhoneNumberUseCase.execute(
            onSuccess = { phoneNumber ->
                if (phoneNumber.isNotEmpty()) {
                    createNotification(quizResultEntity)
                }
            },
            onError = {
                Timber.e("Error checking if notifications are enabled")
            }
        )
    }

    private fun createNotification(quizResultEntity: QuizResultEntity) {
        var delayTime = quizResultEntity.nextTryMillis - System.currentTimeMillis()
        createNotificationUseCase.params(
            title = R.string.covid_title_section,
            message = R.string.notification_quiz,
            graph = R.navigation.nav_graph,
            dest = R.id.notifications_dest,
            delayTime = delayTime
        ).execute(
            onComplete = {},
            onError = {}
        )
    }

    override fun onAcceptButtonClicked() {
        view.closeView()
    }

    override fun onAddAppointmentToCalendar() {
        view.addAppointmentToCalendar(resultAppointment)
    }

    override fun onCancelAppointment() {
        view.showCancelAppointmentDialog()
    }

    override fun onConfirmCancelAppointment() {
        cancelAppointmentUseCase.execute(
            onComplete = {
                view.showAppointmentCanceledDialog()
                view.hideAppointment()
            },
            onError = {
                Timber.e(it)
                view.showErrorAppointmentCanceledDialog()
            })
    }
}
