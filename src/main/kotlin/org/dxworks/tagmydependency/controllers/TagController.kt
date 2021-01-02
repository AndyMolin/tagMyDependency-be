package org.dxworks.tagmydependency.controllers

import org.dxworks.tagmydependency.documents.DependencyRef
import org.dxworks.tagmydependency.dtos.DependencyTagDTO
import org.dxworks.tagmydependency.dtos.TagDTO
import org.dxworks.tagmydependency.services.TagService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/public/tag")
class TagController(
        private val tagService: TagService
) {

    @GetMapping("search")
    fun getTagsByQuery(@RequestParam(required = false, defaultValue = "") query: String, @RequestParam private: Boolean = false): List<TagDTO> =
            if (private)
                tagService.getPrivateTagsForUser(query)
            else
                tagService.getTagsForUser(query)

    @PostMapping
    fun addTag(@RequestBody body: AddTagBody) = tagService.addPrivateTag(body.tag, body.dependencyRef)

    @PostMapping("suggestion")
    fun createSuggestion(@RequestBody tagId: String) = tagService.createTagSuggestion(tagId)

    @GetMapping("suggestion")
    fun getSuggestions(@RequestParam(required = false, defaultValue = "") query: String): List<DependencyTagDTO> =
            tagService.getAllSuggestions(query)

    @GetMapping("suggestion/accepted")
    fun getAcceptedSuggestions(): List<DependencyTagDTO> = tagService.getAcceptedSuggestionsOfCurrentUser()

    @GetMapping("suggestion/rejected")
    fun getRejectedSuggestions(): List<DependencyTagDTO> = tagService.getRejectedSuggestionsOfCurrentUser()

    @PutMapping("suggestion/like")
    fun likeSuggestion(@RequestBody tagId: String) = tagService.increaseLikes(tagId)

    @DeleteMapping("suggestion/like")
    fun removeSuggestionLike(@RequestBody tagId: String) = tagService.decreaseLikes(tagId)

    @PutMapping("suggestion/dislike")
    fun dislikeSuggestion(@RequestBody tagId: String) = tagService.increaseDislikes(tagId)

    @DeleteMapping("suggestion/dislike")
    fun removeSuggestionDislike(@RequestBody tagId: String) = tagService.decreaseDislikes(tagId)
}

class AddTagBody {
    lateinit var tag: String
    lateinit var dependencyRef: DependencyRef
}
