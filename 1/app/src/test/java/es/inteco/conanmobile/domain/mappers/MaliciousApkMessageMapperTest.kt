package es.inteco.conanmobile.domain.mappers

import es.inteco.conanmobile.BaseTest
import es.inteco.conanmobile.data.entities.ApkData
import es.inteco.conanmobile.data.entities.ApkService
import es.inteco.conanmobile.data.entities.MaliciousApkMessage
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MaliciousApkMessageMapperTest : BaseTest() {

    private val data = MaliciousApkMessage(
        data = emptyList()
    )
    private val apkData = ApkData("", "", listOf())
    private val apkService = ApkService("", "", "")

    @Test
    fun test_constructor() {
        val mapper = MaliciousApkMessageMapper()

        assertNotNull(mapper)
    }

    @Test
    fun test_constructor_data() {
        val mapper = MaliciousApkMessageMapper.DataMapper()

        assertNotNull(mapper)
    }

    @Test
    fun test_constructor_apk_service() {
        val mapper = MaliciousApkMessageMapper.ApkServicesMapper()

        assertNotNull(mapper)
    }

    @Test
    fun convert_configuration_should_return_entity() {
        val entity = MaliciousApkMessageMapper.convert(data)
        assertEquals(entity.datas.size, data.data.size)
    }

    @Test
    fun convert_configuration_should_return_entity_data() {
        val entity = MaliciousApkMessageMapper.DataMapper.convert(apkData)
        assertEquals(entity.data, "")
        assertEquals(entity.type, "")
    }

    @Test
    fun convert_configuration_should_return_entity_service() {
        val entity = MaliciousApkMessageMapper.ApkServicesMapper.convert(apkService)
        assertEquals(entity.service, "")
        assertEquals(entity.result, "")
    }
}