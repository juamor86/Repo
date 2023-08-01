package es.inteco.conanmobile.domain.mappers

import es.inteco.conanmobile.BaseTest
import es.inteco.conanmobile.data.entities.PostAnalysisResultData
import es.inteco.conanmobile.data.entities.PostAnalysisResultMessage
import es.inteco.conanmobile.domain.entities.ApplicationEntity
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PostAnalysisMapperTest : BaseTest() {

    @Test
    fun test_constructor() {
        val mapper = PostAnalysisMapper()

        assertNotNull(mapper)
    }

    @Test
    fun convert_post_analysis_should_return_entity() {
        fakeAnalysisResult.apply {
            deviceItems = mutableListOf(fakeModuleResult)
            appsItems = mutableListOf(ApplicationEntity())
        }
        val entity = PostAnalysisMapper.convert(fakeAnalysisResult, ANY_CONFIGURATION_VERSION, fakeAnalysis)
        assertEquals(entity.configurationVersion, ANY_CONFIGURATION_VERSION.toLong())
    }

}