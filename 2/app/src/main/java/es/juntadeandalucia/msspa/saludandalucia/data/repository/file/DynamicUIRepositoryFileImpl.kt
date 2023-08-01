package es.juntadeandalucia.msspa.saludandalucia.data.repository.file

import android.content.Context
import com.google.gson.GsonBuilder
import es.juntadeandalucia.msspa.saludandalucia.data.entities.DynamicHomeData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.DynamicMenuData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.DynamicReleasesData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.DynamicScreenData
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.DynamicUIRepository
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.FileUtils
import io.reactivex.Single
import timber.log.Timber
import java.io.StringReader

class DynamicUIRepositoryFileImpl(val context: Context): DynamicUIRepository {

    override fun getMenu(): Single<DynamicMenuData> =
        FileUtils.readFromFile(context, Consts.FILE_MENU)?.let {
            Single.just(readFromJsonString(it, DynamicMenuData::class.java))
        }?: Single.error(Throwable())

    override fun getHome(): Single<DynamicHomeData> =
        FileUtils.readFromFile(context, Consts.FILE_HOME)?.let {
            Single.just(readFromJsonString(it, DynamicHomeData::class.java))
        }?: Single.error(Throwable())

    override fun getScreen(): Single<DynamicScreenData> =
        FileUtils.readFromFile(context, Consts.FILE_SCREEN)?.let {
            Single.just(readFromJsonString(it, DynamicScreenData::class.java))
        }?: Single.error(Throwable())

    override fun getReleases(): Single<DynamicReleasesData> =
        FileUtils.readFromFile(context, Consts.FILE_RELEASES)?.let {
            try{
                Single.just(readFromJsonString(it, DynamicReleasesData::class.java))
            } catch (e:Exception){
                Single.error(Throwable())
            }
        }?: Single.error(Throwable())

    private fun <T> readFromJsonString(inputString: String, clazz: Class<T>): T? {
        return try {
            val stringReader = StringReader(inputString)

            val gsonBuilder = GsonBuilder().serializeNulls()
            val gson = gsonBuilder.create()

            gson.fromJson(
                stringReader,
                clazz
            )
        } catch (e: Exception) {
            Timber.e("Error getting file data: ${e.message}")
            null
        }
    }
}