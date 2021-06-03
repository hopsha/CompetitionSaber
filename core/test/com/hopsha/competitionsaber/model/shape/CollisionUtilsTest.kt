package com.hopsha.competitionsaber.model.shape

import com.hopsha.competitionsaber.EPS
import com.hopsha.competitionsaber.model.findCollisions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.math.abs

internal class CollisionUtilsTest {

    @Test
    fun `Colliding segments calculated colliding`() {
        val segmentOne = StraightSegment(
            3f,
            3f,
            5f,
            5f
        )
        val segmentTwo = StraightSegment(
            5f,
            3f,
            3f,
            5f
        )
        assertTrue(findCollisions(segmentOne, segmentTwo).isNotEmpty())
    }

    @Test
    fun `Colliding segments where one is shorter calculated colliding`() {
        val segmentOne = StraightSegment(
            1f,
            1f,
            5f,
            5f
        )
        val segmentTwo = StraightSegment(
            5f,
            2f,
            2f,
            4f
        )
        assertTrue(findCollisions(segmentOne, segmentTwo).isNotEmpty())
    }

    @Test
    fun `Parallel non colliding segments calculated not colliding`() {
        val segmentOne = StraightSegment(
            3f,
            3f,
            3f,
            5f
        )
        val segmentTwo = StraightSegment(
            5f,
            5f,
            5f,
            10f
        )
        assertTrue(findCollisions(segmentOne, segmentTwo).isEmpty())
    }

    @Test
    fun `Not colliding segments calculated not colliding`() {
        val segmentOne = StraightSegment(
            1f,
            1f,
            5f,
            5f
        )
        val segmentTwo = StraightSegment(
            5f,
            1f,
            4f,
            2f
        )
        assertTrue(findCollisions(segmentOne, segmentTwo).isEmpty())
    }

    @Test
    fun `Colliding rectangle calculated colliding`() {
        val segment = StraightSegment(
            1f,
            1f,
            5f,
            5f
        )
        val rectangle = Rectangle(
            2f,
            4f,
            6f,
            3f
        )
        assertTrue(findCollisions(segment, rectangle).isNotEmpty())
    }

    @Test
    fun `Non-colliding rectangle, which collides with line, calculated not colliding`() {
        val segment = StraightSegment(
            1f,
            1f,
            3f,
            3f
        )
        val rectangle = Rectangle(
            2f,
            6f,
            6f,
            3f
        )
        assertTrue(findCollisions(segment, rectangle).isEmpty())
    }

    @Test
    fun `Non-colliding rectangle calculated not colliding`() {
        val segment = StraightSegment(
            1f,
            1f,
            1f,
            3f
        )
        val rectangle = Rectangle(
            2f,
            6f,
            6f,
            3f
        )
        assertTrue(findCollisions(segment, rectangle).isEmpty())
    }

    @Test
    fun `Colliding circle calculated colliding`() {
        val segment = StraightSegment(
            1f,
            1f,
            5f,
            5f
        )
        val circle = Circle(
            5f,
            5f,
            2f
        )
        assertTrue(findCollisions(segment, circle).isNotEmpty())
    }

    @Test
    fun `Non-colliding circle, which collides with line, calculated not colliding`() {
        val segment = StraightSegment(
            1f,
            1f,
            2f,
            2f
        )
        val circle = Circle(
            5f,
            5f,
            2f
        )
        assertTrue(findCollisions(segment, circle).isEmpty())
    }

    @Test
    fun `Non-colliding circle calculated not colliding`() {
        val segment = StraightSegment(
            1f,
            1f,
            1f,
            3f
        )
        val circle = Circle(
            5f,
            5f,
            2f
        )
        assertTrue(findCollisions(segment, circle).isEmpty())
    }

    @Test
    fun `Special circle and special segment calculated colliding`() {
        val segment = StraightSegment(
            394.24524f,
            199.04431f,
            494.00043f,
            192.05127f
        )
        val circle = Circle(
            500f,
            200f,
            20f
        )
        assertTrue(findCollisions(segment, circle).isNotEmpty())
    }

    @Test
    fun `Special circle and circle calculated colliding`() {
        val circleOne = Circle(
            388.42914f,
            171.98442f,
            100f
        )
        val circleTwo = Circle(
            500f,
            200f,
            20f
        )
        assertTrue(findCollisions(circleTwo, circleOne).isNotEmpty())
    }

