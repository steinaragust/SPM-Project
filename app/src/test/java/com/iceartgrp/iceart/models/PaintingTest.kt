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
        val expectedInfo = "info0"
        val expectedId = 0

        // Act
        val painting = paintingFromJson(
            "{\"id\": $expectedId,\"info\": \"$expectedInfo\", \"title\": \"$expectedTitle\"}"
        )

        // Assert
        assertEquals(expectedTitle, painting.title)
        assertEquals(expectedInfo, painting.info)
        assertEquals(expectedId, painting.id)
    }
}
