package com.hopsha.competitionsaber.model

import com.hopsha.competitionsaber.*
import com.hopsha.competitionsaber.model.shape.*
import kotlin.math.*

fun findCollisions(segment: StraightSegment, circle: Circle): List<Point> {
    /*val normalizedSegment = StraightSegment(
        segment.xStart - circle.x,
        segment.yStart - circle.y,
        segment.xEnd - circle.x,
        segment.yEnd - circle.y
    )
    val a = normalizedSegment.a
    val b = normalizedSegment.b
    val c = normalizedSegment.c

    val x0 = -a * c / (a * a + b * b)
    val y0 = -b * c / (a * a + b * b)

    val d0Qrt = circle.radius * circle.radius * (a * a + b * b)
    return when {
        c * c > d0Qrt + EPS -> emptyList()
        abs(c * c - d0Qrt) < EPS -> {
            if (x0 in normalizedSegment.xRange && y0 in normalizedSegment.yRange) {
                listOf(Point(x0 + circle.x, y0 + circle.y))
            } else {
                emptyList()
            }
        }
        else -> {
            val d = circle.radius * circle.radius - c * c / (a * a + b * b)
            val mult = sqrt(d / (a * a + b * b))
            val x1 = x0 + b * mult
            val y1 = y0 - a * mult
            val x2 = x0 - b * mult
            val y2 = y0 + a * mult
            mutableListOf<Point>().apply {
                if (x1 in normalizedSegment.xRange && y1 in normalizedSegment.yRange) {
                    add(Point(x1 + circle.x, y1 + circle.y))
                }
                if (x2 in normalizedSegment.xRange && y2 in normalizedSegment.yRange) {
                    add(Point(x2 + circle.x, y2 + circle.y))
                }
            }
        }
    }*/
    /*val segmentK = segment.angleCoefficients.k
    val segmentB = segment.angleCoefficients.b

    val a = 1 + segmentK.pow(2)
    val b = 2 * segmentK * segmentB + circle.a + circle.b * segmentK
    val c = segmentB.pow(2) + circle.b * segmentB + circle.c*/

    if (segment.isVertical) {
        val distance = abs(circle.x - segment.xStart)
        return when {
            distance == circle.radius -> listOf(Point(segment.xStart, circle.y))
            distance < circle.radius -> {
                val angle = acos(distance / circle.radius)
                val yDelta = sin(angle) * circle.radius
                listOf(
                    Point(segment.xStart, circle.y + yDelta),
                    Point(segment.xStart, circle.y - yDelta),
                )
            }
            else -> emptyList()
        }
    } else {
        val a = segment.b.pow(2) + segment.a.pow(2)
        val b = 2 * (segment.a * segment.c + circle.y * segment.b * segment.a - circle.x * segment.b.pow(2))
        val c =
            segment.c.pow(2) + 2 * circle.y * segment.b * segment.c + segment.b.pow(2) * (circle.x.pow(2) + circle.y.pow(
                2
            ) - circle.radius.pow(2))

        return solveQuadraticEquation(a, b, c)
            .asSequence()
            .associateWith { x ->
                (segment.a * x + segment.c) / -segment.b
            }
            .filter { pair ->
                pair.key in segment.xRange && pair.value in segment.yRange
            }
            .map { pair -> Point(pair.key, pair.value) }
    }
}

fun findCollisions(segmentOne: StraightSegment, segmentTwo: StraightSegment): List<Point> {
    if (segmentOne.isParallel(segmentTwo)) {
        if (segmentOne.contains(segmentTwo.xStart, segmentTwo.yStart)) {
            return listOf(Point(segmentTwo.xStart, segmentTwo.yStart))
        }
        if (segmentOne.contains(segmentTwo.xEnd, segmentTwo.yEnd)) {
            return listOf(Point(segmentTwo.xEnd, segmentTwo.yEnd))
        }
        if (segmentTwo.contains(segmentOne.xStart, segmentOne.yStart)) {
            return listOf(Point(segmentOne.xStart, segmentOne.yStart))
        }
        if (segmentTwo.contains(segmentOne.xEnd, segmentOne.yEnd)) {
            return listOf(Point(segmentOne.xEnd, segmentOne.yEnd))
        }
    }

    if (segmentOne.isHorizontal) {
        val y = segmentOne.yStart
        val x = (segmentTwo.b * y + segmentTwo.c) / -segmentTwo.a

        return if (y in segmentTwo.yRange && x in segmentOne.xRange && x in segmentTwo.xRange) {
            listOf(Point(x, y))
        } else {
            emptyList()
        }
    }

    val y = (segmentTwo.a * segmentOne.c - segmentOne.a * segmentTwo.c) / (segmentOne.a * segmentTwo.b - segmentTwo.a * segmentOne.b)
    if (y !in growingRange(segmentOne.yStart, segmentOne.yEnd) || y !in growingRange(segmentTwo.yStart, segmentTwo.yEnd)) {
        return emptyList()
    }

    val x = (segmentOne.b * y + segmentOne.c) / -segmentOne.a
    return if (x in growingRange(segmentOne.xStart, segmentOne.xEnd) && x in growingRange(segmentTwo.xStart, segmentTwo.xEnd)) {
        listOf(Point(x, y))
    } else {
        emptyList()
    }
}

