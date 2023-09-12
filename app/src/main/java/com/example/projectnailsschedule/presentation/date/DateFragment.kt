package com.example.projectnailsschedule.presentation.date

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import com.example.projectnailsschedule.databinding.FragmentDateBinding
import com.example.projectnailsschedule.domain.models.AppointmentModelDb
import com.example.projectnailsschedule.domain.models.DateParams
import com.example.projectnailsschedule.presentation.date.dateRecyclerView.DateAdapter
import com.example.projectnailsschedule.util.Util
import com.example.projectnailsschedule.util.rustore.RuStoreAd
import com.google.android.material.snackbar.Snackbar

class DateFragment : Fragment() {
    val log = this::class.simpleName

    private var _binding: FragmentDateBinding? = null
    private val binding get() = _binding!!
    private val bindingKey = "dateParams"
    private val bindingKeyAppointment = "appointmentParams"
    private val noAppointmentsTextTitle = "Нет записей"
    private val dateRecyclerViewSpanCount = 1

    private var appointmentList: List<AppointmentModelDb>? = null
    private var appointmentsRvAdapter: DateAdapter? = null

    private var dateParams: DateParams? = null
    private var dateViewModel: DateViewModel? = null
    private var appointmentsRv: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // create ViewModel object with Factory
        dateViewModel = ViewModelProvider(
            this,
            DateViewModelFactory(context)
        )[DateViewModel::class.java]

        // get dateParams from Bundle
        dateParams = arguments?.getParcelable(bindingKey)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDateBinding.inflate(inflater, container, false)

        // init widgets
        initWidgets()

        // init clickListeners
        initClickListeners()

        // set current date in viewModel
        dateViewModel!!.selectedDateParams.value = dateParams

        // get and appointments from date
        dateViewModel?.updateDateParams()

        // set observers
        setObservers()

        // swipe to delete
        swipeToDelete()

        return binding.root
    }

    private fun initWidgets() {
        appointmentsRv = binding.scheduleRecyclerView
    }

    private fun initClickListeners() {
        // add new appointment
        binding.addButton.setOnClickListener {
            val appointmentParams = AppointmentModelDb(
                date = Util().dateConverterNew(dateParams?.date.toString()),
                deleted = false
            )
            val bundle = Bundle()
            bundle.putParcelable(bindingKeyAppointment, appointmentParams)
            it.findNavController().navigate(R.id.action_dateFragment_to_appointmentFragment, bundle)
        }
    }

    private fun setObservers() {
        // dateParams observer
        dateViewModel?.selectedDateParams?.observe(viewLifecycleOwner) {
            appointmentList = dateViewModel!!.getDateAppointments().toList()
            if (it.appointmentCount == 0) {
                binding.fragmentDateTitle.text = noAppointmentsTextTitle
            } else {
                binding.fragmentDateTitle.text = "Всего записей: ${it.appointmentCount}"
            }
            binding.fragmentDateDate.text = it.date?.format(Util().formatter)
            inflateAppointmentsRV(it)
        }
    }

    private fun inflateAppointmentsRV(selectedDate: DateParams) {
        // create adapter
        appointmentsRvAdapter = DateAdapter(
            appointmentsCount = selectedDate.appointmentCount!!,
            appointmentsList = appointmentList!!,
            context = requireContext()
        )

        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(activity, dateRecyclerViewSpanCount)

        appointmentsRv?.layoutManager = layoutManager
        appointmentsRv?.adapter = appointmentsRvAdapter
        appointmentsRv?.scheduleLayoutAnimation()

        // set clickListener on dateRV
        appointmentsRvAdapter!!.setOnItemClickListener(object : DateAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                // edit selected appointment
                val bundle = Bundle()
                val appointmentModelDb = AppointmentModelDb(
                    _id = appointmentList?.get(position)?._id,
                    date = appointmentList?.get(position)?.date,
                    name = appointmentList?.get(position)?.name,
                    time = appointmentList?.get(position)?.time,
                    procedure = appointmentList?.get(position)?.procedure,
                    vk = appointmentList?.get(position)?.vk,
                    telegram = appointmentList?.get(position)?.telegram,
                    instagram = appointmentList?.get(position)?.instagram,
                    whatsapp = appointmentList?.get(position)?.whatsapp,
                    phone = appointmentList?.get(position)?.phone,
                    notes = appointmentList?.get(position)?.notes,
                    deleted = appointmentList?.get(position)!!.deleted
                )
                val navController = findNavController()
                bundle.putParcelable(bindingKeyAppointment, appointmentModelDb)
                navController.navigate(R.id.action_dateFragment_to_appointmentFragment, bundle)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        // hide keyboard
        Util().hideKeyboard(requireActivity())

        RuStoreAd().banner(requireContext(), binding.root)
    }

    private fun swipeToDelete() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // this method is called when the item is moved.
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // this method is called when we swipe our item to left direction.
                // on below line we are getting the item at a particular position.
                val deleteAppointmentModelDb: AppointmentModelDb = appointmentList!![viewHolder.adapterPosition]
                val position = viewHolder.adapterPosition

                // delete client from Db
                dateViewModel?.deleteAppointment(deleteAppointmentModelDb)

                appointmentsRvAdapter?.notifyItemRemoved(position)

                // show Snackbar
                Snackbar.make(
                    appointmentsRv!!,
                    "Удалена запись: " + deleteAppointmentModelDb.name,
                    Snackbar.LENGTH_LONG
                ).setBackgroundTint(resources.getColor(R.color.yellow))
                    .setActionTextColor(resources.getColor(R.color.black))
                    .setTextColor(resources.getColor(R.color.black))
                    .setAction(
                        "Отмена"
                    ) {
                        // adding on click listener to our action of snack bar.
                        // below line is to add our item to array list with a position.
                        dateViewModel?.saveAppointment(deleteAppointmentModelDb)

                        // below line is to notify item is
                        // added to our adapter class.
                        appointmentsRvAdapter?.notifyDataSetChanged()
                    }.show()
            }

            override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
                return super.getSwipeEscapeVelocity(defaultValue) * 10
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
            ) {
                val deleteIcon: Drawable? =
                    ContextCompat.getDrawable(requireContext(), R.drawable.baseline_delete_24)!!

                val itemView = viewHolder.itemView
                val iconMarginVertical = (viewHolder.itemView.height - deleteIcon!!.intrinsicHeight) / 2

                val colorDrawableBackground = ColorDrawable(Color.parseColor("#ffcce6"))

                val left = itemView.right - deleteIcon.intrinsicWidth - deleteIcon.intrinsicWidth // 882
                val right = itemView.right - deleteIcon.intrinsicWidth // 1014
                val top = itemView.top + iconMarginVertical
                val bottom = itemView.bottom - iconMarginVertical

                colorDrawableBackground.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                deleteIcon.setBounds(left, top, right, bottom)

                deleteIcon.level = 0

                colorDrawableBackground.draw(c)
                c.save()

                if (dX > 0)
                    c.clipRect(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
                else
                    c.clipRect(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)

                deleteIcon.draw(c)
                c.restore()

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }).attachToRecyclerView(appointmentsRv)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

