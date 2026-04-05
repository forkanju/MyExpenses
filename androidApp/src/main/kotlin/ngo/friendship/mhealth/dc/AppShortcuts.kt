package ngo.friendship.mhealth.dc

import android.content.Context
import android.content.Intent
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat

object AppShortcuts {

    fun setupKtorMonitorShortcut(context: Context) {
        if (!BuildConfig.DEBUG) {
            ShortcutManagerCompat.removeDynamicShortcuts(context, listOf("ktor_monitor"))
            return
        }
        val shortcut = ShortcutInfoCompat.Builder(context, "ktor_monitor")
            .setShortLabel(context.getString(ro.cosminmihu.ktor.monitor.R.string.ktor_activity_name))
            .setIcon(
                IconCompat.createWithResource(
                    context,
                    ro.cosminmihu.ktor.monitor.R.mipmap.ktor_ic_launcher
                )
            )
            .setIntent(
                Intent(Intent.ACTION_VIEW).apply {
                    setClassName(
                        context.packageName,
                        "ro.cosminmihu.ktor.monitor.ui.KtorMonitorActivity"
                    )
                }
            )
            .build()

        ShortcutManagerCompat.setDynamicShortcuts(context, listOf(shortcut))
    }
}