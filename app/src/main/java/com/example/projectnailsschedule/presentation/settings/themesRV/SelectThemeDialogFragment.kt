package com.example.projectnailsschedule.presentation.settings.themesRV

import ZoomOutPageTransformer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.databinding.FragmentSelectThemeBinding
import com.example.projectnailsschedule.presentation.settings.SettingsViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

private const val NUM_PAGES = 5
private val iconArray = arrayOf(
    R.drawable.background_default,
    R.drawable.background_pink,
    R.drawable.background_gray,
    R.drawable.background_green,
    R.drawable.background_orange
)

private val themesArray = arrayOf(
    "Theme.Main",
    "MyNewThemePink",
    "MyNewThemeGray",
    "MyNewThemeGreen",
    "MyNewThemeOrange"
)

@AndroidEntryPoint
class SelectThemeDialogFragment : DialogFragment() {
    val log = this::class.simpleName

    private var _binding: FragmentSelectThemeBinding? = null
    private lateinit var viewPager: ViewPager2
    private val binding get() = _binding!!
    private val settingsViewModel: SettingsViewModel by viewModels()

    private lateinit var currentTheme: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.SelectThemeDialog)
        getCurrentName()
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

        initClickListener()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setIcon(iconArray[position])
        }.attach()
    }

    private fun inflatePager() {
        viewPager = binding.pager

        val pagerAdapter = ScreenSlidePagerAdapter(this)
        viewPager.adapter = pagerAdapter
        viewPager.setPageTransformer(ZoomOutPageTransformer())

        viewPager.clipToPadding = false
        viewPager.setPadding(40, 40, 40, 40)

        viewPager.currentItem = getThemeItem(themeName = currentTheme)
    }

    inner class ScreenSlidePagerAdapter(fa: SelectThemeDialogFragment) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): Fragment = ScreenSlidePageFragment(position)
    }

    class ScreenSlidePageFragment(val position: Int) : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View = inflater.inflate(R.layout.fragment_screen_slide_page, container, false).apply {

            val imageView = findViewById<ImageView>(R.id.theme_image)
            imageView.setImageResource(iconArray[position])
        }

    }

    private fun initClickListener() {
        binding.selectThemeButton.setOnClickListener {
            val themeNum = viewPager.currentItem
            val themeName = getThemeName(themeNum)
            if (themeName == currentTheme) {
                Toast.makeText(requireContext(), "Already done!", Toast.LENGTH_SHORT).show()
            } else {
                dialog?.dismiss()

                settingsViewModel.setUserTheme(theme = themeName)
                settingsViewModel.restartApp()
            }
        }
    }

    private fun getThemeName(item: Int): String {
        return when (item) {
            0 -> themesArray[0]
            1 -> themesArray[1]
            2 -> themesArray[2]
            3 -> themesArray[3]
            4 -> themesArray[4]
            else -> ""
        }
    }

    private fun getThemeItem(themeName: String): Int {
        return when (themeName) {
            themesArray[0] -> 0
            themesArray[1] -> 1
            themesArray[2] -> 2
            themesArray[3] -> 3
            themesArray[4] -> 4
            else -> 0
        }
    }

    private fun getCurrentName() {
        currentTheme = settingsViewModel.getUserTheme()
    }
}


