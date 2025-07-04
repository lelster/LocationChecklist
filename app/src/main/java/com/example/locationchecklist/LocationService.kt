package com.example.locationchecklist

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*

class LocationService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val checklistEntered = mutableSetOf<String>()

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        startForegroundNotification()
        startLocationUpdates()
    }

    private fun startForegroundNotification() {
        val channelId = "location_channel"
        val channel = NotificationChannel(
            channelId,
            "Location Checklist Monitoring",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Location Checklist Active")
            .setContentText("Monitoring your location for checklists.")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        startForeground(1, notification)
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            10_000L
        ).build()

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            stopSelf()
            return
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val currentLocation = result.lastLocation ?: return

                val checklists = ChecklistRepository.loadChecklists(this@LocationService)
                for (checklist in checklists) {
                    val locParts = checklist.location.split(",")
                    if (locParts.size == 2) {
                        val lat = locParts[0].toDoubleOrNull() ?: continue
                        val lon = locParts[1].toDoubleOrNull() ?: continue
                        val distance = distanceInMeters(currentLocation.latitude, currentLocation.longitude, lat, lon)

                        if (distance <= 20) {
                            if (!checklistEntered.contains(checklist.id)) {
                                checklistEntered.add(checklist.id)
                                sendChecklistNotification(checklist)
                            }
                        } else {
                            checklistEntered.remove(checklist.id)
                        }
                    }
                }
            }
        }, Looper.getMainLooper())
    }

    private fun sendChecklistNotification(checklist: Checklist) {
        val channelId = "checklist_notify"
        val channel = NotificationChannel(
            channelId,
            "Checklist Notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Nearby Checklist: ${checklist.name}")
            .setContentText("You are near ${checklist.name}. Don't forget to check it!")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .build()

        manager.notify(checklist.id.hashCode(), notification)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
