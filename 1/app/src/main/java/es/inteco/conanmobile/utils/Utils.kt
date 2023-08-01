package es.inteco.conanmobile.utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

/**
 * Utils
 *
 * @constructor Create empty Utils
 */
class Utils {
    companion object {
        fun dateToString(date: Date, format: String): String =
            SimpleDateFormat(format, Locale.getDefault()).format(date)

        fun checkInternet(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            return capabilities != null
        }

        fun goToWhatsapp(ct: Context) {
            val intent = Intent(Intent.ACTION_VIEW)
            val uri = "whatsapp://send?phone=" + Consts.WHATSAPP_INCIBE
            intent.data = Uri.parse(uri)
            ct.startActivity(intent)
        }

        fun getSHA(input: String): String {
            val bytes = input.toByteArray()
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(bytes)
            return digest.fold("") { str, it -> str + "%02x".format(it) }
        }

        fun convertStreamToString(inputStream: InputStream?): String {
            val reader = BufferedReader(InputStreamReader(inputStream))
            val sb = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                sb.append(line).append("\n")
            }
            reader.close()
            return sb.toString()
        }
    }
}