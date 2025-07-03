package com.example.locationchecklist

import ChecklistAdapter
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // RecyclerView to display a button per Entry
        val recyclerView = findViewById<RecyclerView>(R.id.checklistRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Temporary data to see if it works
        val checklists = listOf(
            Checklist(
                id = "1",
                name = "Home Tasks",
                location = "Home",
                items = mutableListOf(
                    ChecklistItem("1", "Take out trash", false),
                    ChecklistItem("2", "Water plants", true),
                    ChecklistItem("3", "Clean desk", false)
                )
            ),
            Checklist(
                id = "2",
                name = "Grocery List",
                location = "Store",
                items = mutableListOf(
                    ChecklistItem("1", "Milk", false),
                    ChecklistItem("2", "Bread", false),
                    ChecklistItem("3", "Eggs", true)
                )
            )
        )
        // Adapter to go on to ChecklistActivity Page
        val adapter = ChecklistAdapter(checklists) { checklist ->
            val intent = Intent(this, ChecklistActivity::class.java)
            intent.putExtra("checklistId", checklist.id)
            startActivity(intent)
        }
        recyclerView.adapter = adapter
    }
}