package kr.ac.tukorea.plannerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var signupText = findViewById<TextView>(R.id.signUpText)


        signupText.setOnClickListener {
            var signUpIntent = Intent(this, SignUpActivity::class.java)
            startActivity(signUpIntent)
        }

        auth = Firebase.auth

        var login = findViewById<AppCompatButton>(R.id.loginButton)

        login.setOnClickListener {
            signIn()
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

    public override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
        android.os.Process.killProcess(android.os.Process.myPid())
        System.exit(1)
    }

    private fun signIn() {
        var email = findViewById<EditText>(R.id.emailLogin).text.toString()
        var password = findViewById<EditText>(R.id.passwordLogin).text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        sendToast("로그인에 성공했습니다.")
                        var mainIntent = Intent(this, MainActivity::class.java)
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(mainIntent)
                    } else {
                        if (task.exception != null) {
                            sendToast(task.exception.toString())
                            if (task.exception.toString() == "")
                        }
                    }
                }
        } else {
            sendToast("이메일 또는 비밀번호를 입력하세요.")
        }
    }

    private fun sendToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}