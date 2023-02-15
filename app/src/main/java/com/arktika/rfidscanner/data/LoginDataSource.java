package com.arktika.rfidscanner.data;

import android.widget.ProgressBar;

import com.arktika.rfidscanner.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password, ProgressBar loadingProgressBar) {
        LoggedInUser thisUser = null;
        try {
            String[] myTaskParams = {username, password};
            String[] myTaskResult;
            loginAsyncTask lat = new loginAsyncTask(loadingProgressBar);
            myTaskResult= lat.execute(myTaskParams).get();
            if (Integer.parseInt(myTaskResult[0])>0) {
                thisUser =new LoggedInUser( myTaskResult[0], myTaskResult[1]);
            }
            return new Result.Success<>(thisUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}