package com.easyhi.manage.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.easyhi.manage.data.network.Repo
import com.easyhi.manage.repository.RepoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class RepoViewModel @Inject constructor(
    private val repoRepository: RepoRepository
) : ViewModel(){

    fun getRepoPagingData(q: String): Flow<PagingData<Repo>>{
        return repoRepository.searchRepos(q).cachedIn(viewModelScope)
    }

}