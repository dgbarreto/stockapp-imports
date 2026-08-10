package com.danilobarreto.stockapp.imports.presentation

import androidx.compose.runtime.Composable
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.dataWithContentsOfURL
import platform.UIKit.UIApplication
import platform.UIKit.UIDocumentPickerDelegateProtocol
import platform.UIKit.UIDocumentPickerMode
import platform.UIKit.UIDocumentPickerViewController
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow
import platform.darwin.NSObject
import platform.posix.memcpy

private var activeDelegate: DocumentPickerDelegate? = null

@OptIn(ExperimentalForeignApi::class)
private fun NSData.toByteArray(): ByteArray {
    val size = length.toInt()
    return ByteArray(size).apply {
        if (size > 0) usePinned {pinned ->
            memcpy(pinned.addressOf(0), bytes, size.convert())
        }
    }
}

private class DocumentPickerDelegate(
    private val onPicked: (PickedFile) -> Unit
): NSObject(), UIDocumentPickerDelegateProtocol{
    override fun documentPicker(
        controller: UIDocumentPickerViewController,
        didPickDocumentsAtURLs: List<*>
    ) {
        activeDelegate = null
        val url = didPickDocumentsAtURLs.firstOrNull() as? NSURL ?: return
        val didStartAccessing = url.startAccessingSecurityScopedResource()
        try {
            val data = NSData.dataWithContentsOfURL(url) ?: return
            onPicked(PickedFile(data.toByteArray(), url.lastPathComponent ?: "extrato"))
        } finally {
            if (didStartAccessing) url.stopAccessingSecurityScopedResource()
        }
    }

    override fun documentPickerWasCancelled(controller: UIDocumentPickerViewController) {
        activeDelegate = null
    }
}

@Composable
actual fun rememberFilePicker(onPicked: (PickedFile) -> Unit): () -> Unit {
    return {
        val delegate = DocumentPickerDelegate(onPicked)
        activeDelegate = delegate

        val picker = UIDocumentPickerViewController(
            documentTypes = listOf(
                "public.comma-separated-values-text",
                "org.openxmlformats.spreadsheetml.sheet",
                "com.microsoft.excel.xls"
            ),
            inMode = UIDocumentPickerMode.UIDocumentPickerModeImport
        )

        picker.delegate = delegate
        topViewController()?.presentViewController(picker, animated = true, completion = null)
    }
}

private fun topViewController(): UIViewController? {
    val keyWindow = UIApplication.sharedApplication.windows
        .filterIsInstance<UIWindow>()
        .firstOrNull { it.isKeyWindow() }
    var top = keyWindow?.rootViewController
    while (top?.presentedViewController != null) {
        top = top.presentedViewController
    }
    return top
}