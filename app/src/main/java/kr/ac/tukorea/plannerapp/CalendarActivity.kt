package kr.ac.tukorea.plannerapp

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kr.ac.tukorea.plannerapp.databinding.ActivityAddPlanBinding
import kr.ac.tukorea.plannerapp.databinding.ActivityCalendarBinding
import java.time.LocalDate
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class CalendarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalendarBinding
    private lateinit var bindingAddPlanActivity: ActivityAddPlanBinding
    private lateinit var planViewModel: PlanViewModel
    private lateinit var planlistadapter: PlanListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        bindingAddPlanActivity = ActivityAddPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var date: String = "${LocalDate.now()}"
        var time: String = "09:00"

        planViewModel = ViewModelProvider(this, ViewModelFactory())[PlanViewModel::class.java]
        planViewModel.findPlansByDate(date)

        planViewModel.plans.observe(this) { plans ->
            planlistadapter = PlanListAdapter(plans, this)
            binding.rvPlanList.adapter = planlistadapter
        }
        var planLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvPlanList.setHasFixedSize(true)
        binding.rvPlanList.layoutManager = planLayoutManager

        binding.calendarView.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
            date = "${year}-${month + 1}-${dayOfMonth}"
            planViewModel = ViewModelProvider(this, ViewModelFactory())[PlanViewModel::class.java]
            planViewModel.findPlansByDate(date)

            planViewModel.plans.observe(this) { plans ->
                planlistadapter = PlanListAdapter(plans, this)
                binding.rvPlanList.adapter = planlistadapter
            }
        }

        binding.btnAddPlan.setOnClickListener {
            val dialog = AddPlanDialog(this)
            dialog.show(supportFragmentManager, "새로운 일정")
        }
    }
}