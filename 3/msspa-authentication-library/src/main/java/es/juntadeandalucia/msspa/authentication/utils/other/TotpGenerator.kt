package es.juntadeandalucia.msspa.authentication.utils.other


import dev.turingcomplete.kotlinonetimepassword.GoogleAuthenticator
import java.util.Date


class TotpGenerator {

    companion object {

        fun generateGoogleAuthenticate(key: String) : String =
            GoogleAuthenticator(key).generate(Date(System.currentTimeMillis()))
    }

}