package es.inteco.conanmobile.domain.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Analysis result entity
 *
 * @property analysisEntity
 * @property date
 * @property device
 * @property deviceItems
 * @property appsItems
 * @property systemItems
 * @constructor Create empty Analysis result entity
 */
@Parcelize
class AnalysisResultEntity(
    val analysisEntity: AnalysisEntity,
    val date: Date = Date(),
    val device: DeviceEntity = DeviceEntity(),
    var deviceItems: MutableList<ModuleResultEntity> = mutableListOf(),
    var appsItems: MutableList<ApplicationEntity> = mutableListOf(),
    var systemItems: MutableList<ModuleResultEntity> = mutableListOf()
) : Parcelable