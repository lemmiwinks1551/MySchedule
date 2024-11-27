package com.example.projectnailsschedule.domain.models.dto

data class UserInfoDto(
    val id: Long?,
    val username: String?,
    val userEmail: String?,
    val emailVerified: Boolean?,
    val enabled: Boolean?,
    val syncEnabled: Boolean?,
    val accountNonLocked: Boolean?,
    val authorities: List<String>?,
    val credentialsNonExpired: Boolean?,
    val accountNonExpired: Boolean?
)

object UserInfoDtoManager {
    private var userInfoDto: UserInfoDto? = null

    // Возвращает текущий UserDto или null, если он не установлен
    fun getUserDto(): UserInfoDto? {
        return userInfoDto
    }

    // Устанавливает UserDto
    fun setUserDto(newUserInfoDto: UserInfoDto) {
        userInfoDto = newUserInfoDto
    }

    // Очищает сохраненный UserDto
    fun clearUserDto() {
        userInfoDto = null
    }

    // Обновляет отдельные поля UserDto, создавая копию объекта с изменёнными значениями
    fun updateUserDto(
        id: Long? = null,
        username: String? = null,
        userEmail: String? = null,
        emailVerified: Boolean? = null,
        enabled: Boolean? = null,
        accountNonLocked: Boolean? = null,
        authorities: List<String>? = null,
        credentialsNonExpired: Boolean? = null,
        accountNonExpired: Boolean? = null
    ) {
        userInfoDto = userInfoDto?.copy(
            id = id ?: userInfoDto!!.id,
            username = username ?: userInfoDto!!.username,
            userEmail = userEmail ?: userInfoDto!!.userEmail,
            emailVerified = emailVerified ?: userInfoDto!!.emailVerified,
            enabled = enabled ?: userInfoDto!!.enabled,
            accountNonLocked = accountNonLocked ?: userInfoDto!!.accountNonLocked,
            authorities = authorities ?: userInfoDto!!.authorities,
            credentialsNonExpired = credentialsNonExpired ?: userInfoDto!!.credentialsNonExpired,
            accountNonExpired = accountNonExpired ?: userInfoDto!!.accountNonExpired
        )
    }
}