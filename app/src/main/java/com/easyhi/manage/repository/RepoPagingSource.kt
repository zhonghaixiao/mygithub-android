package com.easyhi.manage.repository

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.easyhi.manage.data.network.Repo
import com.easyhi.manage.data.network.RepoService

class RepoPagingSource(
    val q: String,
    private val repoService: RepoService
) : PagingSource<Int, Repo>() {

    override fun getRefreshKey(state: PagingState<Int, Repo>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {
        val loadParam = try{
            val page = params.key ?: 1
            val pageSize = params.loadSize
            val repoResp = repoService.searchRepos(q, "star", page, pageSize)
            val preKey = if(page > 1) page -1 else null
            val nextKey = if(repoResp.items.isNotEmpty()) page + 1 else null
            val param = LoadResult.Page(repoResp.items, preKey, nextKey)
            Log.d("RepoPagingSource", "loadparam.items.size = ${param.data.size}, preKey = ${param.prevKey}, nextKey = ${param.nextKey}")
            param
        } catch (e: Exception) {
            Log.d("RepoPagingSource", "e = $e")
            LoadResult.Error(e)
        }

        return loadParam
    }

}