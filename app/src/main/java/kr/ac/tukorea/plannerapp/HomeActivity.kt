package kr.ac.tukorea.plannerapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kr.ac.tukorea.plannerapp.databinding.ActivityHomeBinding

@RequiresApi(Build.VERSION_CODES.O)
open class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val backKeyHandler: BackKeyHandler = BackKeyHandler(this) //BackKeyHandler 클래스 인스턴스 생성
    private val db = Firebase.firestore
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
            binding.rvPlanList.adapter = planlistadapter
        }


        val planLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvPlanList.setHasFixedSize(true)
        binding.rvPlanList.layoutManager = planLayoutManager

        binding.btnHome.setOnClickListener {
            var homeIntent = Intent(this, HomeActivity::class.java)
            startActivity(homeIntent)
        }
        binding.btnPlan.setOnClickListener {
            var planIntent = Intent(this, CalendarActivity::class.java)
            startActivity(planIntent)
        }
        binding.btnMap.setOnClickListener {
            var mapIntent = Intent(this, HomeActivity::class.java)
            startActivity(mapIntent)
        }
        binding.btnProfile.setOnClickListener {
            var profileIntent = Intent(this, HomeActivity::class.java)
            startActivity(profileIntent)
        }


        if (Firebase.auth.currentUser == null) {
            var loginIntent = Intent(this, MainActivity::class.java)
            startActivity(loginIntent)
        }

        binding.logoutButton.setOnClickListener {
            Firebase.auth.signOut()
            var loginIntent = Intent(this, MainActivity::class.java)
            startActivity(loginIntent)
        }

        db.collection("users")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (i in task.result) {
                        if (i.id == Firebase.auth.currentUser!!.uid) {
                            val userName = i.data["name"]
                            binding.userNameText.text = userName.toString()
                        }
                    }
                }
            }
        }
    override fun onBackPressed() { //뒤로 가기 버튼을 두 번 눌러야 앱이 종료
        backKeyHandler.onBackPressed()
    }
}


