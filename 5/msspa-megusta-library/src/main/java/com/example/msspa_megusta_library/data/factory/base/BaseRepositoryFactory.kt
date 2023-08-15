package com.example.msspa_megusta_library.data.factory.base

import com.example.msspa_megusta_library.domain.Strategy

abstract class BaseRepositoryFactory<T> {
    abstract fun create(strategy: Strategy = Strategy.MOCK): T?
}
