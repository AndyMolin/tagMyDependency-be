package org.dxworks.tagmydependency.repositories

import org.dxworks.tagmydependency.documents.TagDocument
import org.springframework.data.mongodb.repository.MongoRepository

interface TagRepository: MongoRepository<TagDocument, Long> {

    fun findByUsernameAndTagContaining(username: String, tagQuery: String): List<TagDocument>
    fun findByUsernameAndTag(username: String, tag: String): List<TagDocument>
}
