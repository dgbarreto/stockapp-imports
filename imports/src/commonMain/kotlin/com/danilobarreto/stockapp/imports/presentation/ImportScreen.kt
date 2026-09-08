package com.danilobarreto.stockapp.imports.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.danilobarreto.stockapp.designsystem.components.StockAppErrorBanner
import com.danilobarreto.stockapp.designsystem.components.StockAppPrimaryButton
import com.danilobarreto.stockapp.designsystem.components.stockAppDashedBorder
import com.danilobarreto.stockapp.designsystem.icons.StockAppIcons
import com.danilobarreto.stockapp.designsystem.theme.StockAppColors
import com.danilobarreto.stockapp.designsystem.theme.StockAppShapes
import com.danilobarreto.stockapp.designsystem.theme.StockAppTypography
import com.danilobarreto.stockapp.designsystem.util.toDecimalString
import com.danilobarreto.stockapp.imports.domain.CreatedImportRow
import com.danilobarreto.stockapp.imports.domain.SkippedImportRow

@Composable
fun ImportScreen(
    viewModel: ImportViewModel,
    onBack: () -> Unit,
    onViewCarteira: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val pickFile = rememberFilePicker { file -> viewModel.import(file) }

    // Na tela de resultado, "voltar" não deve sair da importação - deve voltar
    // pro estado de escolher outro arquivo (mesmo destino do link "Importar outro arquivo").
    val handleBack: () -> Unit = if (uiState is ImportUiState.Success) {
        { viewModel.reset() }
    } else {
        onBack
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(StockAppColors.surface1)
            .safeContentPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(bottom = 24.dp)
    ) {
        BackButton(handleBack)

        when (val state = uiState) {
            is ImportUiState.Success -> ImportResult(
                state = state,
                onViewCarteira = onViewCarteira,
                onImportAnother = { viewModel.reset() },
            )
            else -> ImportDropzone(state = state, pickFile = pickFile)
        }
    }
}

@Composable
private fun BackButton(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(StockAppColors.surface2)
            .clickable(onClick = onBack),
        contentAlignment = Alignment.Center,
    ) {
        Icon(StockAppIcons.ArrowLeft, contentDescription = "Voltar", tint = StockAppColors.textPrimary, modifier = Modifier.size(18.dp))
    }
}

@Composable
private fun ImportDropzone(state: ImportUiState, pickFile: () -> Unit) {
    Text(
        "Importar extrato",
        style = StockAppTypography.titleLarge,
        color = StockAppColors.textPrimary,
        modifier = Modifier.padding(top = 12.dp),
    )
    Text(
        "Suba o extrato de negociação da B3 e a gente lança as ordens pra você.",
        style = StockAppTypography.bodySmall,
        color = StockAppColors.textSecondary,
        modifier = Modifier.padding(top = 6.dp, bottom = 24.dp),
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(StockAppColors.primaryTint, shape = RoundedCornerShape(24.dp))
            .stockAppDashedBorder(color = StockAppColors.primary, cornerRadius = 24.dp)
            .padding(horizontal = 20.dp, vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(StockAppIcons.Upload, contentDescription = null, tint = StockAppColors.primary, modifier = Modifier.size(34.dp))
        Text(
            "Arraste o arquivo aqui",
            style = StockAppTypography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = StockAppColors.textPrimary,
            modifier = Modifier.padding(top = 12.dp),
        )
        Text(
            "Aceitamos .xlsx e .csv do extrato de negociação",
            style = StockAppTypography.labelSmall,
            color = StockAppColors.textSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp, bottom = 18.dp),
        )

        when (state) {
            is ImportUiState.Uploading -> CircularProgressIndicator(color = StockAppColors.primary)
            else -> StockAppPrimaryButton(text = "Selecionar arquivo", onClick = pickFile)
        }
    }

    if (state is ImportUiState.Error) {
        StockAppErrorBanner(state.message, modifier = Modifier.padding(top = 16.dp))
        StockAppPrimaryButton(text = "Tentar de novo", onClick = pickFile, modifier = Modifier.padding(top = 12.dp))
    }

    Text(
        "Como funciona",
        style = StockAppTypography.titleMedium,
        color = StockAppColors.textPrimary,
        modifier = Modifier.padding(top = 28.dp, bottom = 12.dp),
    )
    HowItWorksStep(1, "Baixe o extrato no portal do investidor da B3")
    HowItWorksStep(2, "Suba o arquivo aqui, sem editar nada")
    HowItWorksStep(3, "Confira o resumo e confirme as ordens")
}

