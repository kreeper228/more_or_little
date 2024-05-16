import kotlin.random.Random
import java.util.Scanner

fun main() {
    val game = GuessNumberGame()
    game.start()
}

class GuessNumberGame {
    private val player: Player
    private val computerPlayer: Player
    private var playerGuesses: Int = 0
    private var computerGuesses: Int = 0

    init {
        player = HumanPlayer("Player")
        computerPlayer = ComputerPlayer("Computer")
    }

    fun start() {
        playerGuesses = playRound(player, computerPlayer)
        computerGuesses = playRound(computerPlayer, player)

        if (playerGuesses < computerGuesses) {
            println("Player wins! Player knowed the number in $playerGuesses variants.")
        } else if (playerGuesses > computerGuesses) {
            println("Computer wins! Computer knowed the number in $computerGuesses variants.")
        } else {
            println("It's a tie!")
        }
    }

    private fun playRound(guesser: Player, answerer: Player): Int {
        val secretNumber = answerer.chooseSecretNumber()
        var guesses = 0

        while (true) {
            val guess = guesser.makeGuess()
            guesses++
            when {
                guess > secretNumber -> {
                    println("${guesser.name}'s variant is $guess. Lower")
                    if (guesser is ComputerPlayer) {
                        (guesser as ComputerPlayer).adjustRange(false, guess)
                    }
                }
                guess < secretNumber -> {
                    println("${guesser.name}'s variant is $guess. Higher")
                    if (guesser is ComputerPlayer) {
                        (guesser as ComputerPlayer).adjustRange(true, guess)
                    }
                }
                else -> {
                    println("$guess Correct!")
                    return guesses
                }
            }
        }
    }
}

interface Player {
    val name: String
    fun makeGuess(): Int
    fun chooseSecretNumber(): Int
}

class HumanPlayer(override val name: String) : Player {
    private val scanner = Scanner(System.`in`)

    override fun makeGuess(): Int {
        print("$name, enter your guess: ")
        return scanner.nextInt()
    }

    override fun chooseSecretNumber(): Int {
        print("$name, please choose a secret number between 0 and 100: ")
        return scanner.nextInt()
    }
}

class ComputerPlayer(override val name: String) : Player {
    private var low = 0
    private var high = 100

    override fun makeGuess(): Int {
        return (low + high) / 2
    }

    override fun chooseSecretNumber(): Int {
        return Random.nextInt(0, 101)
    }

    fun adjustRange(isHigher: Boolean, guess: Int) {
        if (isHigher) {
            low = guess + 1
        } else {
            high = guess - 1
        }
    }
}
