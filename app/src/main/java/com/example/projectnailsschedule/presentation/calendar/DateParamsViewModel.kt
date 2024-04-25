package com.example.projectnailsschedule.presentation.calendar

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.models.CalendarDateModelDb
import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.domain.models.Day
import com.example.projectnailsschedule.domain.repository.ProductionCalendarApi
import com.example.projectnailsschedule.domain.usecase.appointmentUC.DeleteAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.appointmentUC.InsertAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.appointmentUC.SearchAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.appointmentUC.UpdateAppointmentUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.CalendarDbDeleteObj
import com.example.projectnailsschedule.domain.usecase.calendarUC.GetDateAppointments
import com.example.projectnailsschedule.domain.usecase.calendarUC.InsertCalendarDateUseCase
import com.example.projectnailsschedule.domain.usecase.calendarUC.SelectCalendarDateByDateUseCase
import com.example.projectnailsschedule.domain.usecase.clientsUC.GetClientByIdUseCase
import com.example.projectnailsschedule.domain.usecase.socUC.StartInstagramUc
import com.example.projectnailsschedule.domain.usecase.socUC.StartPhoneUc
import com.example.projectnailsschedule.domain.usecase.socUC.StartTelegramUc
import com.example.projectnailsschedule.domain.usecase.socUC.StartVkUc
import com.example.projectnailsschedule.domain.usecase.socUC.StartWhatsAppUc
import com.example.projectnailsschedule.presentation.calendar.calendarRecyclerView.CalendarRvAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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
import java.time.LocalDate
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class DateParamsViewModel @Inject constructor(
    private val getDateAppointments: GetDateAppointments,
    private val selectCalendarDateByDateUseCase: SelectCalendarDateByDateUseCase,
    private val insertCalendarDateUseCase: InsertCalendarDateUseCase,
    private val calendarDbDeleteObj: CalendarDbDeleteObj,
    private var insertAppointmentUseCase: InsertAppointmentUseCase,
    private val updateAppointmentUseCase: UpdateAppointmentUseCase,
    private var searchAppointmentUseCase: SearchAppointmentUseCase,
    private var deleteAppointmentUseCase: DeleteAppointmentUseCase,
    private val startVkUc: StartVkUc,
    private val startTelegramUc: StartTelegramUc,
    private val startInstagramUc: StartInstagramUc,
    private val startWhatsAppUc: StartWhatsAppUc,
    private val startPhoneUc: StartPhoneUc
) : ViewModel() {
    private val tagDateColor = "DateColor"

    var oldPosition: Int = 0

    // var updates when click at day or month in calendar
    var selectedDate = MutableLiveData(
        DateParams(
            date = LocalDate.now()
        )
    )

    val dayOffInfo = MutableLiveData<String?>(null)

    // position of appointments in selectedDate.appointmentsList to edit
    // if position == null - create new appointment
    var appointmentPosition: Int? = null

    var previousDate = MutableLiveData(DateParams())

    var dateDetailsVisibility = MutableLiveData(false)

    var prevCalendarRvHolder: CalendarRvAdapter.ViewHolder? = null

    suspend fun getArrayAppointments(date: LocalDate): MutableList<AppointmentModelDb> {
        return getDateAppointments.execute(date)
    }

    fun updateSelectedDate(dateParams: DateParams) {
        val updatedDateParams =
            selectedDate.value?.copy(
                date = dateParams.date,
                appointmentsList = dateParams.appointmentsList
            )
        selectedDate.postValue(updatedDateParams)
    }

    fun changeMonth(operator: Boolean) {
        val updatedDateParams: DateParams = when (operator) {
            true -> selectedDate.value?.copy(
                date = selectedDate.value?.date?.plusMonths(1)
            )!!

            false -> selectedDate.value?.copy(
                date = selectedDate.value?.date?.minusMonths(1)
            )!!
        }

        selectedDate.postValue(updatedDateParams)
        dateDetailsVisibility.postValue(false)
        dayOffInfo.postValue(null)
    }

    private suspend fun selectCalendarDateByDate(date: String): CalendarDateModelDb {
        return selectCalendarDateByDateUseCase.execute(date)
    }

    suspend fun getDateId(ruFormatDate: String): Int? {
        Log.d(tagDateColor, "Getting color for $ruFormatDate")
        return try {
            val deferredColor = CoroutineScope(Dispatchers.IO).async {
                selectCalendarDateByDate(ruFormatDate)
            }

            Log.d(tagDateColor, "Date $ruFormatDate has color ${deferredColor.await()._id}")
            deferredColor.await()._id
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getDateColor(ruFormatDate: String): String? {
        Log.d(tagDateColor, "Getting color for $ruFormatDate")

        val deferredColor = CoroutineScope(Dispatchers.IO).async {
            selectCalendarDateByDate(ruFormatDate)
        }

        Log.d(tagDateColor, "Date $ruFormatDate has color ${deferredColor.await().color}")
        return deferredColor.await().color
    }

    suspend fun insertCalendarDate(calendarDateModelDb: CalendarDateModelDb): Boolean {
        return insertCalendarDateUseCase.execute(calendarDateModelDb)
    }

    suspend fun calendarDbDeleteObj(calendarDateModelDb: CalendarDateModelDb): Boolean {
        return calendarDbDeleteObj.execute(calendarDateModelDb)
    }

    suspend fun insertAppointment(
        appointmentModelDb: AppointmentModelDb,
        position: Int = -1
    ): Long {
        if (position == -1) {
            selectedDate.value?.appointmentsList?.add(appointmentModelDb)
        } else {
            selectedDate.value!!.appointmentsList?.add(
                position,
                appointmentModelDb
            )
        }
        selectedDate.postValue(selectedDate.value)
        return insertAppointmentUseCase.execute(appointmentModelDb)
    }

    suspend fun updateAppointment(appointmentModelDb: AppointmentModelDb): Boolean {
        selectedDate.value?.appointmentsList?.set(
            appointmentPosition!!,
            appointmentModelDb
        )
        selectedDate.postValue(selectedDate.value)
        return updateAppointmentUseCase.execute(appointmentModelDb)
    }

    suspend fun searchAppointment(searchQuery: String): MutableList<AppointmentModelDb> {
        return searchAppointmentUseCase.execute(searchQuery)
    }

    suspend fun deleteAppointment(
        appointmentModelDb: AppointmentModelDb,
        position: Int = -1
    ) {
        deleteAppointmentUseCase.execute(appointmentModelDb)
        if (position == -1) {
            selectedDate.value!!.appointmentsList?.removeAt(position)
        } else {
            selectedDate.value!!.appointmentsList?.remove(appointmentModelDb)
        }
        selectedDate.postValue(selectedDate.value)
    }

    suspend fun getDataInfo(context: Context, day: Int = 0): Day {
        // add interceptor for logs
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        class CacheInterceptor : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val response: Response = chain.proceed(chain.request())
                val cacheControl = CacheControl.Builder()
                    .maxAge(10, TimeUnit.DAYS)
                    .build()
                return response.newBuilder()
                    .header("Cache-Control", cacheControl.toString())
                    .build()
            }
        }

        class ForceCacheInterceptor : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val builder: Request.Builder = chain.request().newBuilder()
                if (!isInternetAvailable(context)) { // Функция для проверки доступности интернета
                    builder.cacheControl(CacheControl.FORCE_CACHE)
                }
                return chain.proceed(builder.build())
            }
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addNetworkInterceptor(CacheInterceptor())
            .addInterceptor(ForceCacheInterceptor())
            .cache(createOkHttpClient(context).cache)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://production-calendar.ru")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val productionCalendarApi = retrofit.create(ProductionCalendarApi::class.java)

        val year = selectedDate.value?.date?.year.toString()

        // load info
        productionCalendarApi.getYearData(year)

        // return day info
        return productionCalendarApi.getYearData(year).days[day]
    }

    private fun createOkHttpClient(context: Context): OkHttpClient {
        // Размер кэша - 100 МБ
        val cacheSize = 100 * 1024 * 1024
        val cacheDirectory = File(context.cacheDir, "http-cache")
        val cache = Cache(cacheDirectory, cacheSize.toLong())

        return OkHttpClient.Builder()
            .cache(cache)
            .build()
    }

    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

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

    fun getAppointmentPositionInDate(
        appointmentModelDb: AppointmentModelDb,
        appointmentList: MutableList<AppointmentModelDb>
    ): Int {
        return appointmentList.indexOf(appointmentModelDb)
    }

    fun startVk(uri: String) {
        startVkUc.execute(uri)
    }

    fun startTelegram(uri: String) {
        startTelegramUc.execute(uri)
    }

    fun startInstagram(uri: String) {
        startInstagramUc.execute(uri)
    }

    fun startWhatsApp(uri: String) {
        startWhatsAppUc.execute(uri)
    }

    fun startPhone(phoneNum: String) {
        startPhoneUc.execute(phoneNum)
    }

    fun getSelectedMonth(): LocalDate? {
        return selectedDate.value?.date
    }
}