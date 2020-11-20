package org.dxworks.tagmydependency.services

import org.dxworks.tagmydependency.dtos.KeycloakUser
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service


@Service
class AuthenticationService {

    fun getAuthentication(): KeycloakAuthenticationToken {
        return SecurityContextHolder.getContext().authentication as KeycloakAuthenticationToken
    }

    fun getKeycloakUser(): KeycloakUser {
        val authentication = getAuthentication()
        val token = authentication.account.keycloakSecurityContext.token
        return KeycloakUser(
                token.preferredUsername,
                token.email).also {
            it.roles = authentication.authorities.map { ga -> ga.authority.removePrefix("ROLE_") }
        }
    }

    fun getCurrentUsername(): String? {
        return try {
            getKeycloakUser().username
        } catch (e: Exception) {
            null
        }
    }

    val defaultUsername = "public"
}
