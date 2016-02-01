package com.awesome.kkho.socialme.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.awesome.kkho.socialme.R;
import com.awesome.kkho.socialme.model.FacebookUser;
import com.awesome.kkho.socialme.ui.activity.HomeActivity;
import com.awesome.kkho.socialme.ui.activity.LoginActivity;
import com.awesome.kkho.socialme.util.SharedPrefUtil;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kkho on 13.01.2016.
 */
public class LoginFragment extends Fragment {
    @Bind(R.id.login_button)
    LoginButton mLoginButton;
    private CallbackManager mCallbackManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity());
        mCallbackManager = CallbackManager.Factory.create();

    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    private void getAppKeyHash() {
        try {
            PackageInfo info = getActivity().getPackageManager().getPackageInfo(
                    "com.awesome.kkho.socialme",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, viewRoot);
        mLoginButton.setReadPermissions(Arrays.asList("user_photos", "public_profile"));
        mLoginButton.setFragment(this);
        return viewRoot;
    }

    @OnClick({R.id.login_button})
    public void ButtonLoginClicked(View view) {
        /*
        LoginManager.getInstance().logInWithReadPermissions(getActivity(),
                Arrays.asList("user_photos", "public_profile")); */

        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Bundle params = new Bundle();
                params.putString("fields", "id,first_name, last_name, email,cover,picture.type(normal)");
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject json, GraphResponse response) {
                                if (response.getError() != null) {
                                    Toast.makeText(getActivity(),
                                            "Login failed. Check if you have internet turned on!", Toast.LENGTH_SHORT).show();
                                } else {

                                    String jsonResult = String.valueOf(json);
                                    Gson gson = new Gson();
                                    FacebookUser facebookUser = gson.fromJson(jsonResult, FacebookUser.class);
                                    SharedPrefUtil.storeProfileDescription(facebookUser, getActivity());
                                    startActivity(new Intent(getActivity(), HomeActivity.class));
                                }
                            }

                        });
                request.setParameters(params);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                Log.d("PROBLEM! ", "What?");

            }

            @Override
            public void onError(FacebookException e) {
                Log.d("PROBLEM! ", e.getMessage());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
