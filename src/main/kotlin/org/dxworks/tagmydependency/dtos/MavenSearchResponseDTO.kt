package org.dxworks.tagmydependency.dtos

import com.google.api.client.util.Key

class MavenSearchResponseDTO () {
    @Key
    lateinit var response: DependencyResponseDTO
}