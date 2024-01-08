package com.slapshotapps.swimyardagetracker.models.workout

enum class WorkoutUoM(val label: String) {
    YARDS("Yards"),
    METERS("Meters"),
    UNKNOWN("Unknown");

    override fun toString(): String {
        return label
    }

    companion object {
        fun fromString(str: String): WorkoutUoM {
            return when {
                (YARDS.label.equals(str, true)) -> YARDS
                (METERS.label.equals(str, true)) -> METERS
                else -> UNKNOWN
            }
        }
    }
}
