package com.example.msspa_megusta_library.data.factory

import com.example.msspa_megusta_library.data.factory.base.BaseRepositoryFactory
import com.example.msspa_megusta_library.data.repository.mock.LikeItRepositoryMockImpl
import com.example.msspa_megusta_library.data.repository.network.LikeItRepositoryNetworkImpl
import com.example.msspa_megusta_library.domain.Strategy
import com.example.msspa_megusta_library.domain.repository.LikeItRepository

class LikeItRepositoryFactory(
    private val likeItRepositoryMockImpl: LikeItRepositoryMockImpl,
    private val likeItRepositoryNetworkImpl: LikeItRepositoryNetworkImpl
) : BaseRepositoryFactory<LikeItRepository>() {

    override fun create(strategy: Strategy): LikeItRepository =
        when (strategy) {
            Strategy.NETWORK -> likeItRepositoryNetworkImpl
            else -> likeItRepositoryMockImpl
        }
}
