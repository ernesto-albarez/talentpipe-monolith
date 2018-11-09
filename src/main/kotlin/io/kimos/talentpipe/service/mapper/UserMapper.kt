package io.kimos.talentpipe.service.mapper

import io.kimos.talentpipe.domain.Authority
import io.kimos.talentpipe.domain.User
import io.kimos.talentpipe.service.dto.UserDTO

import org.springframework.stereotype.Service

import java.util.*

/**
 * Mapper for the entity User and its DTO called UserDTO.
 *
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */
@Service
class UserMapper {

    fun userToUserDTO(user: User): UserDTO {
        return UserDTO(user)
    }

    fun usersToUserDTOs(users: List<User>): List<UserDTO> {
        return users
            .filter { Objects.nonNull(it) }
            .map { this.userToUserDTO(it) }
    }

    fun userDTOToUser(userDTO: UserDTO?): User? {
        return if (userDTO == null) {
            null
        } else {
            val user = User()
            user.id = userDTO.id
            user.login = userDTO.login
            user.firstName = userDTO.firstName
            user.lastName = userDTO.lastName
            user.email = userDTO.email
            user.imageUrl = userDTO.imageUrl
            user.activated = userDTO.isActivated
            user.langKey = userDTO.langKey
            user.authorities = this.authoritiesFromStrings(userDTO.authorities)
            return user
        }
    }

    fun userDTOsToUsers(userDTOs: List<UserDTO>): List<User?> {
        return userDTOs
            .filter { Objects.nonNull(it) }
            .map { this.userDTOToUser(it) }
    }

    fun userFromId(id: Long?): User? {
        if (id == null) {
            return null
        }
        val user = User()
        user.id = id
        return user
    }


    fun authoritiesFromStrings(strings: Set<String>): MutableSet<Authority> {
        return strings.map { string ->
            val auth = Authority()
            auth.name = string
            auth
        }.toMutableSet()
    }
}
