package io.kimos.talentpipe.service

import io.kimos.talentpipe.config.Constants
import io.kimos.talentpipe.domain.Authority
import io.kimos.talentpipe.domain.User
import io.kimos.talentpipe.repository.AuthorityRepository
import io.kimos.talentpipe.repository.UserRepository
import io.kimos.talentpipe.repository.search.UserSearchRepository
import io.kimos.talentpipe.security.AuthoritiesConstants
import io.kimos.talentpipe.security.SecurityUtils
import io.kimos.talentpipe.service.dto.UserDTO
import io.kimos.talentpipe.service.util.RandomUtil
import io.kimos.talentpipe.web.rest.errors.InvalidPasswordException
import org.slf4j.LoggerFactory
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

/**
 * Service class for managing users.
 */
@Service
@Transactional
class UserService(private val userRepository: UserRepository
                  , private val passwordEncoder: PasswordEncoder
                  , private val userSearchRepository: UserSearchRepository
                  , private val authorityRepository: AuthorityRepository
                  , private val cacheManager: CacheManager) {

    private val log = LoggerFactory.getLogger(UserService::class.java)


    fun activateRegistration(key: String): Optional<User> {
        log.debug("Activating user for activation key {}", key)
        return userRepository.findOneByActivationKey(key)
            .map { user ->
                // activate given user for the registration key.
                user.activated = true
                user.activationKey = null
                userSearchRepository.save(user)
                this.clearUserCaches(user)
                log.debug("Activated user: {}", user)
                user
            }
    }

    fun completePasswordReset(newPassword: String, key: String): Optional<User> {
        log.debug("Reset user password for reset key {}", key)

        return userRepository.findOneByResetKey(key)
            .filter { user -> user.resetDate!!.isAfter(Instant.now().minusSeconds(86400)) }
            .map { user ->
                user.password = passwordEncoder.encode(newPassword)
                user.resetKey = null
                user.resetDate = null
                this.clearUserCaches(user)
                user
            }
    }

    fun requestPasswordReset(mail: String): Optional<User> {
        return userRepository.findOneByEmailIgnoreCase(mail)
            .filter({ it.activated!! })
            .map { user ->
                user.resetKey = RandomUtil.generateResetKey()
                user.resetDate = Instant.now()
                this.clearUserCaches(user)
                user
            }
    }

    fun registerUser(userDTO: UserDTO, password: String): User {

        val newUser = User()


        val encryptedPassword = passwordEncoder.encode(password)
        newUser.login = userDTO.login
        // new user gets initially a generated password
        newUser.password = encryptedPassword
        newUser.firstName = userDTO.firstName
        newUser.lastName = userDTO.lastName
        newUser.email = userDTO.email
        newUser.imageUrl = userDTO.imageUrl
        newUser.langKey = userDTO.langKey
        // new user is not active
        newUser.activated = false
        // new user gets registration key
        newUser.activationKey = RandomUtil.generateActivationKey()
        val authorities = HashSet<Authority>()
        authorityRepository.findById(AuthoritiesConstants.USER).map { authorities.add(it) }
        newUser.authorities = authorities
        userRepository.save(newUser)
        userSearchRepository.save(newUser)
        this.clearUserCaches(newUser)
        log.debug("Created Information for User: {}", newUser)
        return newUser
    }

    fun createUser(userDTO: UserDTO): User {
        val user = User()
        user.login = userDTO.login
        user.firstName = userDTO.firstName
        user.lastName = userDTO.lastName
        user.email = userDTO.email
        user.imageUrl = userDTO.imageUrl
        if (userDTO.langKey == null) {
            user.langKey = Constants.DEFAULT_LANGUAGE // default language
        } else {
            user.langKey = userDTO.langKey
        }

        if (userDTO.authorities != null) {
            val authorities = userDTO.authorities
                .map { authorityRepository.findById(it) }
                .filter { it.isPresent }
                .map { it.get() }
                .toMutableSet()
            user.authorities = authorities
        }
        val encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword())
        user.password = encryptedPassword
        user.resetKey = RandomUtil.generateResetKey()
        user.resetDate = Instant.now()
        user.activated = true
        userRepository.save(user)
        userSearchRepository.save(user)
        this.clearUserCaches(user)
        log.debug("Created Information for User: {}", user)
        return user
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user
     * @param lastName last name of user
     * @param email email id of user
     * @param langKey language key
     * @param imageUrl image URL of user
     */
    fun updateUser(firstName: String, lastName: String, email: String, langKey: String, imageUrl: String) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap { userRepository.findOneByLogin(it) }
            .ifPresent { user ->
                user.firstName = firstName
                user.lastName = lastName
                user.email = email
                user.langKey = langKey
                user.imageUrl = imageUrl
                userSearchRepository.save(user)
                this.clearUserCaches(user)
                log.debug("Changed Information for User: {}", user)
            }
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update
     * @return updated user
     */
    fun updateUser(userDTO: UserDTO): Optional<UserDTO> {
        return Optional.of(userRepository
            .findById(userDTO.id!!))
            .filter { it.isPresent }
            .map { it.get() }
            .map { user ->
                this.clearUserCaches(user)
                this.clearUserCaches(user)

                user.login = userDTO.login
                user.firstName = userDTO.firstName
                user.lastName = userDTO.lastName
                user.email = userDTO.email
                user.imageUrl = userDTO.imageUrl
                user.activated = userDTO.isActivated
                user.langKey = userDTO.langKey
                val managedAuthorities = user.authorities
                managedAuthorities.clear()
                userDTO.authorities
                    .map { authorityRepository.findById(it) }
                    .filter { it.isPresent }
                    .map { it.get() }
                    .forEach { managedAuthorities.add(it) }
                userSearchRepository.save(user)
                this.clearUserCaches(user)
                log.debug("Changed Information for User: {}", user)
                user
            }
            .map { UserDTO(it) }
    }

    fun deleteUser(login: String) {
        userRepository.findOneByLogin(login).ifPresent { user ->
            userRepository.delete(user)
            userSearchRepository.delete(user)
            this.clearUserCaches(user)
            log.debug("Deleted User: {}", user)
        }
    }


    fun changePassword(currentClearTextPassword: String, newPassword: String) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap { userRepository.findOneByLogin(it) }
            .ifPresent { user ->
                val currentEncryptedPassword = user.password
                if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                    throw InvalidPasswordException()
                }
                val encryptedPassword = passwordEncoder.encode(newPassword)
                user.password = encryptedPassword
                this.clearUserCaches(user)
                log.debug("Changed password for User: {}", user)
            }
    }


    @Transactional(readOnly = true)
    fun getAllManagedUsers(pageable: Pageable): Page<UserDTO> {
        return userRepository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER).map { UserDTO(it) }
    }

    @Transactional(readOnly = true)
    fun getUserWithAuthoritiesByLogin(login: String): Optional<User> {
        return userRepository.findOneWithAuthoritiesByLogin(login)
    }

    @Transactional(readOnly = true)
    fun getUserWithAuthorities(id: Long): Optional<User> {
        return userRepository.findOneWithAuthoritiesById(id)
    }

    @Transactional(readOnly = true)
    fun getUserWithAuthorities(): Optional<User> {
        return SecurityUtils.getCurrentUserLogin().flatMap { userRepository.findOneWithAuthoritiesByLogin(it) }
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    fun removeNotActivatedUsers() {
        val users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
        for (user in users) {
            log.debug("Deleting not activated user {}", user.login)
            userRepository.delete(user)
            userSearchRepository.delete(user)
            this.clearUserCaches(user)
        }
    }

    /**
     * @return a list of all the authorities
     */
    fun getAuthorities(): List<String?> {
        return authorityRepository.findAll().map { it.name }.toList()
    }


    private fun clearUserCaches(user: User) {
        Objects.requireNonNull<Cache>(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.login!!)
        Objects.requireNonNull<Cache>(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.email!!)
    }
}
