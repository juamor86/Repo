package es.juntadeandalucia.msspa.app

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import es.juntadeandalucia.msspa.authentication.*
import es.juntadeandalucia.msspa.authentication.domain.entities.AuthorizeEntity
import es.juntadeandalucia.msspa.authentication.domain.entities.KeyValueEntity
import es.juntadeandalucia.msspa.authentication.domain.entities.MsspaAuthenticationUserEntity
import es.juntadeandalucia.msspa.authentication.security.CrytographyManager
import es.juntadeandalucia.msspa.authentication.security.PinPatternCryptographyManager
import es.juntadeandalucia.msspa.authentication.utils.ApiConstants
import kotlinx.android.synthetic.main.activity_main.*
import javax.crypto.Cipher

class MainActivity : AppCompatActivity(),
    MsspaAuthenticationBroadcastReceiver.MsspaAuthenticationCallback {

    private var authEntity: MsspaAuthenticationEntity? = MsspaAuthenticationEntity(
        tokenType = "",
        accessToken = "",
        expiresIn = 0,
        scope = MsspaAuthenticationManager.Scope.LEVEL_1,
        msspaAuthenticationUser = MsspaAuthenticationUserEntity(),
        authorizeEntity = AuthorizeEntity("", "")
    )

    private lateinit var manager: MsspaAuthenticationManager

    var cryptoManager: CrytographyManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("App", "Creating MainActivity")

        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // FIXME: These credentials are from SaludAndalucia app. This example app must have their
        //  own credentials
        val config = MsspaAuthenticationConfig(
            //environment = MsspaAuthenticationConfig.Environment.PRODUCTION,
            //clientId = "l7a7fda79bc20243a490b8e925402979a5",
            //apiKey = "l7de51d5dbe59c439cab7e11812675e7eb",
            environment = MsspaAuthenticationConfig.Environment.PREPRODUCTION,
            clientId = "l7db4659ecc8294d23af050f741e20d8dd",
            apiKey = "l7460a9e8f1e594654ac37ac53fd2ee8e2",
            idSo = "0",
            appKey = "msspa.app.102",
            version = "3.3.1",
            logoToolbar = R.drawable.ic_logo_junta,
            colorToolbar = R.color.colorPrimaryDark,
            errorLayout = R.layout.view_custom_dialog,
            errors = listOf(
                MsspaAuthenticationError.MsspaAuthenticationErrorResource(
                    error = MsspaAuthenticationError.GENERIC_ERROR,
                    errorTitle = es.juntadeandalucia.msspa.authentication.R.string.msspa_auth_dialog_error_title
                ),
                MsspaAuthenticationError.MsspaAuthenticationErrorResource(
                    error = MsspaAuthenticationError.LOADING_WEB,
                    errorTitle = es.juntadeandalucia.msspa.authentication.R.string.msspa_auth_dialog_error_title
                ),
                MsspaAuthenticationError.MsspaAuthenticationErrorResource(
                    error = MsspaAuthenticationError.WRONG_CONFIG,
                    errorTitle = es.juntadeandalucia.msspa.authentication.R.string.msspa_auth_dialog_error_title
                ),
                MsspaAuthenticationError.MsspaAuthenticationErrorResource(
                    error = MsspaAuthenticationError.NETWORK,
                    errorTitle = es.juntadeandalucia.msspa.authentication.R.string.msspa_auth_dialog_error_title
                ),
                MsspaAuthenticationError.MsspaAuthenticationErrorResource(
                    error = MsspaAuthenticationError.INVALID_REQUEST,
                    errorTitle = es.juntadeandalucia.msspa.authentication.R.string.msspa_auth_dialog_error_title
                ),
                MsspaAuthenticationError.MsspaAuthenticationErrorResource(
                    error = MsspaAuthenticationError.MAX_ATTEMPTS,
                    errorMessage = es.juntadeandalucia.msspa.authentication.R.string.msspa_auth_dialog_error_message
                ),

                MsspaAuthenticationError.MsspaAuthenticationErrorResource(
                    error = MsspaAuthenticationError.INVALID_PIN_SMS_RECEIVED,
                    errorMessage = es.juntadeandalucia.msspa.authentication.R.string.msspa_auth_dialog_error_message
                ),
                MsspaAuthenticationError.MsspaAuthenticationErrorResource(
                    error = MsspaAuthenticationError.NO_CERTIFICATE,
                    errorMessage = es.juntadeandalucia.msspa.authentication.R.string.msspa_auth_dialog_error_message
                ),
                MsspaAuthenticationError.MsspaAuthenticationErrorResource(
                    error = MsspaAuthenticationError.INVALID_CERTIFICATE,
                    errorMessage = es.juntadeandalucia.msspa.authentication.R.string.msspa_auth_dialog_error_message
                ),
                MsspaAuthenticationError.MsspaAuthenticationErrorResource(
                    error = MsspaAuthenticationError.RATE_LIMIT_EXCEEDED,
                    errorMessage = es.juntadeandalucia.msspa.authentication.R.string.msspa_auth_dialog_error_message
                )
            ),
            warningLayout = R.layout.view_custom_dialog,
            warnings = listOf(
                MsspaAuthenticationWarning.MsspaAuthenticationWarningResource(
                    warning = MsspaAuthenticationWarning.OTHER_AUTH_METHODS,
                    warningMessage = es.juntadeandalucia.msspa.authentication.R.string.msspa_auth_dialog_info_message
                ),
                MsspaAuthenticationWarning.MsspaAuthenticationWarningResource(
                    warning = MsspaAuthenticationWarning.INVALID_BDU_PHONE,
                    warningMessage = es.juntadeandalucia.msspa.authentication.R.string.msspa_auth_dialog_info_message
                ),
                MsspaAuthenticationWarning.MsspaAuthenticationWarningResource(
                    warning = MsspaAuthenticationWarning.INVALID_BDU_DATA,
                    warningMessage = es.juntadeandalucia.msspa.authentication.R.string.msspa_auth_dialog_info_message
                ),
                MsspaAuthenticationWarning.MsspaAuthenticationWarningResource(
                    warning = MsspaAuthenticationWarning.PROTECTED_USER,
                    warningMessage = es.juntadeandalucia.msspa.authentication.R.string.msspa_auth_dialog_info_message
                ),
                MsspaAuthenticationWarning.MsspaAuthenticationWarningResource(
                    warning = MsspaAuthenticationWarning.NOT_PRIVATE_HEALTH_CARE,
                    warningMessage = es.juntadeandalucia.msspa.authentication.R.string.msspa_auth_dialog_info_message
                )
            )
        )

        manager = MsspaAuthenticationManager(
            context = this, msspaAuthenticationConfig = config
        )

        login_0_bt.setOnClickListener { _ ->
            manager.launch()
        }

        login_1_bt.setOnClickListener { _ ->
            manager.launch(
                requiredScope = MsspaAuthenticationManager.Scope.LEVEL_1.level
            )
        }

        login_2_bt.setOnClickListener { _ ->

            manager.launch(
                mSSPAAuthEntity = null,
                requiredScope = MsspaAuthenticationManager.Scope.LEVEL_2.level
            )
        }

        login_3_bt.setOnClickListener { _ ->
            manager.launch(
                requiredScope = MsspaAuthenticationManager.Scope.LEVEL_3.level
            )
        }

        login_4_bt.setOnClickListener { _ ->
            manager.launchVerificationQRLogin(
                // An example user to check if the unlock works correctly, the example app dosen't save the user.
                MsspaAuthenticationEntity(
                    "Bearer",
                    "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6ImRlZmF1bHRfc3NsX2tleSJ9.ew0KICAiaXNzIjoiaHR0cHM6Ly9TRTQxVkdXNzAxTVNTUEEuYXBpbS1wcmUubG9jYWw6OTQ0MyIsDQogICJpYXQiOjE2MzAzMDUzNDgsDQogICJhdWQiOiJsN2RiNDY1OWVjYzgyOTRkMjNhZjA1MGY3NDFlMjBkOGRkIiwNCiAgImV4cCI6MTYzMDMwODk0OCwNCiAgImp0aSI6IjU2MmUzZGQzLTA5YzgtNDk4OS04Y2MyLWU3ZjhiMDM0NTU2NyIsDQogICJzdWIiOiIzMDIyOTM1N0MgOiBBTjA1NjkyMDc5MjMgOiBGUkFOQ08gSklNRU5FWiwgRVJJQyIsDQogICJ0b2tlbl9kZXRhaWxzIjogew0KICAgICJzY29wZSI6ImNpdWRhZGFubyBjb25mL04gY29uZi9SIGNvbmYvViIsDQogICAgImV4cGlyZXNfaW4iOjM2MDAsDQogICAgInRva2VuX3R5cGUiOiJCZWFyZXIiDQogIH0NCn0.oH4aXbOT_acK_NcGUiVKiqSLGf5FFPUMZVY6T06kr431WUmnvqQ6neY-YpFqwlQO3Cm3BHBv8t-v4ExiQlV-fLNo2QaZwWRQ8-HOlZy29EmgBgCf_c_kOKvu-ADVbXrQ_giIB-EXkW_-4xDbStqhLCLy13EzTYr17eInL_KXP3tRSa_J2r0mNTpRQ3kVqUd0JGzxH8yldSU4KG5FlUYtY9k-R0v-d9GEcLJQhAhP4IIEWT1D6Zn7CSiSdDnzBk90qRz03wFy-TIfBqvoIwSdHtgbEwmoVV5Em-wuxHDUQlJs24LSvHPTyh_hVRtp_XCb7XRXgAY5rGlcFAljyeFtIA",
                    3600,
                    MsspaAuthenticationManager.Scope.LEVEL_3,
                    "b311cea5-43cd-4bf2-a3a4-ec7da08f864c",
                    "GFBEMN2GIU2DENRU",
                    MsspaAuthenticationUserEntity(
                        identification = "30229357C",
                        name = "FICTICIO ACTIVO, CIUDADANO",
                        nuss = "AN0211989760"
                    ),
                    AuthorizeEntity(
                        sessionData = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.ew0KICAgICJzZXNzaW9uIjogew0KICAgICAgICAic2Vzc2lvbklEIjoiM2ZmM2I3MWQtMWNlNy00MGU1LThiMWMtNGQ0NjA1OWY5ZmE1IiwNCiAgICAgICAgImV4cCI6MTYzMDMwODk0NywNCiAgICAgICAgImN1cnJlbnRfdXNlcm5hbWUiOiIiLA0KICAgICAgICAiY3VycmVudF91c2VyX2NvbnNlbnQiOiJub25lIiwNCiAgICAgICAgImN1cnJlbnRfdXNlcl9yb2xlIjoiIiwNCiAgICAgICAgImN1cnJlbnRfdXNlcl9hY3IiOiIwIiwNCiAgICAgICAgImN1cnJlbnRfdXNlcl9hdXRoVGltZSI6IjAiLA0KICAgICAgICAic2FsdCI6IiIsDQogICAgICAgICJ0aGlyZF9wYXJ0eV9zc29fdG9rZW4iOiIiLA0KICAgICAgICAidGhpcmRfcGFydHlfc3NvX3Rva2VuX3R5cGUiOiIiDQogICAgfSwNCiAgICAicmVxdWVzdF9jb25zZW50Ijogew0KICAgICAgICAiY2xpZW50X25hbWUiOiJTYWx1ZEFuZGFsdWNpYSslMjhtb2JpbGUtc3RhbmRhbG9uZSUyOS1Db25zZWplciVDMyVBRGErZGUrU2FsdWQreStGYW1pbGlhcyIsDQogICAgICAgICJzY29wZV92ZXJpZmllZCI6ImNpdWRhZGFubyINCiAgICB9LA0KICAgICJyZXF1ZXN0X3BhcmFtZXRlcnMiOiB7DQogICAgICAgICJkaXNwbGF5IjoicGFnZSIsDQogICAgICAgICJwcm9tcHQiOiJsb2dpbiIsDQogICAgICAgICJpZF90b2tlbl9oaW50IjoiIiwNCiAgICAgICAgImxvZ2luX2hpbnQiOiIiLA0KICAgICAgICAiYWNyX3ZhbHVlcyI6IiIsDQogICAgICAgICJjbGllbnRfaWQiOiJsN2RiNDY1OWVjYzgyOTRkMjNhZjA1MGY3NDFlMjBkOGRkIiwNCiAgICAgICAgIm5vbmNlIjoiIiwNCiAgICAgICAgInNjb3BlIjoiY2l1ZGFkYW5vIiwNCiAgICAgICAgIm1heF9hZ2UiOiAiIg0KICAgIH0NCn0.cZ9x76h1IDVtcq2V7UT8R9m71UCi3JfIRwS_YzVbX-o",
                        sessionId = "3ff3b71d-1ce7-40e5-8b1c-4d46059f9fa5"
                    )
                )
            )
        }
    }
    
    override fun msspaAuthenticationOnSuccess(msspaAuthenticationEntity: MsspaAuthenticationEntity) {
        authEntity = msspaAuthenticationEntity
        Toast.makeText(
            this@MainActivity,
            "Token: " + msspaAuthenticationEntity.accessToken,
            Toast.LENGTH_LONG
        ).show()
    }

    override fun msspaAuthenticationOnError(msspaAuthenticationError: MsspaAuthenticationError) {
        val errorMessage = when (msspaAuthenticationError) {
            MsspaAuthenticationError.GENERIC_ERROR -> "Error genérico"
            MsspaAuthenticationError.LOADING_WEB -> "Error al cargar la web de opciones de autenticación"
            MsspaAuthenticationError.WRONG_CONFIG -> "Error en la configuración"
            MsspaAuthenticationError.NETWORK -> "Error de conectividad"
            MsspaAuthenticationError.MAX_ATTEMPTS -> "Se han alcanzado el máximo número de intentos"
            MsspaAuthenticationError.INVALID_PIN_SMS_RECEIVED -> "El PIN introducido no es correcto"
            MsspaAuthenticationError.NO_CERTIFICATE -> "No existen certificados disponibles"
            MsspaAuthenticationError.INVALID_CERTIFICATE -> "Error de certificado"
            MsspaAuthenticationError.INVALID_REQUEST -> "Error en los datos introducidos"
            MsspaAuthenticationError.RATE_LIMIT_EXCEEDED -> "Error al excederse el máximo número de peticiones simultáneas"
            MsspaAuthenticationError.QR_LOGIN_ERROR -> "Error QR"
            MsspaAuthenticationError.CANCEL_LOGIN -> "Se procede a cerrar la sesión"
            MsspaAuthenticationError.DNIE_CERT_EXPIRED -> "Certificado del dni caducado"
            MsspaAuthenticationError.DNIE_GENERIC_ERROR -> "No se pudo logar con dnie"
            MsspaAuthenticationError.SEND_NOTIFICATION_ERROR -> "Error al enviar la notificación"
            MsspaAuthenticationError.ABORT_INIT_LOGIN -> "Se canceló inicio de sesión"
            MsspaAuthenticationError.SESSION_EXPIRED -> "Caducó la sesión"
            MsspaAuthenticationError.ERROR_TOKEN_VALIDATE -> "Error en la validacion del token"
            MsspaAuthenticationError.ERROR_TOKEN_INVALIDATE -> "Error en la invalidación del token"
            MsspaAuthenticationError.ERROR_REFRESH_TOKEN -> "Error en el refresh token"
        }
        Toast.makeText(
            this@MainActivity,
            "ERROR: $errorMessage",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun msspaAuthenticationOnWarning(msspaAuthenticationWarning: MsspaAuthenticationWarning) {
        val warningMessage = when (msspaAuthenticationWarning) {
            MsspaAuthenticationWarning.OTHER_AUTH_METHODS -> "Cambio de método de autenticación"
            MsspaAuthenticationWarning.GENERIC_WARNING -> "Aviso"
            MsspaAuthenticationWarning.PROTECTED_USER -> "Error por usuario protegido"
            MsspaAuthenticationWarning.INVALID_BDU_DATA -> "Error al comprobar los datos en BDU"
            MsspaAuthenticationWarning.INVALID_BDU_PHONE -> "Error al comprobar el teléfono en BDU"
            MsspaAuthenticationWarning.QR_LOGIN_NOT_FOUND_WARNING -> "Error con el QR not found"
            MsspaAuthenticationWarning.QR_LOGIN_EXPIRED_WARNING -> "Error con el QR expired"
            MsspaAuthenticationWarning.PIN_MAX_ATTEMPTS -> "Número máximo de intentos"
            MsspaAuthenticationWarning.NOT_PRIVATE_HEALTH_CARE -> "Figura usted con tarjeta sanitaria de Andalucía, por favor utilice el acceso general con datos personales para tener acceso a todos los servicios."
        }
        Toast.makeText(
            this@MainActivity,
            "WARNING: $warningMessage",
            Toast.LENGTH_LONG
        ).show()
        manager.launchOtherAuthMethods()
    }

    override fun msspaValidateTokenSuccess(isSuccess: Boolean) {
        Toast.makeText(
            this@MainActivity,
            "El token es valido-> $isSuccess",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun msspaInValidateTokenSuccess() {
        Toast.makeText(
            this@MainActivity,
            "Token invalidado correctamente.",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun msspaRefreshTokenSuccess(msspaAuthenticationEntity: MsspaAuthenticationEntity) {
        Toast.makeText(
            this@MainActivity,
            "RefreshToken:" + msspaAuthenticationEntity.refreshToken + "TotpSecretKey:" + msspaAuthenticationEntity.totpSecretKey,
            Toast.LENGTH_LONG
        ).show()
    }

    override fun msspaEventLaunch(event: String) {

    }
}
