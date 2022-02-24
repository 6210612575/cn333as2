package com.example.mynotes.models

import android.os.Parcel
import android.os.Parcelable

class NoteDetail(val name: String, val Detail: String) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(Detail)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<NoteDetail> {
        override fun createFromParcel(parcel: Parcel): NoteDetail {
            return NoteDetail(parcel)
        }

        override fun newArray(size: Int): Array<NoteDetail?> {
            return arrayOfNulls(size)
        }
    }

}