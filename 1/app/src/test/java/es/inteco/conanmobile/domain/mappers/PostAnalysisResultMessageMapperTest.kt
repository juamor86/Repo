package es.inteco.conanmobile.domain.mappers

import es.inteco.conanmobile.BaseTest
import es.inteco.conanmobile.data.entities.PostAnalysisResultMessage
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PostAnalysisResultMessageMapperTest : BaseTest() {

    companion object {
        private const val ANY_ACKNOWLEDGE = ""
    }

    private val data = PostAnalysisResultMessage(ANY_ACKNOWLEDGE)

    @Test
    fun test_constructor() {
        val mapper = PostAnalysisResultMessageMapper()

        assertNotNull(mapper)
    }

    @Test
    fun convert_configuration_should_return_entity() {
        val entity = PostAnalysisResultMessageMapper.convert(data)
        assertEquals(entity.acknowledge, ANY_ACKNOWLEDGE)
    }

}