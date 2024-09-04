package com.medium.permissions.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraAndLocationPermissionsHandlingScreen(navController: NavHostController) {
    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )
    val context = LocalContext.current

    var hasRequestedPermissions by rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        when {
            permissionsState.allPermissionsGranted -> {
                Text("All permissions granted. You can now access the camera and location.")
                Button(onClick = { navController.popBackStack() }, Modifier.padding(top = 16.dp)) {
                    Text("Go Back")
                }
            }
            permissionsState.shouldShowRationale -> {
                Text("Camera and Location permissions are required to use this feature.")
                Button(onClick = {
                    permissionsState.launchMultiplePermissionRequest()
                }) {
                    Text("Request Permissions")
                }
            }
            else -> {
                // Only show the "Permissions denied" message after the user has interacted
                if (hasRequestedPermissions) {
                    Text("Permissions denied. Please enable them in the app settings to proceed.")
                    Button(onClick = {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                        context.startActivity(intent)
                    }) {
                        Text("Open App Settings")
                    }
                } else {
                    Text("Camera and Location permissions are required to use this feature.")
                    Button(onClick = {
                        permissionsState.launchMultiplePermissionRequest()
                        hasRequestedPermissions = true
                    }) {
                        Text("Request Permissions")
                    }
                }
            }
        }
    }
}