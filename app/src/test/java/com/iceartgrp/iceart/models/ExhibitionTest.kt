package com.iceartgrp.iceart.models

import org.junit.Assert.assertEquals
import org.junit.Test

class ExhibitionTest {
    @Test(expected = IllegalArgumentException::class)
    fun testExhibitionFromJsonFailure() {
        exhibitionsFromJson("")
    }

    @Test
    fun testExhibitionFromJsonSuccess() {
        // Arrange
        val jsonStr = """{"exhibitions":[{"id":3,"info":"Klambratun","latitude":64.1377352,
            |"longitude":-21.9156225,"title":"Kjarvalsstadir"},{"id":4,"info":"Eiríksgata",
            |"latitude":64.1377466,"longitude":-21.9221885,"title":
            |"Listasafn Einars Jónssonar"}]}""".trimMargin()

        // Act
        val exhibitions = exhibitionsFromJson(jsonStr)

        // Assert
        assertEquals(3, exhibitions.exhibitions[0].id)
        assertEquals(4, exhibitions.exhibitions[1].id)
    }
}
