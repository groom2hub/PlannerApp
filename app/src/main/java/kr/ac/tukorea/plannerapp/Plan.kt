package kr.ac.tukorea.plannerapp

import java.io.Serializable

class Plan (
    var id: String = "",
    var context: String = "",
    var d_day: String = "",
    var time: String,
    var isImportant: Boolean
) : Serializable {
    constructor(): this(
        "",
        "",
        "",
        "",
        false
    )
}