package kr.ac.tukorea.plannerapp

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

@RequiresApi(Build.VERSION_CODES.O)
class PlanViewModel: ViewModel(){
    private val planRepository = PlanRepository.getInstance()
    private val _plan = MutableLiveData<Plan>()
    val plan: LiveData<Plan> = _plan
    private val _plans = MutableLiveData<List<Plan>>()
    val plans: LiveData<List<Plan>> = _plans
    private val _plans1 = MutableLiveData<List<Plan>>()
    val plan1: LiveData<List<Plan>> = _plans1

    fun findPlan(planId : String) {
        planRepository.findPlan(planId) {
            _plan.value = it
        }
    }

    fun findAllPlans() {
        planRepository.findAllPlans { plans ->
            _plans.value = plans
        }
    }
}
