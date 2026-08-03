package com.danilobarreto.stockapp.imports.presentation

import androidx.compose.runtime.Composable
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

@Composable
actual fun rememberFilePicker(onPicked: (PickedFile) -> Unit): () -> Unit {
    return {
        val dialog = FileDialog(Frame(), "Selecionar extrato", FileDialog.LOAD)
        dialog.setFilenameFilter { _, name -> name.endsWith(".xlsx") || name.endsWith(".csv") }
        dialog.isVisible = true
        val filename = dialog.file
        val directory = dialog.directory

        if(filename != null && directory != null) {
            val file = File(directory, filename)
            onPicked(PickedFile(file.readBytes(), file.name))
        }
    }
}