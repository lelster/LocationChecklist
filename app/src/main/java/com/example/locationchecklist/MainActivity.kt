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
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChecklistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Recycler View to display a button per entry
        recyclerView = findViewById(R.id.checklistRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val addChecklistButton = findViewById<FloatingActionButton>(R.id.addChecklistButton)
        addChecklistButton.setOnClickListener {
            val intent = Intent(this, CreateChecklistActivity::class.java)
            startActivity(intent)
        }

        loadChecklistsAndSetupAdapter()
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
