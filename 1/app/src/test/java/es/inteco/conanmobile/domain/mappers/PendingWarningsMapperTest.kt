package es.inteco.conanmobile.domain.mappers

import es.inteco.conanmobile.BaseTest
import es.inteco.conanmobile.data.entities.PendingWarningsData
import es.inteco.conanmobile.data.entities.PendingWarningsMessageData
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PendingWarningsMapperTest : BaseTest() {

    private val data = PendingWarningsData(
        timestamp = ANY_TIMESTAMP_STR,
        status = ANY_STATUS,
        statusMessage = ANY_STATUS_MESSAGE,
        path = ANY_PATH,
        message = PendingWarningsMessageData(true)
    )

    @Test
    fun test_constructor() {
        val mapper = PendingWarningsMapper()

        assertNotNull(mapper)
    }

    @Test
    fun convert_configuration_should_return_entity() {
        val entity = PendingWarningsMapper.convert(data)
        assertEquals(entity.timestamp, data.timestamp)
        assertEquals(entity.statusMessage, data.statusMessage)
        assertEquals(entity.path, data.path)
        assertEquals(entity.status, data.status)
        assertEquals(entity.message.haveNotifications, data.message.haveNotifications)
    }

}