package kr.ac.tukorea.plannerapp

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kr.ac.tukorea.plannerapp.databinding.ActivityHomeBinding

@RequiresApi(Build.VERSION_CODES.O)
open class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var planViewModel: PlanViewModel
    private lateinit var planlistadapter: PlanListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        planViewModel = ViewModelProvider(this, ViewModelFactory())[PlanViewModel::class.java]
        planViewModel.findAllPlans()

        planViewModel.plans.observe(this) { plans ->
            planlistadapter = PlanListAdapter(plans, this)
            binding.rvPlan.adapter = planlistadapter
        }

        val planLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvPlan.setHasFixedSize(true)
        binding.rvPlan.layoutManager = planLayoutManager

    }
}

