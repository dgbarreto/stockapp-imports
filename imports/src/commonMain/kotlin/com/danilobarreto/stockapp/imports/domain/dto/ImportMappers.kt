package com.danilobarreto.stockapp.imports.data.dto

import com.danilobarreto.stockapp.imports.domain.CreatedImportRow
import com.danilobarreto.stockapp.imports.domain.ImportResult
import com.danilobarreto.stockapp.imports.domain.SkippedImportRow

fun ImportResultDto.toDomain(): ImportResult = ImportResult(
    importBatchId = importBatchId,
    totalRows = totalRows,
    created = created,
    createdRows = createdRows.map { CreatedImportRow(it.row, it.ticker, it.side, it.quantity, it.price) },
    skipped = skipped.map { SkippedImportRow(it.row, it.ticker, it.reason) },
)