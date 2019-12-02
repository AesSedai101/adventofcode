import java.io.File
import java.io.InputStream

val inputStream: InputStream = File("input/day1.txt").inputStream()

val moduleMass = inputStream.bufferedReader().lines()
        .mapToDouble { it.toDouble() }
        .map { it / 3.0 }
        .map { Math.floor(it) }
        .map { it - 2 }
        .map { it + calculateFuelMass(it)}
        .sum()

println(moduleMass)

fun calculateFuelMass(mass: Double): Double {
    var fuelMass = 0.0
    var fuelMod = mass
    do {
        val fuleReq = Math.floor(fuelMod / 3.0) - 2
        if (fuleReq > 0) {
            fuelMass += fuleReq
        }
        fuelMod = fuleReq
    } while (fuleReq > 0)
    return fuelMass
}