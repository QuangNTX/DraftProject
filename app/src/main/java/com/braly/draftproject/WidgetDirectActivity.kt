package com.braly.draftproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class WidgetDirectActivity : AppCompatActivity() {
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        var z = true

        val extras = intent.extras

        if (extras != null) {
            val str = "pkg_name"
            if (extras.containsKey(str)) {
                try {
                    val string = extras.getString(str, "")
                    if (string.isNotEmpty()) {
                        val launchIntentForPackage =
                            packageManager.getLaunchIntentForPackage(string)
                        if (launchIntentForPackage != null) {
                            startActivity(launchIntentForPackage)
                            z = false
                        }
                    }
                } catch (unused: Error) {
                } catch (unused: Exception) {
                }
            }
        }

        finish()
    }
}