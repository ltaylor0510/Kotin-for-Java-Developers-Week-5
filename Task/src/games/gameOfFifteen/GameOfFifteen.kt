package games.gameOfFifteen

import board.Cell
import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'
 * (or choosing the corresponding run configuration).
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game =
        GameOfFifteen(initializer)

class GameOfFifteen(private val initializer: GameOfFifteenInitializer) : Game {
    val board = createGameBoard<Int?>(4)

    val winningOrder = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, null)

    private fun <T> sortedListOfCells(board: GameBoard<T>): List<Cell> {
        return board.getAllCells().sortedWith(compareBy<Cell> { it.i }.thenBy { it.j })
    }

    override fun initialize() {
        sortedListOfCells(board).forEachIndexed { index, cell ->
            if (index < initializer.initialPermutation.size)
                board[cell] = initializer.initialPermutation[index] else board[cell] = null
        }
    }

    override fun canMove(): Boolean {
        return true
    }

    override fun hasWon(): Boolean {
        return winningOrder == sortedListOfCells(board).map { board[it] }
    }

    override fun processMove(direction: Direction) {
        val emptyCell = board.find { it == null }
        when (direction) {
            Direction.UP -> with(board) {
                val neighbourCell = emptyCell?.getNeighbour(Direction.DOWN)
                makeMove(neighbourCell, emptyCell)
            }
            Direction.DOWN -> with(board) {
                val neighbourCell = emptyCell?.getNeighbour(Direction.UP)
                makeMove(neighbourCell, emptyCell)
            }
            Direction.RIGHT -> with(board) {
                val neighbourCell = emptyCell?.getNeighbour(Direction.LEFT)
                makeMove(neighbourCell, emptyCell)
            }
            Direction.LEFT -> with(board) {
                val neighbourCell = emptyCell?.getNeighbour(Direction.RIGHT)
                makeMove(neighbourCell, emptyCell)
            }
        }
    }

    private fun makeMove(neighbourCell: Cell?, emptyCell: Cell?) {
        if (neighbourCell != null && emptyCell != null) {
            board[emptyCell] = board[neighbourCell]
            board[neighbourCell] = null
        }
    }

    override fun get(i: Int, j: Int): Int? {
        return board.run { get(getCell(i, j)) }
    }
}

