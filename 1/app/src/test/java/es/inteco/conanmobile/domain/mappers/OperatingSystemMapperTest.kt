package es.inteco.conanmobile.domain.mappers

import es.inteco.conanmobile.BaseTest
import es.inteco.conanmobile.data.entities.OperatingSystem
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class OperatingSystemMapperTest : BaseTest() {

    private val data = OperatingSystem(operatingSystem = ANY_OPERATING_SYSTEM, disinfectURL = listOf(
        ANY_DESINFECT_URL))
    private val dataList = listOf(data)

    @Test
    fun test_constructor() {
        val mapper = OperatingSystemEntityMapper()

        assertNotNull(mapper)
    }

    @Test
    fun convert_configuration_should_return_entity() {
        val entity = OperatingSystemEntityMapper.convert(data)
        assertEquals(entity.operatingSystem, data.operatingSystem)
        assertEquals(entity.disinfectURL.first(), data.disinfectURL.first())
    }

    @Test
    fun convert_should_return_a_list_of_entities() {
        val entitiesList = OperatingSystemEntityMapper.convert(dataList)
        assertEquals(entitiesList.size, dataList.size)
    }

}