package com.example.projectnailsschedule.presentation.about

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.BuildConfig
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.databinding.FragmentAboutBinding
import com.example.projectnailsschedule.domain.repository.ProductionCalendarApi
import com.example.projectnailsschedule.util.Util
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

class AboutFragment : Fragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!
    private var versionTextView: TextView? = null
    private var aboutViewModel: AboutViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        aboutViewModel =
            ViewModelProvider(this)[AboutViewModel::class.java]

        _binding = FragmentAboutBinding.inflate(inflater, container, false)

        initWidgets()

        setVersionTextView()

        /*        CoroutineScope(Dispatchers.IO).launch {
                    addTestData()
                }*/

        retrofitTest()

        return binding.root
    }

    private fun initWidgets() {
        versionTextView = binding.versionTextView
    }

    private fun setVersionTextView() {
        val versionLabel = getString(R.string.app_version)
        val appVersion = "$versionLabel ${BuildConfig.VERSION_NAME}"
        versionTextView?.text = appVersion
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private suspend fun addTestData() {
        // Запускаем загрузку данных
        if (BuildConfig.DEBUG) {
            Log.d("Test", "Debug")
            /** for test only !!!
             * add fake appointments */
            Util().addTestData(requireContext())
            Util().createTestClients(requireContext())
            Util().createTestProcedures(requireContext())
        } else {
            Log.d("Test", "Prod")
        }
    }

    private fun retrofitTest() {
        // add interceptor for logs
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        class CacheInterceptor : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val response: Response = chain.proceed(chain.request())
                val cacheControl = CacheControl.Builder()
                    .maxAge(
                        10,
                        TimeUnit.DAYS
                    ) // Устанавливаем максимальный возраст кэшированных данных
                    .build()
                return response.newBuilder()
                    .header("Cache-Control", cacheControl.toString())
                    .build()
            }
        }

        class ForceCacheInterceptor : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val builder: Request.Builder = chain.request().newBuilder()
                if (!isInternetAvailable()) { // Функция для проверки доступности интернета
                    builder.cacheControl(CacheControl.FORCE_CACHE)
                }
                return chain.proceed(builder.build())
            }
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addNetworkInterceptor(CacheInterceptor())
            .addInterceptor(ForceCacheInterceptor())
            .cache(createOkHttpClient().cache)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://production-calendar.ru")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val productionCalendarApi = retrofit.create(ProductionCalendarApi::class.java)

        val date = "09.05.2024"

        CoroutineScope(Dispatchers.IO).launch {
            val cachedData = client.cache

            val result = productionCalendarApi.getDateStatus(date)
            withContext(Dispatchers.Main) {
                binding.releaseYear.text = result.days[0].toString()
            }
        }
    }

    private fun createOkHttpClient(): OkHttpClient {
        // Размер кэша - 10 МБ
        val cacheSize = 10 * 1024 * 1024
        val cacheDirectory = File(requireContext().cacheDir, "http-cache")
        val cache = Cache(cacheDirectory, cacheSize.toLong())

        return OkHttpClient.Builder()
            .cache(cache)
            .build()
    }

    fun isInternetAvailable(): Boolean {
        val connectivityManager =
            context?.getSystemService(android.content.Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo?.isConnected ?: false
        }
    }
}