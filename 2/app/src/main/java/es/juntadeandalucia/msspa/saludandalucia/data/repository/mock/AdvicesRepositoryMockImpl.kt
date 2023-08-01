package es.juntadeandalucia.msspa.saludandalucia.data.repository.mock

import android.content.Context
import com.google.gson.GsonBuilder
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.data.entities.AdviceData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.AdviceRequestData
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.AdvicesRepository
import io.reactivex.Completable
import io.reactivex.Single
import timber.log.Timber
import java.io.InputStream
import java.io.StringReader

class AdvicesRepositoryMockImpl(val context: Context) :
        AdvicesRepository {
    override fun getAdvices(nuhsa: String): Single<AdviceData> {
        return Single.just(readFromJson())
    }

    override fun getAdvicesReceived(phone: String): Single<AdviceData> {
        TODO("Not yet implemented")
    }

    private fun readMockList(): AdviceData? {
        return null
    }

    override fun updateAdvice(id: String, advice: AdviceRequestData.Entry.Resource): Completable = Completable.complete()

    override fun removeAdvice(id: String): Completable = Completable.complete()

    override fun createAdvice(advice: AdviceRequestData): Completable = Completable.complete()

    private fun readFromJson(): AdviceData? {
        try {
            val inputStream: InputStream = context.resources.openRawResource(R.raw.advices)
            val inputString = inputStream.bufferedReader().use { it.readText() }

            var stringReader = StringReader(inputString)

            val gsonBuilder = GsonBuilder().serializeNulls()
            val gson = gsonBuilder.create()

            return gson.fromJson(
                    stringReader,
                    AdviceData::class.java
            )
        } catch (e: Exception) {
            Timber.e("Error getting mock notifications: ${e.message}")
        }

        return null
    }

}