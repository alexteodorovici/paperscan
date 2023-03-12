package com.alex.paperscan

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GridScreenAnimated(viewModel: PaperScanViewModel) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
    ) {
        items(count = viewModel.tiles.size) { index ->
            val tile = viewModel.tiles[index]
            TileCellAnimated(viewModel = viewModel, tile)
        }
    }
}

@Composable
fun TileCellAnimated(viewModel: PaperScanViewModel, tile: Tile) {
    val isSelected = viewModel.selectedTiles.contains(tile)

    val visibleState = remember {
        MutableTransitionState(false).apply {
            targetState = true // start the animation immediately
        }
    }

    AnimatedVisibility(
        visibleState = visibleState,
        enter = fadeIn(animationSpec = TweenSpec(1000, 0)),
        exit = fadeOut(animationSpec = TweenSpec(1000, 0, FastOutLinearInEasing))
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .padding(top = 40.dp, bottom = 40.dp)
                .clickable {
                    if (isSelected) {
                        viewModel.deselectTile(tile)
                    } else {
                        viewModel.selectTile(tile)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Row(
                Modifier
                    .width(100.dp)
                    .height(150.dp)
                    .background(if (isSelected) Color.DarkGray else Color.LightGray),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) { Text(text = tile.id.toString(), fontSize = 18.sp) }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GridScreenAnimatedPreview() {
    val viewModel = PaperScanViewModel()
    GridScreenAnimated(viewModel = viewModel)
}

