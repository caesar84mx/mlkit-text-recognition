package com.caesar84mx.textreader.ui.home.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.caesar84mx.textreader.R
import com.caesar84mx.textreader.common.data.Status
import com.caesar84mx.textreader.ui.home.viewmodel.HomeViewModel

@Composable
fun FAV(viewModel: HomeViewModel) {
    Button(
        enabled = viewModel.captureButtonEnabled,
        onClick = viewModel::onRecognizeClick,
        shape = CircleShape,
        modifier = Modifier.height(70.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when(viewModel.status) {
                Status.Loading -> CircularProgressIndicator()
                else -> Image(
                    painter = painterResource(
                        id = if (viewModel.showCameraPreview) {
                            R.drawable.ic_text_snippet
                        } else {
                            R.drawable.ic_outline_photo_camera_24
                        }
                    ),
                    contentDescription = "Recognize"
                )
            }
        }
    }
}