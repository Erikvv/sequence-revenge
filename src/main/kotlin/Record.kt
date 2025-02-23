package nl.evanv

import kotlinx.io.Sink
import kotlinx.io.writeString
import kotlin.random.Random

data class Record (
    val property1: Int,
    val property2: Int,
) {
    fun writeToCsv(sink: Sink) {
        sink.writeString(property1.toString())
        sink.writeString(",")
        sink.writeString(property2.toString())
        sink.writeString("\n")
    }

    fun update() = copy(property2 = property2 + 1)

    fun isValid() = property1 > 0

    companion object {
        fun random(): Record = Record(Random.nextInt(), Random.nextInt())

        fun fromCsv(line: String): Record {
            val values = line.split(",")
            return Record(values[0].toInt(), values[1].toInt())
        }
    }
}
