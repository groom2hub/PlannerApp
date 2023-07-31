package kr.ac.tukorea.plannerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kr.ac.tukorea.plannerapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val backKeyHandler: BackKeyHandler = BackKeyHandler(this) //BackKeyHandler 클래스 인스턴스 생성
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Firebase.auth.currentUser == null) {
            var loginIntent = Intent(this, MainActivity::class.java)
            startActivity(loginIntent)
        }

        binding.logoutButton.setOnClickListener {
            Firebase.auth.signOut()
            var loginIntent = Intent(this, MainActivity::class.java)
            startActivity(loginIntent)
        }
    }
    override fun onBackPressed() { //뒤로 가기 버튼을 두 번 눌러야 앱이 종료
        backKeyHandler.onBackPressed()
    }
}