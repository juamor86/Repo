package es.juntadeandalucia.msspa.saludandalucia.data.api

import com.google.gson.JsonObject
import es.juntadeandalucia.msspa.saludandalucia.BuildConfig
import es.juntadeandalucia.msspa.saludandalucia.data.entities.AnnouncementData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.AppData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.BeneficiaryListData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.CommunicationData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.DynamicHomeData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.DynamicMenuData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.DynamicScreenData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.FeaturedData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.GreenPassCertData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.MSSPAJsonResponseData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.MSSPARequestData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.MeasureHelperData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.MeasurementsData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.QuestionnaireData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.QuizData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.QuizQuestionsRequestData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.QuizResultData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.SendMonitoringAnswersResponseData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.SendNewMonitoringAnswerData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.UserCovidCertData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.VerificationCodeResponseData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.*
import es.juntadeandalucia.msspa.saludandalucia.data.entities.dynamic_questionnaires.DynQuestListData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.dynamic_questionnaires.NewDynQuestData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.dynamic_questionnaires.SendDynQuestAnswersResponseData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.dynamic_questionnaires.SendNewDynQuestAnswerData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.monitoring.MonitoringData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.monitoring.MonitoringListData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.monitoring.NewMonitoringProgramData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.news.MSSPANewsResponseData
import es.juntadeandalucia.msspa.saludandalucia.data.utils.ApiConstants
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface MSSPAApi {

    companion object {
        private const val URL_NEWS = "${ApiConstants.General.URL_API_V1}/noticias"
        private const val URL_APPS = "${ApiConstants.General.URL_API_V1}/aplicaciones/movil"
        private const val URL_QUIZ =
            "${ApiConstants.General.URL_API_V1}/usuarios/cuestionarios/covid"
        private const val URL_RESPONSES =
            "${ApiConstants.General.URL_API_V1}/usuarios/cuestionarios/respuestas"
        private const val URL_VERIFICATION_CODE =
            "${ApiConstants.General.URL_API_V1}/accesos\$obtenerCodigoVerificacion"
        private const val URL_SUBSCRIPTION =
            "${ApiConstants.General.URL_API_V1}/usuarios/aplicaciones/notificaciones/suscripcion"
        private const val URL_APPOINTMENT =
            "${ApiConstants.General.URL_API_V1}/usuarios/cuestionarios/cita/ciudadano"
        private const val URL_COVID_CERT =
            "${ApiConstants.General.URL_API_V1}/usuarios/historia/vacunas/certificado"
        private const val URL_COVID_QR =
            "${ApiConstants.General.URL_API_V1}/totp"
        private const val URL_GREENPASS =
            "${ApiConstants.General.URL_API_V1}/usuarios/historia/certificado"
        private const val URL_RECEIPTS =
            "${ApiConstants.General.URL_API_V1}/usuarios/beneficiario"
        private const val URL_DYNAMIC_SCREEN =
            "${ApiConstants.General.URL_API_V1}/aplicaciones/contenido"
        private const val URL_DYNAMIC = "${ApiConstants.General.URL_API_V1}/aplicaciones/contenido"
        private const val URL_TRUSTLIST =
            "https://cvd-gcp.sanidad.gob.es/cvdcovid-gcp/spth/listarCertificados"
        private const val URL_MONITORING =
            "${ApiConstants.General.URL_API_V1}/usuarios/cuestionarios"

        private const val URL_NEW_MONITORING =
            "${ApiConstants.General.URL_API_V1}/usuarios/cuestionarios/{id}"

        private const val URL_MONITORING_LIST =
            "${ApiConstants.General.URL_API_V1}/usuarios/cuestionarios/respuestas"

        private const val URL_SEND_NEWMONITORING_ANSWERS =
            "${ApiConstants.General.URL_API_V1}/usuarios/cuestionarios/respuestas"

        private const val URL_QUESTIONNAIRE =
            "${ApiConstants.General.URL_API_V1}/usuarios/cuestionarios/{id}"
        private const val URL_MEASURE =
            "${ApiConstants.General.URL_API_V1}/usuarios/cuestionarios/respuestas"
        private const val URL_NOTIFICATION_RECEIVED =
            "${ApiConstants.General.URL_API_V1}/notificaciones/\$confirmarRecepcion"

        private const val URL_NOTIFICATION_READED =
            "${ApiConstants.General.URL_API_V1}/notificaciones/\$confirmarLectura"
        private const val URL_HELP_MEASURE = "${ApiConstants.General.URL_API_V1}/usuarios/cuestionarios/AyudaMedicion"
        private const val URL_ADVICES =
            "${ApiConstants.General.URL_API_V1}/usuarios/aplicaciones/notificaciones/suscripciones"
        private const val URL_ADVICES_TYPES =
            "${ApiConstants.General.URL_API_V1}/usuarios/aplicaciones/notificaciones/suscripciones/catalogo"
        private const val URL_ADVICES_ID =
            "${ApiConstants.General.URL_API_V1}/usuarios/aplicaciones/notificaciones/suscripciones/{id}"
        private const val URL_FEEDBACK = "${ApiConstants.General.URL_API_V1}/aplicaciones/contenido"
    }

    @GET(URL_FEEDBACK)
    fun getLikeIt(
        @Query(ApiConstants.General.APP_VERSION_PARAM) appVersion: String = BuildConfig.VERSION,
        @Query(ApiConstants.General.ID_SO_PARAM) idSo: String = "0",
        @Query(ApiConstants.General.SCOPE_PARAM) scope: String = ApiConstants.General.SCOPE,
        @Query(ApiConstants.AppsApi.TYPE) type: String = ApiConstants.General.ACTIONS
    ): Single<LikeItResponseData>

    @GET(URL_MONITORING)
    fun getMonitoring(
        @Header(ApiConstants.Monitoring.VERSION_PARAM) version: String = ApiConstants.Monitoring.VERSION_1_0
    ): Single<MonitoringData>

    @GET(URL_MONITORING_LIST)
    fun getMonitoringList(
        @Header(ApiConstants.Monitoring.VERSION_PARAM) version: String = ApiConstants.Monitoring.VERSION_1_0,
        @Query(ApiConstants.Monitoring.QUESTIONNAIRE_PARAM) questionnaireId: String,
        @Query(ApiConstants.Common.PAGE) page: String,
        @Query(ApiConstants.Common.COUNT) count: String = ApiConstants.Monitoring.COUNT
    ): Single<MonitoringListData>

    @GET(URL_NEW_MONITORING)
    fun getNewMonitoring(
        @Header(ApiConstants.Monitoring.VERSION_PARAM) version: String = ApiConstants.Monitoring.VERSION_1_0,
        @Path(ApiConstants.Monitoring.PROGRAM_ID_PARAM) id: String
    ): Single<NewMonitoringProgramData>

    @POST(URL_SEND_NEWMONITORING_ANSWERS)
    fun sendNewMonitoringAnswer(
        @Header(ApiConstants.Monitoring.VERSION_PARAM) version: String = ApiConstants.Monitoring.VERSION_1_0,
        @Header(ApiConstants.Common.CONTENT_TYPE_HEADER) json: String = ApiConstants.Common.APPLICATION_JSON,
        @Body answers: SendNewMonitoringAnswerData
    ): Single<SendMonitoringAnswersResponseData>

    @GET(URL_MONITORING_LIST)
    fun getDynQuestList(
        @Header(ApiConstants.Monitoring.VERSION_PARAM) version: String = ApiConstants.Monitoring.VERSION_1_1,
        @Query(ApiConstants.Monitoring.QUESTIONNAIRE_PARAM) questionnaireId: String,
        @Query(ApiConstants.Common.PAGE) page: String,
        @Query(ApiConstants.Common.COUNT) count: String = ApiConstants.Monitoring.COUNT
    ): Single<DynQuestListData>

    @GET(URL_NEW_MONITORING)
    fun getNewDynQuest(
        @Header(ApiConstants.Monitoring.VERSION_PARAM) version: String = ApiConstants.Monitoring.VERSION_1_1,
        @Path(ApiConstants.Monitoring.PROGRAM_ID_PARAM) id: String
    ): Single<NewDynQuestData>

    @POST(URL_SEND_NEWMONITORING_ANSWERS)
    fun sendNewDynQuestAnswer(
        @Header(ApiConstants.Monitoring.VERSION_PARAM) version: String = ApiConstants.Monitoring.VERSION_1_1,
        @Header(ApiConstants.Common.CONTENT_TYPE_HEADER) json: String = ApiConstants.Common.APPLICATION_JSON,
        @Body answers: SendNewDynQuestAnswerData
    ): Single<SendDynQuestAnswersResponseData>

    @GET(URL_MEASURE)
    fun getMeasurement(
        @Query(ApiConstants.Measurement.MEASURE_TYPE_PARAM) type: String = ApiConstants.Measurement.MEASURE_TYPE_VALUE,
        @Query(ApiConstants.Common.COUNT) count: String = ApiConstants.Monitoring.COUNT,
        @Query(ApiConstants.Common.PAGE) page: Int,
        @Header(ApiConstants.Measurement.MEASURE_VERSION_PARAM) version: String = ApiConstants.Measurement.MEASURE_VERSION_VALUE
    ): Single<MeasurementsData>

    @GET(URL_HELP_MEASURE)
    fun getHelpMeasure(
        @Query(ApiConstants.Measurement.QUESTIONNAIRE_PARAM) type: String = ApiConstants.Measurement.HELPER_TYPE,
        @Header(ApiConstants.Measurement.MEASURE_VERSION_PARAM) version: String = ApiConstants.Measurement.MEASURE_VERSION_VALUE
    ): Single<MeasureHelperData>

    @GET(URL_DYNAMIC_SCREEN)
    fun getDynamicScreen(
        @Query(ApiConstants.General.APP_VERSION_PARAM) appVer: String = BuildConfig.VERSION,
        @Query(ApiConstants.General.ID_SO_PARAM) idSO: String = ApiConstants.General.ID_SO_PARAM_VALUE,
        @Query(ApiConstants.General.SCOPE_PARAM) scope: String = ApiConstants.General.SCOPE,
        @Query(ApiConstants.General.DYNAMIC_SCREEN_TYPE) type: String = ApiConstants.Params.DYNAMIC_SCREEN_PARAM_TYPE,
        @Header(ApiConstants.General.API_KEY_HEADER) apiKey: String = ApiConstants.General.SALUD_ANDALUCIA_API_KEY_IDENTIFICATION
    ): Single<DynamicScreenData>

    @GET(URL_DYNAMIC)
    fun getDynamicLayout(
        @Query(ApiConstants.General.APP_VERSION_PARAM) appVer: String = BuildConfig.VERSION,
        @Query(ApiConstants.General.ID_SO_PARAM) idSO: String = ApiConstants.General.ID_SO_PARAM_VALUE,
        @Query(ApiConstants.General.SCOPE_PARAM) scope: String = ApiConstants.General.SCOPE,
        @Query(ApiConstants.Dynamic.DYNAMIC_PARAM_TYPE) type: String = ApiConstants.Dynamic.DYNAMIC_LAYOUT_TYPE,
        @Header(ApiConstants.General.API_KEY_HEADER) apiKey: String = ApiConstants.General.SALUD_ANDALUCIA_API_KEY_IDENTIFICATION
    ): Single<DynamicHomeData>

    @GET(URL_DYNAMIC)
    fun getDynamicMenu(
        @Query(ApiConstants.General.APP_VERSION_PARAM) appVer: String = BuildConfig.VERSION,
        @Query(ApiConstants.General.ID_SO_PARAM) idSO: String = ApiConstants.General.ID_SO_PARAM_VALUE,
        @Query(ApiConstants.General.SCOPE_PARAM) scope: String = ApiConstants.General.SCOPE,
        @Query(ApiConstants.Dynamic.DYNAMIC_PARAM_TYPE) type: String = ApiConstants.Dynamic.DYNAMIC_AREAS_TYPE,
        @Header(ApiConstants.General.API_KEY_HEADER) apiKey: String = ApiConstants.General.SALUD_ANDALUCIA_API_KEY_IDENTIFICATION
    ): Single<DynamicMenuData>

    @GET(URL_DYNAMIC)
    fun getDynamicReleases(
        @Query(ApiConstants.General.APP_VERSION_PARAM) appVer: String = BuildConfig.VERSION,
        @Query(ApiConstants.General.ID_SO_PARAM) idSO: String = ApiConstants.General.ID_SO_PARAM_VALUE,
        @Query(ApiConstants.General.SCOPE_PARAM) scope: String = ApiConstants.General.SCOPE,
        @Query(ApiConstants.Dynamic.DYNAMIC_PARAM_TYPE) type: String = ApiConstants.Dynamic.DYNAMIC_RELEASES_TYPE,
        @Header(ApiConstants.General.API_KEY_HEADER) apiKey: String = ApiConstants.General.SALUD_ANDALUCIA_API_KEY_IDENTIFICATION
    ): Single<DynamicReleasesData>

    @GET(URL_GREENPASS)
    fun getGreenPass(
        @Query(ApiConstants.CovidCert.DISEASE_PARAM) disease: String = ApiConstants.CovidCert.COVID_VALUE,
        @Query(ApiConstants.CovidCert.FORMAT_HEADER) format: String = ApiConstants.CovidCert.FORMAT_HEADER_INIT,
        @Query(ApiConstants.CovidCert.TYPE_PARAM) type: String,
        @Header(ApiConstants.General.AUTHORIZATION_HEADER) accessToken: String?
    ): Single<GreenPassCertData>

    @GET(URL_NEWS)
    fun getAnnouncements(
        @Query(ApiConstants.NewsApi.TYPE) type: String = ApiConstants.NewsApi.TYPE_BANNER
    ): Single<MSSPAJsonResponseData<AnnouncementData>>

    @GET(URL_NEWS)
    fun getNews(
        @Query(ApiConstants.NewsApi.TYPE) type: String = ApiConstants.NewsApi.TYPE_CONTENT,
        @Query(ApiConstants.General.SCOPE_PARAM) scope: String = ApiConstants.General.SCOPE
    ): Single<MSSPANewsResponseData>

    @GET(URL_NEWS)
    fun getFeatured(
        @Query(ApiConstants.NewsApi.TYPE) type: String = ApiConstants.NewsApi.TYPE_FEATURED,
        @Query(ApiConstants.General.APP_VERSION_PARAM) appVer: String = BuildConfig.VERSION,
        @Query(ApiConstants.General.ID_SO_PARAM) idSO: String = ApiConstants.General.ID_SO_PARAM_VALUE
    ): Single<MSSPAJsonResponseData<FeaturedData>>

    @GET(URL_APPS)
    fun getApps(
        @Query(ApiConstants.AppsApi.TYPE) apiKey: String = ApiConstants.AppsApi.TYPE_ANDROID,
        @Query(ApiConstants.General.SCOPE_PARAM) scope: String = ApiConstants.General.SCOPE
    ): Single<MSSPAJsonResponseData<AppData>>

    @GET(URL_QUIZ)
    fun getQuizQuestions(
        @Header(ApiConstants.General.AUTHORIZATION_HEADER) token: String
    ): Single<QuizData>

    @POST(URL_RESPONSES)
    fun sendQuizQuestions(
        @Body quizQuestionResponseData: QuizQuestionsRequestData,
        @Query(ApiConstants.QuizApi.PHONE_NUMBER) phoneNumber: String,
        @Query(ApiConstants.QuizApi.TIP_PROFESSIONAL) tipProfesional: String,
        @Header(ApiConstants.General.AUTHORIZATION_HEADER) token: String?
    ): Single<QuizResultData>

    @POST(URL_VERIFICATION_CODE)
    fun requestVerificationCode(@Body verificationCodeRequestData: MSSPARequestData.VerificationCodeRequestData): Single<VerificationCodeResponseData>

    @POST(URL_SUBSCRIPTION)
    fun subscribeNotifications(
        @Header(ApiConstants.NotificationsSubscriptionApi.VERIFICATION_CODE) verificationCode: String,
        @Header(ApiConstants.NotificationsSubscriptionApi.VERIFICATION_ID) idVerification: String,
        @Body subscribeNotificationsRequestData: MSSPARequestData.SubscribeNotificationsRequestData
    ): Completable

    @PUT(URL_SUBSCRIPTION)
    fun updateNotificationsSubscription(
        @Header(ApiConstants.NotificationsSubscriptionApi.TOKEN) oldFirebaseToken: String,
        @Body subscribeNotificationsRequestData: MSSPARequestData.SubscribeNotificationsRequestData
    ): Completable

    @DELETE(URL_SUBSCRIPTION)
    fun clearNotificationsSubscription(
        @Query(ApiConstants.NotificationsSubscriptionApi.CONTACT) phoneNumber: String,
        @Query(ApiConstants.NotificationsSubscriptionApi.CHANNEL) firebaseToken: String,
        @Query(ApiConstants.NotificationsSubscriptionApi.ID_DEVICE) idDevice: String = ApiConstants.General.SALUD_ANDALUCIA_APP_KEY_IDENTIFICATION
    ): Completable

    @DELETE(URL_APPOINTMENT)
    fun cancelAppointment(): Completable

    @GET(URL_COVID_CERT)
    fun getUserCovidCert(
        @Query(ApiConstants.CovidCert.DISEASE_PARAM) enfermedad: String = ApiConstants.CovidCert.COVID_VALUE,
        @Query(ApiConstants.CovidCert.FORMAT_HEADER) action: String,
        @Header(ApiConstants.General.AUTHORIZATION_HEADER) accessToken: String?
    ): Single<UserCovidCertData>

    @GET(URL_QUESTIONNAIRE)
    @Headers(ApiConstants.QuestionnaireApi.HEADER_VERSION_MOCK)
    fun getQuestionnaire(
        @Path(
            value = "id",
            encoded = true
        ) id: String
    ): Single<QuestionnaireData>

    @GET(URL_RECEIPTS)
    fun getUserReceipts(): Single<BeneficiaryListData>

    @GET(URL_TRUSTLIST)
    fun getTrustList(): Single<List<JsonObject>>

    @POST(URL_NOTIFICATION_RECEIVED)
    fun sendNotificationReceived(
        @Body communication: CommunicationData
    ): Completable

    @POST(URL_NOTIFICATION_READED)
    fun sendNotificationRead(
        @Body communication: CommunicationData
    ): Completable

    @GET(URL_ADVICES_TYPES)
    fun getAdviceTypes(
    ): Single<AdviceTypesData>

    @GET(URL_ADVICES)
    fun getAdvices(
        @Query(ApiConstants.Advices.ADVICES_CRITERIA) nuhsa: String
    ): Single<AdviceData>

    @GET(URL_ADVICES)
    fun getAdvicesReceived(
        @Query(ApiConstants.Advices.ADVICES_CONTACT) phone : String
    ): Single<AdviceData>

    @DELETE(URL_ADVICES_ID)
    fun removeAdvice(
        @Header(ApiConstants.Advices.VERSION_PARAM) version: String = ApiConstants.Advices.ADVICES_VERSION,
        @Path(ApiConstants.Advices.ADVICES_URL_PATCH_ID) id: String,
    ): Completable

    @PUT(URL_ADVICES_ID)
    fun updateAdvice(
        @Header(ApiConstants.Advices.VERSION_PARAM) version: String = ApiConstants.Advices.ADVICES_VERSION,
        @Path(ApiConstants.Advices.ADVICES_URL_PATCH_ID) id: String,
        @Body AdviceRequestData: AdviceRequestData.Entry.Resource
    ): Completable

    @POST(URL_ADVICES)
    fun createAdvice(
        @Header(ApiConstants.Advices.VERSION_PARAM) version: String = ApiConstants.Advices.ADVICES_VERSION,
        @Body AdviceRequestData: AdviceRequestData
    ): Completable
}
