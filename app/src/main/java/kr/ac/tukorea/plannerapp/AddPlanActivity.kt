package kr.ac.tukorea.plannerapp

import android.R
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
import kr.ac.tukorea.plannerapp.databinding.ActivityAddPlanBinding
import java.time.LocalDate
import kotlin.math.min

@RequiresApi(Build.VERSION_CODES.O)
class AddPlanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPlanBinding
    private val planRepository: PlanRepository = PlanRepository.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val secondIntent = intent
        val date = secondIntent.getStringExtra("날짜").toString()
        var time: String = "시간"
        var isImportant: Boolean = false

        binding.tvDateStart.setText(date)
        binding.tvDateEnd.setText(date)

        binding.btnCancel.setOnClickListener {
            finish()
        }

        binding.tvDateStart.setOnClickListener {
            val cal = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener { _, y, m, d ->
                var year = y
                var month = m + 1
                var day = d
                binding.tvDateStart.setText("$year-${month}-$day")
            }
            var datePickerDialog = DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            datePickerDialog.show()
        }

        binding.tvDateEnd.setOnClickListener {
            val cal = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener { _, y, m, d ->
                var year = y
                var month = m + 1
                var day = d
                binding.tvDateEnd.setText("$year-${month}-$day")
            }
            var datePickerDialog = DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            datePickerDialog.show()
        }

        binding.tvTimeStart.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                var state = "오전"
                var hour = hourOfDay
                if (hourOfDay > 12) {
                    hour -= 12
                    state = "오후"
                }
                if (minute.toString().length == 1) {
                    time = "$state $hour : 0$minute"
                } else {
                    time = "$state $hour : $minute"
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
                var state = "오전"
                var hour = hourOfDay
                if (hourOfDay > 12) {
                    hour -= 12
                    state = "오후"
                }
                if (minute.toString().length == 1) {
                    time = "$state $hour : 0$minute"
                } else {
                    time = "$state $hour : $minute"
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

        binding.btnAdd.setOnClickListener {
            if (binding.edtPlanContent.text.isNotEmpty() && time != "시간") {
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
                finish()
            } else {
                sendToast("제목 또는 시간을 입력하세요.")
            }
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