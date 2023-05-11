package io.github.araujodanilo.splitthebill.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Person(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @NonNull var name: String,
    @NonNull var description: String,
    @NonNull val value: Double,
    @NonNull var difference: Double
) : Parcelable