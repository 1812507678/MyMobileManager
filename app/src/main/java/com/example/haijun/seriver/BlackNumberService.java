package com.example.haijun.seriver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Telephony;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.example.haijun.dao.BlackNumberDao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BlackNumberService extends Service {

    private BlackNumberDao blackNumberDao;
    private BlackNumberBroadcastReceive receiver;

    public BlackNumberService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        telephonyManager.listen(new MyPhoneStatListenernew(), PhoneStateListener.LISTEN_CALL_STATE);

        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(Integer.MAX_VALUE);
        receiver = new BlackNumberBroadcastReceive();
        registerReceiver(receiver,intentFilter);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        blackNumberDao = new BlackNumberDao(this);
    }

    class MyPhoneStatListenernew extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state){
                case TelephonyManager.CALL_STATE_IDLE:

                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.i("Phone：incomingNumber",incomingNumber);
                    int numberMode = blackNumberDao.getNumberMode(incomingNumber);
                    Log.i("Phone：numberMode",numberMode+"");
                    if (numberMode==1 || numberMode==3){
                        try {
                            //通过反射，利用ITelephony的endCall()方法来挂掉电话
                            Class<?> serviceManager = getClassLoader().loadClass("android.os.ServiceManager");
                            Method getService = serviceManager.getMethod("getService", String.class);
                            IBinder binder = (IBinder) getService.invoke(null, Context.TELEPHONY_SERVICE);
                            ITelephony iTelephony = ITelephony.Stub.asInterface(binder);
                            iTelephony.endCall();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:

                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    class BlackNumberBroadcastReceive extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            for (Object obj:pdus) {
                SmsMessage fromPdu = SmsMessage.createFromPdu((byte[]) obj);
                String originatingAddress = fromPdu.getOriginatingAddress();
                Log.i("sms:originatingAddress",originatingAddress);
                int numberMode = blackNumberDao.getNumberMode(originatingAddress);
                Log.i("sms:numberMode",numberMode+"");
                if (numberMode==2 || numberMode==3){
                    abortBroadcast();
                }
            }
        }
    }
}
