package es.inteco.conanmobile.domain.mappers

import es.inteco.conanmobile.BaseTest
import es.inteco.conanmobile.data.entities.ModuleItem
import es.inteco.conanmobile.domain.entities.AssessmentEntity
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ModuleItemMapperTest : BaseTest() {

    private val data = ModuleItem(
        emptyList(),
        emptyList(),
        ANY_VALORATION,
        ANY_CODE,
        ANY_SHOWRESULT,
        ANY_COMPARABLE,
        AssessmentEntity("NEUTRAL"),
        ANY_VALORATION
    )
    private val dataList = listOf(data)

    @Test
    fun test_constructor() {
        val mapper = ModuleItemMapper()

        assertNotNull(mapper)
    }

    @Test
    fun convert_configuration_should_return_entity() {
        val entity = ModuleItemMapper.convert(data)
        assertEquals(entity.code, data.code)
        assertEquals(entity.showResult, data.showResult)
        assertEquals(entity.comparable, data.comparable)
        assertEquals(entity.valoration, data.valoration)
        assertEquals(entity.assessment.criticality, data.assessment.criticality)
        assertEquals(entity.names.size, data.names.size)
    }

    @Test
    fun convert_should_return_a_list_of_entities() {
        val entitiesList = ModuleItemMapper.convert(dataList)
        assertEquals(entitiesList.size, dataList.size)
    }

}