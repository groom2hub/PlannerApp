package kr.ac.tukorea.plannerapp

import android.app.Activity
import android.widget.Toast

class BackKeyHandler(private val activity: Activity) {
    private var backKeyPressedTime: Long = 0
    private var toast: Toast? = null
    fun onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis()
            showGuide()
            return
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            activity.finish()
            toast!!.cancel()
        }
    }

    private fun showGuide() {
        toast = Toast.makeText(activity, "버튼을 한번 더 누르면 종료합니다", Toast.LENGTH_SHORT)
        toast!!.show()
    }
}