package com.hongstudio.geofence_test

import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.hongstudio.geofence_test.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var geofencingClient: GeofencingClient
    val geofenceList: MutableList<Geofence> by lazy {
        mutableListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        geofencingClient = LocationServices.getGeofencingClient(this)
    }

    private fun getGeofence(reqId: String, geo: LatLng, radius: Float): Geofence {
        return Geofence.Builder()
            .setRequestId(reqId)
            .setCircularRegion(geo.latitude, geo.longitude, radius)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setLoiteringDelay(10000)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_DWELL)
            .build()
    }

    val geoPending: PendingIntent by lazy {
        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun addGeofences() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            geofencingClient.addGeofences(getGeofencingRequest(geofenceList), geoPending).run {
                addOnSuccessListener {
                    Log.e("addGeo", "add Success")
                }
                addOnFailureListener {
                    Log.e("addGeo", "add Fail")
                }
            }
        }
    }

    private fun removeGeofences(){
        geofencingClient.removeGeofences(geoPending).run{
            addOnSuccessListener {
                Log.e("removeGeo", "remove Success")
                geofenceList.clear()
            }
            addOnFailureListener{
                Log.e("removeGeo", "remove Fail")
            }
        }
    }

    private fun getGeofencingRequest(list: List<Geofence>): GeofencingRequest {
        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            addGeofences(list)
        }.build()
    }
}