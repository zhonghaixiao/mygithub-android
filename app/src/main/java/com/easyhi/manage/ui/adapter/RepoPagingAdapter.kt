package com.easyhi.manage.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.easyhi.manage.data.network.Repo
import com.easyhi.manage.databinding.ItemRepoBinding

class RepoPagingAdapter: PagingDataAdapter<Repo, RepoPagingAdapter.ViewHolder>(comparator) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    class ViewHolder(val binding: ItemRepoBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(repo: Repo) {
            binding.txtOwnerUser.text = repo.owner.login
            binding.txtRepoDesc.text = repo.description
            binding.txtRepoName.text = repo.name
            binding.txtLanguage.text = repo.language
            binding.txtStarNum.text = repo.stargazersCount.toString()
        }

    }

    companion object {
        val comparator =  object :DiffUtil.ItemCallback<Repo>() {
            override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
                return newItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean {
                return newItem == oldItem
            }

        }
    }



}