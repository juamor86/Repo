package es.inteco.conanmobile.domain.mappers

import es.inteco.conanmobile.BaseTest
import es.inteco.conanmobile.data.entities.MaliciousApkData
import es.inteco.conanmobile.data.entities.MaliciousApkMessage
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MaliciousApkMapperTest : BaseTest() {

    private val data = MaliciousApkData(
        timestamp = ANY_TIMESTAMP_STR,
        status = ANY_STATUS,
        statusMessage = ANY_STATUS_MESSAGE,
        message = MaliciousApkMessage(emptyList()),
        path = ANY_PATH
    )

    @Test
    fun test_constructor() {
        val mapper = MaliciousApkMapper()

        assertNotNull(mapper)
    }

    @Test
    fun convert_configuration_should_return_entity() {
        val entity = MaliciousApkMapper.convert(data)

        assertEquals(entity.timestamp, data.timestamp)
        assertEquals(entity.status, data.status)
        assertEquals(entity.statusMessage, data.statusMessage)
        assertEquals(entity.path, data.path)
        assertEquals(entity.message.datas.size, data.message.data.size)
    }
}