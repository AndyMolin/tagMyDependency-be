package org.dxworks.tagmydependency.dtos

import com.google.api.client.util.Key

class NpmSearchResponseDTO {
    @Key
    var total: Int = 0

    @Key
    lateinit var objects: List<NpmDependencyDTO>
}