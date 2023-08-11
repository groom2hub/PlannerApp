package kr.ac.tukorea.plannerapp

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kr.ac.tukorea.plannerapp.databinding.FragmentPlanBinding
import java.text.DecimalFormat
import java.time.LocalDate

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@RequiresApi(Build.VERSION_CODES.O)
class PlanFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    var date: String = "${LocalDate.now()}"
    private var dateFormat = DecimalFormat("00")

    private var hBinding: FragmentPlanBinding? = null
    private val binding get() = hBinding!!
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
        var days = "${LocalDate.now().monthValue} 월 ${LocalDate.now().dayOfMonth} 일"
        var time: String = "09:00"

        val mDBReference = FirebaseDatabase.getInstance().reference
        mDBReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                planViewModel = ViewModelProvider(this@PlanFragment, ViewModelFactory())[PlanViewModel::class.java]
                planViewModel.findPlansByDate(date)

                planViewModel.plans.observe(viewLifecycleOwner) { plans ->
                    planlistadapter = PlanListAdapter(plans, requireContext())
                    binding.rvPlanList.adapter = planlistadapter
                }

                val planLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                binding.rvPlanList.setHasFixedSize(true)
                binding.rvPlanList.layoutManager = planLayoutManager
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        binding.calendarView.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
            date = "${year}-${dateFormat.format(month + 1)}-${dateFormat.format(dayOfMonth)}"
            days = "${month + 1} 월 ${dayOfMonth} 일"
            planViewModel.findPlansByDate(date)
        }

        binding.btnAddPlan.setOnClickListener {
            activity?.let {
                val intent = Intent(it, AddPlanActivity::class.java)
                intent.apply {
                    this.putExtra("날짜", date)
                }
                startActivityForResult(intent, 0)
            }
        }
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            planViewModel.findPlansByDate(data?.getStringExtra("날짜").toString())
        }
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