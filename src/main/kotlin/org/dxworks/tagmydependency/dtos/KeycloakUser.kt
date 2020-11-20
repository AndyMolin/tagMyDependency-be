package org.dxworks.tagmydependency.dtos

class KeycloakUser(val username: String,
                   val email: String? = null,
                   var roles: List<String> = emptyList(),
                   val githubUsername: String? = null,
                   val googleUsername: String? = null)