    @Test
    fun `Special circle and rectangle calculated colliding`() {
        val circle = Circle(
            240.79543f,
            388.70618f,
            100f
        )
        val rectangle = Rectangle(
            0f,
            475f,
            800f,
            5f
        )
        assertTrue(findCollisions(circle, rectangle).isNotEmpty())
    }

    @Test
    fun `Special circle and vertical segment on the left calculated colliding`() {
        val circle = Circle(
            100.03429f,
            160.52377f,
            100f
        )
        val segment = StraightSegment(
            5f,
            0f,
            5f,
            480f
        )
        assertTrue(findCollisions(segment, circle).isNotEmpty())
    }

    @Test
    fun `Special circle and vertial segment on the right calculated colliding`() {
        val circle = Circle(
            100.03429f,
            160.52377f,
            100f
        )
        val segment = StraightSegment(
            190f,
            0f,
            190f,
            480f
        )
        assertTrue(findCollisions(segment, circle).isNotEmpty())
    }

    @Test
    fun `Special circle and horizontal segment on the top calculated colliding`() {
        val circle = Circle(
            100.03429f,
            160.52377f,
            100f
        )
        val segment = StraightSegment(
            0f,
            250f,
            800f,
            250f
        )
        assertTrue(findCollisions(segment, circle).isNotEmpty())
    }

    @Test
    fun `Special circle and horizontal segment on the bottom calculated colliding`() {
        val circle = Circle(
            100.03429f,
            160.52377f,
            100f
        )
        val segment = StraightSegment(
            0f,
            70f,
            800f,
            70f
        )
        assertTrue(findCollisions(segment, circle).isNotEmpty())
    }

    @Test
    fun `Special circle and rectangle on the left calculated colliding`() {
        val circle = Circle(
            100.03429f,
            160.52377f,
            100f
        )
        val rectangle = Rectangle(
            0f,
            0f,
            5f,
            480f
        )
        assertTrue(findCollisions(circle, rectangle).isNotEmpty())
    }

    @Test
    fun `Special segment and vertical segment on the left calculated colliding`() {
        val segment = StraightSegment(
            64.29604f,
            114.80178f,
            -16.603401f,
            173.5834f
        )
        val verticalSegment = StraightSegment(
            5f,
            0f,
            5f,
            480f
        )
        assertTrue(findCollisions(segment, verticalSegment).isNotEmpty())
    }

    @Test
    fun `Non colliding circles calculated not colliding`() {
        val circleOne = Circle(
            10f,
            10f,
            10f
        )
        val circleTwo = Circle(
            50f,
            50f,
            10f
        )
        assertTrue(findCollisions(circleTwo, circleOne).isEmpty())
    }

    @Test
    fun `Colliding in a single point circles calculated colliding with a correct point`() {
        val circleOne = Circle(
            10f,
            10f,
            10f
        )
        val circleTwo = Circle(
            30f,
            10f,
            10f
        )
        val point = findCollisions(circleTwo, circleOne).first()
        assertTrue(abs(20f - point.x) < EPS)
        assertTrue(abs(10f - point.y) < EPS)
    }

    @Test
    fun `Colliding circles calculated colliding with correct points`() {
        val circleOne = Circle(
            0f,
            0f,
            40f
        )
        val circleTwo = Circle(
            50f,
            0f,
            40f
        )
        val points = findCollisions(circleTwo, circleOne)
        points[0].assertCoordinates(25f, 31.225f)
        points[1].assertCoordinates(25f, -31.225f)
    }

    @Test
    fun `Colliding special segments calculated colliding`() {
        val segmentOne = StraightSegment(
            500f,
            200f,
            600f,
            200f
        )
        val segmentTwo = StraightSegment(
            562.0443f,
            113.37647f,
            558.5361f,
            213.31491f
        )
        assertTrue(findCollisions(segmentOne, segmentTwo).isNotEmpty())
    }

    @Test
    fun `Colliding special segments inverted calculated colliding`() {
        val segmentOne = StraightSegment(
            500f,
            200f,
            600f,
            200f
        )
        val segmentTwo = StraightSegment(
            562.0443f,
            113.37647f,
            558.5361f,
            213.31491f
        )
        assertTrue(findCollisions(segmentTwo, segmentOne).isNotEmpty())
    }
}

fun Point.assertCoordinates(x: Float, y: Float, eps: Float = EPS) {
    assertTrue(abs(x - this.x) < eps)
    assertTrue(abs(y - this.y) < eps)
}