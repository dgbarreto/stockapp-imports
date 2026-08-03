package com.danilobarreto.stockapp.imports.domain

interface ImportRepository {
    suspend fun importOrders(fileBytes: ByteArray, fileName: String): ImportResult
}