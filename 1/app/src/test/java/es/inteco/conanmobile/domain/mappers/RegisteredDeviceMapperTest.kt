package es.inteco.conanmobile.domain.mappers

import es.inteco.conanmobile.BaseTest
import es.inteco.conanmobile.data.entities.MessageKey
import es.inteco.conanmobile.data.entities.RegisteredDeviceData
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RegisteredDeviceMapperTest : BaseTest() {

    private val data = RegisteredDeviceData(
        timestamp = ANY_TIMESTAMP,
        path = ANY_PATH,
        statusMessage = ANY_STATUS_MESSAGE,
        status = ANY_STATUS,
        message = MessageKey(ANY_KEY, ANY_ID_TERMINAL)
    )

    @Test
    fun test_constructor() {
        val mapperr = RegisteredDeviceMapper()

        assertNotNull(mapperr)
    }

    @Test
    fun convert_configuration_should_return_entity() {
        val entity = RegisteredDeviceMapper.convert(data)
        assertEquals(entity.timestamp, data.timestamp)
        assertEquals(entity.path, data.path)
        assertEquals(entity.statusMessage, data.statusMessage)
        assertEquals(entity.status, data.status)
        assertEquals(entity.message.key, data.message.key)
        assertEquals(entity.message.idTerminal, data.message.idTerminal)
    }

}