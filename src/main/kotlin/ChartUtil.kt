package nl.evanv

import java.awt.Desktop
import java.io.File

fun openInBrowser(content: String) {
    val dir = File(System.getProperty("user.dir"), "lets-plot-images")
    dir.mkdir()
    val file = File(dir.canonicalPath, "my_plot.html")
    file.createNewFile()
    file.writeText(content)

    Desktop.getDesktop().browse(file.toURI())
}
