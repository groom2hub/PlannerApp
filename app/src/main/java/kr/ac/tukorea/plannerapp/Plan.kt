package kr.ac.tukorea.plannerapp

import java.io.Serializable

class Plan (
    var id: String,
    var context: String,
    var dateStart: String,
    var dateEnd: String,
    var timeStart: String,
    var timeEnd: String,
    var isImportant: Boolean
) : Serializable {
    constructor(): this(
        "",
        "",
        "",
        "",
        "",
        "",
        false
    )
}