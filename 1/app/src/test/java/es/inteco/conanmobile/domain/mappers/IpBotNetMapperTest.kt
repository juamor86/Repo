package es.inteco.conanmobile.domain.mappers

import es.inteco.conanmobile.BaseTest
import es.inteco.conanmobile.data.entities.Evidence
import es.inteco.conanmobile.data.entities.IpBotnetData
import es.inteco.conanmobile.data.entities.OperatingSystem
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class IpBotNetMapperTest : BaseTest() {

    private val data = IpBotnetData(
        ip = ANY_IP, error = ANY_ERROR, evidences = listOf(
            Evidence(
                name = ANY_NAME,
                operatingSystems = listOf(
                    OperatingSystem(
                        ANY_OPERATING_SYSTEM, listOf(ANY_DESINFECT_URL)
                    )
                ),
                descriptionURL = ANY_DESCRIPTION_URL,
                threatCode = ANY_THREATCODE,
                timestamp = ANY_TIMESTAMP_STR
            )
        )
    )

    @Test
    fun test_constructor() {
        val mapper = IpBotnetMapper()

        assertNotNull(mapper)
    }

    @Test
    fun convert_should_return_entity() {
        val entity = IpBotnetMapper.convert(data)

        assertEquals(entity.ip, data.ip)
        assertEquals(entity.error, data.error)
        assertEquals(entity.evidences.first().name, data.evidences.first().name)
        assertEquals(entity.evidences.first().descriptionURL, data.evidences.first().descriptionURL)
        assertEquals(entity.evidences.first().timestamp, data.evidences.first().timestamp)
        assertEquals(entity.evidences.first().threatCode, data.evidences.first().threatCode)

        assertEquals(
            entity.evidences.first().operatingSystems.first().operatingSystem,
            data.evidences.first().operatingSystems.first().operatingSystem
        )
        assertEquals(
            entity.evidences.first().operatingSystems.first().disinfectURL.first(),
            data.evidences.first().operatingSystems.first().disinfectURL.first()
        )


    }

}