package com.alex.paperscan

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.alex.paperscan.ui.theme.PaperScanTheme

class MainActivity : ComponentActivity() {
    private val viewModel: PaperScanViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PaperScanTheme(darkTheme = false) {
                MainScreen(viewModel)
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(viewModel: PaperScanViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Select And Merge",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                actions = {
                    ActionButton(
                        buttonTitle = "Merge",
                        isMergeEnabled = viewModel.selectedTiles.size > 1,
                        onClick = {
                            viewModel.mergeTiles()
                        }
                    )

                    ActionButton(
                        buttonTitle = "Reset",
                        isMergeEnabled = true,
                        onClick = {
                            viewModel.initializeTiles()
                        }
                    )

                },
                backgroundColor = Color.LightGray,
            )
        },
        modifier = Modifier.background(Color.White)
    ) {
        GridScreenAnimated(viewModel = viewModel)
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(viewModel = PaperScanViewModel())
}











