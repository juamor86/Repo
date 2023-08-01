package es.juntadeandalucia.msspa.saludandalucia.data.repository.mock

import android.content.Context
import com.google.gson.GsonBuilder
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.data.entities.DynamicHomeData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.DynamicMenuData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.DynamicReleasesData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.DynamicScreenData
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.DynamicUIRepository
import io.reactivex.Single
import java.io.InputStream
import java.io.StringReader
import javax.inject.Inject
import timber.log.Timber

class DynamicUIRepositoryMockImpl @Inject constructor(private val context: Context) :
    DynamicUIRepository {

    override fun getMenu(): Single<DynamicMenuData> =
        Single.just(readFromJson(R.raw.menu_dynamic, DynamicMenuData::class.java))

    override fun getHome(): Single<DynamicHomeData> =
        Single.just(readFromJson(R.raw.home_dynamic_grid_3, DynamicHomeData::class.java))

    override fun getScreen(): Single<DynamicScreenData> =
        Single.just(readFromJson(R.raw.dynamic_screen, DynamicScreenData::class.java))

    override fun getReleases(): Single<DynamicReleasesData> =
        Single.just(readFromJson(R.raw.releases_dynamic, DynamicReleasesData::class.java))

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
