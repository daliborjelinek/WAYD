package com.example.wayd.Widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import com.example.wayd.R
import com.example.wayd.dbentities.Activity
import com.example.wayd.dbentities.Record
import com.example.wayd.dbmanagersImpl.ActivityManagerImpl
import com.example.wayd.dbmanagersImpl.RecordManagerImpl
import com.example.wayd.dbmanagersImpl.WAYDManagerImpl
import io.realm.Realm

/**
 * Implementation of App Widget functionality.
 */
class WidgetFinal : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        val views = RemoteViews(context.packageName, R.layout.wayd_widget)
        val activities = activityManager.getallActivities()
        if (ACTION_SIMPLEAPPWIDGET == intent.action) {
            mCounter++
            // Construct the RemoteViews object
            //views.setTextViewText(R.id.tvWidget, Integer.toString(mCounter))
            Log.d("Test","Maybe")
            // This time we dont have widgetId. Reaching our widget with that way.

        }
        if(ACTION_ICON_ONE == intent.action){
            val dbActivity = activities.get(0)
            if (dbActivity != null){
                turnOnOffActivity(dbActivity)
                if (dbActivity.running) {
                    views.setTextViewText(R.id.widgetTvOne, "Stop")
                }else{
                    views.setTextViewText(R.id.widgetTvOne, "Run")
                }
            }
            Log.d("Test","ONE")
        }

        if(ACTION_ICON_TWO == intent.action){
            val dbActivity = activities.get(1)
            if (dbActivity != null) {
                turnOnOffActivity(dbActivity)
                if (dbActivity.running) {
                    views.setTextViewText(R.id.widgetTvTwo, "Stop")
                }else{
                    views.setTextViewText(R.id.widgetTvTwo, "Run")
                }
            }
            Log.d("Test","TWO")
        }

        if(ACTION_ICON_THREE == intent.action){
            val dbActivity = activities.get(2)
            if (dbActivity != null){
                turnOnOffActivity(dbActivity)
                if (dbActivity.running) {
                    views.setTextViewText(R.id.widgetTvThree, "Stop")
                }else{
                    views.setTextViewText(R.id.widgetTvThree, "Run")
                }
            }
            Log.d("Test","THREE")
        }

        if(ACTION_ICON_FOUR == intent.action){
            val dbActivity = activities.get(3)
            if (dbActivity != null){
                turnOnOffActivity(dbActivity)
                if (dbActivity.running) {
                    views.setTextViewText(R.id.widgetTvFour, "Stop")
                }else{
                    views.setTextViewText(R.id.widgetTvFour, "Run")
                }
            }
            Log.d("Test","FOUR")
        }
        val appWidget = ComponentName(context, WidgetFinal::class.java)
        val appWidgetManager = AppWidgetManager.getInstance(context)
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidget, views)
    }

    fun turnOnOffActivity(dbActivity: Activity){
        val activity = Activity(
            dbActivity._ID,
            dbActivity.name,
            dbActivity.color,
            dbActivity.value,
            dbActivity.running,
            dbActivity.type,
            dbActivity.icon
        )
        if(activity.running){
            val dBRecord = WAYDManager.getAllRecordToActivity(activity._ID)?.last()
            if (dBRecord != null) {
                val lastRecord = Record(dBRecord._ID, dBRecord.timeStarted, dBRecord.activityId, dBRecord.endTime)
                lastRecord?.endTime = System.currentTimeMillis()
                recordManager.addOrUpdateRecord(lastRecord)
                activity.running = false
            }

        } else{
            val newRecord = Record(
                recordManager.getNextPrimaryKey(),
                System.currentTimeMillis(),
                activity._ID,
                0L)
            recordManager.addOrUpdateRecord(newRecord)
            activity.running = true
        }
        activityManager.addOrUpdateActivity(activity)
    }


    companion object {
        private val ACTION_SIMPLEAPPWIDGET = "ACTION_BROADCASTWIDGETSAMPLE"
        private var mCounter = 0
        private val ACTION_ICON_ONE = "ICON_ONE_CLICK"
        private val ACTION_ICON_TWO = "ICON_TWO_CLICK"
        private val ACTION_ICON_THREE = "ICON_THREE_CLICK"
        private val ACTION_ICON_FOUR = "ICON_FOUR_CLICK"
        private val activityManager = ActivityManagerImpl(Realm.getDefaultInstance())
        private val recordManager = RecordManagerImpl(Realm.getDefaultInstance())
        private val WAYDManager = WAYDManagerImpl(Realm.getDefaultInstance())

        internal fun updateAppWidget(
            context: Context, appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {

            // Construct the RemoteViews object
            val activities = activityManager.getallActivities()
            val views = RemoteViews(context.packageName, R.layout.wayd_widget)
            // Construct an Intent which is pointing this class.
            val intent = Intent(context, WidgetFinal::class.java)
            intent.action = ACTION_SIMPLEAPPWIDGET
            // And this time we are sending a broadcast with getBroadcast
            var pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            //views.setOnClickPendingIntent(R.id.tvWidget, pendingIntent)
            intent.action = ACTION_ICON_ONE
            pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            views.setOnClickPendingIntent(R.id.widgetIconOne, pendingIntent)
            intent.action = ACTION_ICON_TWO
            pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            views.setOnClickPendingIntent(R.id.widgetIconTwo, pendingIntent)

            intent.action = ACTION_ICON_THREE
            pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            views.setOnClickPendingIntent(R.id.widgetIconThree, pendingIntent)

            intent.action = ACTION_ICON_FOUR
            pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            views.setOnClickPendingIntent(R.id.widgetIconFour, pendingIntent)
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

}

