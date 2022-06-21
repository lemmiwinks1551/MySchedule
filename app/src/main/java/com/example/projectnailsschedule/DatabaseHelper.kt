package com.example.projectnailsschedule

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, SCHEMA) {

    override fun onCreate(db: SQLiteDatabase) {
        Log.e("Database", "Создана")
        db.execSQL(
            "CREATE TABLE users (" + COLUMN_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME
                    + " TEXT, " + COLUMN_YEAR + " INTEGER);"
        )
        // добавление начальных данных
        db.execSQL(
            ("INSERT INTO " + TABLE + " (" + COLUMN_NAME
                    + ", " + COLUMN_YEAR + ") VALUES ('Том Смит', 1981);")
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE)
        onCreate(db)
    }

    companion object {
        private val DATABASE_NAME = "userstore.db" // название бд
        private val SCHEMA = 1 // версия базы данных
        val TABLE = "users" // название таблицы в бд

        // названия столбцов
        val COLUMN_ID = "_id"
        val COLUMN_NAME = "name"
        val COLUMN_YEAR = "year"
    }
}

/*
class DatabaseHelper(context: Context?) : SQLiteOpenHelper(context, dbName, null, dbVersion) {
    companion object {
        // Название БД
        const val dbName = "schedule.db"

        // Версия БД
        const val dbVersion = 1

        // Название таблицы в БД
        const val tableName = "schedule"

        // Названия столбцов
        const val idColumn = "_id"
        const val nameColumn = "name"
        const val phoneColumn = "phone"
        const val instColumn = "instagram"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        Log.e("DataBase", "onCreate")

        db?.execSQL(
            "CREATE TABLE schedule (" +
                    "$idColumn INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE," +
                    "$nameColumn TEXT NOT NULL," +
                    "$phoneColumn TEXT NOT NULL UNIQUE," +
                    "$instColumn TEXT UNIQUE" +
                    ");"
        )
        Log.e("DataBase", "DB created")

        // Добавление тестовых данных
        db?.execSQL(
            "INSERT INTO $tableName ($nameColumn, $phoneColumn, $instColumn) " +
                    "VALUES ('Кирилл', '8 800 555 35 35', 'lemmiwinks');"
        )
        Log.e("DataBase", "test row created")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        Log.e("DataBase", "DB upgraded")
        TODO("Not yet implemented")
    }

}*/
