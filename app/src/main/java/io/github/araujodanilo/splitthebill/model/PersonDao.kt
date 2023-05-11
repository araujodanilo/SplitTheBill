package io.github.araujodanilo.splitthebill.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PersonDao {
    @Insert
    fun create(person: Person)
    @Query("SELECT * FROM Person WHERE id = :id")
    fun getOne(id: Int): Person?
    @Query("SELECT * FROM Person")
    fun getAll(): MutableList<Person>
    @Update
    fun update(person: Person): Int
    @Delete
    fun delete(person: Person): Int
    @Query("DELETE FROM Person")
    fun clear()
}