package com.caesar84mx.textreader.ui.home.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.caesar84mx.textreader.common.data.Status
import com.caesar84mx.textreader.ui.home.viewmodel.HomeViewModel
import com.caesar84mx.textreader.ui.theme.Highlight
import com.caesar84mx.textreader.ui.theme.TextReaderTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView() {
    val viewModel: HomeViewModel = getViewModel()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    when (val status = viewModel.status) {
        is Status.Error -> {
            scope.launch {
                snackbarHostState.showSnackbar(status.message)
            }
        }
        else -> { /* no-op */ }
    }

    Surface {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState) { data ->
                    val buttonColor = ButtonDefaults.textButtonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.error
                    )

                    Snackbar(
                        action = {
                            TextButton(
                                onClick = data::dismiss,
                                colors = buttonColor
                            ) { Text(data.visuals.actionLabel ?: "Ok") }
                        }
                    ) {
                        Text(data.visuals.message)
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = { FAV(viewModel = viewModel) }
        ) { padding ->
            if (viewModel.showCameraPreview) {
                CameraView(
                    imageCapture = viewModel.imageCapture,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Column(Modifier.fillMaxSize()) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(top = padding.calculateTopPadding(), bottom = 10.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Recognition Results",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    LazyColumn(
                        modifier = Modifier.padding(10.dp)
                    ) {
                        items(viewModel.textElements) { line ->
                            Text(
                                text = line,
                                modifier = Modifier
                                    .padding(vertical = 5.dp)
                                    .background(Highlight)
                                    .clickable {
                                        clipboardManager.setText(AnnotatedString(line))
                                        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                                    }
                            )
                        }
                    }
                }
            }
        }
    }
}