fun findCollisions(segment: StraightSegment, rectangle: Rectangle): List<Point> {
    return rectangle.perimeter
        .flatMap { rectangleSegment -> findCollisions(segment, rectangleSegment) }
}

fun findCollisions(rectangleOne: Rectangle, rectangleTwo: Rectangle): List<Point> {
    return rectangleOne.perimeter
        .flatMap { rectangleSegment -> findCollisions(rectangleSegment, rectangleTwo) }
}

fun findCollisions(circleOne: Circle, circleTwo: Circle): List<Point> {
    val distance = circleOne.center.distanceTo(circleTwo.center)
    if (distance > circleOne.radius + circleTwo.radius) {
        return emptyList()
    }
    if (distance == circleOne.radius + circleTwo.radius) {
        val angle = Angle.between(
            circleOne.x,
            circleOne.y,
            circleTwo.x,
            circleTwo.y
        )
        val x = circleOne.x + angle.cos * circleOne.radius
        val y = circleOne.y + angle.sin * circleOne.radius

        return listOf(Point(x, y))
    }

    /*val normalizedCircleTwoX = circleTwo.x - circleOne.x
    val normalizedCircleTwoY = circleTwo.y - circleOne.y

    val a = -2 * normalizedCircleTwoX
    val b = -2 * normalizedCircleTwoY
    val c = normalizedCircleTwoX * normalizedCircleTwoX
                + normalizedCircleTwoY * normalizedCircleTwoY
                + circleOne.radius * circleOne.radius
                - circleTwo.radius * circleTwo.radius

    val x0 = -a * c / (a * a + b * b)
    val y0 = -b * c / (a * a + b * b)

    val d0Qrt = circleOne.radius * circleOne.radius * (a * a + b * b)
    return when {
        c * c > d0Qrt + EPS -> emptyList()
        c * c - d0Qrt <= EPS -> listOf(Point(x0 + circleOne.x, y0 + circleOne.y))
        else -> {
            val d = circleOne.radius * circleOne.radius - c * c / (a * a + b * b)
            val mult = sqrt(d / (a * a + b * b))
            val x1 = x0 + b * mult
            val y1 = y0 - a * mult
            val x2 = x0 - b * mult
            val y2 = y0 + a * mult
            listOf(
                Point(x1 + circleOne.x, y1 + circleOne.y),
                Point(x2 + circleOne.x, y2 + circleOne.y)
            )
        }
    }*/

    val a = (circleOne.radius.pow(2) - circleTwo.radius.pow(2) + distance.pow(2)) / (2 * distance)
    val h = sqrt(circleOne.radius.pow(2) - a.pow(2))

    val p3x = circleOne.x + a / distance * (circleTwo.x - circleOne.x)
    val p3y = circleOne.y + a / distance * (circleTwo.y - circleOne.y)

    val x1 = p3x + h / distance * (circleTwo.y - circleOne.y)
    val y1 = p3y - h / distance * (circleTwo.x - circleOne.x)

    val x2 = p3x - h / distance * (circleTwo.y - circleOne.y)
    val y2 = p3y + h / distance * (circleTwo.x - circleOne.x)

    return if (x1 == x2 && y1 == y2) {
        listOf(Point(x1, y1))
    } else {
        listOf(
            Point(x1, y1),
            Point(x2, y2)
        )
    }
}

fun findCollisions(circle: Circle, rectangle: Rectangle): List<Point> {
    return rectangle.perimeter
        .flatMap { rectangleSegment -> findCollisions(rectangleSegment, circle) }
}