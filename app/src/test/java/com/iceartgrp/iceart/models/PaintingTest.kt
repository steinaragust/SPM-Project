package com.iceartgrp.iceart.models

import org.junit.Assert.assertEquals
import org.junit.Test

class PaintingTest {
    @Test(expected = IllegalArgumentException::class)
    fun testPaintingFromJsonFailure() {
        paintingFromJson("")
    }

    @Test
    fun testPaintingFromJsonSuccess() {
        // Arrange
        val expectedTitle = "title0"
        val expectedTechnique = "technique0"
        val expectedId = 0
        val expectedYear = 1992

        // Act
        val painting = paintingFromJson(
            "{\"id\": $expectedId,\"technique\": \"$expectedTechnique\", \"title\": \"$expectedTitle\", \"year\": \"$expectedYear\"}"
        )

        // Assert
        assertEquals(expectedTitle, painting.title)
        assertEquals(expectedTechnique, painting.technique)
        assertEquals(expectedId, painting.id)
        assertEquals(expectedYear, painting.year)
    }
}
