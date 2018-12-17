package games.game2048

import board.Cell
import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


operator fun <T> GameBoard<T>.get(i: Int, j: Int) = get(getCell(i, j))
operator fun <T> GameBoard<T>.set(i: Int, j: Int, value: T) = set(getCell(i, j), value)

class TestAddNewValue {
    class TestGame2048InitializerSimple(private val move: SimpleMove?) : Game2048Initializer<Int> {
        override fun nextValue(board: GameBoard<Int?>): Pair<Cell, Int>? {
            return if (move == null) {
                null
            } else {
                board.getCell(move.position.first, move.position.second) to move.value
            }
        }
    }

    data class SimpleMove(
            val position: Pair<Int, Int>,
            val value: Int)

    @Test
    fun `value produced by initializer is added to the correct cell on the board`() {
        val board = createGameBoard<Int?>(2)
        val initializer = TestGame2048InitializerSimple(move = SimpleMove(Pair(1, 1), 2))
        board.addNewValue(initializer)
        assertEquals(2, board[1, 1])
    }

    @Test
    fun `full board does not cause the game to crash`() {
        val board = createGameBoard<Int?>(2)
        val initializer = TestGame2048InitializerSimple(move = null)

        board.addNewValue(initializer)

        assertEquals(null, board[1, 1])
        assertEquals(null, board[1, 2])
        assertEquals(null, board[2, 1])
        assertEquals(null, board[2, 2])
    }
}

class TestGame2048 {
    private fun Game.asString() =
            (1..4).joinToString("\n") { i ->
                (1..4).joinToString(" ") { j ->
                    "${get(i, j) ?: "-"}"
                }
            }

    class TestGame2048Initializer(moves: List<Move>) : Game2048Initializer<Int> {
        val iterator = moves.iterator()
        override fun nextValue(board: GameBoard<Int?>): Pair<Cell, Int> {
            val move = iterator.next()
            return board.getCell(move.position.first, move.position.second) to move.value
        }
    }

    private fun testGame(moves: List<Move>) {
        val game = newGame2048(TestGame2048Initializer(moves))
        game.initialize()
        run {
            // checking the state after initialization
            val first = moves[0]
            val second = moves[1]
            assertEquals(second.board, game.asString(), "Wrong result after board initialization " +
                    "by '${first.value}' at ${first.cell} and " +
                    "'${second.value}' at ${second.cell}")
        }

        for ((index, move) in moves.withIndex()) {
            if (move.direction == null) continue
            // checking the state after each move
            game.processMove(move.direction)
            val prev = moves[index - 1].board
            assertEquals(move.board, game.asString(), "Wrong result after moving ${move.direction} " +
                    "and then adding '${move.value}' to ${move.cell} " +
                    "for\n$prev\n")
        }
    }

    data class Move(
            val position: Pair<Int, Int>,
            val value: Int,
            val direction: Direction?,
            val initialBoard: String
    ) {
        val cell: String
            get() = "Cell(${position.first}, ${position.second})"

        val board: String = initialBoard.trimMargin()
    }

    @Test
    fun `when two values on board that are equal collide they merge`() {
        val board = createGameBoard<Int?>(2)
        board[1, 1] = 2
        board[1, 2] = 2
        board.moveValues(Direction.LEFT)
        assertEquals(4, board[1, 1])
        assertEquals(null, board[1, 2])
    }

    @Test
    fun `when two values on board that are equal collide they merge to the right`() {
        val board = createGameBoard<Int?>(2)
        board[1, 1] = 4
        board[1, 2] = 4
        board.moveValues(Direction.RIGHT)
        assertEquals(8, board[1, 2])
        assertEquals(null, board[1, 1])
    }

    @Test
    fun `when two values on board that are equal collide they merge up`() {
        val board = createGameBoard<Int?>(2)
        board[1, 1] = 2
        board[2, 1] = 2
        board.moveValues(Direction.UP)
        assertEquals(4, board[1, 1])
        assertEquals(null, board[2, 1])
    }

    @Test
    fun `when two values on board that are equal collide they merge down`() {
        val board = createGameBoard<Int?>(2)
        board[1, 1] = 4
        board[2, 1] = 4
        board.moveValues(Direction.DOWN)
        assertEquals(null, board[1, 1])
        assertEquals(8, board[2, 1])
    }

    @Test
    fun `when board is full and no values can be combined false is returned`() {
        val board = createGameBoard<Int?>(2)
        board[1, 1] = 2
        board[1, 2] = 4
        board[2, 1] = 8
        board[2, 2] = 16
        assertFalse(board.moveValues(Direction.LEFT))
        assertFalse(board.moveValues(Direction.RIGHT))
        assertFalse(board.moveValues(Direction.DOWN))
        assertFalse(board.moveValues(Direction.UP))
    }

    @Test
    fun `when some cells can move but others cannot true is returned`() {
        val board = createGameBoard<Int?>(2)
        board[1, 1] = 2
        board[1, 2] = 4
        board[2, 1] = 2
        board[2, 2] = 16
        assertFalse(board.moveValues(Direction.LEFT))
        assertFalse(board.moveValues(Direction.RIGHT))
        assertTrue(board.moveValues(Direction.DOWN))
        assertTrue(board.moveValues(Direction.UP))
    }

    @Test
    fun testMoves() {
        testGame(listOf(
                Move(Pair(1, 1), 2, null, """
            |2 - - -
            |- - - -
            |- - - -
            |- - - -"""),
                Move(Pair(1, 4), 2, null, """
            |2 - - 2
            |- - - -
            |- - - -
            |- - - -"""),
                Move(Pair(3, 2), 4, Direction.RIGHT, """
            |- - - 4
            |- - - -
            |- 4 - -
            |- - - -"""),
                Move(Pair(4, 2), 2, Direction.UP, """
            |- 4 - 4
            |- - - -
            |- - - -
            |- 2 - -"""),
                Move(Pair(2, 2), 2, Direction.LEFT, """
            |8 - - -
            |- 2 - -
            |- - - -
            |2 - - -"""),
                Move(Pair(4, 4), 2, Direction.DOWN, """
            |- - - -
            |- - - -
            |8 - - -
            |2 2 - 2"""),
                Move(Pair(3, 3), 2, Direction.RIGHT, """
            |- - - -
            |- - - -
            |- - 2 8
            |- - 2 4"""),
                Move(Pair(1, 2), 4, Direction.DOWN, """
            |- 4 - -
            |- - - -
            |- - - 8
            |- - 4 4"""),
                Move(Pair(3, 1), 2, Direction.RIGHT, """
            |- - - 4
            |- - - -
            |2 - - 8
            |- - - 8"""),
                Move(Pair(3, 3), 2, Direction.DOWN, """
            |- - - -
            |- - - -
            |- - 2 4
            |2 - - 16"""),
                Move(Pair(2, 3), 2, Direction.DOWN, """
            |- - - -
            |- - 2 -
            |- - - 4
            |2 - 2 16"""),
                Move(Pair(1, 4), 2, Direction.RIGHT, """
            |- - - 2
            |- - - 2
            |- - - 4
            |- - 4 16"""),
                Move(Pair(3, 2), 2, Direction.LEFT, """
            |2 - - -
            |2 - - -
            |4 2 - -
            |4 16 - -"""),
                Move(Pair(1, 3), 2, Direction.DOWN, """
            |- - 2 -
            |- - - -
            |4 2 - -
            |8 16 - -""")
        ))
    }
}