package com.example.projectnailsschedule.presentation.account

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.databinding.FragmentAccountHomeBinding
import com.example.projectnailsschedule.domain.models.User
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
    private lateinit var progressBar: ProgressBar

    val showView: (View) -> Unit = { it.visibility = View.VISIBLE }
    val hideView: (View) -> Unit = { it.visibility = View.GONE }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountHomeBinding.inflate(inflater, container, false)

        initViews()

        initObservers()

        initClickListeners()

        return binding.root
    }

    private fun initViews() {
        progressBar = binding.progressCircular
    }

    private fun initObservers() {
        // Мониторинг состояния запроса
        viewModel.requestDone.observe(viewLifecycleOwner) {
            if (it) {
                // Если запрос выполнен - убрать progressCircular
                hideView(progressBar)
            } else {
                // Если запрос выполняется - показать progressCircular
                showView(progressBar)
            }
        }

        // Мониторинг состояния логина пользователя
        viewModel.user.observe(viewLifecycleOwner) {
            if (it != null) {
                inflateHomeFragment(true, user = it)
            } else {
                inflateHomeFragment(false, user = null)
            }
        }
    }

    private fun inflateHomeFragment(loggedIn: Boolean, user: User?) {
        if (loggedIn) {
            binding.welcomeTv.text = "Добро пожаловать, ${user?.username}!"
            binding.loggedCl.visibility = View.VISIBLE
            binding.unloggedCl.visibility = View.GONE
        } else {
            binding.welcomeTv.text = "Добро пожаловать!"
            binding.loggedCl.visibility = View.GONE
            binding.unloggedCl.visibility = View.VISIBLE
        }
    }

    private fun initClickListeners() {
        binding.loginButton.setOnClickListener {
            showDialogLogin()
        }

        binding.logoutButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.logout()
            }
        }

        binding.registrationButton.setOnClickListener {
            showDialogRegistration()
        }

        binding.myAccButton.setOnClickListener {
        }
    }

    private fun showDialogLogin() {
        val dialog = Dialog(requireContext())
        val view = layoutInflater.inflate(R.layout.dialog_login, null)
        dialog.setContentView(view)

        val loginButton = view.findViewById<Button>(R.id.login_button)

        loginButton.setOnClickListener {
            val login = view.findViewById<EditText>(R.id.login_et).text.toString()
            var password: String? = view.findViewById<EditText>(R.id.password_et).text.toString()

            CoroutineScope(Dispatchers.Main).launch {
                dialog.dismiss()
                viewModel.login(login, password!!)
                password = null
            }
        }

        view.findViewById<EditText>(R.id.login_et).requestFocus()

        dialog.show()
    }

    private fun showDialogRegistration() {

    }
}