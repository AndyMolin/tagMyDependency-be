package org.dxworks.tagmydependency.exceptions

class SuggestionAlreadyExistsException(val id: String) : RuntimeException("Suggestion with id $id already exists!")
