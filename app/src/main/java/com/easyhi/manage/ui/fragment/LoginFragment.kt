package com.easyhi.manage.ui.fragment

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.amap.api.location.AMapLocationClient
import com.easyhi.manage.R
import com.easyhi.manage.databinding.FragmentLoginBinding
import com.easyhi.manage.repository.TAG
import com.easyhi.manage.ui.viewmodel.LoginViewModel
import com.easyhi.manage.util.DEBUG_TAG
import com.easyhi.manage.util.GITHUB_CLIENT_ID
import com.easyhi.manage.util.TempStore
import com.easyhi.manage.util.scopeArray
import com.google.android.material.snackbar.Snackbar
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.apache.commons.lang3.RandomStringUtils

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    private val binding: FragmentLoginBinding
        get() = _binding!!

    private val loginViewModel by activityViewModels<LoginViewModel>()

    private var mLocationClient: AMapLocationClient? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        mLocationClient = AMapLocationClient(requireContext())

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            getPermission{
                if (it) {
                    getLocation()
                }else {
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(), "定位需要权限", Toast.LENGTH_SHORT).show()
                    }
                }

            }

//            jumpAuthUrl()
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    loginViewModel.uiState.collectLatest { uiState ->
                        if (uiState.isLoading) {
                            binding.btnLogin.text = "loading"
                        } else {
                            binding.btnLogin.text = "登录"
                        }
                        if (uiState.authSuccess) {
                            //跳转
                            Snackbar.make(binding.btnLogin, "登陆成功", Snackbar.LENGTH_SHORT).show()
                            findNavController().apply {
                                val action = LoginFragmentDirections.actionLoginFragmentToMainFragment()
                                navigate(action)
                            }
                        }
                        if (uiState.errorMessage != null) {
                            Snackbar.make(binding.btnLogin, "登陆失败", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

    }


    private fun getPermission(callback: (Boolean)->Unit) {
        if (XXPermissions.isGranted(
                context,
                Permission.ACCESS_FINE_LOCATION,
                Permission.ACCESS_COARSE_LOCATION,
            )
        ) {
            callback(true)
        } else {
            XXPermissions.with(context) // 申请单个权限
                .permission(Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION,)
                .request(object : OnPermissionCallback {
                    override fun onGranted(permissions: List<String>, all: Boolean) {
                        callback(true)
                    }

                    override fun onDenied(permissions: List<String>, never: Boolean) {
                        callback(false)
                    }
                })
        }
    }

    private fun getLocation(){

        mLocationClient?.setLocationListener { location ->
            GlobalScope.launch(Dispatchers.Main) {
                if (location != null) {
                    if (location.errorCode == 0) {
                        Toast.makeText(requireContext(), "定位 location = $location", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "定位失败 ${location.errorCode}, ${location.errorInfo}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "定位失败", Toast.LENGTH_SHORT).show()
                }

            }
        }

        mLocationClient?.startLocation()

    }

    private fun jumpAuthUrl() {
        val randomState = RandomStringUtils.randomAlphabetic(8)
        TempStore.randomState = randomState
        val githubAuthUri = Uri.parse(
            "https://github.com/login/oauth/authorize" +
                    "?scope=${scopeArray.joinToString("%20")}" +
                    "&client_id=$GITHUB_CLIENT_ID" +
                    "&state=${randomState}" +
                    "&allowSignUp=true"
        )
        Intent(Intent.ACTION_VIEW, githubAuthUri).also {
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(it)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mLocationClient = null
    }

}




