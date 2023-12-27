package com.timserio.solunar

import androidx.test.platform.app.InstrumentationRegistry

object UITestingUtil {
    fun getResourceString(id: Int): String {
        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        return targetContext.resources.getString(id)
    }
}
