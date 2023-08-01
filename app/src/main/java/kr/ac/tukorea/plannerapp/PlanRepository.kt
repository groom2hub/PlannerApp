package kr.ac.tukorea.plannerapp

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.gms.tasks.Task
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

    fun savePlan(callback: (DatabaseReference) -> Task<Void>) {
        callback(databaseReference.child("plan").push())
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
                    callback(planList)
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
}
