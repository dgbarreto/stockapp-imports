import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.danilobarreto.stockapp.imports.sample.SampleApp

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Imports Sample") {
        SampleApp()
    }
}
