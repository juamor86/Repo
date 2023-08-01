package es.inteco.conanmobile.domain.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Device entity
 *
 * @property id
 * @property isRooted
 * @property isAdbEnabled
 * @property unknowOrigins
 * @property clearPasswords
 * @property isKeyguardSecure
 * @property timeoutToLock
 * @property hasInsecureNetwork
 * @property hasInsecureBluetoothDevices
 * @property hasInstantLock
 * @property isVerifyApps
 * @property isWifiTethering
 * @property isBluetoothTethering
 * @property isHostFileEdited
 * @property hostFile
 * @property isBotnet
 * @property isHiddenNotifications
 * @property isAppNotificationsVisible
 * @property isStayOnWhileCharging
 * @property nameWithoutVersion
 * @property isOpenWifiNetworks
 * @property isAutomaticOpenWifiConnection
 * @property isWifiEnabled
 * @property isBleEnabled
 * @property isNfcEnabled
 * @property isAndroidBeam
 * @property brand
 * @property product
 * @property model
 * @property manufacturer
 * @property baseOs
 * @property codeName
 * @property releaseVersion
 * @property securityPatch
 * @property language
 * @property isBootloaderUnlocked
 * @constructor Create empty Device entity
 */
@Parcelize
 class DeviceEntity(
    var id: Int,                                    //Identifier
    var isRooted: Int,	                            //El dispositivo está rooteado, User Story CO2-27. (Int)
    var isAdbEnabled: Int,	                    //Depuración USB, User Story CO2-28. (Int)
    var unknowOrigins: Int,	                    //Orígenes desconocidos, User Story CO2-29. (Int)
    var clearPasswords: Int,	                    //Contraseñas en claro, User Story CO2-3-1. (Int)
    var isKeyguardSecure: Int,	                //Bloqueo del dispositivo, User Story CO2-31. (Int)
    var timeoutToLock: Int,	                    //Tiempo de bloqueo establecido, User Story CO2-32. (Int)
    var hasInsecureNetwork: Int,          	    //Existencia de redes WIFI inseguras, User Story CO2-33. (Int)
    var hasInsecureBluetoothDevices: Int,	    //Dispositivos Bluetooth conectados, User Story CO2-34. (Int)
    var hasInstantLock: Int,	                    //Bloquear el terminal de forma instantánea, User Story CO2-35. (Int)
    var isVerifyApps: Int,	                    //Verificar aplicaciones antes de instalarlas, User Story CO2-36. (Int)
    var isWifiTethering: Int,	                    //WIFI tethering (zona WIFI), User Story CO2-37. (Int)
    var isBluetoothTethering: Int,	            //Bluetooth tethering, User Story CO2-38. (Int)
    var isHostFileEdited: Int,	                //Monitorización del archivo “etc/host”, User Story CO2-39. (Int)
    var hostFile: String,	                        //Contenido actual del fichero host, User Story CO2-39. (Blob)
    var isBotnet: Int,	                            //Comprobación de la IP del terminal con el Servicio Antibotnet de INCIBE, User Story CO2-44. (Int)
    var isHiddenNotifications: Int,	            //Ocultación de notificaciones en la pantalla de bloqueo, User Story CO2-77. (Int)
    var isAppNotificationsVisible: Int,	        //Acceso a notificaciones por parte de las aplicaciones, User Story CO2-8-1. (Int)
    var isStayOnWhileCharging: Int,	            //Pantalla siempre activa al cargar, User Story CO2-81. (Int)
    var nameWithoutVersion: Int,	                //Nombre del dispositivo sin versión de este, User Story CO2-82. (Int)
    var isOpenWifiNetworks: Int,         	    //Notificaciones de redes abiertas, User Story CO2-89. (Int)
    var isAutomaticOpenWifiConnection: Int,	    //Conectarse a redes abiertas de forma automática, User Story CO2-9-1. (Int)
    var isWifiEnabled: Int,	                    //WIFI activado, pero sin conexión, User Story CO2-91. (Int)
    var isBleEnabled: Int,	                    //Bluetooth activado, pero sin conexión, User Story CO2-91. (Int)
    var isNfcEnabled: Int,	                    //NFC activo indefinidamente, User Story CO2-92. (Int)
    var isAndroidBeam: Int,	                    //Android Beam, User Story CO2-93. (Int)
    var brand: String,	                            //Obtención de la información del sistema, User Story CO2-99. (String)
    var product: String,	                        //Obtención de la información del sistema, User Story CO2-99. (String)
    var model: String,	                            //Obtención de la información del sistema, User Story CO2-99. (String)
    var manufacturer: String,	                    //Obtención de la información del sistema, User Story CO2-99. (String)
    var baseOs: String,	                        //Obtención de la información del sistema, User Story CO2-99. (String)
    var codeName: String,	                        //Obtención de la información del sistema, User Story CO2-99. (String)
    var releaseVersion: String,	                //Obtención de la información del sistema, User Story CO2-99. (String)
    var securityPatch: String,	                    //Obtención de la información del sistema, User Story CO2-99. (String)
    var language: String,	                        //Obtención del idioma del terminal, User Story CO2-1-1-1. (String)
    var isBootloaderUnlocked: Int	            //Obtención de información si un dispositivo tiene el bootloader desbloqueado
):Parcelable{
    constructor() : this(-1, -1,
        -1, -1, -1,
        -1, -1, -1,
        -1, -1, -1,
        -1,-1,-1,"",-1,-1,-1,
        -1,-1,-1,-1,-1,-1,
        -1,-1,"", "", "",
        "", "", "", "", "",
        "",-1)
}