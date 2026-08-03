package com.danilobarreto.stockapp.imports.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ImportResultDto(
    val importBatchId: String,
    val totalRows: Int,
    val created: Int,
    val skipped: List<SkippedImportRowDto>,
)

@Serializable
data class SkippedImportRowDto(
    val row: Int,
    val ticker: String?,
    val reason: String,
)

@Serializable
data class ErrorResponseDto(val message: String)