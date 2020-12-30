package org.dxworks.tagmydependency.services

import org.dxworks.tagmydependency.const.SUGGESTION_ACCEPTANCE_THRESHOLD
import org.dxworks.tagmydependency.const.SUGGESTION_REMOVAL_THRESHOLD
import org.dxworks.tagmydependency.documents.DependencyRef
import org.dxworks.tagmydependency.documents.TagDocument
import org.dxworks.tagmydependency.dtos.DependencyTagDTO
import org.dxworks.tagmydependency.dtos.TagDTO
import org.dxworks.tagmydependency.exceptions.SuggestionAlreadyExistsException
import org.dxworks.tagmydependency.exceptions.TagDocumentNotFoundException
import org.dxworks.tagmydependency.repositories.TagRepository
import org.springframework.stereotype.Service

@Service
class TagService(
        private val authenticationService: AuthenticationService,
        private val tagRepository: TagRepository
) {

    fun addPrivateTag(tag: String, dependency: DependencyRef) {
        if (tagRepository.existsByTagAndDependencyRefAndUsernameIn(tag, dependency, listOf(authenticationService.defaultUsername, authenticationService.getCurrentUsername()!!)))
            throw RuntimeException("Cannot add private tag because it exists as public or is already to a private tag of the user!")
        tagRepository.save(TagDocument(tag, dependency, authenticationService.getCurrentUsername()!!))
    }

    fun getTagsForUser(tagQuery: String): List<TagDTO> {
        return (getPrivateTags(tagQuery) +
                tagRepository.findByUsernameAndTagContaining(authenticationService.defaultUsername, tagQuery))
                .map { convertTagDocumentToDTO(it) }
                .groupBy { it.tag }.entries.map { TagDTO(it.key, it.value) }
    }

    fun getPrivateTagsForUser(tagQuery: String): List<TagDTO> {
        return getPrivateTags(tagQuery)
                .map { convertTagDocumentToDTO(it) }
                .groupBy { it.tag }.entries.map { TagDTO(it.key, it.value) }
    }

    private fun convertTagDocumentToDTO(tag: TagDocument): DependencyTagDTO {
        return DependencyTagDTO(id = tag.id,
                dependencyRef = tag.dependencyRef,
                tag = tag.tag,
                deprecated = tag.deprecated,
                rejected = tag.rejected,
                underReview = tag.isUnderReview,
                likes = tag.likes.size,
                dislikes = tag.dislikes.size,
                vote = computeVote(tag))
    }

    private fun computeVote(tag: TagDocument): String? {
        val currentUsername = authenticationService.getCurrentUsername()
        return when {
            currentUsername == null -> null
            tag.likes.contains(currentUsername) -> "like"
            tag.dislikes.contains(currentUsername) -> "dislike"
            else -> null
        }
    }

    private fun getPrivateTags(tagQuery: String): List<TagDocument> {
        return authenticationService.getCurrentUsername()
                ?.let { tagRepository.findByUsernameAndTagContaining(it, tagQuery) }
                ?: emptyList()
    }

    fun getDependenciesForTag(tag: String): List<DependencyRef> {
        return (getDependenciesForPrivateTags(tag) +
                tagRepository.findByUsernameAndTag(authenticationService.defaultUsername, tag))
                .map { it.dependencyRef }
    }

    private fun getDependenciesForPrivateTags(tag: String): List<TagDocument> {
        return (authenticationService.getCurrentUsername()
                ?.let { tagRepository.findByUsernameAndTag(it, tag) }
                ?: emptyList())
    }

    fun createTagSuggestion(tagId: String) {
        val tag = findTagDocument(tagId)
        tagRepository.findByTagAndDependencyRefAndIsUnderReviewTrue(tag.tag, tag.dependencyRef).firstOrNull()?.let {
            throw SuggestionAlreadyExistsException(it.id)
        }
        tag.isUnderReview = true
        tag.likes.add(authenticationService.getCurrentUsername()!!)
        tagRepository.save(tag)
    }


    fun increaseLikes(tagId: String): DependencyTagDTO {
        val tag = findTagDocument(tagId)
        tag.likes.add(authenticationService.getCurrentUsername()!!)
        tag.dislikes.remove(authenticationService.getCurrentUsername()!!)
        return convertTagDocumentToDTO(checkSuggestion(tagRepository.save(tag)))
    }

    fun decreaseLikes(tagId: String): DependencyTagDTO {
        val tag = findTagDocument(tagId)
        tag.likes.remove(authenticationService.getCurrentUsername()!!)
        return convertTagDocumentToDTO(checkSuggestion(tagRepository.save(tag)))
    }

    fun increaseDislikes(tagId: String): DependencyTagDTO {
        val tag = findTagDocument(tagId)
        tag.dislikes.add(authenticationService.getCurrentUsername()!!)
        tag.likes.remove(authenticationService.getCurrentUsername()!!)
        return convertTagDocumentToDTO(checkSuggestion(tagRepository.save(tag)))
    }

    fun decreaseDislikes(tagId: String): DependencyTagDTO {
        val tag = findTagDocument(tagId)
        tag.dislikes.remove(authenticationService.getCurrentUsername()!!)
        return convertTagDocumentToDTO(checkSuggestion(tagRepository.save(tag)))
    }

    private fun findTagDocument(tagId: String) =
            tagRepository.findById(tagId).orElseThrow { TagDocumentNotFoundException(tagId) }

    private fun checkSuggestion(tag: TagDocument): TagDocument {
        val likeNo = tag.likes.size
        val dislikeNo = tag.dislikes.size

        when {
            likeNo - dislikeNo > SUGGESTION_ACCEPTANCE_THRESHOLD -> {
                tag.suggestedBy = tag.username
                tag.username = authenticationService.defaultUsername
                tag.isUnderReview = false
                val newTag = tagRepository.save(tag)
                deprecateAllOtherUsersPrivateTags(newTag)
                return newTag
            }
            dislikeNo - likeNo > SUGGESTION_REMOVAL_THRESHOLD -> {
                tag.rejected = true
                tag.isUnderReview = false
                return tagRepository.save(tag)
            }
            else -> return tag
        }
    }

    private fun deprecateAllOtherUsersPrivateTags(tag: TagDocument) {
        tagRepository.saveAll(
                tagRepository.findByTagAndDependencyRef(tag.tag, tag.dependencyRef)
                        .filter { it.username != authenticationService.defaultUsername }
                        .onEach {
                            it.deprecated = true
                        })
    }

    fun getAllSuggestions(query: String): List<DependencyTagDTO> {
        return tagRepository.findByIsUnderReviewTrueAndTagContaining(query)
                .map { convertTagDocumentToDTO(it) }
    }

    fun getAcceptedSuggestionsOfCurrentUser(): List<DependencyTagDTO> {
        return tagRepository.findBySuggestedByAndUsername(authenticationService.getCurrentUsername()!!, authenticationService.defaultUsername)
                .map { convertTagDocumentToDTO(it) }
    }
}
