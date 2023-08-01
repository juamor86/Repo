package es.juntadeandalucia.msspa.authentication.domain.entities

class NavBackPressed {

    var navBackType = NavBackType.DEFAULT

    enum class NavBackType {
        DEFAULT,
        LOGIN_NUHSA_SCREEN,
        PIN_SCREEN,
        USER_LIST_SCREEN,
        WOA_SCREEN
    }

}