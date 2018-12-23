package munn.adventofcode.utils

data class Point(val x: Int, val y: Int) {
    fun dX(dx: Int) = copy(x = x + dx)
    fun dY(dy: Int) = copy(y = y + dy)

    fun isOrigin() = this == ORIGIN

    companion object {
        val ORIGIN = Point(0, 0)
    }
}