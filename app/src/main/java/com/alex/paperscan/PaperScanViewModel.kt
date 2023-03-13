package com.alex.paperscan

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.DpOffset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class Tile(
    var id: Int,
    var position: DpOffset = DpOffset.Unspecified,
    var positionF: Offset = Offset.Unspecified
)

class PaperScanViewModel : ViewModel() {
    private val maxTileCount = 10
    private val minTileCount = 10

    var tiles = mutableStateListOf<Tile>()
    val selectedTiles = mutableStateListOf<Tile>()
    var mergePressed = mutableStateOf(false)

    init {
        initializeTiles()
    }

    fun initializeTiles() {
        viewModelScope.launch {
            val randomTileCount = (minTileCount..maxTileCount).random()
            tiles.clear()
            selectedTiles.clear()

            for (i in 0 until randomTileCount) {
                val newTile = Tile(id = i + 1)
                tiles.add(newTile)
                delay(500) // Wait for the previous tiles to finish animating
            }
        }
    }

    fun selectTile(tile: Tile) {
        if (!selectedTiles.contains(tile)) {
            selectedTiles.add(tile)
        }
    }

    fun deselectTile(tile: Tile) {
        selectedTiles.remove(tile)
    }

    fun mergeTiles() {
        viewModelScope.launch {
            mergePressed.value = true
            //duplicate the list of selected tiles
            val selectedTilesList = selectedTiles.toList()

            //remember the original tile count for later
            val originalTileCount = tiles.size

            delay(1000) //give time for tile transition animations

            //REQ: •	All selected tiles but the first one are removed from the grid.
            for (i in selectedTilesList.size - 1 downTo 1) {
                val tile = selectedTilesList[i]
                tiles.remove(tile)
            }

            //REQ: •	And at the same time the rest tiles are compacted right to left and bottom to top to get rid of the holes appeared due to removing tiles.
            mergePressed.value = false
            delay(500) //give time for compacting animations

            //REQ: •	At the same time new tiles are added at the end of the grid to keep the overall number of tiles equal to original number.
            val tilesToAdd = originalTileCount - tiles.size
            for (i in 0 until tilesToAdd) {
                val tileWithHighestId = tiles.maxByOrNull { it.id }
                tileWithHighestId?.let {
                    tiles.add(Tile(id = tileWithHighestId.id + 1))
                }
                delay(500) //give time for tile add animations
            }

            //REQ: •	At the same time the first selected tile is deselected
            selectedTiles.clear()
        }
    }

    fun computeOffsetCoordinates(firstSelectedTile: Tile, tile: Tile): Offset {
        var computedX = 0F
        var computedY = 0F
        //move tiles backward if first tile is before current tile
        if (firstSelectedTile.id < tile.id) {
            //compute X axis
            if (firstSelectedTile.positionF.x < tile.positionF.x) {
                //if the first tile number is smaller we substract
                computedX = -(tile.positionF.x - firstSelectedTile.positionF.x)
            } else if (firstSelectedTile.positionF.x > tile.positionF.x) {
                //if the first tile number is bigger we substract
                computedX = firstSelectedTile.positionF.x - tile.positionF.x
            }

            //compute Y axis
            if (firstSelectedTile.positionF.y < tile.positionF.y) {
                //if the first tile number is smaller we substract
                computedY = -(tile.positionF.y - firstSelectedTile.positionF.y)
            } else if (firstSelectedTile.positionF.y > tile.positionF.y) {
                //if the first tile number is bigger we add
                computedY = tile.positionF.y + firstSelectedTile.positionF.y
            }
        }
        //move tiles forward if first tile is after current tile
        else {
            //compute X axis
            if (firstSelectedTile.positionF.x < tile.positionF.x) {
                //if the first tile number is smaller we substract
                computedX = -(tile.positionF.x - firstSelectedTile.positionF.x)
            } else if (firstSelectedTile.positionF.x > tile.positionF.x) {
                //if the first tile number is bigger we substract
                computedX = firstSelectedTile.positionF.x - tile.positionF.x
            }

            //compute Y axis
            if (firstSelectedTile.positionF.y < tile.positionF.y) {
                //if the first tile number is bigger we substract
                computedY = -(firstSelectedTile.positionF.y - tile.positionF.y)
            } else if (firstSelectedTile.positionF.y > tile.positionF.y) {
                //if the first tile number is bigger we substract
                computedY = firstSelectedTile.positionF.y - tile.positionF.y
            }
        }

        Log.d("PaperScanViewModel", "FIRST TILE: ${firstSelectedTile.id} with offset F: X ${firstSelectedTile.positionF.x} Y ${firstSelectedTile.positionF.y}")
        Log.d("PaperScanViewModel", "TILE: ${tile.id} with offset F: X ${tile.positionF.x} Y ${tile.positionF.y}")
        Log.d("PaperScanViewModel", "OFFSET: ${tile.id} new coords F: X $computedX Y $computedY")
        return Offset(computedX, computedY)
    }
}


