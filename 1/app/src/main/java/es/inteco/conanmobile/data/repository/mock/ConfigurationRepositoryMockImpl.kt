package es.inteco.conanmobile.data.repository.mock

import android.content.Context
import es.inteco.conanmobile.data.entities.*
import es.inteco.conanmobile.domain.entities.AssessmentEntity
import es.inteco.conanmobile.domain.entities.ConfigurationEntity
import es.inteco.conanmobile.domain.mappers.ConfigurationMapper
import es.inteco.conanmobile.domain.repository.ConfigurationRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

/**
 * Configuration repository mock impl
 *
 * @property context
 * @constructor Create empty Configuration repository mock impl
 */
class ConfigurationRepositoryMockImpl(private val context: Context) : ConfigurationRepository {

    companion object {
        internal const val APPLICATION_INFORMATION_ES = "Información de la aplicación"
        internal const val APPLICATION_INFORMATION_EN = "Aplication information"
        internal const val TIME_LOCK_ES = "Tiempo de bloqueo"
        internal const val TIME_LOCK_EN = "Lock"
        internal const val BLUETOOTH_TETHERING_ES = "Bluetooth tethering"
        internal const val BLUETOOTH_TETHERING_EN = "Bluetooth tethering"
    }

    override fun getConfiguration(key: String): Single<ConfigurationData> =
        Single.just(getMockConfiguration())

    override fun registerDevice(body: RegisterDeviceRequestData): Single<RegisteredDeviceData> {
        return Single.just(
            RegisteredDeviceData(
                timestamp = System.currentTimeMillis(),
                status = 200,
                statusMessage = "OK",
                message = MessageKey(
                    idTerminal = "010928003890233",
                    key = "H4jf4p2sG2lyCTpkgXpI2lByebK5TBwn"
                ),
                path = "/api/v1/server/device/new/device/new"
            )
        )
    }

    private fun getMockConfiguration(): ConfigurationData {
        val configuration = ConfigurationData()
        configuration.timestamp = "2021-10-14T11:29:32.079283400Z"
        configuration.status = 200
        configuration.statusMessage = "OK"
        configuration.path = "/configuration/latest"

        val message = MessageItem()
        message.id = "61644cc08b9f3053f227fa03"
        message.version = "2"
        message.expirationDate = "2022-10-04T10:11:01.404+00:00"

        val abouts = ArrayList<AdministrationItem>()
        val aboutItem = AdministrationItem()
        aboutItem.key = "about"
        aboutItem.value = "about"
        abouts.add(aboutItem)
        message.about = abouts

        val legals = ArrayList<AdministrationItem>()
        val legalItem = AdministrationItem()
        legalItem.key = "legal"
        legalItem.value = "legal"
        legals.add(legalItem)
        message.legal = legals

        val administrations = ArrayList<AdministrationItem>()
        val administrationItem = AdministrationItem()
        administrationItem.key = "time_between_analysis"
        administrationItem.value = "15000"
        administrations.add(administrationItem)

        message.administration = administrations
        message.analysis = getAnalisisMocked()
        message.permissions = getPermissionsMocked()

        configuration.message = message

        return configuration
    }

