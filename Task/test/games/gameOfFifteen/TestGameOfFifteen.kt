package games.gameOfFifteen

import board.Cell
import board.Direction
import board.GameBoard
import games.game.Game
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

operator fun <T> GameBoard<T>.get(i: Int, j: Int) = get(getCell(i, j))
private fun Cell?.asString() = if (this != null) "($i, $j)" else ""


class TestGameOfFifteenInitialize {
    @Test
    fun `initialize fills board as expected`() {
        val game = GameOfFifteen(RandomGameInitializer())
        game.initialize()
        assertEquals(null, game.board[4, 4])
        assertNotNull(game.board[1, 1])
        assertNotNull(game.board[1, 2])
        assertNotNull(game.board[1, 3])
        assertNotNull(game.board[1, 4])
        assertNotNull(game.board[2, 1])
        assertNotNull(game.board[2, 2])
        assertNotNull(game.board[2, 3])
        assertNotNull(game.board[2, 4])
        assertNotNull(game.board[3, 1])
        assertNotNull(game.board[3, 2])
        assertNotNull(game.board[3, 3])
        assertNotNull(game.board[3, 4])
        assertNotNull(game.board[4, 1])
        assertNotNull(game.board[4, 2])
        assertNotNull(game.board[4, 3])
    }
}

class TestGameOfFifteenHasWon {
    @Test
    fun `game without cell values in correct order has not won`() {
        val game = GameOfFifteen(RandomGameInitializer())
        game.initialize()
        assertFalse(game.hasWon())
    }

    @Test
    fun `game with cell values in correct order has won`() {
        val game = GameOfFifteen(RandomGameInitializer())
        val correctOrder = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)
        game.board.getAllCells().sortedWith(compareBy<Cell> { it.i }.thenBy { it.j })
                .forEachIndexed { index, cell -> if (index < correctOrder.size)
                    game.board[cell] = correctOrder[index]
                else game.board[cell] = null }
        assertTrue(game.hasWon())
    }

}


class TestGameOfFifteen {
    private fun Game.asString() =
            (1..4).joinToString("\n") { i ->
                (1..4).joinToString(" ") { j ->
                    get(i, j)?.let { "%2d".format(it) } ?: " -"
                }
            }

    class TestGameInitializer(
            override val initialPermutation: List<Int>
    ) : GameOfFifteenInitializer

    private fun testGame(initialPermutation: List<Int>, moves: List<Move>) {
        val game = newGameOfFifteen(TestGameInitializer(initialPermutation))
        game.initialize()

        for ((index, move) in moves.withIndex()) {
            if (move.direction == null) continue
            // checking the state after each move
            assertTrue(game.canMove(), "The move for game of fifteen should be always possible")
            game.processMove(move.direction)
            val prev = moves[index - 1].board
            assertEquals(move.board, game.asString(), "Wrong result after pressing ${move.direction} " +
                    "for\n$prev\n")
        }
    }

    data class Move(
            val direction: Direction?,
            val initialBoard: String
    ) {
        val board: String = initialBoard.trimMargin()
    }

    @Test
    fun testInitialState() {
        val initializer = RandomGameInitializer()
        assertNotEquals((1..15).toList(), initializer.initialPermutation, "The initial permutation must not be trivial")
    }

    @Test
    fun `test that moves are correctly processed`() {
        val game = GameOfFifteen(TestGameInitializer(listOf(1, 3, 5, 7, 9, 11, 13, 15, 2, 4, 6, 8, 10, 12, 14)))
        game.initialize()
        game.processMove(Direction.DOWN)
        assertEquals(8, game.board[4, 4])
        assertEquals(null, game.board[3, 4])
        game.processMove(Direction.RIGHT)
        assertEquals(6, game.board[3, 4])
        assertEquals(null, game.board[3, 3])
        game.processMove(Direction.UP)
        assertEquals(14, game.board[3, 3])
        assertEquals(null, game.board[4, 3])
        game.processMove(Direction.LEFT)
        assertEquals(8, game.board[4, 3])
        assertEquals(null, game.board[4, 4])
    }


    @Test
    fun testMoves() {
        testGame(listOf(3, 6, 13, 15, 7, 5, 8, 4, 14, 11, 12, 1, 10, 9, 2),
                listOf(
                        Move(null, """
            | 3  6 13 15
            | 7  5  8  4
            |14 11 12  1
            |10  9  2  -"""),
                        Move(Direction.RIGHT, """
            | 3  6 13 15
            | 7  5  8  4
            |14 11 12  1
            |10  9  -  2"""),
                        Move(Direction.DOWN, """
            | 3  6 13 15
            | 7  5  8  4
            |14 11  -  1
            |10  9 12  2"""),
                        Move(Direction.LEFT, """
            | 3  6 13 15
            | 7  5  8  4
            |14 11  1  -
            |10  9 12  2"""),
                        Move(Direction.UP, """
            | 3  6 13 15
            | 7  5  8  4
            |14 11  1  2
            |10  9 12  -"""),
                        Move(Direction.RIGHT, """
            | 3  6 13 15
            | 7  5  8  4
            |14 11  1  2
            |10  9  - 12""")
                ))
    }

    @Test
    fun testWinning() {
        val game = newGameOfFifteen(TestGameInitializer(
                (1..15).toList()))
        game.initialize()
        assertTrue(game.hasWon(), "The player should win when the numbers are in order")
    }
}