package com.example.appointmentcalendar.data.model

import android.os.Parcel
import android.os.Parcelable

data class ScheduleItem(
    val time: String,
    val isAvailable: Boolean
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(time)
        parcel.writeByte(if (isAvailable) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ScheduleItem> {
        override fun createFromParcel(parcel: Parcel): ScheduleItem {
            return ScheduleItem(parcel)
        }

        override fun newArray(size: Int): Array<ScheduleItem?> {
            return arrayOfNulls(size)
        }
    }
}