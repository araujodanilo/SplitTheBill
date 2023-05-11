package io.github.araujodanilo.splitthebill.controller

import android.content.Context
import androidx.room.Room
import io.github.araujodanilo.splitthebill.model.Person
import io.github.araujodanilo.splitthebill.model.PersonDao
import io.github.araujodanilo.splitthebill.model.PersonRoom

class PersonController(context: Context) {
    private val personRoom: PersonRoom
    private val personDao: PersonDao

    init {
        personRoom = Room.databaseBuilder(context, PersonRoom::class.java, PersonRoom.PERSON_DATABASE_FILE).fallbackToDestructiveMigration().build()
        personDao = personRoom.getPersonDao()
    }

    fun list(callback: (person: List<Person>) -> Unit) {
        Thread {
            callback(personDao.getAll())
        }.start()
    }

    fun create(person: Person, callback: () -> Unit) {
        Thread {
            personDao.create(person)
            callback()
        }.start()
    }

    fun update(person: Person, callback: () -> Unit) {
        Thread {
            personDao.update(person)
            callback()
        }.start()
    }

    fun delete(person: Person, callback: () -> Unit) {
        Thread {
            personDao.delete(person)
            callback()
        }.start()
    }

    fun clear(callback: () -> Unit) {
        Thread {
            personDao.clear()
            callback()
        }.start()
    }
}