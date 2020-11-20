package org.dxworks.tagmydependency.services

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class DependencyServiceTest {

    @Autowired
    private lateinit var mavenSearchService: MavenSearchService

    @Test
    fun `get dependency response`() {
        val findDependencies = mavenSearchService.searchDependencies("guice")

        assertNotNull(findDependencies)
    }
}
