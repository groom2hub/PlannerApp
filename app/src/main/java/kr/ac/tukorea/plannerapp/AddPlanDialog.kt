package kr.ac.tukorea.plannerapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import kr.ac.tukorea.plannerapp.databinding.ActivityAddPlanBinding
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class AddPlanDialog(context: Context) : DialogFragment() {
    private var _binding: ActivityAddPlanBinding? = null
    private val binding get() = _binding!!
    private val planRepository: PlanRepository = PlanRepository.getInstance()
    private val cal = Calendar.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = ActivityAddPlanBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.tvDateStart.setOnClickListener {
            context?.let { it1 ->
                DatePickerDialog(it1, DatePickerDialog.OnDateSetListener { datePicker, y, m, d ->
                    binding.tvDateStart.text = "$y-${m+1}-$d"
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
            }
        }

        binding.tvTimeStart.setOnClickListener {
            TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { timePicker, h, m ->
                binding.tvTimeStart.text = "$h:$m"
            }, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), true ).show()
        }

        binding.tvDateEnd.setOnClickListener {
            context?.let { it1 ->
                DatePickerDialog(it1, DatePickerDialog.OnDateSetListener { datePicker, y, m, d ->
                    binding.tvDateEnd.text = "$y-${m+1}-$d"
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
            }
        }

        binding.tvTimeEnd.setOnClickListener {
            TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { timePicker, h, m ->
                binding.tvTimeEnd.text = "$h:$m"
            }, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), true ).show()
        }

        binding.btnAdd.setOnClickListener {
            val newPlan = Plan(
                "user1",
                binding.edtPlanContent.text.toString(),
                binding.tvDateStart.text.toString(),
                binding.tvDateEnd.text.toString(),
                binding.tvTimeStart.text.toString(),
                binding.tvTimeEnd.text.toString(),
                binding.swIsImportant.isChecked
            )
            planRepository.savePlan {
                it.setValue(newPlan)
            }
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        // full Screen code
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
