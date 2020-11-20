package org.dxworks.tagmydependency.services

import org.dxworks.tagmydependency.dtos.result.DependencySearchResultDTO
import org.springframework.stereotype.Service

@Service
class DependencyService(
        private val dependencySearchServices: List<DependencySearchService>
) {

    private val providerSearchServiceMap = dependencySearchServices.associateBy { it.providerName() }

    fun searchDependencies(query: String, providers: List<String>): DependencySearchResultDTO {
        val requiredProviders = if (providers.isEmpty()) dependencySearchServices else providers.mapNotNull { providerSearchServiceMap[it] }
        val results = requiredProviders.map { it.searchDependencies(query) }
        return DependencySearchResultDTO(results.map { it.total }.sum(), results.flatMap { it.elements })
    }

}
