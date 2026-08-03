package com.danilobarreto.stockapp.imports.sample

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.danilobarreto.stockapp.auth.data.AuthApiClient
import com.danilobarreto.stockapp.auth.data.AuthRepositoryImpl
import com.danilobarreto.stockapp.auth.data.TokenStorage
import com.danilobarreto.stockapp.auth.presentation.LoginScreen
import com.danilobarreto.stockapp.auth.presentation.LoginViewModel
import com.danilobarreto.stockapp.designsystem.theme.StockAppTheme
import com.danilobarreto.stockapp.imports.data.ImportApiClient
import com.danilobarreto.stockapp.imports.data.ImportRepositoryImpl
import com.danilobarreto.stockapp.imports.presentation.ImportScreen
import com.danilobarreto.stockapp.imports.presentation.ImportViewModel

// Sample isolado do módulo imports: só valida login (via auth) + build da árvore de módulos.
// Ainda não existe domain/data/presentation de importação de ordens aqui - assim que isso for
// implementado, a tela de placeholder abaixo vira a tela real de escolher arquivo/enviar/resultado.
@Composable
fun SampleApp() {
    val tokenStorage = remember { TokenStorage() }
    val httpClient = remember { createSampleHttpClient(tokenStorage) }

    val authRepository = remember {
        AuthRepositoryImpl(AuthApiClient(httpClient, sampleBaseUrl()), tokenStorage)
    }
    val importRepository = remember {
        ImportRepositoryImpl(ImportApiClient(httpClient, sampleBaseUrl()))
    }

    val loginViewModel = remember { LoginViewModel(authRepository) }
    val importViewModel = remember { ImportViewModel(importRepository) }

    val isLoggedIn by authRepository.isLoggedIn.collectAsState()

    StockAppTheme {
        if (isLoggedIn) {
            ImportScreen(
                viewModel = importViewModel,
                onBack = {}
            )
        } else {
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = { /* isLoggedIn muda e recompõe pro placeholder sozinho */ },
                onNavigateToRegister = { /* sample é só login, de propósito */ }
            )
        }
    }
}
