package es.juntadeandalucia.msspa.saludandalucia.data.repository.mock

import android.content.Context
import com.google.gson.GsonBuilder
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.data.entities.NewsData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.news.MSSPANewsResponseData
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.NewsRepository
import io.reactivex.Single
import java.io.InputStream
import java.io.StringReader
import timber.log.Timber

class NewsRepositoryMockImpl(private val context: Context) : NewsRepository {

    override fun getNews(): Single<List<NewsData>> = Single.just(readFromJson())

    private fun readFromJson(): List<NewsData> {
        try {
            val inputStream: InputStream = context.resources.openRawResource(R.raw.news_new)
            val inputString = inputStream.bufferedReader().use { it.readText() }

            var stringReader = StringReader(inputString)

            val gsonBuilder = GsonBuilder().serializeNulls()
            val gson = gsonBuilder.create()

            val news = gson.fromJson(
                stringReader,
                MSSPANewsResponseData::class.java
            )
            return news.entry
        } catch (e: Exception) {
            Timber.e("Error getting mock announcements: ${e.message}")
        }

        return emptyList()
    }
}
