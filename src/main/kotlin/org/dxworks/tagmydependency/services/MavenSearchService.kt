package org.dxworks.tagmydependency.services

import com.google.api.client.http.GenericUrl
import com.google.api.client.util.Key
import org.dxworks.tagmydependency.const.MAVEN
import org.dxworks.tagmydependency.dtos.MavenSearchResponseDTO
import org.dxworks.tagmydependency.dtos.result.DependencyResultDTO
import org.dxworks.tagmydependency.dtos.result.DependencySearchResultDTO
import org.dxworks.utils.java.rest.client.RestClient
import org.springframework.stereotype.Service

const val MAVEN_SEARCH_BASE_URL = "https://search.maven.org/solrsearch/select"

const val MAVEN_ARTIFACT_BASE_URL = "https://mvnrepository.com/artifact"

@Service
class MavenSearchService : RestClient(MAVEN_SEARCH_BASE_URL), DependencySearchService {

    override fun searchDependencies(query: String, startAt: Int, pageSize: Int): DependencySearchResultDTO {
        return httpClient.get(MavenSearchUrl(query, startAt, pageSize)).parseAs(MavenSearchResponseDTO::class.java).let { res ->
            DependencySearchResultDTO(res.response.numFound.toLong(), res.response.docs.map {
                DependencyResultDTO(
                        name = it.id,
                        latestVersion = it.latestVersion,
                        provider = MAVEN,
                        url = "$MAVEN_ARTIFACT_BASE_URL/${it.g}/${it.a}"
                )
            })
        }
    }

    override fun providerName() = MAVEN

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
