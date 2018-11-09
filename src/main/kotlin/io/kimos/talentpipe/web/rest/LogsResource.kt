package io.kimos.talentpipe.web.rest

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import com.codahale.metrics.annotation.Timed
import io.kimos.talentpipe.web.rest.vm.LoggerVM
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

/**
 * Controller for view and managing Log Level at runtime.
 */
@RestController
@RequestMapping("/management")
class LogsResource {

    @GetMapping("/logs")
    @Timed
    fun getList(): kotlin.collections.List<LoggerVM> {
        val context = LoggerFactory.getILoggerFactory() as LoggerContext
        return context.loggerList
            .map { LoggerVM(it) }
    }

    @PutMapping("/logs")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Timed
    fun changeLevel(@RequestBody jsonLogger: LoggerVM) {
        val context = LoggerFactory.getILoggerFactory() as LoggerContext
        context.getLogger(jsonLogger.name).level = Level.valueOf(jsonLogger.level)
    }
}
