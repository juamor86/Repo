package es.juntadeandalucia.msspa.saludandalucia.domain.entities


class LauncherScreen {

    var launcherScreenType: LauncherScreenTypes = LauncherScreenTypes.CHECK_USER_SESSION

    enum class LauncherScreenTypes {
        CHECK_USER_SESSION,
        LEGAL_SCREEN,
        PERMISSIONS_SCREEN,
        NOTIFICATION_PERMISSION,
        RELEASES_DIALOG
    }
}