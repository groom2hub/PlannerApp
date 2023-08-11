package kr.ac.tukorea.plannerapp

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@RequiresApi(Build.VERSION_CODES.O)
class PlanRepository(uId: String) {

    private var planPathString = "users/${uId}/plans"
    private var planDatabaseReference: DatabaseReference = Firebase.database.reference.child(planPathString)

    companion object {

        private var INSTANCE: PlanRepository? = null

        fun getInstance(uId: String): PlanRepository {
            INSTANCE = PlanRepository(uId)
            return INSTANCE!!
        }
    }
    fun deletePlan(planId: String) {
        databaseReference.child("plan/${planId}").removeValue()
    }

    fun modifyPlan(plan: Plan) {
        databaseReference.child("plan/${plan.id}").setValue(plan)
    }

    fun deletePlan(planId: String) {
        planDatabaseReference.child("plan/${planId}").removeValue()
    }

    fun modifyPlan(plan: Plan) {
        planDatabaseReference.child("plan/${plan.id}").setValue(plan)
    }

    fun savePlan(plan: Plan) {
        val key = planDatabaseReference.child("plan").push().key
        plan.id = key.toString()
        planDatabaseReference.child("plan/${key}").setValue(plan)
    }

    fun findPlan(postId: String, callback: (Plan) -> Unit) {
        planDatabaseReference
            .child("plan")
            .child(postId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val plan = snapshot.getValue(Plan::class.java)!!
                    callback(plan)
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun findAllPlans(callback: (List<Plan>) -> Unit) {
        planDatabaseReference
            .child("plan")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val planList = mutableListOf<Plan>()
                    for (data in snapshot.children) {
                        val plan = data.getValue(Plan::class.java)
                        if (plan != null){
                            plan.id = data.key.toString()
                            planList.add(plan)
                        }
                    }
                    callback(planList.sortedBy { it.dateStart })
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun findPlansByDate(date: String, callback: (List<Plan>) -> Unit) {
        planDatabaseReference
            .child("plan")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val planList = mutableListOf<Plan>()
                    for (data in snapshot.children) {
                        val plan = data.getValue(Plan::class.java)
                        if (plan?.dateStart == date){
                            plan.id = data.key.toString()
                            planList.add(plan)
                        }
                    }
                    callback(planList)
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
}
