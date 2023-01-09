package com.easyhi.manage.data.bean

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TestBean(
    @PrimaryKey val userId: String,
    val token: String
)