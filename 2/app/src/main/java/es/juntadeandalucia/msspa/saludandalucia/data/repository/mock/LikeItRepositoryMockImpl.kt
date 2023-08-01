package es.juntadeandalucia.msspa.saludandalucia.data.repository.mock

import android.content.Context
import com.google.gson.GsonBuilder
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.data.entities.LikeItResponseData
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.FeedbackRepository
import io.reactivex.Single
import timber.log.Timber
import java.io.InputStream
import java.io.StringReader

class LikeItRepositoryMockImpl(private val context: Context) : FeedbackRepository {

    override fun getEvents(): Single<LikeItResponseData> =
        Single.just(readFromJson(R.raw.events, LikeItResponseData::class.java))

    private fun <T> readFromJson(json: Int, clazz: Class<T>): T {
        try {
            val inputStream: InputStream = context.resources.openRawResource(json)
            val inputString = inputStream.bufferedReader().use { it.readText() }

            val stringReader = StringReader(inputString)

            val gsonBuilder = GsonBuilder().serializeNulls()
            val gson = gsonBuilder.create()

            return gson.fromJson(
                stringReader,
                clazz
            )
        } catch (e: Exception) {
            Timber.e("Error getting mock announcements: ${e.message}")
            return null!!
        }
    }

}