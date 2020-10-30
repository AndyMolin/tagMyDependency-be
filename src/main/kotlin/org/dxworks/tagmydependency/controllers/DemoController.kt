package org.dxworks.tagmydependency.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/demo")
class DemoController {

    @GetMapping
    fun sayHello () = mapOf("greeting" to "Hello, Demo stuff")

}
