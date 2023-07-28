package kr.ac.tukorea.plannerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Firebase.auth.currentUser == null) {
            var loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }

        var logoutButton = findViewById<Button>(R.id.logoutButton)

        logoutButton.setOnClickListener {
            Firebase.auth.signOut()
            var loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
    }
}