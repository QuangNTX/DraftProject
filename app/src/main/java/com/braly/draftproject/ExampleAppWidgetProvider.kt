package com.braly.draftproject

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews

class ExampleAppWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, iArr: IntArray) {
        for (i in iArr) {
            updateAppWidget(context, appWidgetManager, i)
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("appWidgetId", i)
            val stringBuilder = StringBuilder()
            stringBuilder.append("tel:/")
            stringBuilder.append(System.currentTimeMillis().toInt())
            intent.data = Uri.parse(stringBuilder.toString())
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            appWidgetManager.updateAppWidget(
                i,
                RemoteViews(context.packageName, R.layout.activity_main2)
            )
        }
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, i: Int) {
        val stringBuilder = java.lang.StringBuilder()
        stringBuilder.append("AppWidget ::: updateAppWidget :: ")
        stringBuilder.append(i)
        val remoteViews = RemoteViews(context.packageName, R.layout.activity_main2)
        Log.e("TAG", "updateAppWidget: ", )
        remoteViews.setOnClickPendingIntent(
            R.id.tvWidget,
            PendingIntent.getActivity(
                context,
                0,
                Intent("android.intent.action.VIEW", Uri.parse("http://google.com")),
                0
            )
        )
        appWidgetManager.updateAppWidget(i, remoteViews)
    }

    override fun onAppWidgetOptionsChanged(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        i: Int,
        bundle: Bundle?
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, i, bundle)
    }

    override fun onDeleted(context: Context?, iArr: IntArray?) {
        super.onDeleted(context, iArr)
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
    }

    override fun onRestored(context: Context?, iArr: IntArray?, iArr2: IntArray?) {
        super.onRestored(context, iArr, iArr2)
    }

}