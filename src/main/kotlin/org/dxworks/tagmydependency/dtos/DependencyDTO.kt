package org.dxworks.tagmydependency.dtos

import com.google.api.client.json.GenericJson
import com.google.api.client.util.Key

class DependencyDTO : GenericJson() {
    @Key
    lateinit var id: String

    @Key
    lateinit var g: String

    @Key
    lateinit var a: String

    @Key
    lateinit var latestVersion: String
}
