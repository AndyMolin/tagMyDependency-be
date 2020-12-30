package org.dxworks.tagmydependency.repositories

import org.dxworks.tagmydependency.documents.DependencyRef
import org.dxworks.tagmydependency.documents.TagDocument
import org.springframework.data.mongodb.repository.MongoRepository

interface TagRepository : MongoRepository<TagDocument, String> {

    fun findByUsernameAndTagContaining(username: String, tagQuery: String): List<TagDocument>
    fun findByUsernameAndTag(username: String, tag: String): List<TagDocument>
    fun findByTagAndDependencyRef(tag: String, dependencyRef: DependencyRef): List<TagDocument>
    fun existsByTagAndDependencyRefAndUsernameIn(tag: String, dependencyRef: DependencyRef, usernames: List<String>): Boolean
    fun findByIsUnderReviewTrueAndTagContaining(query: String): List<TagDocument>
    fun findBySuggestedByAndUsername(suggestedBy: String, username: String): List<TagDocument>
}
