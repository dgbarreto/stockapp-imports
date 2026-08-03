package com.danilobarreto.stockapp.imports.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.danilobarreto.stockapp.designsystem.components.StockAppErrorBanner
import com.danilobarreto.stockapp.designsystem.components.StockAppPrimaryButton
import com.danilobarreto.stockapp.designsystem.theme.StockAppColors
import com.danilobarreto.stockapp.designsystem.theme.StockAppTypography

@Composable
fun ImportScreen(viewModel: ImportViewModel, onBack: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()
    val pickFile = rememberFilePicker { file -> viewModel.import(file) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Importar ordens") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("←", style = StockAppTypography.titleLarge, color = StockAppColors.textPrimary)
                    }
                },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(StockAppColors.surface1)
                .safeContentPadding()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                "Selecione o extrato de negociação da B3 (XLSX ou CSV, aba \"Negociação\")",
                style = StockAppTypography.bodyMedium,
                color = StockAppColors.textSecondary,
                modifier = Modifier.padding(bottom = 24.dp),
            )

            when (val state = uiState) {
                is ImportUiState.Idle -> {
                    StockAppPrimaryButton(text = "Selecionar arquivo", onClick = pickFile)
                }
                is ImportUiState.Uploading -> {
                    CircularProgressIndicator(modifier = Modifier.padding(top = 24.dp))
                }
                is ImportUiState.Error -> {
                    StockAppErrorBanner(state.message, modifier = Modifier.padding(bottom = 16.dp))
                    StockAppPrimaryButton(text = "Tentar de novo", onClick = pickFile)
                }
                is ImportUiState.Success -> {
                    val result = state.result
                    Text(
                        "${result.created} ordem(ns) criada(s) de ${result.totalRows} linha(s)",
                        style = StockAppTypography.bodyMedium,
                        color = StockAppColors.textSuccess,
                    )
                    if (result.skipped.isNotEmpty()) {
                        Text(
                            "${result.skipped.size} linha(s) não importada(s)",
                            style = StockAppTypography.labelMedium,
                            color = StockAppColors.textSecondary,
                            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(StockAppColors.surface2, RoundedCornerShape(14.dp))
                        ) {
                            result.skipped.forEach { row ->
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    Text(
                                        "Linha ${row.row}${row.ticker?.let { " · $it" } ?: ""}",
                                        style = StockAppTypography.labelSmall,
                                        color = StockAppColors.textPrimary,
                                    )
                                    Text(
                                        row.reason,
                                        style = StockAppTypography.labelSmall,
                                        color = StockAppColors.textMuted,
                                    )
                                }
                            }
                        }
                    }
                    StockAppPrimaryButton(
                        text = "Importar outro arquivo",
                        onClick = { viewModel.reset() },
                        modifier = Modifier.padding(top = 24.dp),
                    )
                }
            }
        }
    }
}