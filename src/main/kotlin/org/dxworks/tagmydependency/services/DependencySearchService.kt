package org.dxworks.tagmydependency.services

import org.dxworks.tagmydependency.dtos.result.DependencySearchResultDTO

interface DependencySearchService {
    fun searchDependencies(query: String, startAt: Int = 0, pageSize: Int = 20): DependencySearchResultDTO
    fun providerName(): String
}
