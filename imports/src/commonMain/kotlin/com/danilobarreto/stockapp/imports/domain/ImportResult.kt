package com.danilobarreto.stockapp.imports.domain

data class ImportResult(
    val importBatchId: String,
    val totalRows: Int,
    val created: Int,
    val createdRows: List<CreatedImportRow>,
    val skipped: List<SkippedImportRow>,
)

data class CreatedImportRow(
    val row: Int,
    val ticker: String,
    val side: String,
    val quantity: Int,
    val price: Double,
)

data class SkippedImportRow(
    val row: Int,
    val ticker: String?,
    val reason: String,
)