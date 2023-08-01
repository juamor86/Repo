package es.inteco.conanmobile.domain.mappers

import es.inteco.conanmobile.BaseTest
import es.inteco.conanmobile.data.entities.MessageItem
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MessageMapperTest : BaseTest() {

    private val data = MessageItem()

    @Test
    fun test_constructor() {
        val mapper = MessageMapper()

        assertNotNull(mapper)
    }

    @Test
    fun convert_configuration_should_return_entity() {
        val entity = MessageMapper.convert(data)
        assertEquals(entity.id, data.id)
        assertEquals(entity.expirationDate, data.expirationDate)
        assertEquals(entity.version, data.version)
    }
}