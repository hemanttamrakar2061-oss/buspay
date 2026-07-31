package com.example.first.screens

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.annotation.OptIn

import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview

import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

import androidx.core.content.ContextCompat

import androidx.navigation.NavController
import com.google.mlkit.vision.barcode.BarcodeScanner

import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

import java.util.concurrent.Executors


@Composable
fun ScannerScreen(
    navController: NavController
) {


    val context = LocalContext.current

    val lifecycleOwner = LocalLifecycleOwner.current


    var scanned by remember {
        mutableStateOf(false)
    }



    val hasPermission =
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED



    if(!hasPermission){

        Text(
            text = "Camera permission required"
        )

        return
    }




    Column(

        modifier = Modifier
            .fillMaxSize(),

        horizontalAlignment = Alignment.CenterHorizontally

    ) {



        AndroidView(

            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp),


            factory = {


                val previewView =
                    PreviewView(it)



                val cameraProviderFuture =
                    ProcessCameraProvider
                        .getInstance(it)



                cameraProviderFuture.addListener({


                    val cameraProvider =
                        cameraProviderFuture.get()



                    val preview =
                        Preview.Builder()
                            .build()



                    preview.setSurfaceProvider(

                        previewView
                            .surfaceProvider

                    )




                    val scanner =
                        BarcodeScanning
                            .getClient()





                    val imageAnalyzer =


                        ImageAnalysis.Builder()

                            .setBackpressureStrategy(

                                ImageAnalysis
                                    .STRATEGY_KEEP_ONLY_LATEST

                            )

                            .build()



                            .also { analysis ->



                                analysis.setAnalyzer(


                                    Executors
                                        .newSingleThreadExecutor()


                                ){ imageProxy ->



                                    if(!scanned){


                                        processImage(

                                            imageProxy,

                                            scanner

                                        ){ result ->



                                            scanned = true



                                            navController
                                                .previousBackStackEntry
                                                ?.savedStateHandle
                                                ?.set(

                                                    "scan_result",

                                                    result

                                                )



                                            navController
                                                .popBackStack()


                                        }


                                    }
                                    else{


                                        imageProxy.close()

                                    }



                                }


                            }





                    cameraProvider.unbindAll()



                    cameraProvider.bindToLifecycle(

                        lifecycleOwner,

                        CameraSelector.DEFAULT_BACK_CAMERA,

                        preview,

                        imageAnalyzer

                    )



                },

                    ContextCompat
                        .getMainExecutor(it)

                )



                previewView


            }

        )





        Spacer(

            modifier = Modifier
                .height(20.dp)

        )





        Text(

            text = "Scan Bus Ticket",

            modifier = Modifier
                .fillMaxWidth(),

            textAlign = TextAlign.Center

        )


    }


}






@OptIn(ExperimentalGetImage::class)

private fun processImage(

    imageProxy: ImageProxy,

    scanner: BarcodeScanner,

    onResult: (String)->Unit

){



    val mediaImage =
        imageProxy.image



    if(mediaImage == null){

        imageProxy.close()

        return

    }





    val image =

        InputImage.fromMediaImage(

            mediaImage,

            imageProxy.imageInfo.rotationDegrees

        )





    scanner.process(image)


        .addOnSuccessListener { barcodes ->



            for(barcode in barcodes){



                val value =
                    barcode.rawValue



                if(value != null){



                    Log.d(

                        "ML_QR",

                        value

                    )



                    onResult(value)



                    break

                }


            }


        }



        .addOnFailureListener {


            Log.d(

                "ML_QR",

                "Scan Error ${it.message}"

            )

        }



        .addOnCompleteListener {


            imageProxy.close()


        }


}