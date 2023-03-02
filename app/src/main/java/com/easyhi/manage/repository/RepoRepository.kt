package com.easyhi.manage.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.easyhi.manage.data.network.Repo
import com.easyhi.manage.data.network.RepoService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepoRepository @Inject constructor(
    private val repoService: RepoService,
) {

    private val PAGE_SIZE = 10

    fun searchRepos(q: String): Flow<PagingData<Repo>> {
        return Pager(
            config = PagingConfig(
                PAGE_SIZE,
            ),
            pagingSourceFactory = {
                RepoPagingSource(q, repoService)
            }
        ).flow
    }


}