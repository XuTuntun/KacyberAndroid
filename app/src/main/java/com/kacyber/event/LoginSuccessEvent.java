package com.kacyber.event;

import com.kacyber.model.IMInfos;
import com.kacyber.model.JSONParceble;
import com.kacyber.model.LoginSuccessModel;

import org.json.JSONObject;

/**
 * Created by guofuming on 27/10/2016.
 */

public class LoginSuccessEvent {
    public LoginSuccessModel loginSuccessModel;


    public LoginSuccessEvent(LoginSuccessModel loginSuccessModelInput) {
        loginSuccessModel = loginSuccessModelInput;
    }
}
