package es.inteco.conanmobile.domain.mappers

import es.inteco.conanmobile.BaseTest
import es.inteco.conanmobile.data.entities.KeyValueData
import es.inteco.conanmobile.data.entities.MessageData
import es.inteco.conanmobile.data.entities.WarningsData
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class WarningsMapperTest : BaseTest() {

    private val data = WarningsData(
        timestamp = ANY_TIMESTAMP,
        path = ANY_PATH,
        statusMessage = ANY_STATUS_MESSAGE,
        status = ANY_STATUS_INT,
        message = listOf(
            MessageData(
                creationDate = ANY_TIMESTAMP,
                id = ANY_KEY,
                importance = ANY_STATUS_MESSAGE,
                title = listOf(KeyValueData(ANY_KEY, ANY_VALUE)),
                description = listOf(KeyValueData(ANY_KEY, ANY_VALUE))
            )
        )
    )

    @Test
    fun test_constructor() {
        val mapper = WarningsMapper()

        assertNotNull(mapper)
    }

    @Test
    fun convert_configuration_should_return_entity() {
        val entity = WarningsMapper.convert(data)
        assertEquals(entity.size, data.message.size)
    }

}