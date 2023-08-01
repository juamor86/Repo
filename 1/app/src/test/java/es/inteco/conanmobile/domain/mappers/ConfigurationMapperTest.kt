package es.inteco.conanmobile.domain.mappers

import es.inteco.conanmobile.BaseTest
import es.inteco.conanmobile.data.entities.ConfigurationData
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ConfigurationMapperTest : BaseTest() {

    private val data = ConfigurationData()

    @Test
    fun test_constructor() {
        val mapperr = ConfigurationMapper()

        assertNotNull(mapperr)
    }

    @Test
    fun convert_configuration_should_return_entity() {
        val entity = ConfigurationMapper.convert(data)

        assertEquals(entity.timestamp, data.timestamp)
        assertEquals(entity.status, data.status)
        assertEquals(entity.statusMessage, data.statusMessage)
        assertEquals(entity.message, MessageMapper.convert(data.message))
        assertEquals(entity.path, data.path)
    }
}