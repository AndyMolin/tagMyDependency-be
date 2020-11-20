package org.dxworks.tagmydependency.services

import com.google.api.client.http.GenericUrl
import com.google.api.client.util.Key
import org.dxworks.tagmydependency.dtos.MavenSearchResponseDTO
import org.dxworks.utils.java.rest.client.RestClient
import org.springframework.stereotype.Service

const val MAVEN_SEARCH_BASE_URL = "https://search.maven.org/solrsearch/select"

@Service
class MavenSearchService : RestClient(MAVEN_SEARCH_BASE_URL) {

    fun searchDependency(query: String, startAt: Int = 0, pageSize: Int = 20): MavenSearchResponseDTO {
        return httpClient.get(MavenSearchUrl(query, startAt, pageSize)).parseAs(MavenSearchResponseDTO::class.java)
    }

    class MavenSearchUrl(
            @Key
            private val q: String,
            @Key
            private val start: Int,
            @Key
            private val rows: Int
    ) : GenericUrl(MAVEN_SEARCH_BASE_URL) {
        @Key
        private val wt: String = "json"
    }
}
