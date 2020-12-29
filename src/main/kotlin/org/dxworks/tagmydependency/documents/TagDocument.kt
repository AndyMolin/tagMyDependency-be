package org.dxworks.tagmydependency.documents

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document
class TagDocument(
        @Indexed
        val tag: String,
        val dependencyRef: DependencyRef,
        @Indexed
        val username: String,
        var isUnderReview: Boolean = false,
        var likes: Int = 0,
        var disLikes: Int = 0
) {
    @Id
    var id: String = createID()

    private fun createID(): String {
        return "{'tag':$tag,'depName':${dependencyRef.name},'depProv':${dependencyRef.provider},'username':$username}"
    }
}

