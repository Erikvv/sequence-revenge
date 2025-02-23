package nl.evanv

import kotlin.time.Duration
import kotlin.time.DurationUnit

data class BenchmarkResult (
    val batchSize: Int,
    val numRecords: Int = 0,
    val listDuration: Duration = Duration.ZERO,
    val sequenceDuration: Duration = Duration.ZERO,
    val loopDuration: Duration = Duration.ZERO,
) {
    operator fun plus(other: BenchmarkResult): BenchmarkResult {
        if (batchSize != other.batchSize) {
            throw Exception("Can only add results with same batch size")
        }

        return copy(
            numRecords = numRecords + other.numRecords,
            listDuration = listDuration + other.listDuration,
            sequenceDuration = sequenceDuration + other.sequenceDuration,
            loopDuration = loopDuration + other.loopDuration,
        )
    }

    fun listRate() = numRecords / listDuration.toDouble(DurationUnit.SECONDS)

    fun sequenceRate() = numRecords / sequenceDuration.toDouble(DurationUnit.SECONDS)

    fun loopRate() = numRecords / loopDuration.toDouble(DurationUnit.SECONDS)
}

fun toGraphData(results: Iterable<BenchmarkResult>): Map<String, List<Any>> {
    val sorted = results.sortedBy { it.batchSize }

    return mapOf(
        "batchSize" to sorted.map { it.batchSize },
        "listRatePerSecond" to sorted.map { it.listRate() },
        "sequenceRatePerSecond" to sorted.map { it.sequenceRate() },
        "loopRatePerSecond" to sorted.map { it.loopRate() }
    )
}
