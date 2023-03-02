package com.easyhi.manage.data.network

import androidx.room.Entity

data class Resp<T>(
    val code: Int,
    val message: String,
    val data: T? = null,
)

data class AuthQrUrl(
    val qrUrl: String,
    val token: String
)

data class BindInfo(
    val bindTime: String?,
    val deviceName: String?,
    val merchantName: String?
)

data class AuthToken(
    val accessToken: String,
    val scope: String,
    val tokenType: String
)

data class AuthTokenResult(
    val errorMessage: String? = null,
    val token: AuthToken? = null
)

@Entity(primaryKeys = ["id"])
data class User(
    val login: String,
    val id: Int,
    val nodeId: String,
    val avatarUrl: String,
    val siteAdmin: Boolean,
    val name: String,
    val company: String,
    val location: String?,
    val bio: String,
    val email: String,
    val twitterUsername: String,
    val createdAt: String,
    val updatedAt: String,
    val publicRepos: Int,
    val publicGists: Int,
    val followers: Int,
    val following: Int,
    val privateGists: Int,
    val totalPrivateRepos: Int,
    val ownedPrivateRepos: Int,
    val diskUsage: Int,
    val collaborators: Int,
)

data class RepoResp(
    val totalCount: Int,
    val incompleteResults: Boolean,
    val items: List<Repo>
)

data class Repo(
    val id: Int,
    val nodeId: String,
    val name: String,
    val fullName: String,
    val private: Boolean,
    val htmlUrl: String,
    val description: String,
    val createdAt: String,
    val updatedAt: String,
    val pushedAt:String,
    val language: String,
    val size: Int,
    val stargazersCount: Int,
    val watchersCount: Int,
    val forksCount: Int,
    val score: Int,
    val forks: Int,
    val openIssues: Int,
    val watchers: Int,
    val owner: OwnerUser
)

data class OwnerUser(
    val login: String,
    val id: Int,
    val avatarUrl: String,
)





