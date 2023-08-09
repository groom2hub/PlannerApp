package kr.ac.tukorea.plannerapp

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Rect
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import kr.ac.tukorea.plannerapp.databinding.ActivityModifyPlanBinding
import kr.ac.tukorea.plannerapp.databinding.FragmentPlanBinding
import java.text.DecimalFormat
import kotlin.properties.Delegates

@RequiresApi(Build.VERSION_CODES.O)
class ModifyPlanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityModifyPlanBinding
    private val planRepository: PlanRepository = PlanRepository.getInstance()
    private val dateFormat = DecimalFormat("00")
    private val timeFormat = DecimalFormat("00")
    private lateinit var date: String
    private lateinit var time: String
    private var isImportant by Delegates.notNull<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModifyPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val secondIntent = intent
        val plan = secondIntent.getSerializableExtra("plan_item") as Plan

        binding.tvDateStart.setText(plan.dateStart)
        binding.tvDateEnd.setText(plan.dateEnd)
        binding.tvTimeStart.setText(plan.timeStart)
        binding.tvTimeEnd.setText(plan.timeEnd)
        binding.swIsImportant.isChecked = plan.isImportant
        binding.edtPlanContent.setText(plan.context)

        binding.btnCancel.setOnClickListener {
            finish()
        }

        binding.tvDateStart.setOnClickListener {
            val cal = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                date = "${year}-${dateFormat.format(month + 1)}-${dateFormat.format(dayOfMonth)}"
                binding.tvDateStart.setText(date)
            }
            var datePickerDialog = DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            datePickerDialog.show()
        }

        binding.tvDateEnd.setOnClickListener {
            val cal = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                date = "${year}-${dateFormat.format(month + 1)}-${dateFormat.format(dayOfMonth)}"
                binding.tvDateEnd.setText(date)
            }
            var datePickerDialog = DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            datePickerDialog.show()
        }

        binding.tvTimeStart.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                if (hourOfDay > 11) {
                    time = "오후 ${timeFormat.format(hourOfDay - 12)} : ${timeFormat.format(minute)}"
                }
                else {
                    time = "오전 ${timeFormat.format(hourOfDay - 12)} : ${timeFormat.format(minute)}"
                }
                binding.tvTimeStart.setText(time)
            }
            var timePicker = TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),false)
            timePicker.window?.setBackgroundDrawableResource(android.R.color.transparent)
            timePicker.show()
        }

        binding.tvTimeEnd.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                if (hourOfDay > 11) {
                    time = "오후 ${timeFormat.format(hourOfDay - 12)} : ${timeFormat.format(minute)}"
                }
                else {
                    time = "오전 ${timeFormat.format(hourOfDay - 12)} : ${timeFormat.format(minute)}"
                }
                binding.tvTimeEnd.setText(time)
            }
            var timePicker = TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),false)
            timePicker.window?.setBackgroundDrawableResource(android.R.color.transparent)
            timePicker.show()
        }

        binding.swIsImportant.setOnCheckedChangeListener { _, isChecked ->
            isImportant = isChecked
        }

        binding.btnDelete.setOnClickListener {
            planRepository.deletePlan(plan.id)
            finish()
        }

        binding.btnModify.setOnClickListener {
            val newPlan = Plan(
                "user1",
                binding.edtPlanContent.text.toString(),
                binding.tvDateStart.text.toString(),
                binding.tvDateEnd.text.toString(),
                binding.tvTimeStart.text.toString(),
                binding.tvTimeEnd.text.toString(),
                binding.swIsImportant.isChecked
            )

            secondIntent.putExtra("plan_item", newPlan)
            setResult(Activity.RESULT_OK, secondIntent)
            finish()
        }
    }

    private fun sendToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
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