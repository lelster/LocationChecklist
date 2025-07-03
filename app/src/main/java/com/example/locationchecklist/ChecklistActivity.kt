package com.example.locationchecklist

import ChecklistAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ChecklistActivity : AppCompatActivity() {
    private lateinit var itemRecyclerView: RecyclerView
    private lateinit var checklistTitle: TextView
    private lateinit var addItemButton: Button
    private lateinit var itemAdapter: ChecklistItemAdapter

    // Sample data
    private var checklist = Checklist(
        id = "1",
        name = "My Checklist",
        location = "null",
        items = mutableListOf(
            ChecklistItem("1", "Buy Milk", false),
            ChecklistItem("2", "Call Alice", true)
        )
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_checklist)

        checklistTitle = findViewById(R.id.checklistTitle)
        itemRecyclerView = findViewById(R.id.itemRecyclerView)
        addItemButton = findViewById(R.id.addItemButton)

        checklistTitle.text = checklist.name

        itemAdapter = ChecklistItemAdapter(checklist.items) { changedItem ->
            // Save state
        }

        itemRecyclerView.layoutManager = LinearLayoutManager(this)
        itemRecyclerView.adapter = itemAdapter

        addItemButton.setOnClickListener {
            // Example add item
            val newItem = ChecklistItem(
                id = System.currentTimeMillis().toString(),
                text = "New Item",
                isDone = false
            )
            checklist.items.add(newItem)
            itemAdapter.notifyItemInserted(checklist.items.size - 1)
        }
    }
}