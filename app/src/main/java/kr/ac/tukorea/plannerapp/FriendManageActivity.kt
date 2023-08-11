package kr.ac.tukorea.plannerapp

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kr.ac.tukorea.plannerapp.databinding.ActivityFriendManageBinding

class FriendManageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFriendManageBinding
    private val editTextEmailId = 1000008
    private val db = Firebase.firestore

    private val friendRepository = FriendRepository.getInstance(Firebase.auth.currentUser!!.uid)
    private lateinit var friendViewModel: FriendViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFriendManageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backImgView.setOnClickListener {
            finish()
        }

        val friendLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.FriendList.setHasFixedSize(true)
        binding.FriendList.layoutManager = friendLayoutManager

        friendViewModel = ViewModelProvider(this, ViewModelFactory())[FriendViewModel::class.java]

        friendViewModel.friends.observe(this) { friends ->
            val friendadapter = FriendAdapter(friends, this)
            binding.FriendList.adapter = friendadapter
        }

        val mDBReference = FirebaseDatabase.getInstance().reference
        mDBReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                friendViewModel.findAllFriends()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        binding.addFriendImgView.setOnClickListener {
            var builder = AlertDialog.Builder(this)
            builder.setTitle("친구 추가")

            val editTextEmail = EditText(this)
            editTextEmail.id = editTextEmailId
            editTextEmail.hint = "이메일 입력"
            editTextEmail.isSingleLine = true
            editTextEmail.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

            builder.setView(editTextEmail)

            val alertDialog = builder.create()

            alertDialog.apply {
                setButton(AlertDialog.BUTTON_POSITIVE, "확인") { _, _ ->
                }

                setButton(AlertDialog.BUTTON_NEGATIVE, "취소") { dialog, _ ->
                    dialog.dismiss()
                }

                show()

                getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    val friendEmail = editTextEmail.text.toString()
                    var friendUid: String? = null
                    lateinit var friendName: String

                    if (friendEmail.isNotEmpty()) {
                        db.collection("users")
                            .get()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val currentUserEmail = Firebase.auth.currentUser?.email

                                    for (i in task.result) {
                                        val email = i.data["email"].toString()

                                        if (email == friendEmail) {
                                            friendUid = i.id
                                            friendName = i.data["name"].toString()

                                            val isEmailExists = friendViewModel.isFriendEmailExists(friendEmail)

                                            if (!isEmailExists) {
                                                if (friendEmail != currentUserEmail) {
                                                    val newFriend = Friend(
                                                        friendUid!!,
                                                        friendName,
                                                        friendEmail
                                                    )
                                                    friendRepository.saveFriend {
                                                        it.setValue(newFriend)
                                                    }
                                                    sendToast("친구를 추가했습니다.")
                                                    alertDialog.dismiss()
                                                } else {
                                                    sendToast("본인의 이메일입니다.")
                                                }
                                            } else {
                                                sendToast("이미 추가된 친구입니다.")
                                            }
                                        }
                                    }
                                    if (friendUid == null) {
                                        sendToast("존재하지 않는 이메일입니다.")
                                    }
                                }
                            }
                    } else {
                        sendToast("이메일을 입력하세요.")
                    }
                }
            }
        }
    }

    private fun sendToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}