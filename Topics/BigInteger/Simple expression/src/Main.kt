import java.math.BigInteger

fun main() {
    val a = BigInteger(readln())
    val b = BigInteger(readln())
    val c = BigInteger(readln())
    val d = BigInteger(readln())
    // (-a) * b + c - d
    val result = a.negate() * b + c - d
    println(result)
}