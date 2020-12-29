package org.dxworks.tagmydependency.services

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class DependencyServiceTest {

    @Autowired
    private lateinit var mavenSearchService: MavenSearchService

    @Autowired
    private lateinit var npmSearchService: NpmSearchService

    @Test
    fun `get maven dependency response`() {
        val findDependencies = mavenSearchService.searchDependencies("guice", 0, 20)

        assertNotNull(findDependencies)
    }

    @Test
    fun `get npm dependency response`() {
        val findDependencies = npmSearchService.searchDependencies("ng", 0, 20)

        assertNotNull(findDependencies)
    }
}
