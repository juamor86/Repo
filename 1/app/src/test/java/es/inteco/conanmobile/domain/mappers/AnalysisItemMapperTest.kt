package es.inteco.conanmobile.domain.mappers

import es.inteco.conanmobile.BaseTest
import es.inteco.conanmobile.data.entities.AnalysisItem
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AnalysisItemMapperTest : BaseTest() {

    private val data = AnalysisItem()
    private val dataList = listOf(data)

    @Test
    fun test_constructor() {
        val mapper = AnalysisItemMapper()

        assertNotNull(mapper)
    }

    @Test
    fun convert_should_return_entity() {
        val entity = AnalysisItemMapper.convert(data)

        assertEquals(entity.id, data.id)
        assertEquals(entity.names, data.names)
        assertEquals(entity.descriptions, data.descriptions)
        assertEquals(entity.applicationModules, data.applicationModules)
        assertEquals(entity.deviceModules, data.deviceModules)
        assertEquals(entity.systemModules, data.systemModules)
    }

    @Test
    fun convert_should_return_a_list_of_entities() {
        val entitiesList = AnalysisItemMapper.convert(dataList)

        assertEquals(entitiesList.size, dataList.size)
    }
}