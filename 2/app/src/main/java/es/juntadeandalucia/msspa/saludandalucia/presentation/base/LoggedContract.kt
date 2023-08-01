package es.juntadeandalucia.msspa.saludandalucia.presentation.base

class LoggedContract {

    interface View : BaseContract.View {
        fun informUserNotLogged()
        fun informUserNotLoggedHighLevel()
        fun informUserNeedsHigherLevel()
    }

    interface Presenter : BaseContract.Presenter
}
