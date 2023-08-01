package es.inteco.conanmobile.data.utils

import es.inteco.conanmobile.BuildConfig

object ApiConstants {

    object General {
        const val API_BASE_URL = BuildConfig.API_BASE_HOST
        const val URL_API_V1 = "api/v1/server"
        const val HTTP_CONNECT_TIMEOUT = 30 * 1000.toLong()
        const val HTTP_READ_TIMEOUT = 30 * 1000.toLong()
        const val CONTENT_TYPE_HEADER = "Content-Type"
        const val ACCEPT_HEADER = "Accept"
        const val APPLICATION_JSON = "application/json"
        const val BOTNET_URL = "https://antibotnet.osi.es/api/wscheckip/es"
    }

    object Header {
        const val KEY_HEADER = "key"
        const val LANGUAGE_HEADER = "traduction"
    }

    object Incibe {
        const val X_INTECO_WS_REQUEST_SOURCE = "X_INTECO_WS_Request_Source"
        const val API = "api"
    }
}
