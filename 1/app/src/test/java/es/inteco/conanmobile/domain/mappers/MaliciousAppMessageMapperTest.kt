package es.inteco.conanmobile.domain.mappers

import es.inteco.conanmobile.BaseTest
import es.inteco.conanmobile.data.entities.Service
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MaliciousAppMessageMapperTest : BaseTest() {

    private val data = Service(
        ANY_KEY, ANY_PATH, ANY_TIMESTAMP_STR
    )

    @Test
    fun test_constructor() {
        val mapper = MaliciousAppMessageMapper()

        assertNotNull(mapper)
    }

    @Test
    fun convert_configuration_should_return_entity() {
        val entity = MaliciousAppMessageMapper.convert(data)
        assertEquals(entity.service, data.service)
        assertEquals(entity.result, data.result)
        assertEquals(entity.analysisDate, data.analysisDate)
    }

}