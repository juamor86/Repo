package es.juntadeandalucia.msspa.saludandalucia.data.repository.json

import android.content.Context
import com.google.gson.GsonBuilder
import es.juntadeandalucia.msspa.saludandalucia.data.entities.KeyValueData
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.KeyValueRepository
import io.reactivex.Single
import java.io.InputStream
import java.io.StringReader
import timber.log.Timber

class KeyValueRepositoryJsonImpl(private val context: Context) : KeyValueRepository {

    override fun getKeyValueList(file: Int): Single<List<KeyValueData>> =
        Single.just(readFromJson(file))

    private fun readFromJson(file: Int): List<KeyValueData> {
        try {

            val inputStream: InputStream = context.resources.openRawResource(file)
            val inputString = inputStream.bufferedReader().use { it.readText() }

            var stringReader = StringReader(inputString)

            val gsonBuilder = GsonBuilder().serializeNulls()
            val gson = gsonBuilder.create()

            return gson.fromJson<Array<KeyValueData>>(
                stringReader,
                Array<KeyValueData>::class.java
            ).toList()
        } catch (e: Exception) {
            Timber.e("Error getting json keyValues: ${e.message}")
        }

        return emptyList()
    }
}
