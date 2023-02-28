package com.easyhi.manage.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.easyhi.manage.R
import com.easyhi.manage.databinding.ActivityMainBinding
import com.easyhi.manage.ui.viewmodel.LoginViewModel
import com.easyhi.manage.util.DEBUG_TAG
import com.easyhi.manage.util.TempStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        Log.d(DEBUG_TAG, "onCreate")
        val uri = intent.data
        if (uri != null) {
            Log.d(DEBUG_TAG, "外部唤起 uri = $uri")
            Log.d(DEBUG_TAG, "loginAuthUtil.state = ${TempStore.randomState}")
            val code = uri.getQueryParameter("code")
            val state = uri.getQueryParameter("state")
            if (state != TempStore.randomState) {
                Toast.makeText(this, "第三方伪造了state", Toast.LENGTH_SHORT).show()
                return
            }
            if (code.isNullOrEmpty()) {
                Toast.makeText(this, "登陆失败 code 为空", Toast.LENGTH_SHORT).show()
                return
            }
            // 获取访问令牌
            loginViewModel.getAuthToken(code)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.loginStatus
                    .collectLatest { hasLogin ->
                        if (!hasLogin) {
                            findNavController(R.id.nav_host_fragment).navigate(R.id.loginFragment)
                        }
                    }
            }
        }

    }

    override fun onBackPressed() {
        if (!findNavController(R.id.nav_host_fragment).popBackStack()) {
            finish()
        }
        super.onBackPressed()
    }

}







