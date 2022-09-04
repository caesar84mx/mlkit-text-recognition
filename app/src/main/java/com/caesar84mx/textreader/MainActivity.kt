package com.caesar84mx.textreader

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.caesar84mx.textreader.ui.home.view.HomeView
import com.caesar84mx.textreader.ui.theme.TextReaderTheme
import com.caesar84mx.textreader.util.REQUEST_CODE_PERMISSIONS
import com.caesar84mx.textreader.util.allPermissionsGranted
import com.caesar84mx.textreader.util.requestPermissions

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (allPermissionsGranted) {
            setViewContent()
        } else {
            requestPermissions()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted) {
                setViewContent()
            } else {
                Toast.makeText(
                    this,
                    "Camera permission not granted yet",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun setViewContent() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()

        setContent {
            TextReaderTheme {
                HomeView()
            }
        }
    }
}
