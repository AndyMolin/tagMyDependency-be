package org.dxworks.tagmydependency.dtos

import com.google.api.client.util.ArrayMap
import com.google.api.client.util.Key

class PypiSearchResponseDTO () {
    @Key
    lateinit var info: ArrayMap<String, Any>
}