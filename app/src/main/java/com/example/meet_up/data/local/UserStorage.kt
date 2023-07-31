package com.example.meet_up.data.local

import com.example.meet_up.domain.models.UserModel

object UserStorage {

    private lateinit var _user: UserModel

    val user: UserModel
        get() = _user

    fun save(userModel: UserModel) {
        _user = userModel
    }
}