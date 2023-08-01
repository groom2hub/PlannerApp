package kr.ac.tukorea.plannerapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kr.ac.tukorea.plannerapp.databinding.ActivityMainBinding


@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private val backKeyHandler: BackKeyHandler = BackKeyHandler(this) //BackKeyHandler 클래스 인스턴스 생성

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.signUpText.setOnClickListener {
            var signUpIntent = Intent(this, SignUpActivity::class.java)
            startActivity(signUpIntent)
        }

        binding.passResetText.setOnClickListener {
            var passResetIntent = Intent(this, PasswordResetActivity::class.java)
            startActivity(passResetIntent)
        }

        auth = Firebase.auth

        binding.loginButton.setOnClickListener {
            signIn()
        }
    }
    public override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
        android.os.Process.killProcess(android.os.Process.myPid())
        System.exit(1)
    }

    private fun signIn() {
        var email = binding.emailLogin.text.toString()
        var password = binding.passwordLogin.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        //val user = auth.currentUser
                        sendToast("로그인에 성공했습니다.")
                        var mainIntent = Intent(this, HomeActivity::class.java)
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(mainIntent)
                    } else {
                        if (task.exception != null) {
                            //Log.d("test", "${task.exception.toString()}")
                            if (task.exception.toString() == "com.google.firebase.auth.FirebaseAuthInvalidUserException: There is no user record corresponding to this identifier. The user may have been deleted.") {
                                sendToast("잘못된 이메일입니다.")
                            }
                            else if (task.exception.toString() == "com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The password is invalid or the user does not have a password.") {
                                sendToast("잘못된 비밀번호입니다.")
                            }
                            else {
                                sendToast(task.exception.toString())
                            }
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