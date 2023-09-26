package com.example.projectnailsschedule.presentation.appointment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.databinding.FragmentAppointmentBinding
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.models.ClientModelDb
import com.example.projectnailsschedule.domain.models.ProcedureModelDb
import com.example.projectnailsschedule.presentation.appointment.selectClient.SelectClientFragment
import com.example.projectnailsschedule.presentation.appointment.selectProcedure.SelectProcedureFragment
import com.example.projectnailsschedule.util.Util
import java.util.*

class AppointmentFragment : Fragment() {
    val log = this::class.simpleName
    private val bindingKey = "appointmentParams"

    private var appointmentViewModel: AppointmentViewModel? = null
    private var _binding: FragmentAppointmentBinding? = null
    private val binding get() = _binding!!

    private var appointmentParams: AppointmentModelDb? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // create ViewModel object with Factory
        appointmentViewModel = ViewModelProvider(
            this,
            AppointmentViewModelFactory(context)
        )[AppointmentViewModel::class.java]

        // get appointmentParams from arguments
        appointmentParams = arguments?.getParcelable(bindingKey)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        // set binding
        _binding = FragmentAppointmentBinding.inflate(inflater, container, false)

        // set ClickListeners
        setClickListeners()

        // set current appointmentParams form DateFragment binding object
        setAppointmentCurrentParams()

