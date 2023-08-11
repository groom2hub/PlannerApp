package kr.ac.tukorea.plannerapp

import java.io.Serializable

class Friend (
    var id: String,
    var name: String,
    var email: String
) : Serializable {
    constructor(): this(
        "",
        "",
        "",
    )
}