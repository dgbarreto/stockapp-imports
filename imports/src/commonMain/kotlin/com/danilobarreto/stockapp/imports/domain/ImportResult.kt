package com.danilobarreto.stockapp.imports.domain

data class ImportResult(
    val importBatchId: String,
    val totalRows: Int,
    val created: Int,
    val skipped: List<SkippedImportRow>,
)

data class SkippedImportRow(
    val row: Int,
    val ticker: String?,
    val reason: String,
)