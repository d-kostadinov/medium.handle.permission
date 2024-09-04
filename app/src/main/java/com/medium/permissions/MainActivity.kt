package com.medium.permissions

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.medium.permissions.ui.CameraAndLocationPermissionsHandlingScreen
import com.medium.permissions.ui.CameraPermissionHandlingScreen
import com.medium.permissions.ui.theme.MediumPermissionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MediumPermissionTheme {
                val navController = rememberNavController()
                NavigationComponent(navController)
            }
        }
    }
}

@Composable
fun NavigationComponent(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "main") {
        composable("main") { MainScreen(navController) }
        composable("cameraPermission") { CameraPermissionHandlingScreen(navController) }
        composable("cameraAndLocationPermissions") {
            CameraAndLocationPermissionsHandlingScreen(navController)
        }
    }
}

@Composable
fun MainScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = { navController.navigate("cameraPermission") },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text("Handle Camera Permission", fontSize = 16.sp)
        }
        Button(onClick = { navController.navigate("cameraAndLocationPermissions") }) {
            Text("Handle Both Camera and Location Permissions", fontSize = 16.sp)
        }
    }
}
