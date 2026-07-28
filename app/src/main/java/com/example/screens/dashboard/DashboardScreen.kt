package com.example.first.screens.dashboard


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController



@Composable
fun DashboardScreen(
    navController: NavController
) {


    var balance by remember {
        mutableStateOf(120)
    }


    var lastDestination by remember {
        mutableStateOf("No trip yet")
    }


    var lastFare by remember {
        mutableStateOf(0)
    }



    /*
       Receive QR data from ScannerScreen

       Example QR:
       Bus:42|From:City|To:americal|Fare:5

    */

    LaunchedEffect(Unit) {


        val data =

            navController
                .currentBackStackEntry
                ?.savedStateHandle
                ?.get<String>("scan_result")



        println("DASHBOARD DATA = $data")



        if(data != null) {


            val parts =
                data.split("|")



            val destination =

                parts.find {

                    it.startsWith("To:")

                }
                    ?.substringAfter("To:")



            val fare =

                parts.find {

                    it.startsWith("Fare:")

                }
                    ?.substringAfter("Fare:")
                    ?.toIntOrNull()



            if(destination != null) {

                lastDestination = destination

            }



            if(fare != null) {

                lastFare = fare

                balance -= fare

            }



            // remove old scan data
            navController
                .currentBackStackEntry
                ?.savedStateHandle
                ?.remove<String>("scan_result")


        }


    }




    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally

    ) {



        Text(

            text = "Passenger Dashboard",

            style = MaterialTheme.typography.headlineMedium

        )



        Spacer(
            modifier = Modifier.height(25.dp)
        )




        // Balance Card

        Card(

            modifier = Modifier
                .fillMaxWidth()

        ) {


            Column(

                modifier = Modifier
                    .padding(20.dp)

            ) {


                Text(
                    text = "💰 Balance"
                )


                Spacer(
                    modifier = Modifier.height(8.dp)
                )


                Text(

                    text = "₹$balance",

                    style = MaterialTheme.typography.headlineMedium

                )


            }

        }




        Spacer(
            modifier = Modifier.height(20.dp)
        )





        // Last Trip Card

        Card(

            modifier = Modifier
                .fillMaxWidth()

        ) {


            Column(

                modifier = Modifier
                    .padding(20.dp)

            ) {



                Text(
                    text = "🚌 Last Trip"
                )



                Spacer(
                    modifier = Modifier.height(10.dp)
                )



                if(lastDestination == "No trip yet") {


                    Text(
                        text = "🚍 No trips yet. Scan your first ticket!"
                    )


                }

                else {


                    Text(
                        text = "Destination: $lastDestination"
                    )


                    Text(
                        text = "Fare: ₹$lastFare"
                    )


                }



            }


        }




        Spacer(
            modifier = Modifier.height(30.dp)
        )




        Button(

            onClick = {


                navController.navigate("scanner")


            },

            modifier = Modifier
                .fillMaxWidth()

        ) {


            Text(
                text = "Scan Ticket"
            )


        }


    }


}