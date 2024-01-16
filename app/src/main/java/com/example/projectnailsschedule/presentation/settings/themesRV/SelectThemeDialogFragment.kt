package com.example.projectnailsschedule.presentation.settings.themesRV

import ZoomOutPageTransformer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.databinding.FragmentSelectThemeBinding
import com.google.android.material.tabs.TabLayoutMediator

private const val NUM_PAGES = 5

class SelectThemeDialogFragment : DialogFragment() {
    val log = this::class.simpleName

    private var _binding: FragmentSelectThemeBinding? = null
    private var themeSelectRv: RecyclerView? = null
    private lateinit var viewPager: ViewPager2
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.SelectThemeDialog)
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels / 1.5).toInt()
        val height = (resources.displayMetrics.heightPixels / 1.5).toInt()
        dialog?.window?.setLayout(width, height)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // set binding
        _binding = FragmentSelectThemeBinding.inflate(inflater, container, false)

        inflatePager()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabLayout = binding.tabLayout

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            //tab.text = "${(position + 1)}"
            tab.setIcon(R.drawable.background)
        }.attach()

    }

    private fun inflatePager() {
        viewPager = binding.pager

        val pagerAdapter = ScreenSlidePagerAdapter(this)
        viewPager.adapter = pagerAdapter
        viewPager.setPageTransformer(ZoomOutPageTransformer())

        viewPager.clipToPadding = false
        viewPager.setPadding(40, 40, 40, 40)
    }

    inner class ScreenSlidePagerAdapter(fa: SelectThemeDialogFragment) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): Fragment = ScreenSlidePageFragment()
    }

    class ScreenSlidePageFragment : Fragment() {

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View = inflater.inflate(R.layout.fragment_screen_slide_page, container, false)
    }
}


