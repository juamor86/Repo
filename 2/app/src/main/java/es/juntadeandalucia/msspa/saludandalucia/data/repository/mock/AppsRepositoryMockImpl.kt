package es.juntadeandalucia.msspa.saludandalucia.data.repository.mock

import android.content.Context
import com.google.gson.GsonBuilder
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.data.entities.AppData
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.AppsRepository
import io.reactivex.Single
import java.io.InputStream
import java.io.StringReader
import timber.log.Timber

class AppsRepositoryMockImpl(private val context: Context) : AppsRepository {

    override fun getApps(device: String): Single<List<AppData>> = Single.just(readFromJson())

    private fun readFromJson(): List<AppData> {
        try {
            val inputStream: InputStream = context.resources.openRawResource(R.raw.apps)
            val inputString = inputStream.bufferedReader().use { it.readText() }

            var stringReader = StringReader(inputString)

            val gsonBuilder = GsonBuilder().serializeNulls()
            val gson = gsonBuilder.create()

            return gson.fromJson<Array<AppData>>(
                stringReader,
                Array<AppData>::class.java
            ).toList()
        } catch (e: Exception) {
            Timber.e("Error getting mock apps: ${e.message}")
        }

        return emptyList()
    }
}
