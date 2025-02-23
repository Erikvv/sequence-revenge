package nl.evanv

import kotlinx.io.Sink
import kotlinx.io.Source
import kotlinx.io.readLine

fun processRecordsUsingSequence(sourceCsv: Source, destinationCsv: Sink) {
    sequence {
        while (true) {
            val line = sourceCsv.readLine() ?: break
            yield(line)
        }
    }.map {
        Record.fromCsv(it)
    }.filter {
        it.isValid()
    }.map {
        it.update()
    }.forEach {
        it.writeToCsv(destinationCsv)
    }
}

fun processRecordsUsingList(sourceCsv: Source, destinationCsv: Sink) {
    buildList {
        while (true) {
            val line = sourceCsv.readLine() ?: break
            add(line)
        }
    }.map {
        Record.fromCsv(it)
    }.filter {
        it.isValid()
    }.map {
        it.update()
    }.forEach {
        it.writeToCsv(destinationCsv)
    }
}

fun processRecordsUsingLoop(sourceCsv: Source, destinationCsv: Sink) {
    while (true) {
        val line = sourceCsv.readLine() ?: break
        val entity = Record.fromCsv(line)
        if (entity.isValid()) {
            continue
        }
        val newEntity = entity.update()
        newEntity.writeToCsv(destinationCsv)
    }
}
