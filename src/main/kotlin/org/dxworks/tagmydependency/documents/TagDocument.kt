package org.dxworks.tagmydependency.documents

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document
class TagDocument(
        @Indexed
        val tag: String,
        val dependencyRef: DependencyRef,
        @Indexed
        val username: String,
        val isUnderReview: Boolean = false,
        val likes: Int = 0,
        val disLikes: Int = 0
) {
    @Id
    val id: String? = null
}

