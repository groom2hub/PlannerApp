package kr.ac.tukorea.plannerapp

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@RequiresApi(Build.VERSION_CODES.O)
class PlanRepository {
    private val databaseReference: DatabaseReference = Firebase.database.reference.child("users/user1/plans")

    companion object{
        private val INSTANCE = PlanRepository()

        fun getInstance(): PlanRepository {
            return INSTANCE
        }
    }
    fun deletePlan(planId: String) {
        databaseReference.child("plan/${planId}").removeValue()
    }

    fun modifyPlan(plan: Plan) {
        databaseReference.child("plan/${plan.id}").setValue(plan)
    }

    fun savePlan(plan: Plan) {
        val key = databaseReference.child("plan").push().key
        plan.id = key.toString()
        databaseReference.child("plan/${key}").setValue(plan)
    }

    fun findPlan(postId: String, callback: (Plan) -> Unit) {
        databaseReference
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
        databaseReference
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
        databaseReference
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
