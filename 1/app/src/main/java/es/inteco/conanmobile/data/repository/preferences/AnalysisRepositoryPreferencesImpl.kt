package es.inteco.conanmobile.data.repository.preferences

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import es.inteco.conanmobile.domain.entities.AnalysisResultEntity
import es.inteco.conanmobile.domain.repository.AnalysisRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

/**
 * Analysis repository preferences impl
 *
 * @property sharedPreferences
 * @constructor Create empty Analysis repository preferences impl
 */
class AnalysisRepositoryPreferencesImpl(private val sharedPreferences: SharedPreferences) :
    AnalysisRepository {

    companion object {
        private const val LAST_ANALYSIS: String = "preferences_last_analysis"
    }

    override fun isLastAnalysis(): Single<Boolean> =
        Single.just(sharedPreferences.contains(LAST_ANALYSIS))

    override fun getLastAnalysis(): Single<AnalysisResultEntity> =
        Single.just(
            Gson().fromJson(
                sharedPreferences.getString(
                    LAST_ANALYSIS, null
                ), object : TypeToken<AnalysisResultEntity>() {}.type
            )
        )

    override fun saveAnalysis(analysis: AnalysisResultEntity): Completable =
        Completable.fromAction {
            sharedPreferences.edit()
                .putString(LAST_ANALYSIS, Gson().toJson(analysis)).apply()
        }

    override fun removeAllAnalysis(): Completable = Completable.fromAction {
        sharedPreferences.edit().remove(LAST_ANALYSIS).apply()
    }

    override fun removeAnalysis(analysis: AnalysisResultEntity): Completable = removeAllAnalysis()
}