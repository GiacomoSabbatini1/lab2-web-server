package es.unizar.webeng.lab2

import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.time.ZoneId

data class TimeDTO(
    val time: LocalDateTime,
)

interface TimeProvider {
    // Aggiungiamo un parametro opzionale per il fuso orario
    fun now(zone: String? = null): LocalDateTime
}

@Service
class TimeService : TimeProvider {
    override fun now(zone: String?): LocalDateTime {
        // Se l'utente specifica una zona, usiamo quella, altrimenti usiamo l'orario di default del server
        return if (zone != null) {
            LocalDateTime.now(ZoneId.of(zone))
        } else {
            LocalDateTime.now()
        }
    }
}

fun LocalDateTime.toDTO(): TimeDTO = TimeDTO(time = this)

@RestController
class TimeController(
    private val service: TimeProvider,
) {
    // Aggiungiamo @RequestParam per intercettare "?zone=..." nell'URL
    @GetMapping("/time")
    fun time(
        @RequestParam(required = false) zone: String?,
    ): TimeDTO = service.now(zone).toDTO()
}
