package com.easyhi.manage.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.easyhi.manage.R
import com.easyhi.manage.databinding.FragmentBindingBinding
import com.easyhi.manage.events.JumpEventType
import com.easyhi.manage.ui.dialog.OptDialog
import com.easyhi.manage.ui.view.ButtonWithCount
import com.easyhi.manage.ui.viewmodel.BindingViewModel
import com.easyhi.manage.ui.viewmodel.COUNT
import dagger.hilt.android.ViewModelLifecycle

private const val TAG = "BindingFragment"

class BindingFragment : Fragment() {

    private val viewModel by activityViewModels<BindingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentBindingBinding>(
            inflater,
            R.layout.fragment_binding, container, false
        ).apply {
            btnCheck.updateCountText("验证")
            btnCheck.setBtnState(ButtonWithCount.ButtonState.INIT)
            btnCheck.setOnClickListener {
                editEasyid.setErrorState("")
                val easyId = editEasyid.getEdit().text.toString()
                if (btnCheck.getBtnState() == ButtonWithCount.ButtonState.INIT) {
                    viewModel.checkEasyId(easyId) { resp ->
                        lifecycleScope.launchWhenResumed {
                            if (resp.code == 0) {
//                                viewModel.beginCount()
                                btnCheck.setBtnState(ButtonWithCount.ButtonState.COUNTING)
                                findNavController().apply {
                                    val action =
                                        BindingFragmentDirections.actionBindingFragmentToBindingCodeFragment(
                                            easyId
                                        )
                                    navigate(action)
                                }
                            } else {
                                // easyId不存在
                                editEasyid.setErrorState(resp.message)
                            }
                        }
                    }
                }
            }
        }

        viewModel.countData.observe(viewLifecycleOwner) { count ->
            if (count == 0) {
                binding.btnCheck.updateCountText("验证")
                binding.btnCheck.setBtnState(ButtonWithCount.ButtonState.INIT)
                binding.btnCheck.setBtnClickable(true)
            } else if (count in 1 until COUNT) {
                binding.btnCheck.updateCountText("${count}s")
                binding.btnCheck.setBtnState(ButtonWithCount.ButtonState.COUNTING)
                binding.btnCheck.setBtnClickable(false)
            }
        }


        viewModel.resetCount()

        return binding.root
    }

}