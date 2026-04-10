package com.pulse.plannex.features.accessControl.data.datasource

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.pulse.plannex.features.accessControl.domain.repositories.QrScanner
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class MLKitQrScanner : QrScanner {
    private val scanner = BarcodeScanning.getClient()

    override fun startScanning(): Flow<String?> = callbackFlow {
        // Implementación de flujo si fuera necesario
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
                    .addOnCompleteListener { imageProxy.close() }
            } else {
                imageProxy.close()
            }
        }
    }
}
