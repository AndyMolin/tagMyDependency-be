package org.dxworks.tagmydependency.services

import com.google.api.client.http.GenericUrl
import com.google.api.client.util.Key
import org.dxworks.tagmydependency.const.NPM
import org.dxworks.tagmydependency.dtos.NpmSearchResponseDTO
import org.dxworks.tagmydependency.dtos.result.DependencyResultDTO
import org.dxworks.tagmydependency.dtos.result.DependencySearchResultDTO
import org.dxworks.utils.java.rest.client.RestClient
import org.springframework.stereotype.Service

const val NPM_SEARCH_BASE_URL = "http://registry.npmjs.com/-/v1/search"

const val NPM_ARTIFACT_BASE_URL = "https://www.npmjs.com/package"

@Service
class NpmSearchService : RestClient(NPM_SEARCH_BASE_URL), DependencySearchService {

    override fun searchDependencies(query: String, startAt: Int, pageSize: Int): DependencySearchResultDTO {
        return httpClient.get(NpmSearchUrl(query, startAt, pageSize)).parseAs(NpmSearchResponseDTO::class.java).let { res ->
            DependencySearchResultDTO(res.total.toLong(), res.objects.map {
                DependencyResultDTO(
                        name = it._package["name"] as String,
                        latestVersion = it._package["version"] as String,
                        provider = NPM,
                        url = "$NPM_ARTIFACT_BASE_URL/${it._package["name"]}"
                )
            })
        }
    }

    override fun providerName() = NPM

    class NpmSearchUrl(
            @Key
            private val text: String,
            @Key
            private val from: Int,
            @Key
            override val size: Int
    ) : GenericUrl(NPM_SEARCH_BASE_URL)
}