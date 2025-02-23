package nl.evanv

import kotlinx.io.*
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.jetbrains.letsPlot.commons.encoding.Base64
import org.jetbrains.letsPlot.core.plot.export.PlotImageExport
import org.jetbrains.letsPlot.geom.geomLine
import org.jetbrains.letsPlot.intern.Plot
import org.jetbrains.letsPlot.intern.toSpec
import org.jetbrains.letsPlot.label.labs
import org.jetbrains.letsPlot.letsPlot
import org.jetbrains.letsPlot.scale.scaleXLog10
import org.jetbrains.letsPlot.scale.scaleYContinuous
import kotlin.time.measureTime


fun main() {
    val batchSizes = listOf(1, 2, 4, 10, 25, 100, 1_000, 10_000, 100_000, 250_000, 1_000_000, 3_000_000, 10_000_000)

    val results = batchSizes.map { batchSize ->
        val numRuns = (3_000_000 / batchSize).coerceAtMost(1_000_000).coerceAtLeast(5)
        println("warmup ${batchSize}")
        repeat(numRuns) {
            doBenchmark(batchSize)
        }
        println("finished warmup")
        var benchmarkResult = BenchmarkResult(batchSize)
        repeat(numRuns) {
            benchmarkResult += doBenchmark(batchSize)
        }
        benchmarkResult
    }

    val graphData = toGraphData(results)
    val plot: Plot = letsPlot(graphData) + geomLine(color = "green", manualKey = "List") {
        x = "batchSize"
        y = "listRatePerSecond"
    } + geomLine(color = "blue", manualKey = "Sequence") {
        x = "batchSize"
        y = "sequenceRatePerSecond"
    } + geomLine(color = "red", manualKey = "Loop") {
        x = "batchSize"
        y = "loopRatePerSecond"
    } +
            scaleXLog10() +
            labs(y = "records/s") +
            scaleYContinuous(limits = 0.0 to Double.NaN)

    val image = PlotImageExport.buildImageFromRawSpecs(
        plotSpec = plot.toSpec(),
        format = PlotImageExport.Format.JPEG(),
        scalingFactor = 2.0,
        targetDPI = Double.NaN
    )

    val imageBuffer = Buffer()
    imageBuffer.write(image.bytes)

    SystemFileSystem.sink(Path("result.jpeg")).write(imageBuffer, imageBuffer.size)

    val base64EncodedPng = Base64.encode(image.bytes)
    val dataUrl = "data:image/png;base64,$base64EncodedPng"
    val content = """
        <html>
           <img src="$dataUrl" alt="plot image" width="${image.plotSize.x}" height="${image.plotSize.y}">
        </html>
    """.trimIndent()

    openInBrowser(content)
}

fun doBenchmark(numRecords: Int): BenchmarkResult {
    val buffer = Buffer()

    repeat(numRecords) {
        Record.random().writeToCsv(buffer)
    }

    val sequenceBuffer = buffer.copy()
    val sequenceDuration = measureTime {
        processRecordsUsingSequence(sequenceBuffer, Buffer())
    }

    val listBuffer = buffer.copy()
    val listDuration = measureTime {
        processRecordsUsingList(listBuffer, Buffer())
    }

    val loopBuffer = buffer.copy()
    val loopDuration = measureTime {
        processRecordsUsingLoop(loopBuffer, Buffer())
    }

    return BenchmarkResult(
        batchSize = numRecords,
        numRecords = numRecords,
        sequenceDuration = sequenceDuration,
        listDuration = listDuration,
        loopDuration = loopDuration,
    )
}
