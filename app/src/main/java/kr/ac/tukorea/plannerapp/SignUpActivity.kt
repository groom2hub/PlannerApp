package kr.ac.tukorea.plannerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = Firebase.auth

        var signupButton = findViewById<AppCompatButton>(R.id.signUpButton)

        signupButton.setOnClickListener {
            signUp()
        }

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            onRestart()
        }
    }

    private fun signUp() {
        var email = findViewById<EditText>(R.id.emailEdit).text.toString()
        var password = findViewById<EditText>(R.id.passwordEdit).text.toString()
        var passwordCheck = findViewById<EditText>(R.id.passwordCheckEdit).text.toString()

        if (email.isNotEmpty() && password.isNotEmpty() && passwordCheck.isNotEmpty()) {
            if (password == passwordCheck) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            sendToast("회원가입에 성공했습니다.")
                            finish()
                        } else {
                            if (task.exception != null) {
                                sendToast(task.exception.toString())
                            }
                        }
                    }
            } else {
                sendToast("비밀번호가 일치하지 않습니다.")
            }
        } else {
            sendToast("이메일 또는 비밀번호를 입력하세요.")
        }
    }

    private fun sendToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}