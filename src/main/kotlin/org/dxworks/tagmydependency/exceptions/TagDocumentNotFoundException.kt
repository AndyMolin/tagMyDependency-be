package org.dxworks.tagmydependency.exceptions

import org.dxworks.tagmydependency.documents.DependencyRef

class TagDocumentNotFoundException(tagId: String) :
        RuntimeException("Tag with id $tagId not found!")
