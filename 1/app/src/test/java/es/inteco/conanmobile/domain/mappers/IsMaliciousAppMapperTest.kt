package es.inteco.conanmobile.domain.mappers

import es.inteco.conanmobile.BaseTest
import es.inteco.conanmobile.data.entities.MaliciousAppData
import es.inteco.conanmobile.data.entities.MaliciousAppMessage
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class IsMaliciousAppMapperTest : BaseTest() {

    private val data = MaliciousAppData(
        timestamp = ANY_TIMESTAMP_STR,
        status = ANY_STATUS,
        statusMessage = ANY_STATUS_MESSAGE,
        message = MaliciousAppMessage(ANY_KEY, emptyList()),
        path = ANY_PATH
    )

    @Test
    fun test_constructor() {
        val mapper = IsMaliciousAppMapper()

        assertNotNull(mapper)
    }

    @Test
    fun convert_configuration_should_return_entity() {
        val entity = IsMaliciousAppMapper.convert(data)

        assertEquals(entity.timestamp, data.timestamp)
        assertEquals(entity.status, data.status)
        assertEquals(entity.statusMessage, data.statusMessage)
        assertEquals(entity.path, data.path)
    }
}