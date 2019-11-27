package com.ddopik.linktask.utilites;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.*;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import androidx.annotation.Nullable;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by ddopik @_@....
 */
public class Utilities {

    private static final String TAG = Utilities.class.getSimpleName();

    /**
     * Convert arabic number to english number "," NOT added here
     */
    private static final String arabic = "\u06f0\u06f1\u06f2\u06f3\u06f4\u06f5\u06f6\u06f7\u06f8\u06f9";

    /**
     * Converts density independent pixels to pixels.
     *
     * @param dp Dp to convert.
     * @return Pixel value.
     */
    public static float convertDpToPx(float dp) {
        return dp * Resources.getSystem().getDisplayMetrics().density;
    }

    /**
     * Converts pixels to density independent pixels.
     *
     * @param px Pixels to convert.
     * @return Dp value.
     */
    public static float convertPxToDp(float px) {
        return px / Resources.getSystem().getDisplayMetrics().density;
    }

    public static String formatTimeInMilliSeconds(long timeInMilliSeconds) {
        Date date = new Date(timeInMilliSeconds);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
        return formatter.format(date);
    }

    @SuppressLint("MissingPermission")
    public static String getMacAddress(Context context) {
        WifiManager wimanager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        assert wimanager != null;
        @SuppressLint("HardwareIds") String macAddress = wimanager.getConnectionInfo().getMacAddress();
        if (macAddress == null) {
            Log.e(context.getClass().getSimpleName(), "Device don't have mac address or wi-fi is disabled");
            macAddress = "";
        }
        return macAddress;
    }


//
//    public static void sendEmail(String email, BaseActivity activity) {
//        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
//        emailIntent.setType("message/rfc822");
//        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});
//        try {
//            activity.startActivity(Intent.createChooser(emailIntent, activity.getString(R.string.contact_us_email_client_chooser_title)));
//        } catch (android.content.ActivityNotFoundException ex) {
//            activity.showToast(activity.getString(R.string.contact_us_error_no_email_client));
//        }
//    }


    public static void showHidePassword(EditText password, View showIcon, View hideIcon, boolean shouldShowPassword) {
        showIcon.setVisibility(View.VISIBLE);
        hideIcon.setVisibility(View.INVISIBLE);
        if (shouldShowPassword)
            password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        else
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        password.setSelection(password.getText().length());
    }

    @SuppressLint("MissingPermission")
    public static String getDeviceIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();

//      return Settings.Secure.getString(context.getContentResolver(),
//                Settings.Secure.ANDROID_ID);
    }

    @SuppressLint("HardwareIds")
    public static String getDeviceUniqueID(Activity activity) {
        String device_unique_id = Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return device_unique_id;
    }


    public static String arabicToDecimal(String number) {
        char[] chars = new char[number.length()];
        for (int i = 0; i < number.length(); i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            chars[i] = ch;
        }
        return new String(chars);
    }

    /**
     * /Convert arabic number to english number "," added here
     */
    public static String getUSNumber(String Numtoconvert) {

        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        try {
            if (Numtoconvert.contains("٫"))
                Numtoconvert = formatter.parse(Numtoconvert.split("٫")[0].trim()) + "." + formatter.parse(Numtoconvert.split("٫")[1].trim());
            else
                Numtoconvert = formatter.parse(Numtoconvert).toString();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            Log.e("Utilities", "Could not convert Arabic number" + e.getMessage());
        }
        return Numtoconvert;
    }


    /**
     * getting instance of the giving String
     * Note that you need to provide the fully qualified name of the class for the class loader to find it.
     * I.e., if 'class' is actually in some package, you need to do forName("your.package.A") for it to work.
     */
    public static Class<?> getClassInstance(String className) throws ClassNotFoundException {

        return Class.forName(className);
    }



    public static void restartContext(Context context) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    /**
     * force entire app to be restarted  App is Destroyed ->rebuild
     *
     * @param context  -->launcher context
     * @param genClass -->Landing Activity
     */
    public void restartApp(Context context, Class<?> genClass) {
        Intent mStartActivity = new Intent(context, genClass);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId, mStartActivity,
                PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }

    /**
     * Returns the consumer friendly device name
     */
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }

    public void printHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.e(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }

    public static List<String> getMentionsList(String comment) {


        String regex = "@+([a-zA-Z0-9_]+)";
        List<String> authorListId = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(comment);

        while (matcher.find()) {
            authorListId.add(matcher.group(1));
        }

        return authorListId;

    }

    public static void intializeData(String keyData) throws Exception {
        byte[] seedValue = {
                0x2d, 0x2a, 0x2d, 0x42, 0x55, 0x49, 0x4c, 0x44, 0x41, 0x43, 0x4f, 0x44, 0x45, 0x2d, 0x2a, 0x2d
        };
        String seedValue2 = "7w!z%C*F)J@NcRfUjXn2r5u8x/A?D(G+";
        SecretKeySpec secretKey = new SecretKeySpec(seedValue2.getBytes(), "AES");


// encrypt
        String encryptedData = encrypt(keyData, secretKey);
        Log.e(TAG, "intializeData()  encryptedData ---->" + encryptedData.toString().toString());
// decrypt
        String decryptedData = decrypt(encryptedData, secretKey);
        Log.e(TAG, "intializeData()   decryptedData ---->" + decryptedData.toString().toString());
    }

    public static String encrypt(String data, SecretKeySpec secretKey) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] cipherText = cipher.doFinal(data.getBytes("UTF8"));
            String encryptedString = new String(Base64.encode(cipherText, Base64.DEFAULT));
            return encryptedString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String data, SecretKeySpec secretKey) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] cipherText = Base64.decode(data.getBytes("UTF8"), Base64.DEFAULT);
            String decryptedString = new String(cipher.doFinal(cipherText), "UTF-8");
            return decryptedString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Nullable
    public static Bitmap loadBitmapFromView(View view) {
        try {
            view.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);
            return bitmap;
        } catch (OutOfMemoryError error) {
            Log.e(TAG, "Out of Memory while loadBitmapFromView");
            return null;
        }
    }

    //    I have done some investigation and sharing my results here,this may be useful for others.
//      First, we can check whether MockSetting option is turned ON
    public static boolean isMockSettingsON(Context context) {
        // returns true if mock location enabled, false if not enabled.
        if (Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ALLOW_MOCK_LOCATION).equals("0"))
            return false;
        else
            return true;
    }


    public static boolean areThereMockPermissionApps(Context context) {
        int count = 0;

        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages =
                pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo applicationInfo : packages) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(applicationInfo.packageName,
                        PackageManager.GET_PERMISSIONS);

                // Get Permissions
                String[] requestedPermissions = packageInfo.requestedPermissions;

                if (requestedPermissions != null) {
                    for (int i = 0; i < requestedPermissions.length; i++) {
                        if (requestedPermissions[i]
                                .equals("android.permission.ACCESS_MOCK_LOCATION")
                                && !applicationInfo.packageName.equals(context.getPackageName())) {
                            count++;
                        }
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                Log.e("Got exception ", e.getMessage());
            }
        }

        if (count > 0)
            return true;
        return false;
    }
}
