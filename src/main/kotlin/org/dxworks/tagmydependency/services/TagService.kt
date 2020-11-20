package org.dxworks.tagmydependency.services

import org.dxworks.tagmydependency.documents.DependencyRef
import org.dxworks.tagmydependency.documents.TagDocument
import org.dxworks.tagmydependency.repositories.TagRepository
import org.springframework.stereotype.Service
import javax.annotation.security.RolesAllowed

@Service
class TagService(
        private val authenticationService: AuthenticationService,
        private val tagRepository: TagRepository
) {

    fun addPrivateTag(tag: String, dependency: DependencyRef) {
        tagRepository.save(TagDocument(tag, dependency, authenticationService.getKeycloakUser().username))
    }

    fun getTagSuggestionsForUser(tagQuery: String): List<String> {
        return (getPrivateTagSuggestions(tagQuery) +
                tagRepository.findByUsernameAndTagContaining(authenticationService.defaultUsername, tagQuery))
                .map { it.tag }
    }

    fun getPrivateTagSuggestionsForUser(tagQuery: String): List<String> {
        return getPrivateTagSuggestions(tagQuery)
                .map { it.tag }
    }

    private fun getPrivateTagSuggestions(tagQuery: String): List<TagDocument> {
        return authenticationService.getCurrentUsername()
                ?.let { tagRepository.findByUsernameAndTagContaining(it, tagQuery) }
                ?: emptyList()
    }

    private fun getDependenciesForTag(tag: String): List<DependencyRef> {
        return (getDependenciesForPrivateTags(tag) +
                tagRepository.findByUsernameAndTag(authenticationService.defaultUsername, tag))
                .map { it.dependencyRef }
    }

    private fun getDependenciesForPrivateTags(tag: String): List<TagDocument> {
        return (authenticationService.getCurrentUsername()
                ?.let { tagRepository.findByUsernameAndTag(it, tag) }
                ?: emptyList())
    }

}
