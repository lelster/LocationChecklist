package com.example.locationchecklist

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object ChecklistRepository {
    private const val PREFS_NAME = "checklist_prefs"
    private const val CHECKLIST_KEY = "checklists"
    private val gson = Gson()

    fun saveChecklists(context: Context, checklists: List<Checklist>) {
        val json = gson.toJson(checklists)
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(CHECKLIST_KEY, json).apply()
    }

    fun loadChecklists(context: Context): MutableList<Checklist> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(CHECKLIST_KEY, null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<Checklist>>() {}.type
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }
    }
}
