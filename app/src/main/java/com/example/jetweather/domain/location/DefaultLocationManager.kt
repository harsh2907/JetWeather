package com.example.jetweather.domain.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import com.example.jetweather.data.utils.Response
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException


class DefaultLocationManager(
    private val context: Context,
    private val client: FusedLocationProviderClient
) : LocationClient {

    @SuppressLint("MissingPermission")
    override fun getLocation(): Flow<Response<Location>> = callbackFlow {
        try {
            if (!context.hasLocationPermission()) {
                send(Response.Error("Missing Location Permission"))
                return@callbackFlow
            }

            send(Response.Loading())
            client.lastLocation
                .addOnSuccessListener { location ->
                        launch {
                            send(Response.Success(location))
                            Log.e("DefaultLocationManager","Successfully emitted")
                        }
                        Log.e("DefaultLocationManager", location.latitude.toString())
                }
                .addOnFailureListener {
                    val error: Response.Error<Location> = if (it is HttpException) {
                        Response.Error("No Network")
                    } else {
                        Response.Error(it.message ?: "Something went wrong, can't fetch location.")
                    }
                    Log.e("DefaultLocationManager", error.message.toString())
                    launch { send(error) }
                }

            awaitClose{

            }
        } catch (e: Exception) {
            Log.e("DefaultLocationManager", e.message.toString())
        }
    }
}