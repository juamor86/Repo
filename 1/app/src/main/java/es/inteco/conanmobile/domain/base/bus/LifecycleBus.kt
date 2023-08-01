package es.inteco.conanmobile.domain.base.bus

import es.inteco.conanmobile.domain.base.BehaviorSubjectUseCase

class LifecycleBus : BehaviorSubjectUseCase<LifecycleBus.LifecycleState>() {

    enum class LifecycleState {
        BACKGROUND,
        FOREGROUND;
    }
}