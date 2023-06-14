package com.example.projectnailsschedule.presentation.clients.clientsRecyclerView

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectnailsschedule.presentation.clients.ClientsViewModel

class ClientsViewModelFactory(private val context: Context?) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ClientsViewModel(context = context!!) as T
    }
}