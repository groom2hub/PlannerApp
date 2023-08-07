package kr.ac.tukorea.plannerapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kr.ac.tukorea.plannerapp.databinding.FragmentPlanBinding
import java.text.DecimalFormat
import java.time.LocalDate

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@RequiresApi(Build.VERSION_CODES.O)
class PlanFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private var dateFormat = DecimalFormat("00")

    private var hBinding: FragmentPlanBinding? = null
    private val binding get() = hBinding!!
    //private lateinit var bindingAddPlanActivity: ActivityAddPlanBinding
    private val planRepository: PlanRepository = PlanRepository.getInstance()
    private lateinit var planViewModel: PlanViewModel
    private lateinit var planlistadapter: PlanListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hBinding = FragmentPlanBinding.inflate(inflater)
        var date: String = "${LocalDate.now()}"
        var days = "${LocalDate.now().monthValue} 월 ${LocalDate.now().dayOfMonth} 일"
        var time: String = "09:00"

        planViewModel = ViewModelProvider(this, ViewModelFactory())[PlanViewModel::class.java]
        planViewModel.findPlansByDate(date)

        planViewModel.plans.observe(/*this*/viewLifecycleOwner) { plans ->
            planlistadapter = PlanListAdapter(plans, /*this*/requireContext())
            binding.rvPlanList.adapter = planlistadapter
        }
        var planLayoutManager = LinearLayoutManager(/*this*/requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvPlanList.setHasFixedSize(true)
        binding.rvPlanList.layoutManager = planLayoutManager

        binding.calendarView.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
            date = "${year}-${dateFormat.format(month + 1)}-${dateFormat.format(dayOfMonth)}"
            days = "${month + 1} 월 ${dayOfMonth} 일"
            planViewModel = ViewModelProvider(this, ViewModelFactory())[PlanViewModel::class.java]
            planViewModel.findPlansByDate(date)

            planViewModel.plans.observe(/*this*/viewLifecycleOwner) { plans ->
                planlistadapter = PlanListAdapter(plans, /*this*/requireContext())
                binding.rvPlanList.adapter = planlistadapter
            }
        }

        binding.btnAddPlan.setOnClickListener {
            activity?.let{
                val intent = Intent(it, AddPlanActivity::class.java)
                intent.putExtra("날짜", days)
                it.startActivity(intent)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        hBinding = null
        super.onDestroyView()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PlanFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}