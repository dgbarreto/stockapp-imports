package com.danilobarreto.stockapp.imports.data

import com.danilobarreto.stockapp.imports.data.dto.ErrorResponseDto
import com.danilobarreto.stockapp.imports.data.dto.ImportResultDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders

class ImportApiClient(
    private val httpClient: HttpClient,
    private val baseUrl: String,
) {
    suspend fun importOrders(fileBytes: ByteArray, fileName: String): ImportResultDto =
        httpClient.submitFormWithBinaryData(
            url = "$baseUrl/orders/import",
            formData = formData {
                append(
                    "file",
                    fileBytes,
                    Headers.build {
                        append(HttpHeaders.ContentType, ContentType.Application.OctetStream.toString())
                        append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                    },
                )
            },
        ).body()
}

suspend fun parseImportErrorMessage(exception: ClientRequestException): String =
    runCatching { exception.response.body<ErrorResponseDto>().message }
        .getOrDefault("Não foi possível completar a importação")