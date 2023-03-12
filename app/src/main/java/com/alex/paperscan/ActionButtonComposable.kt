package com.alex.paperscan

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ActionButton(buttonTitle: String, isMergeEnabled: Boolean, onClick: () -> Unit) {
    val textStyle = MaterialTheme.typography.button.copy(fontSize = 18.sp)
    val textColor = if (isMergeEnabled) Color.Blue else Color.Gray
    Text(
        text = buttonTitle,
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .clickable(
                enabled = isMergeEnabled,
                onClick = onClick
            ),
        style = textStyle.copy(color = textColor),
    )
}

@Preview(showBackground = true)
@Composable
fun ActionButtonPreview() {
    ActionButton(buttonTitle = "action button", isMergeEnabled = true, onClick = {})
}
