package com.danilobarreto.stockapp.imports.presentation

import androidx.compose.runtime.Composable

data class PickedFile(val bytes: ByteArray, val fileName: String)

// Abre o seletor de arquivo nativo da plataforma. Retorna uma função "launch": chamar essa
// função abre o picker; quando o usuário escolhe um arquivo, onPicked é chamado com os bytes.
@Composable
expect fun rememberFilePicker(onPicked: (PickedFile) -> Unit): () -> Unit