package org.dxworks.tagmydependency.dtos.result

class DependencySearchResultDTO(
        val total: Long,
        val elements: List<DependencyResultDTO>
        // pagination later
)
