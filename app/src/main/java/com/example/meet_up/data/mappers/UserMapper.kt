package com.example.meet_up.data.mappers

import com.example.meet_up.data.local.entities.User
import com.example.meet_up.domain.models.UserModel

fun User.toUserModel(): UserModel {
    return UserModel(
        login, password
    )
}