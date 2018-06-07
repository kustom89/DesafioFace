package com.desafiolatam.desafioface.networks;

import android.os.AsyncTask;
import android.util.Log;

import com.desafiolatam.desafioface.models.Developer;
import com.desafiolatam.desafioface.networks.users.UserInterceptor;
import com.desafiolatam.desafioface.networks.users.Users;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GetUsers extends AsyncTask<Map<String, String>, Integer, Integer> {

    private int additionalPages;
    private Map<String, String> queryMap;
    private int result;
    private final Users request = new UserInterceptor().get();

    public GetUsers(int additionalPages) {
        this.additionalPages = additionalPages;
    }

    @Override
    protected Integer doInBackground(Map<String, String>... maps) {
        queryMap = maps[0];
        if (additionalPages < 0) {
            while (200 == connect()) {
                increasePage();
            }
        } else {
            while (additionalPages >= 0) {
                additionalPages--;
                connect();
                increasePage();
            }
        }


        return null;
    }

    private void increasePage() {
        int page = Integer.parseInt(queryMap.get("page"));
        page++;
        queryMap.put("page", String.valueOf(page));

    }

    private int connect() {

        int code = 777;
        retrofit2.Call<Developer[]> call = request.get(queryMap);


        try {
            retrofit2.Response<Developer[]> response = call.execute();
            code = response.code();
            if (200 == code && response.isSuccessful()) {
                Developer[] developers = response.body();
                if (developers != null && developers.length > 0) {
                    Log.d("DEVELOPERS", String.valueOf(developers.length));
                    for (Developer servDev:developers) {
                        List<Developer> localDevs=Developer.find(Developer.class,"serverId=?", String.valueOf(servDev.getId()));
                        if(localDevs != null&& localDevs.size()>0){
                            Developer local= localDevs.get(0);
                            local.setEmail(servDev.getEmail());
                            local.setPhoto_url(servDev.getPhoto_url());
                            local.save();
                        }else {
                            servDev.create();
                        }
                        
                    }
                }

            } else {
                code = 888;

            }

        } catch (IOException e) {
            e.printStackTrace();
            code = 999;
        }
        result = code;
        return result;


    }
}
