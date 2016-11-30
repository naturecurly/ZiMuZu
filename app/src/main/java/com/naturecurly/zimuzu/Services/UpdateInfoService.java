package com.naturecurly.zimuzu.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.naturecurly.zimuzu.Bean.DatabaseInstance;
import com.naturecurly.zimuzu.Bean.Update;
import com.naturecurly.zimuzu.Bean.UpdateResponse;
import com.naturecurly.zimuzu.Databases.FavDataScheme;
import com.naturecurly.zimuzu.Databases.UpdateDataScheme;
import com.naturecurly.zimuzu.Fragments.UpdateFragment;
import com.naturecurly.zimuzu.MainActivity;
import com.naturecurly.zimuzu.NetworkServices.TodayUpdateService;
import com.naturecurly.zimuzu.R;
import com.naturecurly.zimuzu.Utils.AccessUtils;
import com.naturecurly.zimuzu.Utils.DatabaseUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by leveyleonhardt on 11/30/16.
 */

public class UpdateInfoService extends JobService {

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        getUpdate(jobParameters);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }


    private void notifyUpdate() {
        NotificationManager notificationService = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notifyId = 1;
        Intent i = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, 0);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this).setContentTitle("New Update from ZiMuZu").setContentText("Your favorite series have been updated!").setSound(soundUri).setVibrate(new long[]{500, 1000}).setSmallIcon(R.drawable.ic_noti).setContentIntent(pendingIntent).setAutoCancel(true);
        notificationService.notify(notifyId, builder.build());
    }

    private void getUpdate(final JobParameters jobParameters) {
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(getString(R.string.baseUrl)).build();
        TodayUpdateService todayUpdateService = retrofit.create(TodayUpdateService.class);
        Call call = todayUpdateService.getUpdate(AccessUtils.generateAccessKey(getApplicationContext()));
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {


                    DatabaseThread thread = new DatabaseThread(response, jobParameters);
                    thread.start();
//                    readFromDatabase();
//                    recyclerView.setAdapter(new UpdateFragment.UpdateAdapter(dataSet));
//
//                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                jobFinished(jobParameters, false);

            }
        });

    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {

            if (message.arg1 == 1) {
                notifyUpdate();
            }
            jobFinished((JobParameters) message.obj, false);
            return true;
        }
    });

    private class DatabaseThread extends Thread {
        private Response response;
        private JobParameters jobParameters;

        public DatabaseThread(Response response, JobParameters jobParameters) {
            this.response = response;
            this.jobParameters = jobParameters;
        }

        @Override
        public void run() {
            super.run();
            boolean isUpdate = false;
            SharedPreferences preferences = getApplicationContext().getSharedPreferences("zimuzu", Context.MODE_PRIVATE);
            int flag = preferences.getInt("updateId", 0);
            Log.i("download", flag + "");
            UpdateResponse updateResponse = (UpdateResponse) response.body();
            List<Update> updateList = updateResponse.getData();
            preferences.edit().putInt("updateId", Integer.parseInt(updateList.get(0).getId())).commit();
            for (Update item : updateList) {
                if (Integer.parseInt(item.getId()) > flag) {
                    Cursor cursor = DatabaseInstance.database.query(FavDataScheme.FavTable.NAME, null, FavDataScheme.FavTable.Cols.ID + "= ?", new String[]{item.getResourceid()}, null, null, null);
                    if (cursor.getCount() != 0) {
                        if (DatabaseInstance.database.insert(UpdateDataScheme.UpdateTable.NAME, null, DatabaseUtils.generateUpdateContentValues(item)) != -1) {
                            isUpdate = true;
                        }
                    }
                }
            }

            Message message = Message.obtain(handler, 1, isUpdate ? 1 : 0, 0, jobParameters);
            handler.sendMessage(message);


        }
    }


}
