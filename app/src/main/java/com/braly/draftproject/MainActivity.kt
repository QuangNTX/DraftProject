package com.braly.draftproject

import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.graphics.drawable.toBitmap
import com.braly.draftproject.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private var isFromShortcut: Boolean? = false

    private val appAdapter by lazy {
        AppAdapter {
            if (isFromShortcut== false){
                val intent2 = Intent(applicationContext, WidgetDirectActivity::class.java)
                intent2.putExtra("pkg_name", it.pname)
                intent2.putExtra("duplicate", true)
                val applicationContext = applicationContext
                val build = ShortcutInfoCompat.Builder(
                    applicationContext,
                    Calendar.getInstance().timeInMillis.toString() + ""
                ).setIntent(intent2.setAction("android.intent.action.MAIN"))
                    .setShortLabel("ABCDEFFF")
                    .setIcon(IconCompat.createWithBitmap(it.icon)).build()
                App.icon = it.icon
                val intent3 = Intent()
                val bitmap2: Bitmap? = it.icon
                if (bitmap2 != null && !bitmap2.isRecycled) {
                    intent3.putExtra("icon", it.icon)
                }
                intent3.putExtra("app_name", "ABCDEFFF")
                intent3.putExtra("pkg_name", it.pname)
                intent3.putExtra("isFromShortCut", false)
                ShortcutManagerCompat.requestPinShortcut(
                    this,
                    build,
                    PendingIntent.getBroadcast(
                        getApplicationContext(),
                        1001, intent3,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    ).intentSender
                )
            } else if (isFromShortcut == true){
                Log.e("TAG", "true: ")
                val intent = Intent()
                App.icon = it.icon
                intent.putExtra("app_name", it.appname)
                intent.putExtra("pkg_name", it.pname)
                intent.putExtra("isFromShortCut", isFromShortcut)
                setResult(-1, intent)
                Log.e("TAG", "${it.icon}: ${it.appname}: ${it.pname} ")
                finish()

            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        if ("android.intent.action.CREATE_SHORTCUT" == intent.action) {
            Log.e("TAG", "onCreate: ")
            this.isFromShortcut = true
        } else {
            val extras = intent.extras
            if (extras != null && extras.containsKey("isFromShortCut")) {
                this.isFromShortcut = extras.getBoolean("isFromShortCut", false)
            }
        }
        setContentView(binding.root)
        binding.recyclerView.apply {
            adapter = appAdapter
        }
        appAdapter.submitList(getInstalledApps())
        binding.buttonEnd.setOnClickListener {
            stopService(Intent(this, MyService::class.java))
        }
        binding.buttonStart.setOnClickListener{
            startForegroundService(Intent(this, MyService::class.java))
        }
        setResult(Activity.RESULT_CANCELED)

    }

    private fun getPackages(): ArrayList<PInfo> {
        val apps = getInstalledApps() /* false = no system packages */
        val max: Int = apps.size
        for (i in 0 until max) {
            apps[i].prettyPrint()
        }
        return apps
    }

    private fun getInstalledApps(): ArrayList<PInfo> {
        val res = ArrayList<PInfo>()
        val packs: List<PackageInfo> = packageManager.getInstalledPackages(0)
        for (i in packs.indices) {
            val p = packs[i]
            if (p.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM === 0) {
                val newInfo = PInfo()
                newInfo.appname = p.applicationInfo.loadLabel(getPackageManager()).toString()
                newInfo.pname = p.packageName
                newInfo.versionName = p.versionName
                newInfo.versionCode = p.versionCode
                newInfo.icon =
                    p.applicationInfo.loadIcon(applicationContext.packageManager).toBitmap()
                res.add(newInfo)
            }
        }
        return res
    }


    private fun createWidget(context: Context) {
    }

    private fun createShortcut(context: Context, appName: String, appIcon: Bitmap) {
        if (ShortcutManagerCompat.isRequestPinShortcutSupported(context)) {
            val shortcutInfo =
                ShortcutInfoCompat.Builder(context, System.currentTimeMillis().toString())
                    .setIntent(
                        Intent(
                            context,
                            MainActivity::class.java
                        ).setAction(Intent.ACTION_MAIN)
                    )
                    .setShortLabel(appName)
                    .setIcon(IconCompat.createWithBitmap(appIcon))
                    .build()
            ShortcutManagerCompat.requestPinShortcut(context, shortcutInfo, null)
        } else {
            Toast.makeText(
                context,
                "abc",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }
}

class PInfo {
    var appname = ""
    var pname = ""
    var versionName = ""
    var versionCode = 0
    var icon: Bitmap? = null
    fun prettyPrint() {
        Log.e("TAG", appname + "\t" + pname + "\t" + versionName + "\t" + versionCode)
    }
}
