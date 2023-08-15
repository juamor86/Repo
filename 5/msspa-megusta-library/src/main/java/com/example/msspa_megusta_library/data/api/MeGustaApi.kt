package com.example.msspa_megusta_library.data.api

import com.example.msspa_megusta_library.data.entities.ServiceData
import com.example.msspa_megusta_library.utils.ApiConstants
import com.example.msspa_megusta_library.utils.ApiConstants.Api.ACTIONS
import com.example.msspa_megusta_library.utils.ApiConstants.Api.APP_VERSION
import com.example.msspa_megusta_library.utils.ApiConstants.Api.ID_SO
import com.example.msspa_megusta_library.utils.ApiConstants.Api.SCOPE
import com.example.msspa_megusta_library.utils.ApiConstants.Api.SCOPE_CIUDADANO
import com.example.msspa_megusta_library.utils.ApiConstants.Api.TYPE
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MeGustaApi {

    companion object {
        private const val URL_LIKEIT = "${ApiConstants.General.URL_API_V1}/aplicaciones/contenido"
    }

    @GET(URL_LIKEIT)
    fun getLikeIt(
        @Query(APP_VERSION) appVersion: String = "3.4.0",
        @Query(ID_SO) idSo: String = "0",
        @Query(SCOPE) scope: String = SCOPE_CIUDADANO,
        @Query(TYPE) type: String = ACTIONS
    ): Single<ServiceData>

}