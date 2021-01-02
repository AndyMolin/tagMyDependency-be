package org.dxworks.tagmydependency.services

import com.google.api.client.http.GenericUrl
import com.google.api.client.util.Key
import org.dxworks.tagmydependency.const.PYPI
import org.dxworks.tagmydependency.dtos.PypiSearchResponseDTO
import org.dxworks.tagmydependency.dtos.result.DependencyResultDTO
import org.dxworks.tagmydependency.dtos.result.DependencySearchResultDTO
import org.dxworks.utils.java.rest.client.RestClient
import org.springframework.stereotype.Service

const val PYPI_SEARCH_BASE_URL = "https://pypi.org/pypi/"

@Service
class PypiSearchService : RestClient(PYPI_SEARCH_BASE_URL), DependencySearchService {

    override fun searchDependencies(query: String, startAt: Int, pageSize: Int): DependencySearchResultDTO {
        try {
            return httpClient.get(PypiSearchUrl(query)).parseAs(PypiSearchResponseDTO::class.java).let { res ->
                DependencySearchResultDTO(1,
                        listOf(DependencyResultDTO(
                                name = res.info["name"] as String,
                                latestVersion = res.info["version"] as String,
                                provider = PYPI,
                                url = res.info["package_url"] as String
                        ))
                )
            }
        } catch (e: Exception) {
            println(e.stackTrace)
        }
        return DependencySearchResultDTO(0, emptyList())
    }

    override fun providerName() = PYPI

    class PypiSearchUrl(
            @Key
            private val text: String
    ) : GenericUrl("$PYPI_SEARCH_BASE_URL$text/json")
}