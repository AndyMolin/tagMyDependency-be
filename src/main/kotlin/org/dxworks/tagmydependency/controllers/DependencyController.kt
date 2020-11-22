package org.dxworks.tagmydependency.controllers

import org.dxworks.tagmydependency.dtos.result.DependencySearchResultDTO
import org.dxworks.tagmydependency.services.DependencyService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/public/dependency")
class DependencyController(
        private val dependencyService: DependencyService
) {
    @GetMapping("/search")
    fun search(@RequestParam query: String, @RequestParam providers: List<String>, @RequestParam pageSize: Int, @RequestParam pageNumber: Int): DependencySearchResultDTO {
        return if (query.isBlank())
            DependencySearchResultDTO(0, emptyList())
        else
            dependencyService.searchDependencies(query, providers, pageSize, pageNumber)
    }
}
