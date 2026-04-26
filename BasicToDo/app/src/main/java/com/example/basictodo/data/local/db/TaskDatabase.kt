package com.example.basictodo.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.basictodo.data.local.dao.TaskDao
import com.example.basictodo.data.local.entity.TaskEntity

@Database(entities = [TaskEntity::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {
    abstract val taskDao: TaskDao

    companion object {
        const val DATABASE_NAME = "task_db"
    }
}
