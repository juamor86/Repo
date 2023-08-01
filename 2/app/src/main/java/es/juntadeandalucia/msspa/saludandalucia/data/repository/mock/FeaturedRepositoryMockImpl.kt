package es.juntadeandalucia.msspa.saludandalucia.data.repository.mock

import android.content.Context
import com.google.gson.GsonBuilder
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.data.entities.FeaturedData
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.FeaturedRepository
import io.reactivex.Single
import java.io.InputStream
import java.io.StringReader
import timber.log.Timber

class FeaturedRepositoryMockImpl(private val context: Context) : FeaturedRepository {

    override fun getFeatured(): Single<List<FeaturedData>> = Single.just(readFromJson())

    private fun readFromJson(): List<FeaturedData> {
        try {
            val inputStream: InputStream = context.resources.openRawResource(R.raw.featured)
            val inputString = inputStream.bufferedReader().use { it.readText() }

            var stringReader = StringReader(inputString)

            val gsonBuilder = GsonBuilder().serializeNulls()
            val gson = gsonBuilder.create()

            return gson.fromJson(
                stringReader,
                Array<FeaturedData>::class.java
            ).toList()
        } catch (e: Exception) {
            Timber.e("Error getting mock announcements: ${e.message}")
        }

        return emptyList()
    }
}
