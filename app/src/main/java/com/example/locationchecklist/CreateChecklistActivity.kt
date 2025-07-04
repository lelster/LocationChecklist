package com.example.locationchecklist

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CreateChecklistActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var itemsRecyclerView: RecyclerView
    private lateinit var addItemButton: Button
    private lateinit var cancelButton: Button
    private lateinit var saveButton: Button

    private lateinit var itemAdapter: CreateChecklistItemAdapter
    private val items = mutableListOf<ChecklistItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_checklist)

        titleEditText = findViewById(R.id.titleEditText)
        locationEditText = findViewById(R.id.locationEditText)
        itemsRecyclerView = findViewById(R.id.itemsRecyclerView)
        addItemButton = findViewById(R.id.addItemButton)
        cancelButton = findViewById(R.id.cancelButton)
        saveButton = findViewById(R.id.saveButton)

        itemAdapter = CreateChecklistItemAdapter(items)
        itemsRecyclerView.layoutManager = LinearLayoutManager(this)
        itemsRecyclerView.adapter = itemAdapter

        // Add item dialog
        addItemButton.setOnClickListener {
            val editText = EditText(this)
            editText.setTextColor(Color.WHITE)
            editText.hint = "Item description"
            editText.setHintTextColor(Color.GRAY)
            AlertDialog.Builder(this)
                .setTitle("Add Item")
                .setView(editText)
                .setPositiveButton("Add") { _, _ ->
                    val text = editText.text.toString().trim()
                    if (text.isNotEmpty()) {
                        val newItem = ChecklistItem(
                            id = System.currentTimeMillis().toString(),
                            text = text,
                            isDone = false
                        )
                        items.add(newItem)
                        itemAdapter.notifyItemInserted(items.size - 1)
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        cancelButton.setOnClickListener {
            finish()
        }

        saveButton.setOnClickListener {
            val title = titleEditText.text.toString().trim()
            val location = locationEditText.text.toString().trim()
            if (title.isEmpty()) {
                Toast.makeText(this, "Please enter a checklist name.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (items.isEmpty()) {
                Toast.makeText(this, "Add at least one item.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val checklist = Checklist(
                id = System.currentTimeMillis().toString(),
                name = title,
                location = location,
                items = items.toMutableList()
            )

            val currentChecklists = ChecklistRepository.loadChecklists(this)
            currentChecklists.add(checklist)
            ChecklistRepository.saveChecklists(this, currentChecklists)

            finish()
        }
    }
}
