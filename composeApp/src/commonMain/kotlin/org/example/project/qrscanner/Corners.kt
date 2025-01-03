package org.example.project.qrscanner

import androidx.compose.runtime.Immutable

@Immutable
class Corners(
    val topLeft: Boolean = false,
    val topRight: Boolean = false,
    val left: Boolean = false,
    val top: Boolean = false,
    val right: Boolean = false,
    val bottomLeft: Boolean = false,
    val bottom: Boolean = false,
    val bottomRight: Boolean = false
) {
    companion object {
        val Empty = Corners()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Corners) return false

        return topLeft == other.topLeft &&
                topRight == other.topRight &&
                left == other.left &&
                top == other.top &&
                right == other.right &&
                bottomLeft == other.bottomLeft &&
                bottom == other.bottom &&
                bottomRight == other.bottomRight
    }

    override fun hashCode(): Int {
        var result = topLeft.hashCode()
        result = 31 * result + topRight.hashCode()
        result = 31 * result + left.hashCode()
        result = 31 * result + top.hashCode()
        result = 31 * result + right.hashCode()
        result = 31 * result + bottomLeft.hashCode()
        result = 31 * result + bottom.hashCode()
        result = 31 * result + bottomRight.hashCode()
        return result
    }
}