@Composable
private fun HowItWorksStep(number: Int, text: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(26.dp)
                .background(StockAppColors.primaryTint, shape = RoundedCornerShape(9.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Text("$number", style = StockAppTypography.labelMedium, color = StockAppColors.primaryDeep)
        }
        Text(
            text,
            style = StockAppTypography.bodyMedium,
            color = StockAppColors.textSecondary,
            modifier = Modifier.padding(start = 12.dp),
        )
    }
}

@Composable
private fun ImportResult(state: ImportUiState.Success, onViewCarteira: () -> Unit, onImportAnother: () -> Unit) {
    val result = state.result

    Text(
        "Importação concluída",
        style = StockAppTypography.titleLarge,
        color = StockAppColors.textPrimary,
        modifier = Modifier.padding(top = 12.dp),
    )
    Text(
        "${result.totalRows} linha(s) processada(s)",
        style = StockAppTypography.bodySmall,
        color = StockAppColors.textSecondary,
        modifier = Modifier.padding(top = 6.dp, bottom = 20.dp),
    )

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        ResultTile(modifier = Modifier.weight(1f), value = "${result.created}", label = "Importadas", valueColor = StockAppColors.textSuccess)
        ResultTile(modifier = Modifier.weight(1f), value = "${result.skipped.size}", label = "Ignoradas", valueColor = StockAppColors.textWarning)
        ResultTile(modifier = Modifier.weight(1f), value = "${result.totalRows}", label = "Linhas", valueColor = StockAppColors.textPrimary)
    }

    if (result.createdRows.isNotEmpty() || result.skipped.isNotEmpty()) {
        if (result.createdRows.isNotEmpty() || result.skipped.isNotEmpty()) {
            Text(
                "Detalhes",
                style = StockAppTypography.titleMedium,
                color = StockAppColors.textPrimary,
                modifier = Modifier.padding(top = 24.dp, bottom = 10.dp),
            )
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                result.createdRows.forEach { row -> CreatedRow(row) }
                result.skipped.forEach { row -> SkippedRow(row) }
            }
        }
    }

    StockAppPrimaryButton(
        text = "Ver carteira atualizada",
        onClick = onViewCarteira,
        modifier = Modifier.padding(top = 28.dp),
    )

    // Link de texto era fácil de passar despercebido - agora é um botão de verdade (contorno),
    // do mesmo jeito que o "Ver carteira atualizada" acima, só que secundário.
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .clip(RoundedCornerShape(100))
            .border(1.dp, StockAppColors.border, RoundedCornerShape(100))
            .clickable(onClick = onImportAnother)
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            "Importar outro arquivo",
            style = StockAppTypography.buttonLabel,
            color = StockAppColors.primaryDeep,
        )
    }
}

@Composable
private fun Divider() {
    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(StockAppColors.divider))
}

@Composable
private fun CreatedRow(row: CreatedImportRow) {
    DetailRow(
        title = row.ticker,
        subtitle = "${if (row.side == "BUY") "Compra" else "Venda"} · ${row.quantity} un · R$ ${row.price.toDecimalString()}",
        tag = "Importada",
        tagColor = StockAppColors.textSuccess,
        tagBg = StockAppColors.bgSuccess,
    )
}

@Composable
private fun SkippedRow(row: SkippedImportRow) {
    DetailRow(
        title = row.ticker ?: "Linha ${row.row}",
        subtitle = row.reason,
        tag = "Ignorada",
        tagColor = StockAppColors.textWarning,
        tagBg = StockAppColors.bgWarning,
    )
}

@Composable
private fun DetailRow(title: String, subtitle: String, tag: String, tagColor: Color, tagBg: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(StockAppColors.surface2, StockAppShapes.cardRadius)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = StockAppTypography.titleMedium, color = StockAppColors.textPrimary)
            Text(subtitle, style = StockAppTypography.bodySmall, color = StockAppColors.textSecondary, modifier = Modifier.padding(top = 2.dp))
        }
        Text(
            tag,
            style = StockAppTypography.labelMedium,
            color = tagColor,
            modifier = Modifier
                .background(tagBg, shape = RoundedCornerShape(100))
                .padding(horizontal = 12.dp, vertical = 6.dp),
        )
    }
}

@Composable
private fun ResultTile(modifier: Modifier = Modifier, value: String, label: String, valueColor: Color) {
    Column(
        modifier = modifier
            .background(StockAppColors.surface2, StockAppShapes.cardRadius)
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(value, style = StockAppTypography.headerTitle, color = valueColor)
        Text(label, style = StockAppTypography.labelSmall, color = StockAppColors.textSecondary, modifier = Modifier.padding(top = 4.dp))
    }
}