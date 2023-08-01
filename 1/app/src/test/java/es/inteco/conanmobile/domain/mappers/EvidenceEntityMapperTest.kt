package es.inteco.conanmobile.domain.mappers

import es.inteco.conanmobile.BaseTest
import es.inteco.conanmobile.data.entities.Evidence
import es.inteco.conanmobile.data.entities.OperatingSystem
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class EvidenceEntityMapperTest : BaseTest() {

    private val data = Evidence(
        name = ANY_NAME, threatCode = ANY_THREATCODE, operatingSystems = listOf(
            OperatingSystem(ANY_OPERATING_SYSTEM, listOf(ANY_DESINFECT_URL))
        ), timestamp = ANY_TIMESTAMP_STR, descriptionURL = ANY_DESCRIPTION_URL
    )
    private val dataList = listOf(data)

    @Test
    fun test_constructor() {
        val mapper = EvidenceEntityMapper()

        assertNotNull(mapper)
    }

    @Test
    fun convert_should_return_entity() {
        val entity = EvidenceEntityMapper.convert(data)
        assertEquals(entity.name, data.name)
        assertEquals(entity.timestamp, data.timestamp)
        assertEquals(entity.threatCode, data.threatCode)
        assertEquals(entity.descriptionURL, data.descriptionURL)

        assertEquals(
            entity.operatingSystems.first().operatingSystem,
            data.operatingSystems.first().operatingSystem
        )
    }

    @Test
    fun convert_should_return_a_list_of_entities() {
        val entitiesList = EvidenceEntityMapper.convert(dataList)

        assertEquals(entitiesList.size, dataList.size)
    }

}