package com.awesome.kkho.socialme.receiver;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.os.ResultReceiver;

/**
 * Created by kkho on 24.01.2016.
 */
@SuppressLint("ParcelCreator")

public class ServiceReceiver extends ResultReceiver {

        private Listener listener;
        public ServiceReceiver(Handler handler) {
            super(handler);
        }


        public void setListener(Listener listener) {
            this.listener = listener;
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (listener != null)
                listener.onReceiveResult(resultCode, resultData);
        }


        public static interface Listener {
            void onReceiveResult(int resultCode, Bundle resultData);
        }
    }
