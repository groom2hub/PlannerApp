package kr.ac.tukorea.plannerapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kr.ac.tukorea.plannerapp.databinding.FragmentHomeBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@RequiresApi(Build.VERSION_CODES.O)
class HomeFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private var hBinding: FragmentHomeBinding? = null
    private val binding get() = hBinding!!
    private val db = Firebase.firestore
    private lateinit var planViewModel: PlanViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        if (Firebase.auth.currentUser == null) {
            activity?.let{
                val intent = Intent (it, LoginActivity::class.java)
                it.startActivity(intent)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        hBinding = FragmentHomeBinding.inflate(inflater)

        db.collection("users")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (i in task.result) {
                        if (i.id == Firebase.auth.currentUser?.uid) {
                            val userName = i.data["name"]
                            val userId = i.id
                            binding.userNameText.text = userName.toString()

                            val userData = db.collection("users").document(userId)

                            userData.addSnapshotListener { snapshot, e ->
                                if (e != null) {
                                    return@addSnapshotListener
                                }

                                if (snapshot != null && snapshot.exists()) {
                                    var newName = snapshot.data?.get("name").toString()
                                    binding.userNameText.text = newName
                                }
                            }
                        }
                    }
                }
            }

        val mDBReference = FirebaseDatabase.getInstance().reference
        mDBReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                planViewModel = ViewModelProvider(this@HomeFragment, ViewModelFactory())[PlanViewModel::class.java]
                planViewModel.findAllPlans()

                planViewModel.plans.observe(viewLifecycleOwner) { plans ->
                    val planlistadapter = context?.let { PlanListAdapter(plans, it) }
                    binding.rvPlanList.adapter = planlistadapter
                }

                val planLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                binding.rvPlanList.setHasFixedSize(true)
                binding.rvPlanList.layoutManager = planLayoutManager
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        hBinding = null
        super.onDestroyView()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}