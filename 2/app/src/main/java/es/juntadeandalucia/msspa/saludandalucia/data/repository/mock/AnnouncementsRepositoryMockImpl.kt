package es.juntadeandalucia.msspa.saludandalucia.data.repository.mock

import android.content.Context
import com.google.gson.GsonBuilder
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.data.entities.AnnouncementData
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.AnnouncementsRepository
import io.reactivex.Single
import java.io.InputStream
import java.io.StringReader
import timber.log.Timber

class AnnouncementsRepositoryMockImpl(private val context: Context) : AnnouncementsRepository {

    override fun getAnnouncements(): Single<List<AnnouncementData>> = Single.just(readFromJson())

    private fun readFromJson(): List<AnnouncementData> {
        try {
            val inputStream: InputStream = context.resources.openRawResource(R.raw.announcements)
            val inputString = inputStream.bufferedReader().use { it.readText() }

            var stringReader = StringReader(inputString)

            val gsonBuilder = GsonBuilder().serializeNulls()
            val gson = gsonBuilder.create()

            return gson.fromJson<Array<AnnouncementData>>(
                stringReader,
                Array<AnnouncementData>::class.java
            ).toList()
        } catch (e: Exception) {
            Timber.e("Error getting mock announcements: ${e.message}")
        }

        return emptyList()
    }
}
