package com.danilobarreto.stockapp.imports.presentation

import androidx.compose.runtime.Composable

@Composable
actual fun rememberFilePicker(onPicked: (PickedFile) -> Unit): () -> Unit {
    // TODO(Fase 8 - Shell iOS): implementar com UIDocumentPickerViewController.
    // Sem shell iOS rodando ainda, não dá pra validar - fica registrado como pendência.
    return { throw NotImplementedError("Seletor de arquivo ainda não implementado no iOS") }
}