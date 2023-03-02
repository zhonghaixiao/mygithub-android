package com.easyhi.manage.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface RepoService {

    @GET("/search/repositories")
    suspend fun searchRepos(
        @Query("q") q: String,
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): RepoResp

}