    private fun getPermissionsMocked(): ArrayList<PermissionsItem> {
        val permissions = ArrayList<PermissionsItem>()

        // Region permission - INTERNET
        val permissionsItem1 = PermissionsItem()
        permissionsItem1.permissionID = "android.permission.INTERNET"

        val permissionsItemDescription1 = ArrayList<AdministrationItem>()

        val permissionDescription1ItemEN = AdministrationItem()
        permissionDescription1ItemEN.key = "EN"
        permissionDescription1ItemEN.value = "INTERNET"
        permissionsItemDescription1.add(permissionDescription1ItemEN)

        val permissionDescription1ItemES = AdministrationItem()
        permissionDescription1ItemES.key = "ES"
        permissionDescription1ItemES.value = "INTERNET"
        permissionsItemDescription1.add(permissionDescription1ItemES)

        permissionsItem1.permissionDescriptions = permissionsItemDescription1

        val permissionsItemRiskLevel1 = ArrayList<AdministrationItem>()

        val permissionRiskLevel1ItemEN = AdministrationItem()
        permissionRiskLevel1ItemEN.key = "EN"
        permissionRiskLevel1ItemEN.value = "Low"
        permissionsItemRiskLevel1.add(permissionRiskLevel1ItemEN)

        val permissionRiskLevel1ItemES = AdministrationItem()
        permissionRiskLevel1ItemES.key = "ES"
        permissionRiskLevel1ItemES.value = "Bajo"
        permissionsItemRiskLevel1.add(permissionRiskLevel1ItemES)

        permissionsItem1.permissionRiskLevels = "Bajo"

        permissions.add(permissionsItem1)

        // Region permission - MANAGE_ACCOUNTS
        val permissionsItem2 = PermissionsItem()
        permissionsItem2.permissionID = "android.permission.MANAGE_ACCOUNTS"

        val permissionsItemDescription2 = ArrayList<AdministrationItem>()

        val permissionDescription2ItemEN = AdministrationItem()
        permissionDescription2ItemEN.key = "EN"
        permissionDescription2ItemEN.value = "MANAGE_ACCOUNTS"
        permissionsItemDescription2.add(permissionDescription2ItemEN)

        val permissionDescription2ItemES = AdministrationItem()
        permissionDescription2ItemES.key = "ES"
        permissionDescription2ItemES.value = "MANAGE_CUENTAS"
        permissionsItemDescription2.add(permissionDescription2ItemES)

        permissionsItem2.permissionDescriptions = permissionsItemDescription2

        val permissionsItemRiskLevel2 = ArrayList<AdministrationItem>()

        val permissionRiskLevel2ItemEN = AdministrationItem()
        permissionRiskLevel2ItemEN.key = "EN"
        permissionRiskLevel2ItemEN.value = "High"
        permissionsItemRiskLevel2.add(permissionRiskLevel2ItemEN)

        val permissionRiskLevel2ItemES = AdministrationItem()
        permissionRiskLevel2ItemES.key = "ES"
        permissionRiskLevel2ItemES.value = "Alto"
        permissionsItemRiskLevel2.add(permissionRiskLevel2ItemES)

        permissionsItem2.permissionRiskLevels = "Alto"

        permissions.add(permissionsItem2)

        // Region permission - AUTHENTICATE_ACCOUNTS
        val permissionsItem3 = PermissionsItem()
        permissionsItem3.permissionID = "android.permission.AUTHENTICATE_ACCOUNTS"

        val permissionsItemDescription3 = ArrayList<AdministrationItem>()

        val permissionDescription3ItemEN = AdministrationItem()
        permissionDescription3ItemEN.key = "EN"
        permissionDescription3ItemEN.value = "AUTHENTICATE_ACCOUNTS"
        permissionsItemDescription3.add(permissionDescription3ItemEN)

        val permissionDescription3ItemES = AdministrationItem()
        permissionDescription3ItemES.key = "ES"
        permissionDescription3ItemES.value = "AUTHENTICATE_CUENTAS"
        permissionsItemDescription3.add(permissionDescription3ItemES)

        permissionsItem3.permissionDescriptions = permissionsItemDescription3

        val permissionsItemRiskLevel3 = ArrayList<AdministrationItem>()

        val permissionRiskLevel3ItemEN = AdministrationItem()
        permissionRiskLevel3ItemEN.key = "EN"
        permissionRiskLevel3ItemEN.value = "Medium"
        permissionsItemRiskLevel3.add(permissionRiskLevel3ItemEN)

        val permissionRiskLevel3ItemES = AdministrationItem()
        permissionRiskLevel3ItemES.key = "ES"
        permissionRiskLevel3ItemES.value = "Medio"
        permissionsItemRiskLevel3.add(permissionRiskLevel3ItemES)

        permissionsItem3.permissionRiskLevels = "Bajo"

        permissions.add(permissionsItem3)

        // Region permission - READ_SYNC_STATS
        val permissionsItem4 = PermissionsItem()
        permissionsItem4.permissionID = "android.permission.READ_SYNC_STATS"

        val permissionsItemDescription4 = ArrayList<AdministrationItem>()

        val permissionDescription4ItemEN = AdministrationItem()
        permissionDescription4ItemEN.key = "EN"
        permissionDescription4ItemEN.value = "READ_SYNC_STATS"
        permissionsItemDescription4.add(permissionDescription4ItemEN)

        val permissionDescription4ItemES = AdministrationItem()
        permissionDescription4ItemES.key = "ES"
        permissionDescription4ItemES.value = "LEER_SYNC_STATS"
        permissionsItemDescription4.add(permissionDescription4ItemES)

        permissionsItem4.permissionDescriptions = permissionsItemDescription4

        val permissionsItemRiskLevel4 = ArrayList<AdministrationItem>()

        val permissionRiskLevel4ItemEN = AdministrationItem()
        permissionRiskLevel4ItemEN.key = "EN"
        permissionRiskLevel4ItemEN.value = "Low"
        permissionsItemRiskLevel4.add(permissionRiskLevel4ItemEN)

        val permissionRiskLevel4ItemES = AdministrationItem()
        permissionRiskLevel4ItemES.key = "ES"
        permissionRiskLevel4ItemES.value = "Bajo"
        permissionsItemRiskLevel4.add(permissionRiskLevel4ItemES)

        permissionsItem4.permissionRiskLevels = "Bajo"

        permissions.add(permissionsItem4)

        // Region permission - READ_SYNC_SETTINGS
        val permissionsItem5 = PermissionsItem()
        permissionsItem5.permissionID = "android.permission.READ_SYNC_SETTINGS"

        val permissionsItemDescription5 = ArrayList<AdministrationItem>()

        val permissionDescription5ItemEN = AdministrationItem()
        permissionDescription5ItemEN.key = "EN"
        permissionDescription5ItemEN.value = "READ_SYNC_SETTINGS"
        permissionsItemDescription5.add(permissionDescription5ItemEN)

        val permissionDescription5ItemES = AdministrationItem()
        permissionDescription5ItemES.key = "ES"
        permissionDescription5ItemES.value = "LEER_SYNC_SETTINGS"
        permissionsItemDescription5.add(permissionDescription5ItemES)

        permissionsItem5.permissionDescriptions = permissionsItemDescription5

        val permissionsItemRiskLevel5 = ArrayList<AdministrationItem>()

        val permissionRiskLevel5ItemEN = AdministrationItem()
        permissionRiskLevel5ItemEN.key = "EN"
        permissionRiskLevel5ItemEN.value = "Others"
        permissionsItemRiskLevel5.add(permissionRiskLevel5ItemEN)

        val permissionRiskLevel5ItemES = AdministrationItem()
        permissionRiskLevel5ItemES.key = "ES"
        permissionRiskLevel5ItemES.value = "Otros"
        permissionsItemRiskLevel5.add(permissionRiskLevel5ItemES)

        permissionsItem5.permissionRiskLevels = "Bajo"

        permissions.add(permissionsItem5)

        // Region permission - WRITE_SYNC_SETTINGS
        val permissionsItem6 = PermissionsItem()
        permissionsItem6.permissionID = "android.permission.WRITE_SYNC_SETTINGS"

        val permissionsItemDescription6 = ArrayList<AdministrationItem>()

        val permissionDescription6ItemEN = AdministrationItem()
        permissionDescription6ItemEN.key = "EN"
        permissionDescription6ItemEN.value = "WRITE_SYNC_SETTINGS"
        permissionsItemDescription6.add(permissionDescription6ItemEN)

        val permissionDescription6ItemES = AdministrationItem()
        permissionDescription6ItemES.key = "ES"
        permissionDescription6ItemES.value = "ESCRIBIR_SYNC_SETTINGS"
        permissionsItemDescription6.add(permissionDescription6ItemES)

        permissionsItem6.permissionDescriptions = permissionsItemDescription6

        val permissionsItemRiskLevel6 = ArrayList<AdministrationItem>()

        val permissionRiskLevel6ItemEN = AdministrationItem()
        permissionRiskLevel6ItemEN.key = "EN"
        permissionRiskLevel6ItemEN.value = "Medium"
        permissionsItemRiskLevel6.add(permissionRiskLevel6ItemEN)

        val permissionRiskLevel6ItemES = AdministrationItem()
        permissionRiskLevel6ItemES.key = "ES"
        permissionRiskLevel6ItemES.value = "Medio"
        permissionsItemRiskLevel6.add(permissionRiskLevel6ItemES)

        permissionsItem6.permissionRiskLevels = "Bajo"

        permissions.add(permissionsItem6)

        // Region permission - WAKE_LOCK
        val permissionsItem7 = PermissionsItem()
        permissionsItem7.permissionID = "android.permission.WAKE_LOCK"

        val permissionsItemDescription7 = ArrayList<AdministrationItem>()

        val permissionDescription7ItemEN = AdministrationItem()
        permissionDescription7ItemEN.key = "EN"
        permissionDescription7ItemEN.value = "WAKE_LOCK"
        permissionsItemDescription7.add(permissionDescription7ItemEN)

        val permissionDescription7ItemES = AdministrationItem()
        permissionDescription7ItemES.key = "ES"
        permissionDescription7ItemES.value = "WAKE_LOCK"
        permissionsItemDescription7.add(permissionDescription7ItemES)

        permissionsItem7.permissionDescriptions = permissionsItemDescription7

        val permissionsItemRiskLevel7 = ArrayList<AdministrationItem>()

        val permissionRiskLevel7ItemEN = AdministrationItem()
        permissionRiskLevel7ItemEN.key = "EN"
        permissionRiskLevel7ItemEN.value = "High"
        permissionsItemRiskLevel7.add(permissionRiskLevel7ItemEN)

        val permissionRiskLevel7ItemES = AdministrationItem()
        permissionRiskLevel7ItemES.key = "ES"
        permissionRiskLevel7ItemES.value = "Alto"
        permissionsItemRiskLevel7.add(permissionRiskLevel7ItemES)

        permissionsItem7.permissionRiskLevels = "Alto"

        permissions.add(permissionsItem7)

        // Region permission - ACCESS_NETWORK_STATE
        val permissionsItem8 = PermissionsItem()
        permissionsItem8.permissionID = "android.permission.ACCESS_NETWORK_STATE"

        val permissionsItemDescription8 = ArrayList<AdministrationItem>()

        val permissionDescription8ItemEN = AdministrationItem()
        permissionDescription8ItemEN.key = "EN"
        permissionDescription8ItemEN.value = "ACCESS_NETWORK_STATE"
        permissionsItemDescription8.add(permissionDescription8ItemEN)

        val permissionDescription8ItemES = AdministrationItem()
        permissionDescription8ItemES.key = "ES"
        permissionDescription8ItemES.value = "ACCESS_NETWORK_ESTADO"
        permissionsItemDescription8.add(permissionDescription8ItemES)

        permissionsItem8.permissionDescriptions = permissionsItemDescription8

        val permissionsItemRiskLevel8 = ArrayList<AdministrationItem>()

        val permissionRiskLevel8ItemEN = AdministrationItem()
        permissionRiskLevel8ItemEN.key = "EN"
        permissionRiskLevel8ItemEN.value = "High"
        permissionsItemRiskLevel8.add(permissionRiskLevel8ItemEN)

        val permissionRiskLevel8ItemES = AdministrationItem()
        permissionRiskLevel8ItemES.key = "ES"
        permissionRiskLevel8ItemES.value = "Alto"
        permissionsItemRiskLevel8.add(permissionRiskLevel8ItemES)

        permissionsItem8.permissionRiskLevels = "Alto"

        permissions.add(permissionsItem8)

        // Region permission - VIBRATE
        val permissionsItem9 = PermissionsItem()
        permissionsItem9.permissionID = "android.permission.VIBRATE"

        val permissionsItemDescription9 = ArrayList<AdministrationItem>()

        val permissionDescription9ItemEN = AdministrationItem()
        permissionDescription9ItemEN.key = "EN"
        permissionDescription9ItemEN.value = "VIBRATE"
        permissionsItemDescription9.add(permissionDescription9ItemEN)

        val permissionDescription9ItemES = AdministrationItem()
        permissionDescription9ItemES.key = "ES"
        permissionDescription9ItemES.value = "VIBRAR"
        permissionsItemDescription9.add(permissionDescription9ItemES)

        permissionsItem9.permissionDescriptions = permissionsItemDescription9

        val permissionsItemRiskLevel9 = ArrayList<AdministrationItem>()

        val permissionRiskLevel9ItemEN = AdministrationItem()
        permissionRiskLevel9ItemEN.key = "EN"
        permissionRiskLevel9ItemEN.value = "Low"
        permissionsItemRiskLevel9.add(permissionRiskLevel9ItemEN)

        val permissionRiskLevel9ItemES = AdministrationItem()
        permissionRiskLevel9ItemES.key = "ES"
        permissionRiskLevel9ItemES.value = "Bajo"
        permissionsItemRiskLevel9.add(permissionRiskLevel9ItemES)

        permissionsItem9.permissionRiskLevels = "Bajo"

        permissions.add(permissionsItem9)

        // Region permission - CHECK_LICENSE
        val permissionsItem10 = PermissionsItem()
        permissionsItem10.permissionID = "android.permission.CHECK_LICENSE"

        val permissionsItemDescription10 = ArrayList<AdministrationItem>()

        val permissionDescription10ItemEN = AdministrationItem()
        permissionDescription10ItemEN.key = "EN"
        permissionDescription10ItemEN.value = "CHECK_LICENSE"
        permissionsItemDescription10.add(permissionDescription10ItemEN)

        val permissionDescription10ItemES = AdministrationItem()
        permissionDescription10ItemES.key = "ES"
        permissionDescription10ItemES.value = "COMPROBAR_LICENSE"
        permissionsItemDescription10.add(permissionDescription10ItemES)

        permissionsItem10.permissionDescriptions = permissionsItemDescription10

        val permissionsItemRiskLevel10 = ArrayList<AdministrationItem>()

        val permissionRiskLevel10ItemEN = AdministrationItem()
        permissionRiskLevel10ItemEN.key = "EN"
        permissionRiskLevel10ItemEN.value = "Medium"
        permissionsItemRiskLevel10.add(permissionRiskLevel10ItemEN)

        val permissionRiskLevel10ItemES = AdministrationItem()
        permissionRiskLevel10ItemES.key = "ES"
        permissionRiskLevel10ItemES.value = "Medio"
        permissionsItemRiskLevel10.add(permissionRiskLevel10ItemES)

        permissionsItem10.permissionRiskLevels = "Bajo"

        permissions.add(permissionsItem10)

        // Region permission - ACCESS_WIFI_STATE
        val permissionsItem11 = PermissionsItem()
        permissionsItem11.permissionID = "android.permission.ACCESS_WIFI_STATE"

        val permissionsItemDescription11 = ArrayList<AdministrationItem>()

        val permissionDescription11ItemEN = AdministrationItem()
        permissionDescription11ItemEN.key = "EN"
        permissionDescription11ItemEN.value = "ACCESS_WIFI_STATE"
        permissionsItemDescription11.add(permissionDescription11ItemEN)

        val permissionDescription11ItemES = AdministrationItem()
        permissionDescription11ItemES.key = "ES"
        permissionDescription11ItemES.value = "ACCESS_WIFI_ESTADO"
        permissionsItemDescription11.add(permissionDescription11ItemES)

        permissionsItem11.permissionDescriptions = permissionsItemDescription11

        val permissionsItemRiskLevel11 = ArrayList<AdministrationItem>()

        val permissionRiskLevel11ItemEN = AdministrationItem()
        permissionRiskLevel11ItemEN.key = "EN"
        permissionRiskLevel11ItemEN.value = "Medium"
        permissionsItemRiskLevel11.add(permissionRiskLevel11ItemEN)

        val permissionRiskLevel11ItemES = AdministrationItem()
        permissionRiskLevel11ItemES.key = "ES"
        permissionRiskLevel11ItemES.value = "Medio"
        permissionsItemRiskLevel11.add(permissionRiskLevel11ItemES)

        permissionsItem11.permissionRiskLevels = "Bajo"

        permissions.add(permissionsItem11)

        // Region permission - RECEIVE_BOOT_COMPLETED
        val permissionsItem12 = PermissionsItem()
        permissionsItem12.permissionID = "android.permission.RECEIVE_BOOT_COMPLETED"

        val permissionsItemDescription12 = ArrayList<AdministrationItem>()

        val permissionDescription12ItemEN = AdministrationItem()
        permissionDescription12ItemEN.key = "EN"
        permissionDescription12ItemEN.value = "RECEIVE_BOOT_COMPLETED"
        permissionsItemDescription12.add(permissionDescription12ItemEN)

        val permissionDescription12ItemES = AdministrationItem()
        permissionDescription12ItemES.key = "ES"
        permissionDescription12ItemES.value = "RECIBIR_BOOT_COMPLETED"
        permissionsItemDescription12.add(permissionDescription12ItemES)

        permissionsItem12.permissionDescriptions = permissionsItemDescription12

        val permissionsItemRiskLevel12 = ArrayList<AdministrationItem>()

        val permissionRiskLevel12ItemEN = AdministrationItem()
        permissionRiskLevel12ItemEN.key = "EN"
        permissionRiskLevel12ItemEN.value = "Low"
        permissionsItemRiskLevel12.add(permissionRiskLevel12ItemEN)

        val permissionRiskLevel12ItemES = AdministrationItem()
        permissionRiskLevel12ItemES.key = "ES"
        permissionRiskLevel12ItemES.value = "Bajo"
        permissionsItemRiskLevel12.add(permissionRiskLevel12ItemES)

        permissionsItem12.permissionRiskLevels = "Bajo"

        permissions.add(permissionsItem12)

        // Region permission - FOREGROUND_SERVICE
        val permissionsItem13 = PermissionsItem()
        permissionsItem13.permissionID = "android.permission.FOREGROUND_SERVICE"

        val permissionsItemDescription13 = ArrayList<AdministrationItem>()

        val permissionDescription13ItemEN = AdministrationItem()
        permissionDescription13ItemEN.key = "EN"
        permissionDescription13ItemEN.value = "FOREGROUND_SERVICE"
        permissionsItemDescription13.add(permissionDescription13ItemEN)

        val permissionDescription13ItemES = AdministrationItem()
        permissionDescription13ItemES.key = "ES"
        permissionDescription13ItemES.value = "FOREGROUND_SERVICIO"
        permissionsItemDescription13.add(permissionDescription13ItemES)

        permissionsItem13.permissionDescriptions = permissionsItemDescription13

        val permissionsItemRiskLevel13 = ArrayList<AdministrationItem>()

        val permissionRiskLevel13ItemEN = AdministrationItem()
        permissionRiskLevel13ItemEN.key = "EN"
        permissionRiskLevel13ItemEN.value = "Low"
        permissionsItemRiskLevel13.add(permissionRiskLevel13ItemEN)

        val permissionRiskLevel13ItemES = AdministrationItem()
        permissionRiskLevel13ItemES.key = "ES"
        permissionRiskLevel13ItemES.value = "Bajo"
        permissionsItemRiskLevel13.add(permissionRiskLevel13ItemES)

        permissionsItem13.permissionRiskLevels = "Bajo"

        permissions.add(permissionsItem13)

        // Region permission - BILLING
        val permissionsItem14 = PermissionsItem()
        permissionsItem14.permissionID = "com.android.vending.BILLING"

        val permissionsItemDescription14 = ArrayList<AdministrationItem>()

        val permissionDescription14ItemEN = AdministrationItem()
        permissionDescription14ItemEN.key = "EN"
        permissionDescription14ItemEN.value = "BILLING"
        permissionsItemDescription14.add(permissionDescription14ItemEN)

        val permissionDescription14ItemES = AdministrationItem()
        permissionDescription14ItemES.key = "ES"
        permissionDescription14ItemES.value = "BILLING"
        permissionsItemDescription14.add(permissionDescription14ItemES)

        permissionsItem14.permissionDescriptions = permissionsItemDescription14

        val permissionsItemRiskLevel14 = ArrayList<AdministrationItem>()

        val permissionRiskLevel14ItemEN = AdministrationItem()
        permissionRiskLevel14ItemEN.key = "EN"
        permissionRiskLevel14ItemEN.value = "High"
        permissionsItemRiskLevel14.add(permissionRiskLevel14ItemEN)

        val permissionRiskLevel14ItemES = AdministrationItem()
        permissionRiskLevel14ItemES.key = "ES"
        permissionRiskLevel14ItemES.value = "Alto"
        permissionsItemRiskLevel14.add(permissionRiskLevel14ItemES)

        permissionsItem14.permissionRiskLevels = "Bajo"

        permissions.add(permissionsItem14)

        // Region permission - MODIFY_AUDIO_SETTINGS
        val permissionsItem15 = PermissionsItem()
        permissionsItem15.permissionID = "android.permission.MODIFY_AUDIO_SETTINGS"

        val permissionsItemDescription15 = ArrayList<AdministrationItem>()

        val permissionDescription15ItemEN = AdministrationItem()
        permissionDescription15ItemEN.key = "EN"
        permissionDescription15ItemEN.value = "MODIFY_AUDIO_SETTINGS"
        permissionsItemDescription15.add(permissionDescription15ItemEN)

        val permissionDescription15ItemES = AdministrationItem()
        permissionDescription15ItemES.key = "ES"
        permissionDescription15ItemES.value = "MODIFICAR_AUNDIO_SETTINGS"
        permissionsItemDescription15.add(permissionDescription15ItemES)

        permissionsItem15.permissionDescriptions = permissionsItemDescription15

        val permissionsItemRiskLevel15 = ArrayList<AdministrationItem>()

        val permissionRiskLevel15ItemEN = AdministrationItem()
        permissionRiskLevel15ItemEN.key = "EN"
        permissionRiskLevel15ItemEN.value = "High"
        permissionsItemRiskLevel15.add(permissionRiskLevel15ItemEN)

        val permissionRiskLevel15ItemES = AdministrationItem()
        permissionRiskLevel15ItemES.key = "ES"
        permissionRiskLevel15ItemES.value = "Alto"
        permissionsItemRiskLevel15.add(permissionRiskLevel15ItemES)

        permissionsItem15.permissionRiskLevels = "Bajo"

        permissions.add(permissionsItem15)

        // Region permission - USE_BIOMETRIC
        val permissionsItem16 = PermissionsItem()
        permissionsItem16.permissionID = "android.permission.USE_BIOMETRIC"

        val permissionsItemDescription16 = ArrayList<AdministrationItem>()

        val permissionDescription16ItemEN = AdministrationItem()
        permissionDescription16ItemEN.key = "EN"
        permissionDescription16ItemEN.value = "USE_BIOMETRIC"
        permissionsItemDescription16.add(permissionDescription16ItemEN)

        val permissionDescription16ItemES = AdministrationItem()
        permissionDescription16ItemES.key = "ES"
        permissionDescription16ItemES.value = "USAR_BIOMETRIC"
        permissionsItemDescription16.add(permissionDescription16ItemES)

        permissionsItem16.permissionDescriptions = permissionsItemDescription16

        val permissionsItemRiskLevel16 = ArrayList<AdministrationItem>()

        val permissionRiskLevel16ItemEN = AdministrationItem()
        permissionRiskLevel16ItemEN.key = "EN"
        permissionRiskLevel16ItemEN.value = "Low"
        permissionsItemRiskLevel16.add(permissionRiskLevel16ItemEN)

        val permissionRiskLevel16ItemES = AdministrationItem()
        permissionRiskLevel16ItemES.key = "ES"
        permissionRiskLevel16ItemES.value = "Bajo"
        permissionsItemRiskLevel16.add(permissionRiskLevel16ItemES)

        permissionsItem16.permissionRiskLevels = "Bajo"

        permissions.add(permissionsItem16)

        // Region permission - android.permission.USE_FINGERPRINT
        val permissionsItem17 = PermissionsItem()
        permissionsItem17.permissionID = "android.permission.USE_FINGERPRINT"

        val permissionsItemDescription17 = ArrayList<AdministrationItem>()

        val permissionDescription17ItemEN = AdministrationItem()
        permissionDescription17ItemEN.key = "EN"
        permissionDescription17ItemEN.value = "USE_FINGERPRINT"
        permissionsItemDescription17.add(permissionDescription17ItemEN)

        val permissionDescription17ItemES = AdministrationItem()
        permissionDescription17ItemES.key = "ES"
        permissionDescription17ItemES.value = "USAR_FINGERPRINT"
        permissionsItemDescription17.add(permissionDescription17ItemES)

        permissionsItem17.permissionDescriptions = permissionsItemDescription17

        val permissionsItemRiskLevel17 = ArrayList<AdministrationItem>()

        val permissionRiskLevel17ItemEN = AdministrationItem()
        permissionRiskLevel17ItemEN.key = "EN"
        permissionRiskLevel17ItemEN.value = "High"
        permissionsItemRiskLevel17.add(permissionRiskLevel17ItemEN)

        val permissionRiskLevel17ItemES = AdministrationItem()
        permissionRiskLevel17ItemES.key = "ES"
        permissionRiskLevel17ItemES.value = "Alto"
        permissionsItemRiskLevel17.add(permissionRiskLevel17ItemES)

        permissionsItem17.permissionRiskLevels = "Bajo"

        permissions.add(permissionsItem17)

        // Region permission - android.permission.NFC
        val permissionsItem18 = PermissionsItem()
        permissionsItem18.permissionID = "android.permission.NFC"

        val permissionsItemDescription18 = ArrayList<AdministrationItem>()

        val permissionDescription18ItemEN = AdministrationItem()
        permissionDescription18ItemEN.key = "EN"
        permissionDescription18ItemEN.value = "NFC"
        permissionsItemDescription18.add(permissionDescription18ItemEN)

        val permissionDescription18ItemES = AdministrationItem()
        permissionDescription18ItemES.key = "ES"
        permissionDescription18ItemES.value = "NFC"
        permissionsItemDescription18.add(permissionDescription18ItemES)

        permissionsItem18.permissionDescriptions = permissionsItemDescription18

        val permissionsItemRiskLevel18 = ArrayList<AdministrationItem>()

        val permissionRiskLevel18ItemEN = AdministrationItem()
        permissionRiskLevel18ItemEN.key = "EN"
        permissionRiskLevel18ItemEN.value = "High"
        permissionsItemRiskLevel18.add(permissionRiskLevel18ItemEN)

        val permissionRiskLevel18ItemES = AdministrationItem()
        permissionRiskLevel18ItemES.key = "ES"
        permissionRiskLevel18ItemES.value = "Alto"
        permissionsItemRiskLevel18.add(permissionRiskLevel18ItemES)

        permissionsItem18.permissionRiskLevels = "Bajo"

        permissions.add(permissionsItem18)

        return permissions
    }

