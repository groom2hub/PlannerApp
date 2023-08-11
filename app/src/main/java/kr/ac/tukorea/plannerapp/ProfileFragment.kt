package kr.ac.tukorea.plannerapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kr.ac.tukorea.plannerapp.databinding.FragmentProfileBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private var hBinding: FragmentProfileBinding? = null
    private val binding get() = hBinding!!
    private val db = Firebase.firestore
    private val editTextId = 1000006
    private val editTextPasswordId = 1000007

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        hBinding = FragmentProfileBinding.inflate(inflater)
        lateinit var uId: String

        db.collection("users")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (i in task.result) {
                        if (i.id == Firebase.auth.currentUser?.uid) {
                            val userName = i.data["name"]
                            val userId = i.id
                            uId = userId
                            val userEmail = i.data["email"]
                            binding.userName.text = userName.toString()
                            binding.userId.text = userId
                            binding.userEmail.text = userEmail.toString()
                        }
                    }
                }
            }

        binding.btnModifyName.setOnClickListener {
            var builder = AlertDialog.Builder(requireContext())
            builder.setTitle("이름 변경")

            val editText = EditText(requireContext())
            editText.id = editTextId
            editText.hint = "변경할 이름"
            editText.isSingleLine = true

            builder.setView(editText)

            val alertDialog = builder.create()

            alertDialog.apply {
                setButton(AlertDialog.BUTTON_POSITIVE, "확인") { _, _ ->
                }

                setButton(AlertDialog.BUTTON_NEGATIVE, "취소") { dialog, _ ->
                    dialog.dismiss()
                }

                show()

                getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    val newName = editText.text.toString()
                    if (newName.isNotEmpty()) {
                        if (newName.length > 8 || newName.length < 2) {
                            sendToast("2자 이상 8자 이하로 입력하세요.")
                        }
                        else {
                            sendToast("이름을 변경했습니다.")
                            val userData = db.collection("users").document(uId)
                            userData.update("name", newName)
                            binding.userName.text = newName
                            dismiss()
                        }
                    } else {
                        sendToast("이름을 입력하세요.")
                    }
                }
            }
        }

        binding.btnDeleteInfo.setOnClickListener {
            var builder = AlertDialog.Builder(requireContext())
            builder.setTitle("계정을 탈퇴하시겠습니까?")

            val editTextPassword = EditText(requireContext())
            editTextPassword.id = editTextPasswordId
            editTextPassword.hint = "비밀번호 입력"
            editTextPassword.isSingleLine = true
            editTextPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

            builder.setView(editTextPassword)

            val alertDialog = builder.create()

            alertDialog.apply {
                setButton(AlertDialog.BUTTON_POSITIVE, "확인") { _, _ ->
                }

                setButton(AlertDialog.BUTTON_NEGATIVE, "취소") { dialog, _ ->
                    dialog.dismiss()
                }

                show()

                getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    val user = Firebase.auth.currentUser

                    if (user != null) {
                        val enteredPassword = editTextPassword.text.toString() // 사용자가 입력한 비밀번호 가져오기

                        if (enteredPassword.isNotEmpty()) {
                            val credential =
                                EmailAuthProvider.getCredential(user.email!!, enteredPassword)

                            // 사용자 재인증
                            user.reauthenticate(credential)
                                .addOnCompleteListener { reauthTask ->
                                    if (reauthTask.isSuccessful) {
                                        val db = FirebaseFirestore.getInstance()
                                        val userDocRef = db.collection("users").document(user.uid)

                                        userDocRef.delete()
                                            .addOnSuccessListener {
                                                user.delete()
                                                    .addOnCompleteListener { deleteTask ->
                                                        if (deleteTask.isSuccessful) {
                                                            sendToast("계정 탈퇴가 완료되었습니다.")
                                                            val userDatabaseReference: DatabaseReference = Firebase.database.reference.child("users")
                                                            userDatabaseReference.child(user.uid).removeValue()
                                                            Firebase.auth.signOut()
                                                            activity?.let {
                                                                val intent = Intent (it, LoginActivity::class.java)
                                                                it.startActivity(intent)
                                                            }
                                                        } else {
                                                            sendToast("계정 탈퇴 중 오류가 발생했습니다.")
                                                        }
                                                    }
                                            }
                                            .addOnFailureListener {
                                                sendToast("데이터 삭제 중 오류가 발생했습니다.")
                                            }
                                    } else {
                                        val error = reauthTask.exception?.message
                                        if (error == "The password is invalid or the user does not have a password.") {
                                            sendToast("비밀번호가 일치하지 않습니다.")
                                        } else if (error == "We have blocked all requests from this device due to unusual activity. Try again later. [ Access to this account has been temporarily disabled due to many failed login attempts. You can immediately restore it by resetting your password or you can try again later. ]") {
                                            sendToast("잠시후에 다시 시도해 주세요.")
                                        }
                                    }
                                }
                        } else {
                            sendToast("비밀번호를 입력하세요.")
                        }
                    } else {
                        sendToast("로그인된 사용자가 없습니다.")
                    }
                }
            }
        }

        binding.btnFriendManage.setOnClickListener {
            activity?.let {
                val intent = Intent(it, FriendManageActivity::class.java)
                startActivity(intent)
            }
        }

        binding.logoutText.setOnClickListener {
            Firebase.auth.signOut()
            activity?.let{
                val intent = Intent (it, LoginActivity::class.java)
                it.startActivity(intent)
            }
        }

        return binding.root
    }

    private fun sendToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        hBinding = null
        super.onDestroyView()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}