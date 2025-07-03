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
    private lateinit var locationSpinner: Spinner
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
        locationSpinner = findViewById(R.id.locationSpinner)
        itemsRecyclerView = findViewById(R.id.itemsRecyclerView)
        addItemButton = findViewById(R.id.addItemButton)
        cancelButton = findViewById(R.id.cancelButton)
        saveButton = findViewById(R.id.saveButton)

        // Populate spinner with example locations
        val locations = arrayOf("Home", "Work", "Other")
        locationSpinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            locations
        )

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
            val location = locationSpinner.selectedItem.toString()
            if (title.isEmpty()) {
                Toast.makeText(this, "Please enter a checklist name.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (items.isEmpty()) {
                Toast.makeText(this, "Add at least one item.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Get current location (mocked here; implement real GPS if needed)
            // val currentLocation = getCurrentLocation() // Implement as needed
            val currentLocation = location // For demo, use selected spinner value

            val checklist = Checklist(
                id = System.currentTimeMillis().toString(),
                name = title,
                location = currentLocation,
                items = items.toMutableList()
            )

            // TODO: Save checklist to repository/database

            // Return to previous screen
            finish()
        }
    }
}
