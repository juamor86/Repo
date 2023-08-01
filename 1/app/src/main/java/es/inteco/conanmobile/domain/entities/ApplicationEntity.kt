package es.inteco.conanmobile.domain.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

/**
 * Application entity
 *
 * @property packageId
 * @property name
 * @property hash
 * @property version
 * @property fingerprint
 * @property safetynetResult
 * @property isMalicious
 * @property origin
 * @property isPrivileged
 * @property isAllowedUnknownSources
 * @property isSystemApplication
 * @property isNotificationAccessEnabled
 * @property apkApiKeys
 * @property apkUrlsMalicious
 * @property hashAnalysis
 * @property apkUrls
 * @property apkIps
 * @property apkDomains
 * @property permissions
 * @property criticalPermissions
 * @constructor Create empty Application entity
 */
@Parcelize
data class ApplicationEntity(
    var packageId: String = "",                //Clave principal, no puede ser repetida y es el campo de identificación único de cada línea de la tabla, User Story CO2-41. (String)
    var name: String = "",                      //El nombre de la App, User Story CO2-41. (String)
    var hash: String = "",                      //Hash en SHA-256, User Story CO2-40. (String)
    var version: String = "",                   //Versión de la App, User Story CO2-41. (String)
    var fingerprint: String = "",               //FingerPrint de la Aplicación extraído de su certificado, User Story CO2-41 (String)
    var safetynetResult: String = "",          //Verificación con servicio Safetynet, User Story CO2-95, (String)
    var isMalicious: Int = -1,                 //Es una aplicación maliciosa, User Story CO2-43,(Integer)
    var origin: String = "",                    //Origen del Apk instalado, User Story CO2-96,(String)
    var isPrivileged: Int = -1,                //Es una aplicación privilegiada --> rootDirectory -> priv-app
    var isAllowedUnknownSources: Int = -1,   //Es una aplicación que permite la instalación de aplicaciones de origenes desconocidos
    var isSystemApplication: Int = -1,        //Es una aplicación que se encuentra pre-instalada en el dispositivo
    var isNotificationAccessEnabled: Int = -1, //Es una aplicación que tiene acceso para leer las notificaciones
    var apkApiKeys: List<String> = emptyList(),
    var apkUrlsMalicious: Int = -1,
    var hashAnalysis: @RawValue List<ServiceEntity> = emptyList(),
    var apkUrls: @RawValue List<OtherApkEntity> = emptyList(),
    var apkIps: @RawValue List<OtherApkEntity> = emptyList(),
    var apkDomains: @RawValue List<OtherApkEntity> = emptyList(),
    var permissions: List<PermissionEntity> = emptyList(),
    var criticalPermissions: List<PermissionEntity> = emptyList()
) : Parcelable