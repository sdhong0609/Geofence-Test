package com.hongstudio.geofence_test

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent!!)

        if (geofencingEvent.hasError()) {
            Log.e("GeofenceErr", GeofenceStatusCodes.getStatusCodeString(geofencingEvent.errorCode))
            return
        } else {
            Log.e("GeofenceErr", "NoErr")
        }
        val geofenceTransaction = geofencingEvent.geofenceTransition

        if (geofenceTransaction == Geofence.GEOFENCE_TRANSITION_ENTER ||
            geofenceTransaction == Geofence.GEOFENCE_TRANSITION_EXIT
        ) {
            val triggeringGeofences = geofencingEvent.triggeringGeofences
            val transitionMsg = when (geofenceTransaction) {
                Geofence.GEOFENCE_TRANSITION_ENTER -> "Enter"
                Geofence.GEOFENCE_TRANSITION_EXIT -> "Exit"
                else -> "-"
            }
            triggeringGeofences.forEach {
                Log.e("geofence", "${it.requestId} - $transitionMsg")
            }
        } else {
            Log.e("geofence", "Unknown")
        }
    }
}