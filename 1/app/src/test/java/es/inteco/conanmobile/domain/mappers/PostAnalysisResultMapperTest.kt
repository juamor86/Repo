package es.inteco.conanmobile.domain.mappers

import es.inteco.conanmobile.BaseTest
import es.inteco.conanmobile.data.entities.PostAnalysisResultData
import es.inteco.conanmobile.data.entities.PostAnalysisResultMessage
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PostAnalysisResultMapperTest : BaseTest() {

    private val data = PostAnalysisResultData(
        timestamp = ANY_TIMESTAMP_STR,
        path = ANY_PATH,
        statusMessage = ANY_STATUS_MESSAGE,
        status = ANY_STATUS,
        message = PostAnalysisResultMessage(
            ANY_KEY
        )
    )

    @Test
    fun test_constructor() {
        val mapper = PostAnalysisResultMapper()

        assertNotNull(mapper)
    }

    @Test
    fun convert_configuration_should_return_entity() {
        val entity = PostAnalysisResultMapper.convert(data)
        assertEquals(entity.timestamp, data.timestamp)
        assertEquals(entity.path, data.path)
        assertEquals(entity.statusMessage, data.statusMessage)
        assertEquals(entity.status, data.status)
        assertEquals(entity.message.acknowledge, data.message.acknowledge)

    }

}