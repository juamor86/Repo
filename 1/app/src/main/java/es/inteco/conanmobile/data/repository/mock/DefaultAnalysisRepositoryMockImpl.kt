package es.inteco.conanmobile.data.repository.mock

import android.content.Context
import com.google.gson.GsonBuilder
import es.inteco.conanmobile.R
import es.inteco.conanmobile.data.entities.DefaultAnalysisListData
import es.inteco.conanmobile.domain.repository.DefaultAnalysisRepository
import io.reactivex.rxjava3.core.Single
import timber.log.Timber
import java.io.InputStream
import java.io.StringReader

/**
 * Default analysis repository mock impl
 *
 * @property context
 * @constructor Create empty Default analysis repository mock impl
 */
class DefaultAnalysisRepositoryMockImpl(private val context: Context) : DefaultAnalysisRepository {

    override fun getAnalysis(): Single<DefaultAnalysisListData> = Single.just(readFromJson())

    private fun readFromJson(): DefaultAnalysisListData {
        try {
            val inputStream: InputStream = context.resources.openRawResource(R.raw.configuration)
            val inputString = inputStream.bufferedReader().use { it.readText() }
            val stringReader = StringReader(inputString)
            val gsonBuilder = GsonBuilder().serializeNulls()
            val gson = gsonBuilder.create()

            return gson.fromJson(
                stringReader,
                DefaultAnalysisListData::class.java
            )
        } catch (e: Exception) {
            Timber.e("Error getting mock announcements: ${e.message}")
        }
        return null!!
    }
}