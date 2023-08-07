package kr.ac.tukorea.plannerapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
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
    private lateinit var planlistadapter: PlanListAdapter

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
        lateinit var uId: String

        planViewModel = ViewModelProvider(this, ViewModelFactory())[PlanViewModel::class.java]
        planViewModel.findAllPlans()

        planViewModel.plans.observe(viewLifecycleOwner) { plans ->
            planlistadapter = PlanListAdapter(plans, requireContext())
            binding.rvPlanList.adapter = planlistadapter
        }

        val planLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvPlanList.setHasFixedSize(true)
        binding.rvPlanList.layoutManager = planLayoutManager

        db.collection("users")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (i in task.result) {
                        if (i.id == Firebase.auth.currentUser?.uid) {
                            val userName = i.data["name"]
                            val userId = i.id
                            uId = userId
                            binding.userNameText.text = userName.toString()

                            val userData = db.collection("users").document(uId)
                            userData.addSnapshotListener { snapshot, e ->
                                if (e != null) {
                                    Log.w("test", "Listen failed.", e)
                                    return@addSnapshotListener
                                }

                                if (snapshot != null && snapshot.exists()) {
                                    Log.d("test", "Current data: ${snapshot.data}")
                                    var newName = snapshot.data?.get("name").toString()
                                    binding.userNameText.text = newName
                                } else {
                                    Log.d("test", "Current data: null")
                                }
                            }
                        }
                    }
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
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}