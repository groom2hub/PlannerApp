package kr.ac.tukorea.plannerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kr.ac.tukorea.plannerapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
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
}