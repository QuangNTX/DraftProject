package com.braly.draftproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import java.util.Calendar;

public class WidgetAppListActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_app_list);
        new Handler().postDelayed(this::openAppList, 300);
    }

    private void openAppList() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("isFromShortCut", true);
        startActivityForResult(intent, 1111);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle extras = data.getExtras();

        String str2 = "";
        String str3 = "app_name";
        String string = extras.getString(str3, str2);
        String str4 = "pkg_name";
        String string2 = extras.getString(str4, str2);
        int dimension = (int) getResources().getDimension(android.R.dimen.app_icon_size);
        Intent intent2 = new Intent(getApplicationContext(), WidgetDirectActivity.class);
        intent2.putExtra(str4, string2);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string2);
        stringBuilder.append("_");
        stringBuilder.append(Calendar.getInstance().getTimeInMillis());

        Bitmap bitmap = App.INSTANCE.getIcon();
        intent2.setAction(stringBuilder.toString());
        Intent intent3 = new Intent();
        intent3.putExtra("android.intent.extra.shortcut.INTENT", intent2);
        intent3.putExtra("android.intent.extra.shortcut.NAME", string);
        intent3.putExtra("android.intent.extra.shortcut.ICON", Bitmap.createScaledBitmap(bitmap, dimension, dimension, false));
        setResult(-1, intent3);
        finish();
    }
}
