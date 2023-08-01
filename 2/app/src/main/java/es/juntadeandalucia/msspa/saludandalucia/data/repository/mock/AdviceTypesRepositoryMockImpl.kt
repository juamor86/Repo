package es.juntadeandalucia.msspa.saludandalucia.data.repository.mock

import android.content.Context
import com.google.gson.GsonBuilder
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.data.entities.AdviceTypesData
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.AdviceTypesRepository
import io.reactivex.Single
import timber.log.Timber
import java.io.InputStream
import java.io.StringReader

class AdviceTypesRepositoryMockImpl(val context: Context) :
        AdviceTypesRepository {
    override fun getAdviceTypes(): Single<AdviceTypesData> {
        return Single.just(readFromJson())
    }

    private fun readFromJson(): AdviceTypesData? {
        try {
            val inputStream: InputStream = context.resources.openRawResource(R.raw.advice_types)
            val inputString = inputStream.bufferedReader().use { it.readText() }

            var stringReader = StringReader(inputString)

            val gsonBuilder = GsonBuilder().serializeNulls()
            val gson = gsonBuilder.create()

            return gson.fromJson(
                    stringReader,
                    AdviceTypesData::class.java
            )
        } catch (e: Exception) {
            Timber.e("Error getting mock notifications: ${e.message}")
        }

        return null
    }
}