package com.example.locationchecklist

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.LocationServices
import android.Manifest
import android.content.pm.PackageManager



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
            if (title.isEmpty()) {
                Toast.makeText(this, "Please enter a checklist name.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (items.isEmpty()) {
                Toast.makeText(this, "Add at least one item.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    1
                )
                Toast.makeText(this, "Location permission required to save location.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val latLng = "${location.latitude},${location.longitude}"

                    val checklist = Checklist(
                        id = System.currentTimeMillis().toString(),
                        name = title,
                        location = latLng,
                        items = items.toMutableList()
                    )

                    val currentChecklists = ChecklistRepository.loadChecklists(this)
                    currentChecklists.add(checklist)
                    ChecklistRepository.saveChecklists(this, currentChecklists)

                    Toast.makeText(this, "Checklist saved with location.", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Unable to get current location.", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}
