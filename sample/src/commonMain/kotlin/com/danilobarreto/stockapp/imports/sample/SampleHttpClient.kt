package com.danilobarreto.stockapp.imports.sample

import com.danilobarreto.stockapp.auth.data.TokenStorage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createSampleHttpClient(tokenStorage: TokenStorage): HttpClient =
    HttpClient {
        expectSuccess = true
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(Auth) {
            bearer {
                loadTokens {
                    tokenStorage.read()?.let { BearerTokens(it, refreshToken = "") }
                }
                // Sem isso, o Ktor só manda o token depois de já ter tomado um 401 (reativo).
                // Funciona pra GET simples, mas quebra em upload multipart (import de extrato) -
                // manda o token de cara pra não depender de repetir a requisição com o corpo.
                sendWithoutRequest { true }
            }
        }
    }
