package com.example.first.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.first.screens.LoginScreen
import com.example.first.screens.ScannerScreen
import com.example.first.screens.dashboard.DashboardScreen
import com.example.first.screens.dashboard.StaffDashboardScreen


@Composable
fun AppNavigation() {

    val navController = rememberNavController()


    NavHost(
        navController = navController,
        startDestination = "login"
    ) {


        composable("login") {

            LoginScreen(navController)

        }


        composable("dashboard") {

            DashboardScreen(navController)

        }


        composable("scanner") {

            ScannerScreen(navController)

        }


        composable("staff") {

            StaffDashboardScreen()

        }

    }
}