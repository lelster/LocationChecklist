package com.example.locationchecklist

data class Checklist(
    val id: String,
    val name: String,
    val location: String,
    val items: List<ChecklistItem>
)