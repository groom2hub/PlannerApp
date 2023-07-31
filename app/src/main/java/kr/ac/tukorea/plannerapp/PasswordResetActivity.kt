package kr.ac.tukorea.plannerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kr.ac.tukorea.plannerapp.databinding.ActivityPasswordResetBinding

class PasswordResetActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityPasswordResetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordResetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.sendButton.setOnClickListener {
            send()
        }
    }

    private fun send() {
        var email = binding.emailEdit.text.toString()

        if (email.isNotEmpty()) {
            Firebase.auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        sendToast("이메일을 보냈습니다.")
                        finish()
                    } else {
                        if (task.exception != null) {
                            Log.d("test", "${task.exception.toString()}")
                            if (task.exception.toString() == "com.google.firebase.auth.FirebaseAuthInvalidUserException: There is no user record corresponding to this identifier. The user may have been deleted.") {
                                sendToast("등록되지 않은 이메일입니다.")
                            }
                            else if (task.exception.toString() == "com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The email address is badly formatted.") {
                                sendToast("잘못된 형식의 이메일입니다.")
                            }
                        }
                    }
                }
        } else {
            sendToast("이메일을 입력하세요.")
        }
    }

    private fun sendToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}