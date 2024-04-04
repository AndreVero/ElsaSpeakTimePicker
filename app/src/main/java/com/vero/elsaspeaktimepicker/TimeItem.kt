package com.vero.elsaspeaktimepicker

data class TimeItem(
    val time: String,
    val isDayTime: Boolean,
)

fun getTimes() : List<TimeItem> {
    var minutesInDay = 24 * 60
    val times = mutableListOf<TimeItem>()

    while (minutesInDay > 0) {
        minutesInDay - 30
        times.add(
            TimeItem(
                time = getParsedTime(minutesInDay),
                isDayTime = isDayTime(minutesInDay)
            )
        )
        minutesInDay -= 30
    }

    return times
}

private fun getParsedTime(time: Int) : String {
    val minutes = time.mod(60)
    val hours = time / 60
    return String.format("%02d:%02d", hours, minutes)
}

private fun isDayTime(time: Int) : Boolean {
    return time in (6 * 60)..(18 * 60)
}