package org.yuttadhammo.tipitaka;

import com.google.android.vending.expansion.downloader.impl.DownloaderService;

/**
 * Created by noah on 5/29/14.
 */
public class PlayDownloaderService extends DownloaderService {
    // You must use the public key belonging to your publisher account
    public static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlia0hdaSUXmQnrVTxK/tiWdzBFOztp41NU0pg0PLqvvl1I+tKBqhmA16sFN9p+969nE4xjmaPuxwqUPCWfETrsZBuLbwGOSWAGWTBMnV8Dklq19FvDx64DJOTnmkCSyQ7BVWvZlcQ4nhHfjeDiTu+rXnzsrGGh5aQAImLm27Ye/Niwo6pqd56T08Vh5zYOA1KM3+mXNOr8TR0u5LrleoaFHwx74C31uhPuYAidNRB582y9Zqdv/FTxs5Yl1K+Tsvn5HOVYLa5siv7UELgU+FydPshSLOvLhTgnFhDb7kxLxosuY81MyoEC4IpdaKx0iOM+ct4gyop6SWkiCJCuQAPQIDAQAB";
    // You should also modify this salt
    public static final byte[] SALT = new byte[] { 1, 42, -12, -1, 54, 98,
            -100, -12, 43, 2, -8, -4, 9, 5, -106, -107, -33, 45, -1, 84
    };

    @Override
    public String getPublicKey() {
        return BASE64_PUBLIC_KEY;
    }

    @Override
    public byte[] getSALT() {
        return SALT;
    }

    @Override
    public String getAlarmReceiverClassName() {
        return PlayAlarmReceiver.class.getName();
    }
}