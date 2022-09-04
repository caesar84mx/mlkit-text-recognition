package com.caesar84mx.textreader.ui.home.viewmodel

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.media.Image
import android.util.Size
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY
import androidx.camera.core.ImageCapture.FLASH_MODE_OFF
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.impl.utils.executor.CameraXExecutors
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.caesar84mx.textreader.common.data.Status
import com.caesar84mx.textreader.util.toBitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class HomeViewModel: ViewModel() {
    var status: Status by mutableStateOf(Status.Idle)
        private set

    var captureButtonEnabled: Boolean by mutableStateOf(true)
        private set

    var textElements: List<String> by mutableStateOf(listOf())
        private set

    var showCameraPreview: Boolean by mutableStateOf(true)
        private set

    var capturedImage: Bitmap? by mutableStateOf(null)
        private set

    val imageCapture: ImageCapture = ImageCapture.Builder()
        .setCaptureMode(CAPTURE_MODE_MAXIMIZE_QUALITY)
        .setTargetResolution(Size(IMAGE_WIDTH, IMAGE_HEIGHT))
        .setFlashMode(FLASH_MODE_OFF)
        .build()

    @SuppressLint("RestrictedApi")
    private val cameraExecutor = CameraXExecutors.ioExecutor()

    fun onRecognizeClick() {
        if (showCameraPreview) {
            status = Status.Loading
            captureButtonEnabled = false

            imageCapture.takePicture(
                cameraExecutor,
                object : ImageCapture.OnImageCapturedCallback() {
                    override fun onCaptureSuccess(image: ImageProxy) {
                        onImageCaptured(image.image)
                    }

                    override fun onError(exception: ImageCaptureException) {
                        onImageCaptureError(exception.message ?: "An error occurred")
                    }
                }
            )
        } else {
            showCameraPreview = true
            textElements = listOf()
        }
    }

    private fun onImageCaptured(image: Image?) {
        showCameraPreview = false
        captureButtonEnabled = true
        status = Status.Success

        if (image == null) {
            status = Status.Error(message = "Failed to take photo, please try again")
        } else {
            capturedImage = image.toBitmap()
            recognizeImage()
        }
    }

    private fun recognizeImage() {
        capturedImage?.let { image ->
            val input = InputImage.fromBitmap(image, 0)
            TextRecognition.getClient(
                TextRecognizerOptions.Builder().build()
            ).process(input)
                .addOnSuccessListener(::processTextRecognitionResult)
                .addOnFailureListener { exception ->
                    onImageCaptureError(exception.message ?: "Failed to recognize text. Please, try again.")
                }
        }
    }

    private fun processTextRecognitionResult(output: Text) {
        val blocks = output.textBlocks
        if (blocks.isEmpty()) {
            onImageCaptureError("No text found")
        } else {
            textElements = listOf()
            textElements = blocks.flatMap { block ->
                block.lines.map { line -> line.text }
            }
        }
    }

    private fun onImageCaptureError(message: String) {
        captureButtonEnabled = true
        showCameraPreview = true
        status = Status.Error(message)
    }

    companion object {
        const val IMAGE_HEIGHT = 1280
        const val IMAGE_WIDTH = 720
    }
}