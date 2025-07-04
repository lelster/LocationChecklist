package com.example.locationchecklist

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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

    private lateinit var checklist: Checklist

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_checklist)

        checklistTitle = findViewById(R.id.checklistTitle)
        itemRecyclerView = findViewById(R.id.itemRecyclerView)
        addItemButton = findViewById(R.id.addItemButton)

        val checklistId = intent.getStringExtra("checklistId")
        if (checklistId == null) {
            Toast.makeText(this, "Checklist not found.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val allChecklists = ChecklistRepository.loadChecklists(this)
        checklist = allChecklists.find { it.id == checklistId } ?: run {
            Toast.makeText(this, "Checklist not found.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        checklistTitle.text = checklist.name

        itemAdapter = ChecklistItemAdapter(checklist.items) { changedItem ->
            ChecklistRepository.saveChecklists(this, allChecklists)
        }

        itemRecyclerView.layoutManager = LinearLayoutManager(this)
        itemRecyclerView.adapter = itemAdapter

        addItemButton.setOnClickListener {
            val newItem = ChecklistItem(
                id = System.currentTimeMillis().toString(),
                text = "New Item",
                isDone = false
            )
            checklist.items.add(newItem)
            itemAdapter.notifyItemInserted(checklist.items.size - 1)

            ChecklistRepository.saveChecklists(this, allChecklists)
        }
        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

    }
}
