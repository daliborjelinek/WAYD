package com.example.wayd.Widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.ImageView
import android.widget.RemoteViews
import com.example.wayd.Constants
import com.example.wayd.R
import com.example.wayd.activities.MainActivity
import com.example.wayd.dbmanagersImpl.ActivityManagerImpl
import io.realm.Realm

/**
 * Implementation of App Widget functionality.
 */
class WaydWidget : AppWidgetProvider() {
    private val activityManager = ActivityManagerImpl(Realm.getDefaultInstance())
    private val icon1:ImageView? = null
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
;
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object {
        private val activityManager = ActivityManagerImpl(Realm.getDefaultInstance())
        internal fun updateAppWidget(
            context: Context, appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {


            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.wayd_widget)
            views.setOnClickPendingIntent(R.id.widgetIconTest, getPendingIntent(context, 1))

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
        private fun getPendingIntent(context: Context, value: Int): PendingIntent {
            //1
            val intent = Intent(context, MainActivity::class.java)
            //2
            intent.action = "WidgetClicked"
            //3
            intent.putExtra(Constants.ACTIVITY_ID_WIDGET_INTENT, 666)
            //4
            return PendingIntent.getActivity(context, value, intent, 0)
        }

    }

}

