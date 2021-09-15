package com.wrj.battling.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.os.Handler;
import android.util.Patterns;

import com.wrj.battling.connection.Connection;
import com.wrj.battling.data.LoginRepository;
import com.wrj.battling.data.Result;
import com.wrj.battling.data.model.LoggedInUser;
import com.wrj.battling.R;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(final String username, final String password) {
        // can be launched in a separate asynchronous job
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Result<LoggedInUser> result = loginRepository.login(username, password);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (result instanceof Result.Success) {
                            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
                            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
                        } else {
                            loginResult.setValue(new LoginResult(R.string.login_failed));
                        }
                    }
                });
            }
        }).start();
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        } else {
            return Patterns.IP_ADDRESS.matcher(username).matches();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    public Connection getConnection() {
        return loginRepository.getConnection();
    }
}
