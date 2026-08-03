package com.danilobarreto.stockapp.imports.data

import com.danilobarreto.stockapp.imports.data.dto.toDomain
import com.danilobarreto.stockapp.imports.domain.ImportRepository
import com.danilobarreto.stockapp.imports.domain.ImportResult

class ImportRepositoryImpl(
    private val apiClient: ImportApiClient,
) : ImportRepository {
    override suspend fun importOrders(fileBytes: ByteArray, fileName: String): ImportResult =
        apiClient.importOrders(fileBytes, fileName).toDomain()
}