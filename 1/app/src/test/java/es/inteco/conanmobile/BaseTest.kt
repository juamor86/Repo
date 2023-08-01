package es.inteco.conanmobile

import es.inteco.conanmobile.data.entities.Evidence
import es.inteco.conanmobile.data.entities.IpBotnetData
import es.inteco.conanmobile.data.entities.OperatingSystem
import es.inteco.conanmobile.domain.entities.*
import es.inteco.conanmobile.utils.Consts
import java.util.*

open class BaseTest {

    companion object {
        const val ANY_TIMESTAMP = 0L
        const val ANY_TIMESTAMP_STR = "timestamp"
        const val ANY_STATUS = 0L
        const val ANY_STATUS_INT = 0
        const val ANY_STATUS_MESSAGE = "statusMessage"
        const val ANY_ID_TERMINAL = "1234567890"
        const val ANY_KEY = "key"
        const val ANY_VALUE = "value"
        const val ANY_PATH = "path"
        const val ANY_IP = "123.123.123.123"
        const val ANY_ERROR = "error"
        const val ANY_BOOLEAN = false
        const val ANY_NAME = "name"
        const val ANY_THREATCODE = "threatcode"
        const val ANY_DESCRIPTION_URL = "descriptionurl"
        const val ANY_OPERATING_SYSTEM = "operatingSysttem"
        const val ANY_DESINFECT_URL = "desinfectUrl"
        const val ANY_CODE = "anycode"
        const val ANY_TYPE = "anytype"
        const val ANY_COMPARABLE = true
        const val ANY_VALORATION = "anyvaloration"
        const val ANY_SHOWRESULT = true
        const val ANY_CONFIGURATION_VERSION = "1"
    }

    val fakeIpBotnetData = IpBotnetData(
        "123.123.123.123", "fake_error", listOf(
            Evidence(
                "fakename",
                "fakeThretcode",
                emptyList<OperatingSystem>(),
                "description",
                "timestamp"
            )
        )
    )

    val fakeIpBotnetEntity = IpBotnetEntity(
        "123.123.123.123", "fake_error", listOf(
            EvidenceEntity(
                "fakename",
                "fakeThretcode",
                emptyList<OperatingSystemEntity>(),
                "description",
                "timestamp"
            )
        )
    )

    val fakeIdDevice = "1234567890"
    val fakeRegisteredDevice = RegisteredDeviceEntity(
        timestamp = 0,
        status = 0,
        statusMessage = "",
        message = MessageImeiEntity(idTerminal = "", key = ANY_KEY),
        path = ""
    )
    val fakeDefaultAnalysis = AnalysisEntity(null)
    private val fakeAdministrationTimeBetweenAnalysis =
        AdministrationEntity(key = Consts.TIME_BETWEEN_ANALYSIS, "2000")
    private val fakeAdministrationRecommendedTimeBetweenAnalysis =
        AdministrationEntity(key = Consts.RECOMMENDED_TIME_BETWEEN_ANALYSIS, "2000")
    val fakeConfiguration: ConfigurationEntity = ConfigurationEntity(
        timestamp = "", status = 0, statusMessage = "", message = MessageEntity(
            id = "",
            version = "0",
            expirationDate = "",
            analysis = listOf(fakeDefaultAnalysis),
            administration = listOf(
                fakeAdministrationTimeBetweenAnalysis,
                fakeAdministrationRecommendedTimeBetweenAnalysis
            ),
            about = "",
            legal = "",
            permissions = emptyMap()
        ), path = ""
    )
    val fakeAnalysis = AnalysisEntity("").apply { names = listOf(AdministrationEntity("", "")) }
    val fakeModuleResult = ModuleResultEntity(
        item = ModuleEntity(
            names = emptyList(),
            descriptions = "",
            valoration = "",
            code = "",
            showResult = false,
            comparable = false,
            assessment = AssessmentEntity(),
            type = ModuleEntity.AnalysisType.SETTING
        )
    )
    val fakeAnalysisResult = AnalysisResultEntity(
        analysisEntity = fakeAnalysis,
        date = Date(System.currentTimeMillis() - Consts.ONE_DAY_MILLIS)
    )
    val fakePendingWarnings = PendingWarningsEntity(
        timestamp = "",
        status = 0,
        statusMessage = "",
        message = PendingWarningsMessageEntity(haveNotifications = false),
        path = ""
    )

    val fakePermissionEntity = PermissionEntity()
}
