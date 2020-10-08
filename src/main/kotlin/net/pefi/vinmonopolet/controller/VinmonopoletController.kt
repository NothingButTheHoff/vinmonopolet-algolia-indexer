package net.pefi.vinmonopolet.controller

import net.pefi.vinmonopolet.service.VinmonopoletService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class VinmonopoletController (
    private val vinmonopoletService: VinmonopoletService
    ) {

    @GetMapping(value = ["/"])
    fun index() : String {
        vinmonopoletService.fetchProductsCsvFile()

        return "OK"
    }



}