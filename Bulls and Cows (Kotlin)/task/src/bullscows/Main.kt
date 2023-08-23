package bullscows

import kotlin.NumberFormatException
import kotlin.random.Random

const val SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyz"

fun main() {
    try {
        val (code, symbols) = getSecretCode()
        println("The secret is prepared: ${"*".repeat(code.length)} $symbols.")
        var turn = 1
        println("Okay, let's start a game!")
        do {
            println("Turn $turn:")
            turn++
            val guess = readln()
        } while (!gradeGuess(guess = guess, secretCode = code))
        println("Congratulations! You guessed the secret code.")
    } catch (e: Exception) {
        println(e.message)
    }
}

/**
 * Returns the secret code and possible symbols in a Pair.
 */
private fun getSecretCode(): Pair<String, String> {
    println("Input the length of the secret code:")
    val length = try { readln().toInt() } catch (e: NumberFormatException) { throw Exception("Error: Invalid input") }
    if (length > SYMBOLS.length) {
        throw IllegalArgumentException("Error: Maximum length of secret code is ${SYMBOLS.length}.")
    } else if (length < 1) {
        throw java.lang.IllegalArgumentException("Error: Code must be at least 1 characters long.")
    }

    println("Input the number of possible symbols in the code:")
    val symbols = try { readln().toInt() } catch (e: NumberFormatException) { throw Exception("Error: Invalid input") }
    if (symbols < length) {
        throw IllegalArgumentException("Error: Minimum number of symbols is $length")
    } else if (symbols > SYMBOLS.length) {
        throw IllegalArgumentException("Error: Maximum number of symbols is $length.")
    }

    return generateSecretCode(length, symbols)
}

private fun generateSecretCode(length: Int, symbols: Int): Pair<String, String> {
    var code = ""
    do {
        val i = Random.nextInt(0, length)
        val symbol = SYMBOLS[i]
        if (!code.contains(symbol)) {
            code += symbol
        }
    } while (code.length < length)
    return Pair(code, generateUsedSymbolsString(symbols))
}

private fun generateUsedSymbolsString(length: Int): String {
    val maxNumber = if (length > 10) { "9" } else { SYMBOLS[length - 1] }
    val maxLetter = if (length < 11) { null } else { SYMBOLS[length - 1] }
    return listOfNotNull(
            "(0-$maxNumber",
            ", a-$maxLetter".takeIf { maxLetter != null },
            ")",
    ).joinToString("")
}

/**
 * Returns true if guess is correct.
 */
private fun gradeGuess(guess: String, secretCode: String): Boolean {
    if (guess.length != secretCode.length) {
        throw Exception("Error: Invalid guess length")
    }
    var bulls = 0
    var cows = 0
    for (i in secretCode.indices) {
        for (j in secretCode.indices) {
            if (guess[i] == secretCode[j]) {
                if (i == j) {
                    bulls++
                } else {
                    cows++
                }
            }
        }
    }

    val grade = when {
        bulls > 0 && cows > 0 -> "$bulls bull(s) and $cows cow(s)"
        bulls > 0 -> "$bulls bull(s)"
        cows > 0 -> "$cows cow(s)"
        else -> "None"
    }
    println("Grade: $grade")
    return bulls == secretCode.length
}
