package com.desafiolatam.desafioface.background;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.desafiolatam.desafioface.networks.GetUsers;

import java.util.HashMap;
import java.util.Map;


public class ResentUserService extends IntentService {

    private static final String ACTION_RECENT_USERS = "com.desafiolatam.desafioface.background.action.ACTION_RECENT_USERS";
    public static final String USERS_FINISHED="com.desafiolatam.desafioface.background.USERS_FINISHED";


    public ResentUserService() {
        super("ResentUserService");
    }


    public static void startActionRecentUsers(Context context) {
        Intent intent = new Intent(context, ResentUserService.class);
        intent.setAction(ACTION_RECENT_USERS);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_RECENT_USERS.equals(action)) {
                fetchUsers();
            }
        }
    }

    private void fetchUsers() {

        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("page", "1");
        new fetchUsers(3).execute(queryMap);

    }

    private class fetchUsers extends GetUsers {

        public fetchUsers(int additionalPages) {
            super(additionalPages);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            Log.d("RESULT", String.valueOf(integer));
            Intent intent= new Intent();
            intent.setAction(USERS_FINISHED);
            LocalBroadcastManager.getInstance(ResentUserService.this).sendBroadcast(intent);
        }
    }


}
