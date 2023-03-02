package com.easyhi.manage.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.easyhi.manage.R
import com.easyhi.manage.databinding.FragmentLoginBinding
import com.easyhi.manage.databinding.FragmentMainBinding
import com.easyhi.manage.util.toMenuIndex

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null

    private val binding: FragmentMainBinding
        get() = _binding!!

    private lateinit var adapter: FragmentStateAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vpMain.isUserInputEnabled = false
        binding.vpMain.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
        val field = ViewPager2::class.java.getDeclaredField("mRecyclerView")
        field.isAccessible = true
        val recyclerView = field.get(binding.vpMain) as RecyclerView
        recyclerView.setItemViewCacheSize(5)

        adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return 4
            }

            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> RepoFragment()
                    1 -> NotificationFragment()
                    2 -> ExploreFragment()
                    3 -> MineFragment()
                    else -> EmptyFragment()
                }
            }

        }
        binding.vpMain.adapter = adapter

        binding.bottomMenu.setOnItemSelectedListener {
            binding.vpMain.setCurrentItem(it.itemId.toMenuIndex(), false)
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}




