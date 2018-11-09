package io.kimos.talentpipe.config

import io.github.jhipster.config.JHipsterConstants
import org.springframework.core.env.Environment

/**
 * Utility class to load a Spring profile to be used as default
 * when there is no <code>spring.profiles.active</code> set in the environment or as command line argument.
 * If the value is not available in <code>application.yml</code> then <code>dev</code> profile will be used as default.
 */
class DefaultProfileUtil {

    private constructor()

    companion object {

        private val SPRING_PROFILE_DEFAULT = "spring.profiles.default"

        /**
         * Set a default to use when no profile is configured.
         *
         * @param app the Spring application
         */
        fun addDefaultProfile(): Map<String, Any>? {
            val defProperties = HashMap<String, String>()
            /*
            * The default profile to use when no other profiles are defined
            * This cannot be set in the <code>application.yml</code> file.
            * See https://github.com/spring-projects/spring-boot/issues/1219
            */
            defProperties[SPRING_PROFILE_DEFAULT] = JHipsterConstants.SPRING_PROFILE_DEVELOPMENT
            return defProperties
        }

        /**
         * Get the profiles that are applied else get default profiles.
         *
         * @param env spring environment
         * @return profiles
         */
        fun getActiveProfiles(env: Environment): Array<String> {
            val profiles = env.activeProfiles
            if (profiles.isEmpty()) {
                return env.defaultProfiles
            }
            return profiles
        }
    }
}
