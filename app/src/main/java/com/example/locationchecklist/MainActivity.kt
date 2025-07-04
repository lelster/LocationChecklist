package com.example.locationchecklist

import ChecklistAdapter
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChecklistAdapter

    private val LOCATION_PERMISSION_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.checklistRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val addChecklistButton = findViewById<FloatingActionButton>(R.id.addChecklistButton)
        addChecklistButton.setOnClickListener {
            val intent = Intent(this, CreateChecklistActivity::class.java)
            startActivity(intent)
        }

        loadChecklistsAndSetupAdapter()

        // Request permissions before starting location service
        val requiredPermissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.FOREGROUND_SERVICE_LOCATION
        )

        if (requiredPermissions.all {
                ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
            }) {
            startLocationService()
        } else {
            ActivityCompat.requestPermissions(this, requiredPermissions, LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                startLocationService()
            } else {
                Toast.makeText(this, "Location permissions are required for location-based checklists.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun startLocationService() {
        val intent = Intent(this, LocationService::class.java)
        ContextCompat.startForegroundService(this, intent)
    }

    override fun onResume() {
        super.onResume()
        loadChecklistsAndSetupAdapter()
    }

    private fun loadChecklistsAndSetupAdapter() {
        val checklists = ChecklistRepository.loadChecklists(this)
        adapter = ChecklistAdapter(checklists) { checklist ->
            val intent = Intent(this, ChecklistActivity::class.java)
            intent.putExtra("checklistId", checklist.id)
            startActivity(intent)
        }
        recyclerView.adapter = adapter
    }
}
