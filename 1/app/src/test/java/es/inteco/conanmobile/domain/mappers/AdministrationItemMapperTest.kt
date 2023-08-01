package es.inteco.conanmobile.domain.mappers

import es.inteco.conanmobile.BaseTest
import es.inteco.conanmobile.data.entities.AdministrationItem
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AdministrationItemMapperTest : BaseTest() {

    private val data = AdministrationItem(key = ANY_KEY, value = ANY_VALUE)
    private val dataList = listOf(data)

    @Test
    fun test_constructor() {
        val mapper = AdministrationItemMapper()

        assertNotNull(mapper)
    }


    @Test
    fun convert_should_return_entity() {
        val entity = AdministrationItemMapper.convert(data)

        assertEquals(entity.key, data.key)
        assertEquals(entity.value, data.value)
    }

    @Test
    fun convert_should_return_a_list_of_entities() {
        val entitiesList = AdministrationItemMapper.convert(dataList)

        assertEquals(entitiesList.size, dataList.size)
    }

    @Test
    fun convertToString_should_return_the_value_of_the_first_item() {
        val value = AdministrationItemMapper.convertToString(dataList)

        assertEquals(value, dataList.first().value)
    }
}