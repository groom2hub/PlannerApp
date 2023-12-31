package kr.ac.tukorea.plannerapp

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kr.ac.tukorea.plannerapp.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignUpBinding
    private val db = Firebase.firestore

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
        var name = binding.nameEdit.text.toString()
        var email = binding.emailEdit.text.toString()
        var password = binding.passwordEdit.text.toString()
        var passwordCheck = binding.passwordCheckEdit.text.toString()

        if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && passwordCheck.isNotEmpty()) {
            if (password == passwordCheck) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            sendToast("회원가입에 성공했습니다.")
                            val memberInfo = MemberInfo(name, email)
                            if (user != null) {
                                db.collection("users").document(user.uid).set(memberInfo)
                                    .addOnSuccessListener { Log.d("test", "DocumentSnapshot successfully written!") }
                                    .addOnFailureListener { Log.d("test", "Error writing document") }
                            }
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

    data class MemberInfo(
        val name: String? = null,
        val email: String? = null,
    )

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean { //키보드 이외의 화면 터치 시 키보드 비활성화
        val focusView: View? = currentFocus
        if (focusView != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            val x = ev.x.toInt()
            val y = ev.y.toInt()
            if (!rect.contains(x, y)) {
                val imm: InputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(focusView.windowToken, 0)
                focusView.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}