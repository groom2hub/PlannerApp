package kr.ac.tukorea.plannerapp

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kr.ac.tukorea.plannerapp.databinding.ActivityCalendarBinding
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class CalendarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalendarBinding
    private val planRepository: PlanRepository = PlanRepository.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var date: String = "${LocalDate.now()}"
        var time: String = "09:00"
        binding.calendarView.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
             date = "${year}-${month}-${dayOfMonth}"
        }

        binding.btnSave.setOnClickListener {
            val newPlan = Plan("user1", binding.Edt.text.toString(), date, time, true)
            planRepository.savePlan {
                it.setValue(newPlan)
            }
            binding.Edt.setText("")
        }
    }
}