    private fun getAnalisisMocked(): ArrayList<AnalysisItem> {
        val analysis = ArrayList<AnalysisItem>()

        //region Analisis Completo
        val analysisItem1 = AnalysisItem()
        val administrationItemNames1 = ArrayList<AdministrationItem>()
        val descriptions1 = ArrayList<AdministrationItem>()
        val deviceModules1 = ArrayList<ModuleItem>()
        val applicationModules1 = ArrayList<ModuleItem>()
        val systemModules1 = ArrayList<ModuleItem>()

        analysisItem1.id = "61644cbf8b9f3053f227fa02"

        //region Names
        val administrationItemES1 = AdministrationItem()
        administrationItemES1.key = "ES"
        administrationItemES1.value = "Análisis completo"
        administrationItemNames1.add(administrationItemES1)

        val administrationItemES2 = AdministrationItem()
        administrationItemES2.key = "EN"
        administrationItemES2.value = "Analysis complete"
        administrationItemNames1.add(administrationItemES2)

        analysisItem1.names = administrationItemNames1
        //endregion

        //region descriptions
        val administrationItemDescES1 = AdministrationItem()
        administrationItemDescES1.key = "ES"
        administrationItemDescES1.value =
            "Análisis completo del terminal que buscará problemas de seguridad tanto a nivel de configuración, aplicaciones y de sistema"
        descriptions1.add(administrationItemDescES1)

        val administrationItemDescES2 = AdministrationItem()
        administrationItemDescES2.key = "EN"
        administrationItemDescES2.value =
            "Device complete analysis that will search for security problem at aplication, configuration and system level"
        descriptions1.add(administrationItemDescES2)

        analysisItem1.descriptions = descriptions1
        //endregion

        //region deviceModules
        //region COM001
        val deviceModuleCom001 = ModuleItem()
        val deviceModuleCom001Names = ArrayList<AdministrationItem>()
        val deviceModuleCom001Descrptions = ArrayList<AdministrationItem>()

        deviceModuleCom001.code = "COM-001"
        deviceModuleCom001.showResult = false
        deviceModuleCom001.comparable = false
        deviceModuleCom001.assessment = AssessmentEntity("CRITICAL")
        deviceModuleCom001.type = "SETTING"

        val administrationItemCom001ES = AdministrationItem()
        administrationItemCom001ES.key = "ES"
        administrationItemCom001ES.value = "Dispositivo rooteado"
        deviceModuleCom001Names.add(administrationItemCom001ES)

        val administrationItemCom001EN = AdministrationItem()
        administrationItemCom001EN.key = "EN"
        administrationItemCom001EN.value = "Rooted device"
        deviceModuleCom001Names.add(administrationItemCom001EN)

        deviceModuleCom001.names = deviceModuleCom001Names

        val administrationItem2Com001ES = AdministrationItem()
        administrationItem2Com001ES.key = "ES"
        administrationItem2Com001ES.value = "Dispositivo rooteado"
        deviceModuleCom001Descrptions.add(administrationItem2Com001ES)

        val administrationItem2Com001EN = AdministrationItem()
        administrationItem2Com001EN.key = "EN"
        administrationItem2Com001EN.value = "Rooted device"
        deviceModuleCom001Descrptions.add(administrationItem2Com001EN)

        deviceModuleCom001.descriptions = deviceModuleCom001Descrptions

        deviceModules1.add(deviceModuleCom001)
        //endregion

        //region COM002
        val deviceModuleCom002 = ModuleItem()
        val deviceModuleCom002Names = ArrayList<AdministrationItem>()
        val deviceModuleCom002Descriptions = ArrayList<AdministrationItem>()
        deviceModuleCom002.code = "COM-002"
        deviceModuleCom002.showResult = false
        deviceModuleCom002.comparable = false
        deviceModuleCom002.assessment = AssessmentEntity("NEUTRAL")
        deviceModuleCom002.type = "SETTING"

        val administrationItemCom002ES = AdministrationItem()
        administrationItemCom002ES.key = "ES"
        administrationItemCom002ES.value = "Depuracion USB"
        deviceModuleCom002Names.add(administrationItemCom002ES)

        val administrationItemCom002EN = AdministrationItem()
        administrationItemCom002EN.key = "EN"
        administrationItemCom002EN.value = "USB Debugging"
        deviceModuleCom002Names.add(administrationItemCom002EN)

        deviceModuleCom002.names = deviceModuleCom002Names

        val administrationItem2Com002ES = AdministrationItem()
        administrationItem2Com002ES.key = "ES"
        administrationItem2Com002ES.value = "Depuracion USB"
        deviceModuleCom002Descriptions.add(administrationItem2Com002ES)

        val administrationItem2Com002EN = AdministrationItem()
        administrationItem2Com002EN.key = "EN"
        administrationItem2Com002EN.value = "USB Debugging"
        deviceModuleCom002Descriptions.add(administrationItem2Com002EN)

        deviceModuleCom002.descriptions = deviceModuleCom002Descriptions

        deviceModules1.add(deviceModuleCom002)
        //endregion

        //region COM003
        val deviceModuleCom003 = ModuleItem()
        val deviceModuleCom003Names = ArrayList<AdministrationItem>()
        val deviceModuleCom003Descriptions = ArrayList<AdministrationItem>()

        deviceModuleCom003.valoration = "4"
        deviceModuleCom003.code = "COM-003"
        deviceModuleCom003.showResult = false
        deviceModuleCom003.comparable = false
        deviceModuleCom003.assessment = AssessmentEntity("GOOD")
        deviceModuleCom003.type = "SETTING"

        val administrationItemCom003ES = AdministrationItem()
        administrationItemCom003ES.key = "ES"
        administrationItemCom003ES.value = "Origenes desconocidos"
        deviceModuleCom003Names.add(administrationItemCom003ES)

        val administrationItemCom003EN = AdministrationItem()
        administrationItemCom003EN.key = "EN"
        administrationItemCom003EN.value = "Unknown origins"
        deviceModuleCom003Names.add(administrationItemCom003EN)

        deviceModuleCom003.names = deviceModuleCom003Names

        val administrationItem2Com003ES = AdministrationItem()
        administrationItem2Com003ES.key = "ES"
        administrationItem2Com003ES.value = "Origenes desconocidos"
        deviceModuleCom003Descriptions.add(administrationItem2Com003ES)

        val administrationItem2Com003EN = AdministrationItem()
        administrationItem2Com003EN.key = "EN"
        administrationItem2Com003EN.value = "Unknown origins"
        deviceModuleCom003Descriptions.add(administrationItem2Com003EN)

        deviceModuleCom003.descriptions = deviceModuleCom003Descriptions
        deviceModules1.add(deviceModuleCom003)
        //endregion

        //region COM004
        val deviceModuleCom004 = ModuleItem()
        val deviceModuleCom004Names = ArrayList<AdministrationItem>()
        val deviceModuleCom004Descriptions = ArrayList<AdministrationItem>()
        deviceModuleCom004.code = "COM-004"
        deviceModuleCom004.showResult = false
        deviceModuleCom004.comparable = false
        deviceModuleCom004.assessment = AssessmentEntity("GOOD")
        deviceModuleCom004.type = "SETTING"

        val administrationItemCom004ES = AdministrationItem()
        administrationItemCom004ES.key = "ES"
        administrationItemCom004ES.value = "Contraseñas en claro"
        deviceModuleCom004Names.add(administrationItemCom004ES)

        val administrationItemCom004EN = AdministrationItem()
        administrationItemCom004EN.key = "EN"
        administrationItemCom004EN.value = "Clear passwords"
        deviceModuleCom004Names.add(administrationItemCom004EN)

        deviceModuleCom004.names = deviceModuleCom004Names

        val administrationItem2Com004ES = AdministrationItem()
        administrationItem2Com004ES.key = "ES"
        administrationItem2Com004ES.value = "Contraseñas en claro"
        deviceModuleCom004Descriptions.add(administrationItem2Com004ES)

        val administrationItem2Com004EN = AdministrationItem()
        administrationItem2Com004EN.key = "EN"
        administrationItem2Com004EN.value = "Clear passwords"
        deviceModuleCom004Descriptions.add(administrationItem2Com004EN)

        deviceModuleCom004.descriptions = deviceModuleCom004Descriptions

        deviceModules1.add(deviceModuleCom004)
        //endregion

        //region COM005
        val deviceModuleCom005 = ModuleItem()
        val deviceModuleCom005Names = ArrayList<AdministrationItem>()
        val deviceModuleCom005Descriptions = ArrayList<AdministrationItem>()
        deviceModuleCom005.code = "COM-005"
        deviceModuleCom005.showResult = false
        deviceModuleCom005.comparable = false
        deviceModuleCom005.assessment = AssessmentEntity("CRITICAL")
        deviceModuleCom005.type = "SETTING"

        val administrationItemCom005ES = AdministrationItem()
        administrationItemCom005ES.key = "ES"
        administrationItemCom005ES.value = "Bloqueo del dispositivo"
        deviceModuleCom005Names.add(administrationItemCom005ES)

        val administrationItemCom005EN = AdministrationItem()
        administrationItemCom005EN.key = "EN"
        administrationItemCom005EN.value = "Device lock"
        deviceModuleCom005Names.add(administrationItemCom005EN)

        deviceModuleCom005.names = deviceModuleCom005Names

        val administrationItem2Com005ES = AdministrationItem()
        administrationItem2Com005ES.key = "ES"
        administrationItem2Com005ES.value = "Bloqueo del dispositivo"
        deviceModuleCom005Descriptions.add(administrationItem2Com005ES)

        val administrationItem2Com005EN = AdministrationItem()
        administrationItem2Com005EN.key = "EN"
        administrationItem2Com005EN.value = "Device lock"
        deviceModuleCom005Descriptions.add(administrationItem2Com005EN)

        deviceModuleCom005.descriptions = deviceModuleCom005Descriptions

        deviceModules1.add(deviceModuleCom005)
        //endregion

        //region COM006
        val deviceModuleCom006 = ModuleItem()
        val deviceModuleCom006Names = ArrayList<AdministrationItem>()
        val deviceModuleCom006Descriptions = ArrayList<AdministrationItem>()
        deviceModuleCom006.code = "COM-006"
        deviceModuleCom006.showResult = false
        deviceModuleCom006.comparable = false
        deviceModuleCom006.assessment = AssessmentEntity("GOOD")
        deviceModuleCom006.type = "SETTING"

        val administrationItemCom006ES = AdministrationItem()
        administrationItemCom006ES.key = "ES"
        administrationItemCom006ES.value = TIME_LOCK_ES
        deviceModuleCom006Names.add(administrationItemCom006ES)

        val administrationItemCom006EN = AdministrationItem()
        administrationItemCom006EN.key = "EN"
        administrationItemCom006EN.value = TIME_LOCK_EN
        deviceModuleCom006Names.add(administrationItemCom006EN)

        deviceModuleCom006.names = deviceModuleCom006Names

        val administrationItem2Com006ES = AdministrationItem()
        administrationItem2Com006ES.key = "ES"
        administrationItem2Com006ES.value = TIME_LOCK_ES
        deviceModuleCom006Descriptions.add(administrationItem2Com006ES)

        val administrationItem2Com006EN = AdministrationItem()
        administrationItem2Com006EN.key = "EN"
        administrationItem2Com006EN.value = TIME_LOCK_EN
        deviceModuleCom006Descriptions.add(administrationItem2Com006EN)

        deviceModuleCom006.descriptions = deviceModuleCom006Descriptions

        deviceModules1.add(deviceModuleCom006)
        //endregion

        //region COM007
        val deviceModuleCom007 = ModuleItem()
        val deviceModuleCom007Names = ArrayList<AdministrationItem>()
        val deviceModuleCom007Descriptions = ArrayList<AdministrationItem>()
        deviceModuleCom007.code = "COM-007"
        deviceModuleCom007.showResult = false
        deviceModuleCom007.comparable = false
        deviceModuleCom007.assessment = AssessmentEntity("GOOD")
        deviceModuleCom007.type = "SETTING"

        val administrationItemCom007ES = AdministrationItem()
        administrationItemCom007ES.key = "ES"
        administrationItemCom007ES.value = "Redes Wi-Fi inseguras"
        deviceModuleCom007Names.add(administrationItemCom007ES)

        val administrationItemCom007EN = AdministrationItem()
        administrationItemCom007EN.key = "EN"
        administrationItemCom007EN.value = "Insecure Wi-Fi networks"
        deviceModuleCom007Names.add(administrationItemCom007EN)

        deviceModuleCom007.names = deviceModuleCom007Names

        val administrationItem2Com007ES = AdministrationItem()
        administrationItem2Com007ES.key = "ES"
        administrationItem2Com007ES.value = TIME_LOCK_ES
        deviceModuleCom007Descriptions.add(administrationItem2Com007ES)

        val administrationItem2Com007EN = AdministrationItem()
        administrationItem2Com007EN.key = "EN"
        administrationItem2Com007EN.value = TIME_LOCK_EN
        deviceModuleCom007Descriptions.add(administrationItem2Com007EN)

        deviceModuleCom007.descriptions = deviceModuleCom007Descriptions

        deviceModules1.add(deviceModuleCom007)
        //endregion

        //region COM008
        val deviceModuleCom008 = ModuleItem()
        val deviceModuleCom008Names = ArrayList<AdministrationItem>()
        val deviceModuleCom008Descriptions = ArrayList<AdministrationItem>()
        deviceModuleCom008.code = "COM-008"
        deviceModuleCom008.showResult = false
        deviceModuleCom008.comparable = false
        deviceModuleCom008.assessment = AssessmentEntity("GOOD")
        deviceModuleCom008.type = "SETTING"

        val administrationItemCom008ES = AdministrationItem()
        administrationItemCom008ES.key = "ES"
        administrationItemCom008ES.value = "Dispositivos bluetooth conectados"
        deviceModuleCom008Names.add(administrationItemCom008ES)

        val administrationItemCom008EN = AdministrationItem()
        administrationItemCom008EN.key = "EN"
        administrationItemCom008EN.value = "Connected bluetooth devices"
        deviceModuleCom008Names.add(administrationItemCom008EN)

        deviceModuleCom008.names = deviceModuleCom008Names

        val administrationItem2Com008ES = AdministrationItem()
        administrationItem2Com008ES.key = "ES"
        administrationItem2Com008ES.value = "Dispositivos bluetooth conectados"
        deviceModuleCom008Descriptions.add(administrationItem2Com008ES)

        val administrationItem2Com008EN = AdministrationItem()
        administrationItem2Com008EN.key = "EN"
        administrationItem2Com008EN.value = "Connected bluetooth devices"
        deviceModuleCom008Descriptions.add(administrationItem2Com008EN)

        deviceModuleCom008.descriptions = deviceModuleCom008Descriptions

        deviceModules1.add(deviceModuleCom008)
        //endregion

        //region COM009
        val deviceModuleCom009 = ModuleItem()
        val deviceModuleCom009Names = ArrayList<AdministrationItem>()
        val deviceModuleCom009Descriptions = ArrayList<AdministrationItem>()
        deviceModuleCom009.code = "COM-009"
        deviceModuleCom009.showResult = false
        deviceModuleCom009.comparable = false
        deviceModuleCom009.assessment = AssessmentEntity("GOOD")
        deviceModuleCom009.type = "SETTING"

        val administrationItemCom009ES = AdministrationItem()
        administrationItemCom009ES.key = "ES"
        administrationItemCom009ES.value = "Bloqueo del terminal"
        deviceModuleCom009Names.add(administrationItemCom009ES)

        val administrationItemCom009EN = AdministrationItem()
        administrationItemCom009EN.key = "EN"
        administrationItemCom009EN.value = "Terminal lock"
        deviceModuleCom009Names.add(administrationItemCom009EN)

        deviceModuleCom009.names = deviceModuleCom009Names

        val administrationItem2Com009ES = AdministrationItem()
        administrationItem2Com009ES.key = "ES"
        administrationItem2Com009ES.value = "Bloqueo del terminal"
        deviceModuleCom009Descriptions.add(administrationItem2Com009ES)

        val administrationItem2Com009EN = AdministrationItem()
        administrationItem2Com009EN.key = "EN"
        administrationItem2Com009EN.value = "Terminal lock"
        deviceModuleCom009Descriptions.add(administrationItem2Com009EN)

        deviceModuleCom009.descriptions = deviceModuleCom009Descriptions

        deviceModules1.add(deviceModuleCom009)
        //endregion

        //region COM0010
        val deviceModuleCom010 = ModuleItem()
        val deviceModuleCom010Names = ArrayList<AdministrationItem>()
        val deviceModuleCom010Descriptions = ArrayList<AdministrationItem>()
        deviceModuleCom010.code = "COM-010"
        deviceModuleCom010.showResult = false
        deviceModuleCom010.comparable = false
        deviceModuleCom010.assessment = AssessmentEntity("GOOD")
        deviceModuleCom010.type = "SETTING"

        val administrationItemCom010ES = AdministrationItem()
        administrationItemCom010ES.key = "ES"
        administrationItemCom010ES.value = "Verificacion de aplicaciones"
        deviceModuleCom010Names.add(administrationItemCom010ES)

        val administrationItemCom010EN = AdministrationItem()
        administrationItemCom010EN.key = "EN"
        administrationItemCom010EN.value = "Application verification"
        deviceModuleCom010Names.add(administrationItemCom010EN)

        deviceModuleCom010.names = deviceModuleCom010Names

        val administrationItem2Com010ES = AdministrationItem()
        administrationItem2Com010ES.key = "ES"
        administrationItem2Com010ES.value = "Verificacion de aplicaciones"
        deviceModuleCom010Descriptions.add(administrationItem2Com010ES)

        val administrationItem2Com010EN = AdministrationItem()
        administrationItem2Com010EN.key = "EN"
        administrationItem2Com010EN.value = "Application verification"
        deviceModuleCom010Descriptions.add(administrationItem2Com010EN)

        deviceModuleCom010.descriptions = deviceModuleCom010Descriptions

        deviceModules1.add(deviceModuleCom010)
        //endregion

        //region COM0011
        val deviceModuleCom011 = ModuleItem()
        val deviceModuleCom011Names = ArrayList<AdministrationItem>()
        val deviceModuleCom011Descriptions = ArrayList<AdministrationItem>()
        deviceModuleCom011.code = "COM-011"
        deviceModuleCom011.showResult = false
        deviceModuleCom011.comparable = false
        deviceModuleCom011.assessment = AssessmentEntity("GOOD")
        deviceModuleCom011.type = "SETTING"

        val administrationItemCom011ES = AdministrationItem()
        administrationItemCom011ES.key = "ES"
        administrationItemCom011ES.value = "Zona Wi-Fi"
        deviceModuleCom011Names.add(administrationItemCom011ES)

        val administrationItemCom011EN = AdministrationItem()
        administrationItemCom011EN.key = "EN"
        administrationItemCom011EN.value = "WIFI zone"
        deviceModuleCom011Names.add(administrationItemCom011EN)

        deviceModuleCom011.names = deviceModuleCom011Names

        val administrationItem2Com011ES = AdministrationItem()
        administrationItem2Com011ES.key = "ES"
        administrationItem2Com011ES.value = "Zona Wi-Fi"
        deviceModuleCom011Descriptions.add(administrationItem2Com011ES)

        val administrationItem2Com011EN = AdministrationItem()
        administrationItem2Com011EN.key = "EN"
        administrationItem2Com011EN.value = "WIFI zone"
        deviceModuleCom011Descriptions.add(administrationItem2Com011EN)

        deviceModuleCom011.descriptions = deviceModuleCom011Descriptions

        deviceModules1.add(deviceModuleCom011)
        //endregion

        //region COM0012
        val deviceModuleCom012 = ModuleItem()
        val deviceModuleCom012Names = ArrayList<AdministrationItem>()
        val deviceModuleCom012Descriptions = ArrayList<AdministrationItem>()
        deviceModuleCom012.code = "COM-012"
        deviceModuleCom012.showResult = false
        deviceModuleCom012.comparable = false
        deviceModuleCom012.assessment = AssessmentEntity("GOOD")
        deviceModuleCom012.type = "SETTING"

        val administrationItemCom012ES = AdministrationItem()
        administrationItemCom012ES.key = "ES"
        administrationItemCom012ES.value = BLUETOOTH_TETHERING_ES
        deviceModuleCom012Names.add(administrationItemCom012ES)

        val administrationItemCom012EN = AdministrationItem()
        administrationItemCom012EN.key = "EN"
        administrationItemCom012EN.value = BLUETOOTH_TETHERING_EN
        deviceModuleCom012Names.add(administrationItemCom012EN)

        deviceModuleCom012.names = deviceModuleCom012Names

        val administrationItem2Com012ES = AdministrationItem()
        administrationItem2Com012ES.key = "ES"
        administrationItem2Com012ES.value = BLUETOOTH_TETHERING_ES
        deviceModuleCom012Descriptions.add(administrationItem2Com012ES)

        val administrationItem2Com012EN = AdministrationItem()
        administrationItem2Com012EN.key = "EN"
        administrationItem2Com012EN.value = BLUETOOTH_TETHERING_EN
        deviceModuleCom012Descriptions.add(administrationItem2Com012EN)

        deviceModuleCom012.descriptions = deviceModuleCom012Descriptions

        deviceModules1.add(deviceModuleCom012)
        //endregion

        //region COM0013
        val deviceModuleCom013 = ModuleItem()
        val deviceModuleCom013Names = ArrayList<AdministrationItem>()
        val deviceModuleCom013Descriptions = ArrayList<AdministrationItem>()
        deviceModuleCom013.code = "COM-013"
        deviceModuleCom013.showResult = false
        deviceModuleCom013.comparable = false
        deviceModuleCom013.assessment = AssessmentEntity("GOOD")
        deviceModuleCom013.type = "SETTING"

        val administrationItemCom013ES = AdministrationItem()
        administrationItemCom013ES.key = "ES"
        administrationItemCom013ES.value = "Monitorizacion del archivo host"
        deviceModuleCom013Names.add(administrationItemCom013ES)

        val administrationItemCom013EN = AdministrationItem()
        administrationItemCom013EN.key = "EN"
        administrationItemCom013EN.value = "Host file monitoring"
        deviceModuleCom013Names.add(administrationItemCom013EN)

        deviceModuleCom013.names = deviceModuleCom013Names

        val administrationItem2Com013ES = AdministrationItem()
        administrationItem2Com013ES.key = "ES"
        administrationItem2Com013ES.value = "Monitorizacion del archivo host"
        deviceModuleCom013Descriptions.add(administrationItem2Com013ES)

        val administrationItem2Com013EN = AdministrationItem()
        administrationItem2Com013EN.key = "EN"
        administrationItem2Com013EN.value = "Host file monitoring"
        deviceModuleCom013Descriptions.add(administrationItem2Com013EN)

        deviceModuleCom013.descriptions = deviceModuleCom013Descriptions

        deviceModules1.add(deviceModuleCom013)
        //endregion

        //region COM0014
        val deviceModuleCom014 = ModuleItem()
        val deviceModuleCom014Names = ArrayList<AdministrationItem>()
        val deviceModuleCom014Descriptions = ArrayList<AdministrationItem>()
        deviceModuleCom014.code = "COM-014"
        deviceModuleCom014.showResult = false
        deviceModuleCom014.comparable = false
        deviceModuleCom014.assessment = AssessmentEntity("GOOD")
        deviceModuleCom014.type = "SETTING"

        val administrationItemCom014ES = AdministrationItem()
        administrationItemCom014ES.key = "ES"
        administrationItemCom014ES.value = "Ocultación de notificaciones"
        deviceModuleCom014Names.add(administrationItemCom014ES)

        val administrationItemCom014EN = AdministrationItem()
        administrationItemCom014EN.key = "EN"
        administrationItemCom014EN.value = "Notification hiding"
        deviceModuleCom014Names.add(administrationItemCom014EN)

        deviceModuleCom014.names = deviceModuleCom014Names

        val administrationItem2Com014ES = AdministrationItem()
        administrationItem2Com014ES.key = "ES"
        administrationItem2Com014ES.value = "Ocultación de notificaciones"
        deviceModuleCom014Descriptions.add(administrationItem2Com014ES)

        val administrationItem2Com014EN = AdministrationItem()
        administrationItem2Com014EN.key = "EN"
        administrationItem2Com014EN.value = "Notification hiding"
        deviceModuleCom014Descriptions.add(administrationItem2Com014EN)

        deviceModuleCom014.descriptions = deviceModuleCom014Descriptions

        deviceModules1.add(deviceModuleCom014)


        //region COM0016
        val deviceModuleCom016 = ModuleItem()
        val deviceModuleCom016Names = ArrayList<AdministrationItem>()
        val deviceModuleCom016Descriptions = ArrayList<AdministrationItem>()
        deviceModuleCom016.code = "COM-016"
        deviceModuleCom016.showResult = false
        deviceModuleCom016.comparable = false
        deviceModuleCom016.assessment = AssessmentEntity("GOOD")
        deviceModuleCom016.type = "SETTING"

        val administrationItemCom016ES = AdministrationItem()
        administrationItemCom016ES.key = "ES"
        administrationItemCom016ES.value = "Desbloqueo del bootloader"
        deviceModuleCom016Names.add(administrationItemCom016ES)

        val administrationItemCom016EN = AdministrationItem()
        administrationItemCom016EN.key = "EN"
        administrationItemCom016EN.value = "Unlocking the bootloader"
        deviceModuleCom016Names.add(administrationItemCom016EN)

        deviceModuleCom016.names = deviceModuleCom016Names

        val administrationItem2Com016ES = AdministrationItem()
        administrationItem2Com016ES.key = "ES"
        administrationItem2Com016ES.value = "Explicación Desbloqueo del bootloader"
        deviceModuleCom016Descriptions.add(administrationItem2Com016ES)

        val administrationItem2Com016EN = AdministrationItem()
        administrationItem2Com016EN.key = "EN"
        administrationItem2Com016EN.value = "Explanation Unlocking the bootloader"
        deviceModuleCom016Descriptions.add(administrationItem2Com016EN)

        deviceModuleCom016.descriptions = deviceModuleCom016Descriptions

        deviceModules1.add(deviceModuleCom016)
        //endregion


        //region COM0017
        val deviceModuleCom017 = ModuleItem()
        val deviceModuleCom017Names = ArrayList<AdministrationItem>()
        val deviceModuleCom017Descriptions = ArrayList<AdministrationItem>()
        deviceModuleCom017.code = "COM-017"
        deviceModuleCom017.showResult = false
        deviceModuleCom017.comparable = false
        deviceModuleCom017.assessment = AssessmentEntity("GOOD")
        deviceModuleCom017.type = "SETTING"

        val administrationItemCom017ES = AdministrationItem()
        administrationItemCom017ES.key = "ES"
        administrationItemCom017ES.value = "Acceso a notificaciones"
        deviceModuleCom017Names.add(administrationItemCom017ES)

        val administrationItemCom017EN = AdministrationItem()
        administrationItemCom017EN.key = "EN"
        administrationItemCom017EN.value = "Notification access"
        deviceModuleCom017Names.add(administrationItemCom017EN)

        deviceModuleCom017.names = deviceModuleCom017Names

        val administrationItem2Com017ES = AdministrationItem()
        administrationItem2Com017ES.key = "ES"
        administrationItem2Com017ES.value = "Acceso a notificaciones"
        deviceModuleCom017Descriptions.add(administrationItem2Com017ES)

        val administrationItem2Com017EN = AdministrationItem()
        administrationItem2Com017EN.key = "EN"
        administrationItem2Com017EN.value = "Notification access"
        deviceModuleCom017Descriptions.add(administrationItem2Com017EN)

        deviceModuleCom017.descriptions = deviceModuleCom017Descriptions

        deviceModules1.add(deviceModuleCom017)
        //endregion

        //region COM0018
        val deviceModuleCom018 = ModuleItem()
        val deviceModuleCom018Names = ArrayList<AdministrationItem>()
        val deviceModuleCom018Descriptions = ArrayList<AdministrationItem>()
        deviceModuleCom018.code = "COM-018"
        deviceModuleCom018.showResult = false
        deviceModuleCom018.comparable = false
        deviceModuleCom018.assessment = AssessmentEntity("GOOD")
        deviceModuleCom018.type = "SETTING"

        val administrationItemCom018ES = AdministrationItem()
        administrationItemCom018ES.key = "ES"
        administrationItemCom018ES.value = "Pantalla activa durante la carga"
        deviceModuleCom018Names.add(administrationItemCom018ES)

        val administrationItemCom018EN = AdministrationItem()
        administrationItemCom018EN.key = "EN"
        administrationItemCom018EN.value = "Screen active while charging"
        deviceModuleCom018Names.add(administrationItemCom018EN)

        deviceModuleCom018.names = deviceModuleCom018Names

        val administrationItem2Com018ES = AdministrationItem()
        administrationItem2Com018ES.key = "ES"
        administrationItem2Com018ES.value = "Pantalla activa durante la carga"
        deviceModuleCom018Descriptions.add(administrationItem2Com018ES)

        val administrationItem2Com018EN = AdministrationItem()
        administrationItem2Com018EN.key = "EN"
        administrationItem2Com018EN.value = "Screen active while charging"
        deviceModuleCom018Descriptions.add(administrationItem2Com018EN)

        deviceModuleCom018.descriptions = deviceModuleCom018Descriptions

        deviceModules1.add(deviceModuleCom018)
        //endregion

        //region COM0019
        val deviceModuleCom019 = ModuleItem()
        val deviceModuleCom019Names = ArrayList<AdministrationItem>()
        val deviceModuleCom019Descriptions = ArrayList<AdministrationItem>()
        deviceModuleCom019.code = "COM-019"
        deviceModuleCom019.showResult = false
        deviceModuleCom019.comparable = false
        deviceModuleCom019.assessment = AssessmentEntity("GOOD")
        deviceModuleCom019.type = "SETTING"

        val administrationItemCom019ES = AdministrationItem()
        administrationItemCom019ES.key = "ES"
        administrationItemCom019ES.value = "Nombre con version del dispositivo"
        deviceModuleCom019Names.add(administrationItemCom019ES)

        val administrationItemCom019EN = AdministrationItem()
        administrationItemCom019EN.key = "EN"
        administrationItemCom019EN.value = "Device name with version"
        deviceModuleCom019Names.add(administrationItemCom019EN)

        deviceModuleCom019.names = deviceModuleCom019Names

        val administrationItem2Com019ES = AdministrationItem()
        administrationItem2Com019ES.key = "ES"
        administrationItem2Com019ES.value = "Nombre con version del dispositivo"
        deviceModuleCom019Descriptions.add(administrationItem2Com019ES)

        val administrationItem2Com019EN = AdministrationItem()
        administrationItem2Com019EN.key = "EN"
        administrationItem2Com019EN.value = "Device name with version"
        deviceModuleCom019Descriptions.add(administrationItem2Com019EN)

        deviceModuleCom019.descriptions = deviceModuleCom019Descriptions

        deviceModules1.add(deviceModuleCom019)
        //endregion

        //region COM0025
        val deviceModuleCom025 = ModuleItem()
        val deviceModuleCom025Names = ArrayList<AdministrationItem>()
        deviceModuleCom025.code = "COM-025"
        deviceModuleCom025.showResult = false
        deviceModuleCom025.comparable = false
        deviceModuleCom025.assessment = AssessmentEntity("GOOD")
        deviceModuleCom025.type = "SETTING"

        val administrationItemCom025ES = AdministrationItem()
        administrationItemCom025ES.key = "ES"
        administrationItemCom025ES.value = "Bluetooth activado pero sin conexión"
        deviceModuleCom025Names.add(administrationItemCom025ES)

        val administrationItemCom025EN = AdministrationItem()
        administrationItemCom025EN.key = "EN"
        administrationItemCom025EN.value = "Bluetooth activated but without connection"
        deviceModuleCom025Names.add(administrationItemCom025EN)

        deviceModuleCom025.names = deviceModuleCom025Names

        deviceModules1.add(deviceModuleCom025)
        //endregion

        //region COM0026
        val deviceModuleCom026 = ModuleItem()
        val deviceModuleCom026Names = ArrayList<AdministrationItem>()
        deviceModuleCom026.code = "COM-026"
        deviceModuleCom026.showResult = false
        deviceModuleCom026.comparable = false
        deviceModuleCom026.assessment = AssessmentEntity("GOOD")
        deviceModuleCom026.type = "SETTING"

        val administrationItemCom026ES = AdministrationItem()
        administrationItemCom026ES.key = "ES"
        administrationItemCom026ES.value = "Notificaciones de redes abiertas"
        deviceModuleCom026Names.add(administrationItemCom026ES)

        val administrationItemCom026EN = AdministrationItem()
        administrationItemCom026EN.key = "EN"
        administrationItemCom026EN.value = "Open Network Notifications"
        deviceModuleCom026Names.add(administrationItemCom026EN)

        deviceModuleCom026.names = deviceModuleCom026Names

        deviceModules1.add(deviceModuleCom026)

        //region COM0027
        val deviceModuleCom027 = ModuleItem()
        val deviceModuleCom027Names = ArrayList<AdministrationItem>()
        val deviceModuleCom027Descriptions = ArrayList<AdministrationItem>()
        deviceModuleCom027.code = "COM-027"
        deviceModuleCom027.showResult = false
        deviceModuleCom027.comparable = false
        deviceModuleCom027.assessment = AssessmentEntity("GOOD")
        deviceModuleCom014.type = "SETTING"

        val administrationItemCom027ES = AdministrationItem()
        administrationItemCom027ES.key = "ES"
        administrationItemCom027ES.value = "Conectarse a redes abiertas de forma automática"
        deviceModuleCom027Names.add(administrationItemCom027ES)

        val administrationItemCom027EN = AdministrationItem()
        administrationItemCom027EN.key = "EN"
        administrationItemCom027EN.value = "Connect to open networks automatically"
        deviceModuleCom027Names.add(administrationItemCom027EN)

        deviceModuleCom027.names = deviceModuleCom027Names

        val administrationItem2Com027ES = AdministrationItem()
        administrationItem2Com027ES.key = "ES"
        administrationItem2Com027ES.value = "Conectarse a redes abiertas de forma automática"
        deviceModuleCom027Descriptions.add(administrationItem2Com027ES)

        val administrationItem2Com027EN = AdministrationItem()
        administrationItem2Com027EN.key = "EN"
        administrationItem2Com027EN.value = "Connect to open networks automatically"
        deviceModuleCom027Descriptions.add(administrationItem2Com027EN)

        deviceModuleCom027.descriptions = deviceModuleCom027Descriptions

        deviceModules1.add(deviceModuleCom027)
        //endregion

        //region COM0028
        val deviceModuleCom028 = ModuleItem()
        val deviceModuleCom028Names = ArrayList<AdministrationItem>()
        deviceModuleCom028.code = "COM-028"
        deviceModuleCom028.showResult = false
        deviceModuleCom028.comparable = false
        deviceModuleCom028.assessment = AssessmentEntity("GOOD")
        deviceModuleCom028.type = "SETTING"

        val administrationItemCom028ES = AdministrationItem()
        administrationItemCom028ES.key = "ES"
        administrationItemCom028ES.value = "WIFI activado pero sin conexión"
        deviceModuleCom028Names.add(administrationItemCom028ES)

        val administrationItemCom028EN = AdministrationItem()
        administrationItemCom028EN.key = "EN"
        administrationItemCom028EN.value = "WIFI activated but without connection"
        deviceModuleCom028Names.add(administrationItemCom028EN)

        deviceModuleCom028.names = deviceModuleCom028Names

        deviceModules1.add(deviceModuleCom028)
        //endregion

        //region COM0018
        val deviceModuleCom029 = ModuleItem()
        val deviceModuleCom029Names = ArrayList<AdministrationItem>()
        val deviceModuleCom029Descriptions = ArrayList<AdministrationItem>()
        deviceModuleCom029.code = "COM-029"
        deviceModuleCom029.showResult = false
        deviceModuleCom029.comparable = false
        deviceModuleCom029.assessment = AssessmentEntity("GOOD")
        deviceModuleCom029.type = "SETTING"

        val administrationItemCom029ES = AdministrationItem()
        administrationItemCom029ES.key = "ES"
        administrationItemCom029ES.value = "NFC activo indefinidamente"
        deviceModuleCom029Names.add(administrationItemCom029ES)

        val administrationItemCom029EN = AdministrationItem()
        administrationItemCom029EN.key = "EN"
        administrationItemCom029EN.value = "NFC active indefinitely"
        deviceModuleCom029Names.add(administrationItemCom029EN)

        deviceModuleCom029.names = deviceModuleCom029Names

        val administrationItem2Com029ES = AdministrationItem()
        administrationItem2Com029ES.key = "ES"
        administrationItem2Com029ES.value = "NFC activo indefinidamente"
        deviceModuleCom029Descriptions.add(administrationItem2Com029ES)

        val administrationItem2Com029EN = AdministrationItem()
        administrationItem2Com029EN.key = "EN"
        administrationItem2Com029EN.value = "NFC active indefinitely"
        deviceModuleCom029Descriptions.add(administrationItem2Com029EN)

        deviceModuleCom029.descriptions = deviceModuleCom029Descriptions

        deviceModules1.add(deviceModuleCom029)
        //endregion

        //region COM0030
        val deviceModuleCom030 = ModuleItem()
        val deviceModuleCom030Names = ArrayList<AdministrationItem>()
        deviceModuleCom030.code = "COM-030"
        deviceModuleCom030.showResult = false
        deviceModuleCom030.comparable = false
        deviceModuleCom030.assessment = AssessmentEntity("GOOD")
        deviceModuleCom030.type = "SETTING"

        val administrationItemCom030ES = AdministrationItem()
        administrationItemCom030ES.key = "ES"
        administrationItemCom030ES.value = "Android Beam"
        deviceModuleCom030Names.add(administrationItemCom030ES)

        val administrationItemCom030EN = AdministrationItem()
        administrationItemCom030EN.key = "EN"
        administrationItemCom030EN.value = "Android Beam"
        deviceModuleCom030Names.add(administrationItemCom030EN)

        deviceModuleCom030.names = deviceModuleCom030Names

        deviceModules1.add(deviceModuleCom030)
        //endregion

        analysisItem1.deviceModules = deviceModules1

        //endregion

        //region appliacionModules

        //region COM0102
        val deviceModuleCom102 = ModuleItem()
        val deviceModuleCom102Names = ArrayList<AdministrationItem>()
        val deviceModuleCom102Descriptions = ArrayList<AdministrationItem>()
        deviceModuleCom102.code = "COM-102"
        deviceModuleCom102.showResult = false
        deviceModuleCom102.comparable = false
        deviceModuleCom102.assessment = AssessmentEntity("GOOD")
        deviceModuleCom102.type = "APPLICATION"

        val administrationItemCom102ES = AdministrationItem()
        administrationItemCom102ES.key = "ES"
        administrationItemCom102ES.value = APPLICATION_INFORMATION_ES
        deviceModuleCom102Names.add(administrationItemCom102ES)

        val administrationItemCom102EN = AdministrationItem()
        administrationItemCom102EN.key = "EN"
        administrationItemCom102EN.value = APPLICATION_INFORMATION_EN
        deviceModuleCom102Names.add(administrationItemCom102EN)

        deviceModuleCom102.names = deviceModuleCom102Names

        val administrationItem2Com102ES = AdministrationItem()
        administrationItem2Com102ES.key = "ES"
        administrationItem2Com102ES.value = APPLICATION_INFORMATION_ES
        deviceModuleCom102Descriptions.add(administrationItem2Com102ES)

        val administrationItem2Com102EN = AdministrationItem()
        administrationItem2Com102EN.key = "EN"
        administrationItem2Com102EN.value = APPLICATION_INFORMATION_EN
        deviceModuleCom102Descriptions.add(administrationItem2Com102EN)

        deviceModuleCom102.descriptions = deviceModuleCom102Descriptions

        applicationModules1.add(deviceModuleCom102)
        //endregion

        //region COM0103
        val deviceModuleCom103 = ModuleItem()
        val deviceModuleCom103Names = ArrayList<AdministrationItem>()
        val deviceModuleCom103Descriptions = ArrayList<AdministrationItem>()
        deviceModuleCom103.code = "COM-103"
        deviceModuleCom103.showResult = false
        deviceModuleCom103.comparable = false
        deviceModuleCom103.assessment = AssessmentEntity("GOOD")
        deviceModuleCom103.type = "APPLICATION"

        val administrationItemCom103ES = AdministrationItem()
        administrationItemCom103ES.key = "ES"
        administrationItemCom103ES.value = "Obtención de permisos habilitados en las aplicaciones"
        deviceModuleCom103Names.add(administrationItemCom103ES)

        val administrationItemCom103EN = AdministrationItem()
        administrationItemCom103EN.key = "EN"
        administrationItemCom103EN.value = "Obtaining permissions enabled in the applications"
        deviceModuleCom103Names.add(administrationItemCom103EN)

        deviceModuleCom103.names = deviceModuleCom103Names

        val administrationItem2Com103ES = AdministrationItem()
        administrationItem2Com103ES.key = "ES"
        administrationItem2Com103ES.value = "Obtención de permisos habilitados en las aplicaciones"
        deviceModuleCom103Descriptions.add(administrationItem2Com103ES)

        val administrationItem2Com103EN = AdministrationItem()
        administrationItem2Com103EN.key = "EN"
        administrationItem2Com103EN.value = "Obtaining permissions enabled in the applications"
        deviceModuleCom103Descriptions.add(administrationItem2Com103EN)

        deviceModuleCom103.descriptions = deviceModuleCom103Descriptions

        applicationModules1.add(deviceModuleCom103)
        //endregion


        analysisItem1.applicationModules = applicationModules1
        //endregion

        //region systemModules
        //region COM0201
        val deviceModuleCom201 = ModuleItem()
        val deviceModuleCom201Names = ArrayList<AdministrationItem>()
        val deviceModuleCom201Descriptions = ArrayList<AdministrationItem>()
        deviceModuleCom201.code = "COM-201"
        deviceModuleCom201.showResult = false
        deviceModuleCom201.comparable = false
        deviceModuleCom201.assessment = AssessmentEntity("GOOD")
        deviceModuleCom201.type = "SYSTEM"

        val administrationItemCom201ES = AdministrationItem()
        administrationItemCom201ES.key = "ES"
        administrationItemCom201ES.value = "Obtención de información del sistema"
        deviceModuleCom201Names.add(administrationItemCom201ES)

        val administrationItemCom201EN = AdministrationItem()
        administrationItemCom201EN.key = "EN"
        administrationItemCom201EN.value = "Obtaining information from the system"
        deviceModuleCom201Names.add(administrationItemCom201EN)

        deviceModuleCom201.names = deviceModuleCom201Names

        val administrationItem2Com201ES = AdministrationItem()
        administrationItem2Com201ES.key = "ES"
        administrationItem2Com201ES.value = "Obtención de información del sistema"
        deviceModuleCom201Descriptions.add(administrationItem2Com201ES)

        val administrationItem2Com201EN = AdministrationItem()
        administrationItem2Com201EN.key = "EN"
        administrationItem2Com201EN.value = "Obtaining information from the system"
        deviceModuleCom201Descriptions.add(administrationItem2Com201EN)

        deviceModuleCom201.descriptions = deviceModuleCom201Descriptions

        systemModules1.add(deviceModuleCom201)
        //endregion

        //region COM0203
        val deviceModuleCom203 = ModuleItem()
        val deviceModuleCom203Names = ArrayList<AdministrationItem>()
        val deviceModuleCom203Descriptions = ArrayList<AdministrationItem>()
        deviceModuleCom203.code = "COM-203"
        deviceModuleCom203.showResult = false
        deviceModuleCom203.comparable = false
        deviceModuleCom203.assessment = AssessmentEntity("GOOD")
        deviceModuleCom203.type = "SYSTEM"

        val administrationItemCom203ES = AdministrationItem()
        administrationItemCom203ES.key = "ES"
        administrationItemCom203ES.value = "La IP del dispositivo pertenece a una Botnet"
        deviceModuleCom203Names.add(administrationItemCom203ES)

        val administrationItemCom203EN = AdministrationItem()
        administrationItemCom203EN.key = "EN"
        administrationItemCom203EN.value = "Your device IP belongs to a Botnet"
        deviceModuleCom203Names.add(administrationItemCom203EN)

        deviceModuleCom203.names = deviceModuleCom203Names

        val administrationItem2Com203ES = AdministrationItem()
        administrationItem2Com203ES.key = "ES"
        administrationItem2Com203ES.value = "Comprobar que la IP no pertenece a una Botnet"
        deviceModuleCom203Descriptions.add(administrationItem2Com203ES)

        val administrationItem2Com203EN = AdministrationItem()
        administrationItem2Com203EN.key = "EN"
        administrationItem2Com203EN.value = "Check that the IP does not belong to a Botnet"
        deviceModuleCom203Descriptions.add(administrationItem2Com203EN)

        deviceModuleCom203.descriptions = deviceModuleCom203Descriptions
        systemModules1.add(deviceModuleCom203)
        //endregion
        analysisItem1.systemModules = systemModules1
        //endregion
        analysis.add(analysisItem1)
        //endregion

        //region Analisis Aplicaciones
        val analysisItem2 = AnalysisItem()
        val administrationItemNames2 = ArrayList<AdministrationItem>()
        val descriptions2 = ArrayList<AdministrationItem>()
        val deviceModules2 = ArrayList<ModuleItem>()
        val applicationModules2 = ArrayList<ModuleItem>()
        val systemModules2 = ArrayList<ModuleItem>()
        analysisItem2.deviceModules = deviceModules2
        analysisItem2.systemModules = systemModules2

        analysisItem2.id = "61644cbf8b9f3053f227fa03"

        //region Names
        val administrationItemNamesES = AdministrationItem()
        administrationItemNamesES.key = "ES"
        administrationItemNamesES.value = "Análisis de aplicaciones"
        administrationItemNames2.add(administrationItemNamesES)

        val administrationItemNamesEN = AdministrationItem()
        administrationItemNamesEN.key = "EN"
        administrationItemNamesEN.value = "Application analysis"
        administrationItemNames2.add(administrationItemNamesEN)

        analysisItem2.names = administrationItemNames2
        //endregion

        //region descriptions
        val administrationItemDescES = AdministrationItem()
        administrationItemDescES.key = "ES"
        administrationItemDescES.value =
            "Se realizará aquellas comprobaciones relativas a las aplicaciones y a la configuración del móvil relativas a la gestión de las aplicaciones en el terminal"
        descriptions2.add(administrationItemDescES)

        val administrationItemDescEN = AdministrationItem()
        administrationItemDescEN.key = "EN"
        administrationItemDescEN.value =
            "Checks related to the applications and the configuration of the mobile will be carried out regarding the management of the applications in the terminal."
        descriptions2.add(administrationItemDescEN)

        analysisItem2.descriptions = descriptions2
        //endregion

        //region applicationModules

        //region COM0102
        val deviceModuleCom102_2 = ModuleItem()
        val deviceModuleCom102_2Names = ArrayList<AdministrationItem>()
        deviceModuleCom102_2.code = "COM-102"
        deviceModuleCom102_2.showResult = false
        deviceModuleCom102_2.comparable = false
        deviceModuleCom102_2.assessment = AssessmentEntity("GOOD")
        deviceModuleCom102_2.type = "APPLICATION"

        val administrationItemCom102_2ES = AdministrationItem()
        administrationItemCom102_2ES.key = "ES"
        administrationItemCom102_2ES.value = APPLICATION_INFORMATION_ES
        deviceModuleCom102_2Names.add(administrationItemCom102_2ES)

        val administrationItemCom102Es = AdministrationItem()
        administrationItemCom102Es.key = "EN"
        administrationItemCom102Es.value = APPLICATION_INFORMATION_EN
        deviceModuleCom102_2Names.add(administrationItemCom102Es)

        deviceModuleCom102_2.names = deviceModuleCom102_2Names

        applicationModules2.add(deviceModuleCom102_2)
        //endregion

        //region COM0202
        val deviceModuleCom202 = ModuleItem()
        val deviceModuleCom202Names = ArrayList<AdministrationItem>()
        deviceModuleCom202.code = "COM-202"
        deviceModuleCom202.showResult = false
        deviceModuleCom202.comparable = false
        deviceModuleCom202.assessment = AssessmentEntity("GOOD")
        deviceModuleCom202.type = "SETTING"

        val administrationItemCom202ES = AdministrationItem()
        administrationItemCom202ES.key = "ES"
        administrationItemCom202ES.value = "Lenguage del dispositivo"
        deviceModuleCom202Names.add(administrationItemCom202ES)

        val administrationItemCom202EN = AdministrationItem()
        administrationItemCom202EN.key = "EN"
        administrationItemCom202EN.value = "Device Language"
        deviceModuleCom202Names.add(administrationItemCom012EN)

        deviceModuleCom202.names = deviceModuleCom202Names

        deviceModules1.add(deviceModuleCom202)
        //endregion
        analysisItem2.applicationModules = applicationModules2
        //endregion
        analysis.add(analysisItem2)
        //endregion
        return analysis
    }

    override fun saveConfiguration(configuration: ConfigurationEntity): Completable =
        Completable.complete()

    override fun getConfiguration(): Single<ConfigurationEntity> =
        Single.just(ConfigurationMapper.convert(getMockConfiguration()))
}