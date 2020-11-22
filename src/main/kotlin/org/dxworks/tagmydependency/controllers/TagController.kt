package org.dxworks.tagmydependency.controllers

import org.dxworks.tagmydependency.documents.DependencyRef
import org.dxworks.tagmydependency.services.TagService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/public/tag")
class TagController(
        private val tagService: TagService
) {

    @GetMapping("search")
    fun getTagsByQuery(@RequestParam(required = false, defaultValue = "") query: String, @RequestParam private: Boolean = false): List<String> {
        return (if (private)
            tagService.getPrivateTagSuggestionsForUser(query)
        else
            tagService.getTagSuggestionsForUser(query)).distinct()
    }

    @GetMapping()
    fun getDependencyByTag(@RequestParam tag: String): List<DependencyRef> {
        return tagService.getDependenciesForTag(tag)
    }

    @PostMapping
    fun addTag(@RequestBody body: AddTagBody) {
        tagService.addPrivateTag(body.tag, body.dependencyRef)
    }
}

class AddTagBody {
    lateinit var tag: String
    lateinit var dependencyRef: DependencyRef
}
