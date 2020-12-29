package org.dxworks.tagmydependency.dtos

import com.google.api.client.json.GenericJson
import com.google.api.client.util.ArrayMap
import com.google.api.client.util.Key

class NpmDependencyDTO : GenericJson() {
    @Key("package")
    lateinit var _package: ArrayMap<String, Any>
}