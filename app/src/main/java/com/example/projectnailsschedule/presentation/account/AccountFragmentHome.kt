package com.example.projectnailsschedule.presentation.account

import android.app.Dialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
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

    private val loginSuccess = "Вход выполнен"
    private val loginError = "Не удалось выполнить вход"
    private val logoutSuccess = "Выход выполнен"
    private val logoutError = "Не удалось выполнить выход"

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
                logout()
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

        val login = view.findViewById<EditText>(R.id.login_et)
        val password = view.findViewById<EditText>(R.id.password_et)

        val loginButton = view.findViewById<Button>(R.id.login_button)
        val showPassword = view.findViewById<CheckBox>(R.id.show_password)

        showPassword.setOnCheckedChangeListener { _, isChecked ->
            togglePasswordVisibility(isChecked, password)
        }

        loginButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                dialog.dismiss()
                val success = viewModel.login(login.text.toString(), password.text.toString())

                if (success) {
                    showToast(loginSuccess)
                } else {
                    showToast(loginError)
                }
            }
        }

        view.findViewById<EditText>(R.id.login_et).requestFocus()

        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialog.show()
    }

    private fun showDialogRegistration() {
        val dialog = Dialog(requireContext())
        val view = layoutInflater.inflate(R.layout.dialog_registration, null)
        dialog.setContentView(view)

        val loginEt = view.findViewById<EditText>(R.id.login_et)
        val loginErrorTv = view.findViewById<TextView>(R.id.login_error)

        val emailEt = view.findViewById<EditText>(R.id.email_et)
        val emailErrorTv = view.findViewById<TextView>(R.id.email_error)

        val passwordEt = view.findViewById<EditText>(R.id.password_et)
        val passwordErrorTv = view.findViewById<TextView>(R.id.password_error)

        val passwordConfirmEt = view.findViewById<EditText>(R.id.password_confirm_et)
        val passwordConfirmErrorTv = view.findViewById<TextView>(R.id.password_confirm_error)

        val showPassword = view.findViewById<CheckBox>(R.id.show_password)
        val registerButton = view.findViewById<Button>(R.id.register_button)

        showPassword.setOnCheckedChangeListener { _, isChecked ->
            togglePasswordVisibility(isChecked, passwordEt)
            togglePasswordVisibility(isChecked, passwordConfirmEt)
        }

        registerButton.setOnClickListener {
            val loginError = viewModel.checkLogin(loginEt.text.toString())
            val emailError = viewModel.checkEmail(emailEt.text.toString())
            val passwordError = viewModel.checkPassword(passwordEt.text.toString())
            val passwordConfirmError = viewModel.checkPasswordConfirm(
                passwordEt.text.toString(),
                passwordConfirmEt.text.toString()
            )

            val handleError: (TextView, String?) -> Unit = { errorTv, error ->
                if (error != null) {
                    showError(errorTv, error)
                } else {
                    hideError(errorTv)
                }
            }

            handleError(loginErrorTv, loginError)
            handleError(emailErrorTv, emailError)
            handleError(passwordErrorTv, passwordError)
            handleError(passwordConfirmErrorTv, passwordConfirmError)

            if (loginError == null && emailError == null && passwordError == null && passwordConfirmError == null) {
                CoroutineScope(Dispatchers.Main).launch {
                    val status = viewModel.registration(
                        loginEt.text.toString(), emailEt.text.toString(), passwordEt.text.toString()
                    )
                    if (status == "Пользователь успешно зарегистрирован") {
                        showDialogMessage("Пользователь успешно зарегистрирован", dialog)
                    } else {
                        Toast.makeText(context, status, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialog.show()
    }

    private fun showDialogMessage(messageText: String, dialog: Dialog) {
        val dialogMessage = Dialog(requireContext())
        val view = layoutInflater.inflate(R.layout.dialog_message, null)
        dialogMessage.setContentView(view)

        val messageTv = view.findViewById<TextView>(R.id.message_tv)
        val buttonOk = view.findViewById<Button>(R.id.button_ok)

        messageTv.text = messageText

        buttonOk.setOnClickListener {
            dialogMessage.dismiss()
            dialog.dismiss()
        }

        dialogMessage.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialogMessage.show()
    }

    private suspend fun logout() {
        val success = viewModel.logout()
        if (success) {
            showToast(logoutSuccess)
        } else {
            showToast(logoutError)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun togglePasswordVisibility(isChecked: Boolean, password: EditText) {
        if (isChecked) {
            // Галочка установлена - показываем пароль
            password.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            // Галочка снята - скрываем пароль
            password.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        // Чтобы курсор остался в поле EditText после изменения inputType
        password.setSelection(password.text.length)
    }

    private fun showError(textView: TextView, errorMessage: String) {
        textView.text = errorMessage
        textView.visibility = View.VISIBLE
    }

    private fun hideError(textView: TextView) {
        textView.text = null
        textView.visibility = View.GONE
    }
}