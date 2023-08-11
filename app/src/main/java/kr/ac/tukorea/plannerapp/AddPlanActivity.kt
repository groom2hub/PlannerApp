package kr.ac.tukorea.plannerapp

import android.R
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kr.ac.tukorea.plannerapp.databinding.ActivityAddPlanBinding
import java.text.DecimalFormat
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
class AddPlanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPlanBinding

    private val planRepository = PlanRepository.getInstance(Firebase.auth.currentUser!!.uid)
    private val dateFormat = DecimalFormat("00")
    private val timeFormat = DecimalFormat("00")
    private lateinit var date: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val secondIntent = intent
        date = secondIntent.getStringExtra("날짜").toString()
        var time: String = "시간"
        var isImportant: Boolean = false

        binding.tvDateStart.setText(secondIntent.getStringExtra("dateStart").toString())
        binding.tvDateEnd.setText(secondIntent.getStringExtra("dateEnd").toString())
        binding.tvTimeStart.setText(secondIntent.getStringExtra("timeStart").toString())
        binding.tvTimeEnd.setText(secondIntent.getStringExtra("timeEnd").toString())

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
                if (hourOfDay > 12) {
                    if (hourOfDay < 22)
                        time = "오후 ${timeFormat.format(hourOfDay - 12)[1]} : ${timeFormat.format(minute)}"
                    else
                        time = "오후 ${timeFormat.format(hourOfDay - 12)} : ${timeFormat.format(minute)}"
                }
                else if (hourOfDay == 12) {
                    time = "오후 ${timeFormat.format(hourOfDay)} : ${timeFormat.format(minute)}"
                }
                else if (hourOfDay == 0) {
                    time = "오전 ${timeFormat.format(12)} : ${timeFormat.format(minute)}"
                }
                else {
                    if (hourOfDay < 10)
                        time = "오전 ${timeFormat.format(hourOfDay)[1]} : ${timeFormat.format(minute)}"
                    else
                        time = "오전 ${timeFormat.format(hourOfDay)} : ${timeFormat.format(minute)}
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
                if (hourOfDay > 12) {
                    if (hourOfDay < 22)
                        time = "오후 ${timeFormat.format(hourOfDay - 12)[1]} : ${timeFormat.format(minute)}"
                    else
                        time = "오후 ${timeFormat.format(hourOfDay - 12)} : ${timeFormat.format(minute)}"
                }
                else if (hourOfDay == 12) {
                    time = "오후 ${timeFormat.format(hourOfDay)} : ${timeFormat.format(minute)}"
                }
                else if (hourOfDay == 0) {
                    time = "오전 ${timeFormat.format(12)} : ${timeFormat.format(minute)}"
                }
                else {
                    if (hourOfDay < 10)
                        time = "오전 ${timeFormat.format(hourOfDay)[1]} : ${timeFormat.format(minute)}"
                    else
                        time = "오전 ${timeFormat.format(hourOfDay)} : ${timeFormat.format(minute)}"
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
            if (binding.edtPlanContent.text.isEmpty()) {
                sendToast("제목을 입력하세요.")
            } else {
                val newPlan = Plan(
                    Firebase.auth.currentUser!!.uid,
                    binding.edtPlanContent.text.toString(),
                    binding.tvDateStart.text.toString(),
                    binding.tvDateEnd.text.toString(),
                    binding.tvTimeStart.text.toString(),
                    binding.tvTimeEnd.text.toString(),
                    binding.swIsImportant.isChecked
                )
                planRepository.savePlan(newPlan)

                secondIntent.putExtra("날짜", binding.tvDateStart.text.toString())
                setResult(Activity.RESULT_OK, secondIntent)
                finish()
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