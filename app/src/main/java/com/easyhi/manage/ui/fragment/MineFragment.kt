package com.easyhi.manage.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.easyhi.manage.R
import com.easyhi.manage.databinding.FragmentMineBinding
import com.easyhi.manage.ui.viewmodel.MineViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MineFragment : Fragment() {

    private var _binding: FragmentMineBinding? = null

    private val binding: FragmentMineBinding
        get() = _binding!!

    private val viewModel: MineViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.userBaseInfoData.observe(viewLifecycleOwner) { user ->
            if (user == null) return@observe
            Glide.with(this).load(user.avatarUrl).into(binding.baseInfo.imageHead)
            binding.baseInfo.txtLogin.text = user.login
            binding.baseInfo.txtCompany.text = user.company
            binding.baseInfo.txtBio.text = user.bio
            binding.baseInfo.txtEmail.text = user.email
            binding.baseInfo.txtFacebook.text = user.twitterUsername
            binding.baseInfo.txtUserName.text = user.name
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.pullUserBaseInfo()
    }

}