package com.iceartgrp.iceart

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @Test
    fun useAppContext() {
        // Placeholder test
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.iceartgrp.iceart", appContext.packageName)
    }
}
