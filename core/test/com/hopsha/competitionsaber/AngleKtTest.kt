package com.hopsha.competitionsaber

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class AngleKtTest {

    @Test
    fun `Verify close distance calculated correctly for two positive angles clockwise`() {
        val angleOne = Angle(70f)
        val angleTwo = Angle(90f)
        assertEquals(20.degrees, angleOne.closeDistance(angleTwo))
    }

    @Test
    fun `Verify close distance calculated correctly for two positive angles counter-clockwise`() {
        val angleOne = Angle(90f)
        val angleTwo = Angle(70f)
        assertEquals(20.degrees, angleOne.closeDistance(angleTwo))
    }

    @Test
    fun `Verify signed close distance calculated correctly for two positive angles clockwise`() {
        val angleOne = Angle(70f)
        val angleTwo = Angle(90f)
        assertEquals(20.degrees, angleOne.signedCloseDistance(angleTwo))
    }

    @Test
    fun `Verify signed close distance calculated correctly for two positive angles counter-clockwise`() {
        val angleOne = Angle(90f)
        val angleTwo = Angle(70f)
        assertEquals((-20).degrees, angleOne.signedCloseDistance(angleTwo))
    }

    @Test
    fun `Verify close distance calculated correctly for two negative angles clockwise`() {
        val angleOne = Angle(-70f)
        val angleTwo = Angle(-90f)
        assertEquals(20.degrees, angleOne.closeDistance(angleTwo))
    }

    @Test
    fun `Verify close distance calculated correctly for two negative angles counter-clockwise`() {
        val angleOne = Angle(-90f)
        val angleTwo = Angle(-70f)
        assertEquals(20.degrees, angleOne.closeDistance(angleTwo))
    }

    @Test
    fun `Verify signed close distance calculated correctly for two negative angles clockwise`() {
        val angleOne = Angle(-70f)
        val angleTwo = Angle(-90f)
        assertEquals((-20).degrees, angleOne.signedCloseDistance(angleTwo))
    }

    @Test
    fun `Verify signed close distance calculated correctly for two negative angles counter-clockwise`() {
        val angleOne = Angle(-90f)
        val angleTwo = Angle(-70f)
        assertEquals(20.degrees, angleOne.signedCloseDistance(angleTwo))
    }

    @Test
    fun `Verify close distance calculated correctly for positive and negative angles clockwise`() {
        val angleOne = Angle(10f)
        val angleTwo = Angle(-20f)
        assertEquals(30.degrees, angleOne.closeDistance(angleTwo))
    }

    @Test
    fun `Verify close distance calculated correctly for positive and negative angles counter-clockwise`() {
        val angleOne = Angle(-20f)
        val angleTwo = Angle(10f)
        assertEquals(30.degrees, angleOne.closeDistance(angleTwo))
    }

    @Test
    fun `Verify signed close distance calculated correctly for positive and negative angles clockwise`() {
        val angleOne = Angle(10f)
        val angleTwo = Angle(-20f)
        assertEquals((-30).degrees, angleOne.signedCloseDistance(angleTwo))
    }

    @Test
    fun `Verify signed close distance calculated correctly for positive and negative angles counter-clockwise`() {
        val angleOne = Angle(-20f)
        val angleTwo = Angle(10f)
        assertEquals(30.degrees, angleOne.signedCloseDistance(angleTwo))
    }

    @Test
    fun `Verify close distance calculated correctly for positive and large positive angles clockwise`() {
        val angleOne = Angle(10f)
        val angleTwo = Angle(350f)
        assertEquals(20.degrees, angleOne.closeDistance(angleTwo))
    }

    @Test
    fun `Verify close distance calculated correctly for positive and large positive angles counter-clockwise`() {
        val angleOne = Angle(350f)
        val angleTwo = Angle(10f)
        assertEquals(20.degrees, angleOne.closeDistance(angleTwo))
    }

    @Test
    fun `Verify signed close distance calculated correctly for positive and large positive angles clockwise`() {
        val angleOne = Angle(10f)
        val angleTwo = Angle(350f)
        assertEquals((-20).degrees, angleOne.signedCloseDistance(angleTwo))
    }

    @Test
    fun `Verify signed close distance calculated correctly for positive and large positive angles counter-clockwise`() {
        val angleOne = Angle(350f)
        val angleTwo = Angle(10f)
        assertEquals(20.degrees, angleOne.signedCloseDistance(angleTwo))
    }
}