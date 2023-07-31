package kr.ac.tukorea.plannerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kr.ac.tukorea.plannerapp.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.signUpButton.setOnClickListener {
            signUp()
        }

    }

    private fun signUp() {
        var email = binding.emailEdit.text.toString()
        var password = binding.passwordEdit.text.toString()
        var passwordCheck = binding.passwordCheckEdit.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty() && passwordCheck.isNotEmpty()) {
            if (password == passwordCheck) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            //val user = auth.currentUser
                            sendToast("회원가입에 성공했습니다.")
                            //Log.d("test", "${auth.currentUser}")
                            Firebase.auth.signOut()
                            finish()
                        } else {
                            if (task.exception != null) {
                                //Log.d("test", "${task.exception.toString()}")
                                if (task.exception.toString() == "com.google.firebase.auth.FirebaseAuthUserCollisionException: The email address is already in use by another account.") {
                                    sendToast("이미 가입된 이메일입니다.")
                                }
                                else if (task.exception.toString() == "com.google.firebase.auth.FirebaseAuthWeakPasswordException: The given password is invalid. [ Password should be at least 6 characters ]") {
                                    sendToast("비밀번호를 6자 이상 입력하세요.")
                                }
                                else if (task.exception.toString() == "com.google.firebase.auth.FirebaseAuthWeakPasswordException: The email address is badly formatted.") {
                                    sendToast("잘못된 형식의 이메일입니다.")
                                }
                                else {
                                    sendToast(task.exception.toString())
                                }
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