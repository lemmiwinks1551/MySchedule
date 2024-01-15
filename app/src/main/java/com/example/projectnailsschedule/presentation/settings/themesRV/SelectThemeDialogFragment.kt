package com.example.projectnailsschedule.presentation.settings.themesRV

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.databinding.FragmentSelectThemeBinding

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
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
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

    private fun inflatePager() {

        viewPager = binding.pager

        val pagerAdapter = ScreenSlidePagerAdapter(this)
        viewPager.adapter = pagerAdapter
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


