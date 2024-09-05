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
import androidx.compose.runtime.LaunchedEffect
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
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPermissionHandlingScreen(navController: NavHostController) {
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
    val context = LocalContext.current

    // Track if the permission request has been processed after user interaction
    var hasRequestedPermission by rememberSaveable { mutableStateOf(false) }
    var permissionRequestCompleted by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(cameraPermissionState.status) {
        // Check if the permission state has changed after the request
        if (hasRequestedPermission) {
            permissionRequestCompleted = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when (val status = cameraPermissionState.status) {
            is PermissionStatus.Granted -> {
                // Permission granted, show success message
                Text("Camera permission granted. You can now use the camera.")
                Button(onClick = { navController.popBackStack() }, Modifier.padding(top = 16.dp)) {
                    Text("Go Back")
                }
            }
            is PermissionStatus.Denied -> {
                if (permissionRequestCompleted) {
                    // Show rationale only after the permission request is completed
                    if (status.shouldShowRationale) {
                        Text("Camera permission is required to use this feature.")
                        Button(onClick = {
                            cameraPermissionState.launchPermissionRequest()
                            hasRequestedPermission = true
                        }) {
                            Text("Request Camera Permission")
                        }
                    } else {
                        // Show "Denied" message only after the user has denied permission
                        Text("Camera permission denied. Please enable it in the app settings to proceed.")
                        Button(onClick = {
                            // Open app settings to manually enable the permission
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.fromParts("package", context.packageName, null)
                            }
                            context.startActivity(intent)
                        }) {
                            Text("Open App Settings")
                        }
                    }
                } else {
                    // Show the initial request button
                    Button(onClick = {
                        cameraPermissionState.launchPermissionRequest()
                        hasRequestedPermission = true
                    }) {
                        Text("Request Camera Permission")
                    }
                }
            }
        }
    }
}



