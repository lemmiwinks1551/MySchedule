package com.example.projectnailsschedule.presentation.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.databinding.FragmentAccountHomeBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountFragmentHome : Fragment() {
    private val log = this::class.simpleName
    private var _binding: FragmentAccountHomeBinding? = null
    private val viewModel: AccountViewModel by viewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountHomeBinding.inflate(inflater, container, false)

        CoroutineScope(Dispatchers.IO).launch {
            inflateHomeFragment()
        }

        initClickListeners()

        return binding.root
    }

    private suspend fun inflateHomeFragment() {
        if (viewModel.getJwt().isNullOrBlank()) {
            binding.welcomeTv.text = "Добро пожаловать!"
            binding.loggedCl.visibility = View.GONE
            binding.unloggedCl.visibility = View.VISIBLE
        } else {
            binding.welcomeTv.text = "Добро пожаловать, ${viewModel.user?.login}!"
            binding.loggedCl.visibility = View.VISIBLE
            binding.unloggedCl.visibility = View.GONE
        }
    }

    private fun initClickListeners() {
        binding.loginButton.setOnClickListener {
            showDialogLogin()
        }

        binding.registrationButton.setOnClickListener {
            showDialogRegistration()
        }
    }

    private fun showDialogLogin() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog_login, null)
        bottomSheetDialog.setContentView(view)

        val loginButton = view.findViewById<Button>(R.id.login_button)
        loginButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val login = view.findViewById<EditText>(R.id.login_et).text.toString()
                var password: String? = view.findViewById<EditText>(R.id.password_et).text.toString()

                viewModel.login(login, password!!)
                password = null
            }
        }

        bottomSheetDialog.show()
    }

    private fun showDialogRegistration() {

    }
}