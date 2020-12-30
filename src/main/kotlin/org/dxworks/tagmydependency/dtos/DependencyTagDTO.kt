package org.dxworks.tagmydependency.dtos

import org.dxworks.tagmydependency.documents.DependencyRef

class DependencyTagDTO(
        val id: String,
        val dependencyRef: DependencyRef,
        val tag: String,
        val deprecated: Boolean,
        val rejected: Boolean,
        val underReview: Boolean,
        val likes: Int,
        val dislikes: Int,
        var vote: String? = null
)
