package com.danilobarreto.stockapp.imports.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danilobarreto.stockapp.imports.data.parseImportErrorMessage
import com.danilobarreto.stockapp.imports.domain.ImportRepository
import com.danilobarreto.stockapp.imports.domain.ImportResult
import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface ImportUiState {
    data object Idle : ImportUiState
    data object Uploading : ImportUiState
    data class Success(val result: ImportResult) : ImportUiState
    data class Error(val message: String) : ImportUiState
}

class ImportViewModel(
    private val repository: ImportRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ImportUiState>(ImportUiState.Idle)
    val uiState: StateFlow<ImportUiState> = _uiState.asStateFlow()

    fun import(file: PickedFile) {
        viewModelScope.launch {
            _uiState.value = ImportUiState.Uploading
            _uiState.value = try {
                ImportUiState.Success(repository.importOrders(file.bytes, file.fileName))
            } catch (e: ClientRequestException) {
                ImportUiState.Error(parseImportErrorMessage(e))
            } catch (e: Exception) {
                ImportUiState.Error(e.message ?: "Erro ao importar o arquivo")
            }
        }
    }

    fun reset() {
        _uiState.value = ImportUiState.Idle
    }
}