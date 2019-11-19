package com.odukabdulbasit.callanalysis

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class MoreSpokenModel(var name: String,var  duraction: Int): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(duraction)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MoreSpokenModel> {
        override fun createFromParcel(parcel: Parcel): MoreSpokenModel {
            return MoreSpokenModel(parcel)
        }

        override fun newArray(size: Int): Array<MoreSpokenModel?> {
            return arrayOfNulls(size)
        }
    }
}