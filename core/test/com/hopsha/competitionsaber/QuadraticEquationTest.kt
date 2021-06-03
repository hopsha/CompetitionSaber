package com.hopsha.competitionsaber

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class QuadraticEquationTest {

    @Test
    fun `Verify equation with 2 roots calculated in order`() {
        val roots = solveQuadraticEquation(5f, 6f, 1f)
        assertEquals(-0.2f, roots[0])
        assertEquals(-1f, roots[1])
    }

    @Test
    fun `Verify equation with 1 root calculated in order`() {
        val roots = solveQuadraticEquation(-4f, 12f, -9f)
        assertEquals(1, roots.size)
        assertEquals(1.5f, roots[0])
    }

    @Test
    fun `Verify equation without roots calculated in order`() {
        val roots = solveQuadraticEquation(1f, -3f, 4f)
        assertEquals(0, roots.size)
    }
}