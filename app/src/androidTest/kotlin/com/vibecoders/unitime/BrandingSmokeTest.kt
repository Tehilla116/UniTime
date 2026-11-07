package com.vibecoders.unitime

import android.content.ComponentName
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BrandingSmokeTest {
    @Suppress("DEPRECATION")
    @Test
    fun appDeclaresItsOwnLauncherIconAndBrandedSplashTheme() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        val packageManager = context.packageManager
        val applicationInfo = packageManager.getApplicationInfo(context.packageName, 0)
        val activityInfo = packageManager.getActivityInfo(ComponentName(context, MainActivity::class.java), 0)

        assertNotEquals("The app should declare its own launcher icon", 0, applicationInfo.icon)
        assertEquals("Theme_UniTime_Starting", context.resources.getResourceEntryName(activityInfo.themeResource))
        assertTrue(packageManager.getApplicationLabel(applicationInfo).toString().contains("UniTime"))
    }
}
