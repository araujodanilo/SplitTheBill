package io.github.araujodanilo.splitthebill.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Person::class], version = 1)
abstract class PersonRoom: RoomDatabase() {
    companion object Constants {
        const val PERSON_DATABASE_FILE = "persons_room"
    }
    abstract fun getPersonDao(): PersonDao
}