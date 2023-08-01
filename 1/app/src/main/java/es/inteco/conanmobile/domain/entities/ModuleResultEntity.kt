package es.inteco.conanmobile.domain.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Module result entity
 *
 * @property item
 * @property failed
 * @constructor Create empty Module result entity
 */
@Parcelize
class ModuleResultEntity(val item: ModuleEntity, var failed: Boolean = false):Parcelable {
    var data: MutableList<NetworkEntity>? = null
    var notOk: Boolean = false
    var shouldLaunchAction = item.shouldLaunchAction
    var previousAnalysisDifferent = false
}