package com.slapshotapps.swimyardagetracker.models.personalrecords

import android.os.Parcel
import android.os.Parcelable
import com.slapshotapps.swimyardagetracker.models.workout.WorkoutUoM
import java.util.*
import kotlin.collections.ArrayList

class YardageTrackerPersonalRecord(
    val recordID: Long,
    val stroke: String,
    val distance: Int,
    val recordTimes: List<YardageTrackerRecordTime>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.createTypedArrayList(YardageTrackerRecordTime)!!) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(recordID)
        parcel.writeString(stroke)
        parcel.writeInt(distance)
        parcel.writeTypedList(recordTimes)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<YardageTrackerPersonalRecord> {
        override fun createFromParcel(parcel: Parcel): YardageTrackerPersonalRecord {
            return YardageTrackerPersonalRecord(parcel)
        }

        override fun newArray(size: Int): Array<YardageTrackerPersonalRecord?> {
            return arrayOfNulls(size)
        }

        fun fromEntities(record: PersonalRecord, times: List<RecordTime>): YardageTrackerPersonalRecord {
            val recordTimes = ArrayList<YardageTrackerRecordTime>()
            for (time: RecordTime in times) {
                recordTimes.add(YardageTrackerRecordTime(time.id, time.date, time.unitOfMeasure, time.hours,
                time.minutes, time.seconds, time.milliseconds))
            }

            return YardageTrackerPersonalRecord(record.id, record.stroke, record.distance, recordTimes)
        }
    }
}

class YardageTrackerRecordTime(
    val id: Long = 0L,
    val date: Date?,
    val uom: WorkoutUoM,
    val hours: Int = 0,
    val minutes: Int,
    val seconds: Int,
    val milliseconds: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        if (parcel.readLong() == 0L) null else Date(parcel.readLong()),
        WorkoutUoM.fromString(parcel.readString()!!),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()) {
    }

    fun toRecordTime(recordId: Long, timeRecordId: Long): RecordTime? {
        return if (date == null) null else RecordTime(timeRecordId, recordId, date, uom, hours, minutes, seconds, milliseconds)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeLong(date?.time ?: 0)
        parcel.writeString(uom.label)
        parcel.writeInt(hours)
        parcel.writeInt(minutes)
        parcel.writeInt(seconds)
        parcel.writeInt(milliseconds)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<YardageTrackerRecordTime> {
        override fun createFromParcel(parcel: Parcel): YardageTrackerRecordTime {
            return YardageTrackerRecordTime(parcel)
        }

        override fun newArray(size: Int): Array<YardageTrackerRecordTime?> {
            return arrayOfNulls(size)
        }
    }
}
