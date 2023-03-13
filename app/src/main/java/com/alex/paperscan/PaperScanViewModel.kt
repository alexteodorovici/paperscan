package com.alex.paperscan

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
    private val maxTileCount = 6
    private val minTileCount = 4

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

            //REQ: •	All selected tiles but the first one are removed from the grid.
            // Remove all selected tiles but the first one from the viewModel's tiles list
            //        tiles.removeAll(selectedTilesList.drop(1))
            for (i in selectedTilesList.size - 1 downTo 1) {
                val tile = selectedTilesList[i]
                delay(1000) // delay each removal by 1000ms
                tiles.remove(tile)
//                delay(1000)
            }

            //REQ: •	At the same time new tiles are added at the end of the grid to keep the overall number of tiles equal to original number.
            //REQ: •	And at the same time the rest tiles are compacted right to left and bottom to top to get rid of the holes appeared due to removing tiles.
            // Add new tiles to the end of the list to maintain the original number of tiles
            val tilesToAdd = originalTileCount - tiles.size
            for (i in 0 until tilesToAdd) {
                val tileWithHighestId = tiles.maxByOrNull { it.id }
                tileWithHighestId?.let {
                    tiles.add(Tile(id = tileWithHighestId.id + 1))
                }
                delay(500)
            }

            //REQ: •	At the same time the first selected tile is deselected
            // Clear the list of selected tiles and add the first selected tile if it exists
            selectedTiles.clear()
            delay(3000L)
            mergePressed.value = false
        }
    }


//    fun mergeTiles() {
//        viewModelScope.launch {
//            //duplicate the list of selected tiles
//            val selectedTilesList = selectedTiles.toList()
//
//            //remember the original tile count for later
//            val originalTileCount = tiles.size
//
//            //REQ: •	All selected tiles but the first one are removed from the grid.
//            // Remove all selected tiles but the first one from the viewModel's tiles list
//            //        tiles.removeAll(selectedTilesList.drop(1))
//            for (i in selectedTilesList.size - 1 downTo 1) {
//                val tile = selectedTilesList[i]
//                tiles.remove(tile)
//                delay(1000L)
//            }
//
//            //REQ: •	At the same time new tiles are added at the end of the grid to keep the overall number of tiles equal to original number.
//            //REQ: •	And at the same time the rest tiles are compacted right to left and bottom to top to get rid of the holes appeared due to removing tiles.
//            // Add new tiles to the end of the list to maintain the original number of tiles
//            val tilesToAdd = originalTileCount - tiles.size
//            for (i in 0 until tilesToAdd) {
//                val tileWithHighestId = tiles.maxByOrNull { it.id }
//                tileWithHighestId?.let {
//                    tiles.add(Tile(id = tileWithHighestId.id + 1))
//                }
//                delay(1000L) // delay each removal by 1000ms
//            }
//
//            //REQ: •	At the same time the first selected tile is deselected
//            // Clear the list of selected tiles and add the first selected tile if it exists
//            selectedTiles.clear()
//            delay(1000L) // delay each removal by 1000ms
//        }
//    }


}


