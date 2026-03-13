package com.pulse.plannex.features.accessControl.data

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.pulse.plannex.features.accessControl.domain.QrScanner
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.Executors

class MLKitQrScanner : QrScanner {
    private val scanner = BarcodeScanning.getClient()
    private val analysisExecutor = Executors.newSingleThreadExecutor()

    override fun startScanning(): Flow<String?> = callbackFlow {
        // This is a simplified version. In a real app, this would be attached to CameraX ImageAnalysis
        // For the flow to work, we need an ImageAnalysis.Analyzer
        // However, startScanning is usually called from the UI where the camera is.
        // Let's rethink the interface or provide a way to process images.
        
        // Actually, for MVVM, the ViewModel might just handle the result.
        // Let's provide an analyzer instead.
        close()
    }

    @SuppressLint("UnsafeOptInUsageError")
    fun createAnalyzer(onQrCodeScanned: (String) -> Unit): ImageAnalysis.Analyzer {
        return ImageAnalysis.Analyzer { imageProxy ->
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                scanner.process(image)
                    .addOnSuccessListener { barcodes ->
                        for (barcode in barcodes) {
                            barcode.rawValue?.let { onQrCodeScanned(it) }
                        }
                    }
                    .addOnCompleteListener {
                        imageProxy.close()
                    }
            } else {
                imageProxy.close()
            }
        }
    }
}
