package org.dxworks.tagmydependency.dtos

import com.google.api.client.json.GenericJson
import com.google.api.client.util.Key

class DependencyResponseDTO : GenericJson() {
    @Key
    var numFound: Int = 0

    @Key
    var start: Int = 0

    @Key
    lateinit var docs: List<DependencyDTO>
}
