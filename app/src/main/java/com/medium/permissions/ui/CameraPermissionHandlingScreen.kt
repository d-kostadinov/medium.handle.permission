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

    // State to track if the permission request has been processed
    var hasRequestedPermission by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Only display the permission status if the permission has been requested and processed
        when (val status = cameraPermissionState.status) {
            is PermissionStatus.Granted -> {
                Text("Camera permission granted. You can now use the camera.")
                Button(onClick = { navController.popBackStack() }, Modifier.padding(top = 16.dp)) {
                    Text("Go Back")
                }
            }
            is PermissionStatus.Denied -> {
                // Show rationale only after the user interacts with the permission dialog
                if (hasRequestedPermission) {
                    if (status.shouldShowRationale) {
                        Text("Camera permission is required to use this feature.")
                        Button(onClick = {
                            cameraPermissionState.launchPermissionRequest()
                        }) {
                            Text("Request Camera Permission")
                        }
                    } else {
                        // Only show the "Denied" message after the permission has been requested and denied
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
                    Text("Camera permission is required to use this feature.")

                    // Display the initial request button
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
