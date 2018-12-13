package com.slapshotapps.swimyardagetracker.models.workout


enum class WorkoutUoM(val label: String) {
    YARDS("Yards"),
    METERS("Meters");

    override fun toString(): String {
        return label
    }}