package com.alex.paperscan

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TileCellAnimated(viewModel: PaperScanViewModel, tile: Tile) {
    val isSelected = viewModel.selectedTiles.contains(tile)
    val isNotFirst = viewModel.selectedTiles.indexOf(tile) > 0
    val firstSelectedTile = viewModel.selectedTiles.firstOrNull()
    val density = LocalDensity.current

    var computedOffset = Offset.Zero
    if (viewModel.mergePressed.value && isSelected && isNotFirst && firstSelectedTile != null) {
        computedOffset = viewModel.computeOffsetCoordinates(firstSelectedTile, tile)
    }

    val offset: Offset by animateOffsetAsState(
        targetValue = if (viewModel.mergePressed.value && isSelected && isNotFirst) computedOffset else Offset.Zero,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
    )

    val visibleState = remember { MutableTransitionState(false).apply { targetState = true } }

    AnimatedVisibility(
        visibleState = visibleState,
        enter = fadeIn(animationSpec = TweenSpec(500, 0)),
        exit = fadeOut(animationSpec = TweenSpec(500, 0, FastOutLinearInEasing))
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .onGloballyPositioned { layoutCoordinates ->
                    with(density) {
                        layoutCoordinates
                            .positionInWindow()
                            .run {
                                tile.position = DpOffset(x = x.toDp(), y = y.toDp())
                                tile.positionF = Offset(x = x, y = y)
                            }
                    }
                }
                .padding(top = 40.dp, bottom = 40.dp)
                .offset(
                    x = with(LocalDensity.current) { offset.x.toDp() },
                    y = with(LocalDensity.current) { offset.y.toDp() })
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
    GridScreenAnimated(viewModel = PaperScanViewModel())
}

