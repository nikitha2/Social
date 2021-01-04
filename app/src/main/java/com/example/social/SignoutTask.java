package com.example.social;

import android.content.Context;
import android.os.AsyncTask;

import java.net.URL;

public class SignoutTask extends AsyncTask<URL, Integer, Long> {
    private Context context;
    CommonDb commonDb=new CommonDb(context,null,null,null,null);
    protected Long doInBackground(URL... urls) {
        commonDb.logoffUser();
        return 0L;
    }
    protected void onProgressUpdate(Integer... progress) {

    }
    protected void onPostExecute(Long result) {

    }
}
