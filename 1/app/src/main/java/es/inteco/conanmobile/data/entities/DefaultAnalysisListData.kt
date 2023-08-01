package es.inteco.conanmobile.data.entities

import com.google.gson.annotations.SerializedName

/**
 * Default analysis list data
 *
 * @property listDefaultAnalysisData
 * @constructor Create empty Default analysis list data
 */
data class DefaultAnalysisListData (
    @SerializedName("default_analysis")
    val listDefaultAnalysisData: List<DefaultAnalysisData>
)

