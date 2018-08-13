package cf.playhi.freezeyou;

import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import java.util.HashSet;
import java.util.Set;

import static cf.playhi.freezeyou.Support.getThemeDot;
import static cf.playhi.freezeyou.Support.processActionBar;
import static cf.playhi.freezeyou.Support.processSetTheme;

public class ScheduledTasksAddActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        processSetTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stma_add);
        ActionBar actionBar = getActionBar();
        processActionBar(actionBar);
        if (actionBar != null) {
                actionBar.setTitle(getIntent().getStringExtra("label"));
        }
        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void init() {
        int id = getIntent().getIntExtra("id",-5);
        if (id!=-5) {
            final SQLiteDatabase db = ScheduledTasksAddActivity.this.openOrCreateDatabase("scheduledTasks", MODE_PRIVATE, null);
            db.execSQL(
                    "create table if not exists tasks(_id integer primary key autoincrement,hour integer(2),minutes integer(2),repeat varchar,enabled integer(1),label varchar,task varchar,column1 varchar,column2 varchar)"
            );
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ScheduledTasksAddActivity.this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Cursor cursor = db.query("tasks",null,"_id=?",new String[]{Integer.toString(id)},null,null,null);
            if (cursor.moveToFirst()) {
                editor.putString("stma_add_time", Integer.toString(cursor.getInt(cursor.getColumnIndex("hour"))) + ":" + Integer.toString(cursor.getInt(cursor.getColumnIndex("minutes"))));
                editor.putBoolean("stma_add_enable", cursor.getInt(cursor.getColumnIndex("enabled")) == 1);
                editor.putString("stma_add_label", cursor.getString(cursor.getColumnIndex("label")));
                editor.putString("stma_add_task", cursor.getString(cursor.getColumnIndex("task")));
                HashSet<String> hashSet = new HashSet<>();
                String repeat = cursor.getString(cursor.getColumnIndex("repeat"));
                for (int i = 0; i < repeat.length(); i++) {
                    hashSet.add(repeat.substring(i,i+1));
                }
                editor.putStringSet("stma_add_repeat",hashSet);
            }
            cursor.close();
            db.close();
            editor.apply();
        }

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.staa_sp, new STAAFragment())
                .commit();

        ImageButton saveButton = findViewById(R.id.staa_saveButton);
        saveButton.setBackgroundResource(Build.VERSION.SDK_INT < 21 ? getThemeDot(ScheduledTasksAddActivity.this) : R.drawable.oval_ripple);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String time = defaultSharedPreferences.getString("stma_add_time", "09:09");
                int hour = Integer.valueOf(time.substring(0, time.indexOf(":")));
                int minutes = Integer.valueOf(time.substring(time.indexOf(":") + 1));
                int enabled = defaultSharedPreferences.getBoolean("stma_add_enable", true) ? 1 : 0;
                StringBuilder repeatStringBuilder = new StringBuilder();
                Set<String> stringSet = defaultSharedPreferences.getStringSet("stma_add_repeat", null);
                if (stringSet != null) {
                    for (String str : stringSet) {
                        switch (str) {
                            case "1":
                                repeatStringBuilder.append(str);
                                break;
                            case "2":
                                repeatStringBuilder.append(str);
                                break;
                            case "3":
                                repeatStringBuilder.append(str);
                                break;
                            case "4":
                                repeatStringBuilder.append(str);
                                break;
                            case "5":
                                repeatStringBuilder.append(str);
                                break;
                            case "6":
                                repeatStringBuilder.append(str);
                                break;
                            case "7":
                                repeatStringBuilder.append(str);
                                break;
                            default:
                                break;
                        }
                    }
                }
                String repeat = repeatStringBuilder.toString().equals("") ? "0" : repeatStringBuilder.toString();
                String label = defaultSharedPreferences.getString("stma_add_label", getString(R.string.label));
                String task = defaultSharedPreferences.getString("stma_add_task", "okuf");
                SQLiteDatabase db = ScheduledTasksAddActivity.this.openOrCreateDatabase("scheduledTasks", MODE_PRIVATE, null);
                db.execSQL(
                        "create table if not exists tasks(_id integer primary key autoincrement,hour integer(2),minutes integer(2),repeat varchar,enabled integer(1),label varchar,task varchar,column1 varchar,column2 varchar)"
                );//column1\2 留作备用
                db.execSQL(
                        "insert into tasks(_id,hour,minutes,repeat,enabled,label,task,column1,column2) values(null,"
                                + hour + ","
                                + minutes + ","
                                + repeat + ","
                                + enabled + ","
                                + "'" + label + "'" + ","
                                + "'" + task + "'" + ",'','')"
                );
                db.close();
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}