package com.caesar84mx.textreader.ui.home.view

import android.annotation.SuppressLint
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.caesar84mx.textreader.ui.home.viewmodel.HomeViewModel
import com.caesar84mx.textreader.ui.theme.TextReaderTheme

@ExperimentalGetImage
@Composable
fun CameraView(
    imageCapture: ImageCapture,
    modifier: Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    AndroidView(
        factory = { ctx ->
            val cameraController = LifecycleCameraController(context).apply { bindToLifecycle(lifecycleOwner) }
            val preview = PreviewView(ctx).apply { controller = cameraController }
            val executor = ContextCompat.getMainExecutor(ctx)

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                bindPreview(
                    lifecycleOwner,
                    preview,
                    cameraProvider,
                    imageCapture
                )
            }, executor)
            preview
        },
        modifier = modifier,
    )
}

@SuppressLint("UnsafeExperimentalUsageError")
private fun bindPreview(
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView,
    cameraProvider: ProcessCameraProvider,
    imageCapture: ImageCapture
) {
    val preview = Preview.Builder()
        .build()
        .also { preview ->
            preview.setSurfaceProvider(previewView.surfaceProvider)
        }

    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
        .build()

    cameraProvider.unbindAll()
    cameraProvider.bindToLifecycle(
        lifecycleOwner,
        cameraSelector,
        imageCapture,
        preview
    )
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun CameraView_Preview() {
    TextReaderTheme {
        val vm = HomeViewModel()
        CameraView(
            imageCapture = vm.imageCapture,
            modifier = Modifier.fillMaxSize()
        )
    }
}