        // set title text
        setTitle()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<ClientModelDb>("client")
            ?.observe(viewLifecycleOwner) { result ->
                // здесь обрабатываем результат после выбора клиента
                with(binding) {
                    nameEt.setText(result.name)
                    phoneEt.setText(result.phone)
                    clientVkLinkEt.setText(result.vk)
                    clientTelegramLinkEt.setText(result.telegram)
                    clientInstagramLinkEt.setText(result.instagram)
                    clientWhatsappLinkEt.setText(result.whatsapp)
                    Util().animateEditTexts(
                        nameEt,
                        phoneEt,
                        clientVkLinkEt,
                        clientTelegramLinkEt,
                        clientInstagramLinkEt,
                        clientWhatsappLinkEt
                    )
                }
            }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<ProcedureModelDb>("procedure")
            ?.observe(viewLifecycleOwner) { result ->
                with(binding) {
                    procedureEt.setText(result.procedureName)
                    Util().animateEditTexts(procedureEt)
                }
            }
    }

    private fun setClickListeners() {
        // Set ClickListener on save_changes_button
        binding.saveButton.setOnClickListener {
            if (appointmentParams?._id == null) {
                // no _id - add new Appointment
                createAppointment()
            } else {
                // _id - edit Appointment
                editAppointment()
            }
        }

        // set ClickListener on cancel_button
        binding.cancelButton.setOnClickListener {
            cancelButton()
        }

        // set ClickListener on day_edit_text
        binding.dayEditText.setOnClickListener {
            setDatePicker()
        }

        // set ClickListener on time_edit_text
        binding.timeEditText.setOnClickListener {
            setTimePicker()
        }

        binding.procedureSelectButton.setOnClickListener {
            val dialogFragment = SelectProcedureFragment()
            dialogFragment.show(parentFragmentManager, "SelectProcedureFragment")
        }

        // set phone as hyperlink
        binding.callClientButton.setOnClickListener {
            val phone = binding.phoneEt.text.toString()
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
            startActivity(intent)
        }

        // set call client button
        binding.selectClientButton.setOnClickListener {
            val dialogFragment = SelectClientFragment()
            dialogFragment.show(parentFragmentManager, "SelectClientFragment")
        }

        initSocClickListeners()
    }

    private fun setTitle() {
        val title: TextView = binding.fragmentAppointmentTitle
        val titleDate: TextView = binding.fragmentAppointmentDate

        if (appointmentParams?._id == null) {
            // no _id - add new Appointment
            title.text = getString(R.string.new_appointment_text)
        } else {
            // _id - edit Appointment
            title.text = getString(R.string.change_appointment_text)
        }
        titleDate.text = appointmentParams?.date
    }

    private fun createAppointment() {
        /** Send params to ViewModel */

        // create appointmentParams object
        with(binding) {
            val appointmentModelDb = AppointmentModelDb(
                _id = null,
                date = dayEditText.text.toString(),
                name = nameEt.text.toString(),
                time = timeEditText.text.toString(),
                procedure = procedureEt.text.toString(),
                phone = phoneEt.text.toString(),
                vk = clientVkLinkEt.text.toString(),
                telegram = clientTelegramLinkEt.text.toString(),
                instagram = clientInstagramLinkEt.text.toString(),
                whatsapp = clientWhatsappLinkEt.text.toString(),
                notes = notesEt.text.toString(),
                deleted = false
            )
            appointmentViewModel?.createAppointment(appointmentModelDb)

            val toast: Toast = Toast.makeText(
                context,
                "${appointmentModelDb.date}\n${getString(R.string.toast_created)}",
                Toast.LENGTH_LONG
            )
            toast.show()
        }

        // Return to previous screen
        findNavController().popBackStack()
    }

    private fun editAppointment() {
        /** Send params to ViewModel */

        // create appointmentParams object
        with(binding) {
            val appointmentModelDb = AppointmentModelDb(
                _id = appointmentParams?._id,
                date = dayEditText.text.toString(),
                name = nameEt.text.toString(),
                time = timeEditText.text.toString(),
                procedure = procedureEt.text.toString(),
                phone = phoneEt.text.toString(),
                vk = clientVkLinkEt.text.toString(),
                telegram = clientTelegramLinkEt.text.toString(),
                instagram = clientInstagramLinkEt.text.toString(),
                whatsapp = clientWhatsappLinkEt.text.toString(),
                notes = notesEt.text.toString(),
                deleted = false
            )

            // send to AppointmentViewModel
            appointmentViewModel?.editAppointment(appointmentModelDb)

            Toast.makeText(context, getString(R.string.toast_edited), Toast.LENGTH_LONG).show()

            findNavController().popBackStack()
        }
    }

    private fun setAppointmentCurrentParams() {
        // set current appointmentParams from DateFragment binding object

        with(binding) {
            dayEditText.text = appointmentParams?.date
            timeEditText.text = appointmentParams?.time
            procedureEt.setText(appointmentParams?.procedure)
            nameEt.setText(appointmentParams?.name)
            phoneEt.setText(appointmentParams?.phone)
            clientVkLinkEt.setText(appointmentParams?.vk)
            clientTelegramLinkEt.setText(appointmentParams?.telegram)
            clientInstagramLinkEt.setText(appointmentParams?.instagram)
            clientWhatsappLinkEt.setText(appointmentParams?.whatsapp)
            notesEt.setText(appointmentParams?.notes)
        }
    }

    private fun cancelButton() {
        // Cancel button
        findNavController().popBackStack()
    }

    private fun setDatePicker() {
        // set datePicker to select date field
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(), { _, pickedYear, pickedMonth, pickedDay ->
                val date = Util().dateConverter("$pickedDay.${pickedMonth + 1}.$pickedYear")
                binding.dayEditText.text = date
            }, year, month, day
        )
        datePickerDialog.show()
    }

    private fun setTimePicker() {
        // set time Picker to select time field
        val calendar = Calendar.getInstance()
        val mTimePicker: TimePickerDialog
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        mTimePicker = TimePickerDialog(
            context, { _, pickedHour, pickedMinute ->
                val time = String.format("%02d:%02d", pickedHour, pickedMinute)
                binding.timeEditText.text = time
            }, hour, minute, true
        )
        mTimePicker.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initSocClickListeners() {
        with(binding) {
            vkLogoImageButton.setOnClickListener {
                startVk(clientVkLinkEt.text.toString().trim())
            }

            telegramLogoImageButton.setOnClickListener {
                startTelegram(clientTelegramLinkEt.text.toString().trim())
            }

            instagramLogoImageButton.setOnClickListener {
                startInstagram(clientInstagramLinkEt.text.toString().trim())
            }

            whatsappLogoImageButton.setOnClickListener {
                startWhatsapp(clientWhatsappLinkEt.text.toString().trim())
            }
        }
    }

    private fun startVk(uri: String) {
        val uri = uri.replace("https://vk.com/", "")
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/$uri"))
            requireContext().startActivity(intent)
        } catch (e: Exception) {
            try {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/$uri"))
                requireContext().startActivity(browserIntent)
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.unknown_error),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun startTelegram(uri: String) {
        val uri = uri.replace("https://t.me/", "")
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=$uri"))
            requireContext().startActivity(intent)
        } catch (e: Exception) {
            try {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/$uri"))
                requireContext().startActivity(browserIntent)
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.unknown_error),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun startInstagram(uri: String) {
        val regex = Regex("https:\\/\\/instagram\\.com\\/([^?\\/]+)(?:\\?igshid=.*)?|([\\w.-]+)")
        val matchResult = regex.find(uri)
        var username = matchResult?.groupValues?.getOrNull(1)
        if (username == "") {
            username = matchResult?.groupValues?.getOrNull(2)
        }

        try {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/$username"))
            intent.setPackage("com.instagram.android")
            requireContext().startActivity(intent)
        } catch (e: Exception) {
            try {
                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/$username"))
                requireContext().startActivity(browserIntent)
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.unknown_error),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun startWhatsapp(uri: String) {
        try {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=$uri"))
            requireContext().startActivity(intent)
        } catch (e: Exception) {
            try {
                val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://web.whatsapp.com/send?phone=$uri")
                )
                requireContext().startActivity(browserIntent)
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.unknown_error),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}