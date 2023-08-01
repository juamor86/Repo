package es.inteco.conanmobile.domain.mappers

import es.inteco.conanmobile.BaseTest
import es.inteco.conanmobile.data.entities.AdministrationItem
import es.inteco.conanmobile.data.entities.PermissionsItem
import es.inteco.conanmobile.domain.entities.PermissionRiskLevel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PermissionsItemMapperTest : BaseTest() {

    private val data = PermissionsItem( ANY_ID_TERMINAL, listOf(AdministrationItem(ANY_KEY, ANY_VALUE)), ANY_VALORATION )

    @Test
    fun test_constructor() {
        val mapper = PermissionsItemMapper()

        assertNotNull(mapper)
    }

    @Test
    fun convert_configuration_should_return_entity() {
        val entity = PermissionsItemMapper.convert(data)
        assertEquals(entity.permissionID, data.permissionID)
        assertEquals(entity.riskLevel.description, PermissionRiskLevel.OTHERS.description)
    }

}