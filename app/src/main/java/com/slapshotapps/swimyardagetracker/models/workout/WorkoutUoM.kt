package com.slapshotapps.swimyardagetracker.models.workout


enum class WorkoutUoM(private val uom: String) {
    YARDS("Yards"),
    METERS("Meters");

    override fun toString(): String {
        return uom
    }}