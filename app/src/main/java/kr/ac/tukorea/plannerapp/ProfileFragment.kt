package kr.ac.tukorea.plannerapp

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kr.ac.tukorea.plannerapp.databinding.FragmentHomeBinding
import kr.ac.tukorea.plannerapp.databinding.FragmentProfileBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private var hBinding: FragmentProfileBinding? = null
    private val binding get() = hBinding!!
    private val db = Firebase.firestore

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
            editText.id = R.id.editTextName
            editText.hint = "변경할 이름"

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
                        if (newName.length > 8) {
                            Toast.makeText(requireContext(), "8자 이하로 입력하세요.", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            Toast.makeText(requireContext(), "${newName}으로 이름 변경 성공", Toast.LENGTH_SHORT).show()
                            val userData = db.collection("users").document(uId)
                            userData.update("name", newName)
                            binding.userName.text = newName
                            dismiss()
                        }
                    } else {
                        Toast.makeText(requireContext(), "이름을 입력하세요.", Toast.LENGTH_SHORT).show()
                    }
                }
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