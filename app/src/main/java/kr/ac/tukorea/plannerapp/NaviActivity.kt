package kr.ac.tukorea.plannerapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kr.ac.tukorea.plannerapp.databinding.ActivityNaviBinding


private const val TAG_HOME = "home_fragment"
private const val TAG_PLAN = "plan_fragment"
private const val TAG_MAP = "map_fragment"
private const val TAG_PROFILE = "profile_fragment"

@RequiresApi(Build.VERSION_CODES.O)
class NaviActivity : AppCompatActivity() {

    private lateinit var binding : ActivityNaviBinding
    private val backKeyHandler: BackKeyHandler = BackKeyHandler(this) //BackKeyHandler 클래스 인스턴스 생성

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNaviBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setFragment(TAG_HOME, HomeFragment())

        binding.navigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.homeFragment -> setFragment(TAG_HOME, HomeFragment())
                R.id.planFragment -> setFragment(TAG_PLAN, PlanFragment())
                R.id.mapFragment -> setFragment(TAG_MAP, MapFragment())
                R.id.profileFragment-> setFragment(TAG_PROFILE, ProfileFragment())
            }
            true
        }
    }

    private fun setFragment(tag: String, fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val fragTransaction = manager.beginTransaction()

        if (manager.findFragmentByTag(tag) == null){
            fragTransaction.add(R.id.mainFrameLayout, fragment, tag)
        }

        val home = manager.findFragmentByTag(TAG_HOME)
        val plan = manager.findFragmentByTag(TAG_PLAN)
        val map = manager.findFragmentByTag(TAG_MAP)
        val profile = manager.findFragmentByTag(TAG_PROFILE)

        if (home != null){
            fragTransaction.hide(home)
        }

        if (plan != null){
            fragTransaction.hide(plan)
        }

        if (map != null){
            fragTransaction.hide(map)
        }

        if (profile != null) {
            fragTransaction.hide(profile)
        }

        if (tag == TAG_HOME) {
            if (home!=null){
                fragTransaction.show(home)
            }
        }

        else if (tag == TAG_PLAN) {
            if (plan != null) {
                fragTransaction.show(plan)
            }
        }

        else if (tag == TAG_MAP){
            if (map != null){
                fragTransaction.show(map)
            }
        }

        else if (tag == TAG_PROFILE){
            if (profile != null){
                fragTransaction.show(profile)
            }
        }

        fragTransaction.commitAllowingStateLoss()
    }

    override fun onBackPressed() { //뒤로 가기 버튼을 두 번 눌러야 앱이 종료
        backKeyHandler.onBackPressed()
    }
}