package com.mytuition.utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.mytuition.R;
import com.mytuition.interfaces.ApiInterface;
import com.mytuition.interfaces.UploadImageInterface;
import com.mytuition.models.CalendarModel;
import com.mytuition.models.SpecialityModel;
import com.mytuition.models.TeacherModel;
import com.mytuition.models.TimeSlotModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;


public class AppUtils {
    private static final String TAG = "AppUtils";
    public static Toast mToast;

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    static ProgressDialog progressDialog;

    public static final String MY_PREFS_NAME = "myPref";
    static int uploadImageCounter = 0;


    public static List<String> getAllSpeciality() {
        List<String> speciality = new ArrayList<>();
        speciality.add("English");
        speciality.add("Hindi");
        speciality.add("Maths");
        speciality.add("Science");
        speciality.add("Computer");
        speciality.add("Java");
        speciality.add("Physics");
        speciality.add("Hindi");
        speciality.add("Chemistry");
        speciality.add("All Subject");
        return speciality;
    }

    public static String getMimeType(Context context, Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = context.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }


    public static List<TimeSlotModel.TimeDetails> getSlots(boolean b, int i, int i1) {
        List<TimeSlotModel.TimeDetails> s1 = new ArrayList<>();
        ArrayList<String> results = getTimeSet(b, i, i1, 60);
        for (String s : results)
            s1.add(new TimeSlotModel.TimeDetails(s, false));
        return s1;
    }

    public static ArrayList<String> getTimeSet(boolean isCurrentDay, int from, int to, int duration) {
        double hrs = (float) duration / 60;
        Log.d(TAG, "getTimeSet: hrs " + (int) ((to - from) / hrs));
        ArrayList results = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);// what should be the default?
        if (!isCurrentDay)
            calendar.set(Calendar.HOUR_OF_DAY, from);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        int count = (int) ((to - from) / hrs);
        for (int i = 0; i < count; i++) {

            String day1 = sdf.format(calendar.getTime());
            calendar.add(Calendar.MINUTE, duration);

            String day2 = sdf.format(calendar.getTime());

            String day = day1 + " - " + day2;

            results.add(i, day);

        }
        return results;
    }


    public static List<CalendarModel> getNextWeekDays() {
        List<CalendarModel> calendarModelList = new ArrayList<>();
        ArrayList<HashMap<String, String>> getNextWeekDays = getNextWeekDay();
        for (int a = 1; a < getNextWeekDays.size(); a++) {
            calendarModelList.add(new CalendarModel(
                    getNextWeekDays.get(a).get("date"),
                    getNextWeekDays.get(a).get("day"),
                    getNextWeekDays.get(a).get("dateSend")));
        }
        return calendarModelList;
    }

    public static void showRequestDialog(Activity activity) {


        try {
            if (!((Activity) activity).isFinishing()) {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(activity);
                    progressDialog = ProgressDialog.show(activity, null, null, false, true);
                    progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(activity.getResources().getColor(android.R.color.transparent)));
                    progressDialog.setContentView(R.layout.progress_bar);
                    progressDialog.show();
                } else {
                    progressDialog = ProgressDialog.show(activity, null, null, false, true);
                    progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(activity.getResources().getColor(android.R.color.transparent)));
                    progressDialog.setContentView(R.layout.progress_bar);
                    progressDialog.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static List<SpecialityModel> getClassData() {
        List<SpecialityModel> specialityModels = new ArrayList<>();
        specialityModels.add(new SpecialityModel("", "Class 1", "1", false));
        specialityModels.add(new SpecialityModel("", "Class 2", "2", false));
        specialityModels.add(new SpecialityModel("", "Class 3", "3", false));
        specialityModels.add(new SpecialityModel("", "Class 4", "4", false));
        specialityModels.add(new SpecialityModel("", "Class 5", "5", false));
        specialityModels.add(new SpecialityModel("", "Class 6", "6", false));
        specialityModels.add(new SpecialityModel("", "Class 7", "7", false));
        specialityModels.add(new SpecialityModel("", "Class 8", "8", false));
        specialityModels.add(new SpecialityModel("", "Class 9 (UP Board)", "9", false));
        specialityModels.add(new SpecialityModel("", "Class 10 (UP Board)", "10", false));
        specialityModels.add(new SpecialityModel("", "Class 10 (ICSE Board)", "11", false));
        specialityModels.add(new SpecialityModel("", "Class 10 (ICSE Board)", "12", false));
        specialityModels.add(new SpecialityModel("", "Class 11 (UP Board)", "13", false));
        specialityModels.add(new SpecialityModel("", "Class 11 (UP Board)", "14", false));
        specialityModels.add(new SpecialityModel("", "Class 12 (ISE Board)", "15", false));
        specialityModels.add(new SpecialityModel("", "Class 12 (ISE Board)", "16", false));
        AppUtils.hideDialog();
        return specialityModels;

    }

    public static void hideDialog() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getJSONFromModel(Object o) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(o);
        try {
            JSONObject request = new JSONObject(jsonString);
            return request.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void updateTeacherProfile(TeacherModel teacherModel, final ApiInterface apiInterface) {
        Log.d(TAG, "getUid: " + getUid());
        getFirestoreReference().collection("Users").document(getUid()).update(getTeacherProfileMap(teacherModel))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        apiInterface.onSuccess("Profile Updated Successfully !!");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                apiInterface.onFailed("failed to upload Image, try again !!");
                Log.d(TAG, "onFailureUpdateProfile: " + e.getLocalizedMessage());
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                apiInterface.onFailed("cancel to update profile, try again !!");
            }
        });
    }

    private static Map<String, Object> getTeacherProfileMap(TeacherModel teacherModel) {
        Map<String, Object> map = new HashMap<>();
        map.put("image", teacherModel.getImage());
        map.put("name", teacherModel.getName());
        map.put("fatherName", teacherModel.getFatherName());
        map.put("email", teacherModel.getEmail());
        map.put("experience", teacherModel.getExperience());
        map.put("rating", teacherModel.getRating());
        map.put("review", teacherModel.getReview());
        map.put("speciality", teacherModel.getSpeciality());
        map.put("fee", teacherModel.getFee());
        map.put("perVisit", teacherModel.getPerVisit());
        map.put("degree", teacherModel.getDegree());
        map.put("collegeName", teacherModel.getCollegeName());
        map.put("address", teacherModel.getAddress());
        map.put("landMark", teacherModel.getLandMark());
        map.put("city", teacherModel.getCity());
        map.put("state", teacherModel.getState());
        map.put("aadharFrontImage", teacherModel.getAadharFrontImage());
        map.put("aadharBackImage", teacherModel.getAadharBackImage());
        Log.d(TAG, "getTeacherProfileMap: " + map);
        return map;
    }

    public static void uploadImages(final List<Bitmap> bitmaps, final UploadImageInterface uploadImageInterface) {

        final List<String> uploadedImageUrl = new ArrayList<>();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();


        for (uploadImageCounter = 0; uploadImageCounter < bitmaps.size(); uploadImageCounter++) {

            final String STORAGE_PATH = "aadhar_image/" + getUid() + "/" + System.currentTimeMillis() + ".jpg";
            StorageReference spaceRef = storageRef.child(STORAGE_PATH);

            Log.d(TAG, "uploadImages: " + uploadImageCounter);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmaps.get(uploadImageCounter).compress(Bitmap.CompressFormat.JPEG, 75, baos);
            byte[] compressData = baos.toByteArray();
            UploadTask uploadTask = spaceRef.putBytes(compressData);

            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    //  double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageRef.child(STORAGE_PATH).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            uploadedImageUrl.add(uri.toString());
                            if (uploadImageCounter == (bitmaps.size())) {
                                Log.d(TAG, "onSuccessEqual: " + uploadImageCounter);
                                uploadImageInterface.onSuccess(uploadedImageUrl);
                            } else Log.d(TAG, "onSuccess: notEqual " + uploadImageCounter);

                        }
                    });

                }
            }).addOnCanceledListener(new OnCanceledListener() {
                @Override
                public void onCanceled() {
                    uploadImageInterface.onFailed("Upload cancelled, try again");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    uploadImageInterface.onFailed("failed to upload Image " + e.getLocalizedMessage());
                }
            });
            
        }


    }


    private void showPdf(String filePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(filePath), "application/pdf");
        try {

        } catch (ActivityNotFoundException e) {
            Log.d(TAG, "showPdf: " + e.getLocalizedMessage());
        }
    }

    public static String parseDate(String inDate, String outPattern) {

        String inputPattern = "dd/MM/yy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(inDate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;

    }


    public static String getDayOfWeekDayFromDate(String date) {
        String dayName = "";
        SimpleDateFormat inFormat = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date myDate = inFormat.parse(date);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM d");
            dayName = simpleDateFormat.format(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dayName;

    }

    public static String getCurrentDateInWeekMonthDayFormat() {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current Date => " + c);

        SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d");
        String formattedDate = df.format(c);

        return formattedDate;
    }

    public static void showToastSort(Context context, String text) {
        if (mToast != null && mToast.getView().isShown()) {
            mToast.cancel();
        }
        mToast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        mToast.show();
    }

    public static boolean isEmailValid(String email) {

        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static void hideSoftKeyboard(Activity activity) {
        if (activity != null) {
            try {
                @SuppressLint("WrongConstant") InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService("input_method");
                View view = activity.getCurrentFocus();
                if (view != null) {
                    IBinder binder = view.getWindowToken();
                    if (binder != null) {
                        inputMethodManager.hideSoftInputFromWindow(binder, 0);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }

        return false;
    }

    public static String getDateInDMYFormatFromTimestamp(long currentTimeMillis) {
        try {
            String value = new java.text.SimpleDateFormat("yyyy-MM-dd").
                    format(new java.util.Date(currentTimeMillis));
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String getTimeFormat(long currentTimeMillis, String outFormat) {
        try {
            String value = new java.text.SimpleDateFormat(outFormat).
                    format(new java.util.Date(currentTimeMillis));
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    public static Animation fadeIn(Activity activity) {
        return AnimationUtils.loadAnimation(activity, R.anim.fade_in);
    }

    public static Animation fadeOut(Activity activity) {
        return AnimationUtils.loadAnimation(activity, R.anim.fade_out);
    }


    public static void setString(String key, String value, Activity activity) {
        SharedPreferences sharedpreferences = activity.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void setBoolean(String key, boolean value, Activity activity) {
        SharedPreferences sharedpreferences = activity.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static String getString(String key, Activity activity) {
        if (activity != null) {
            SharedPreferences pref = activity.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            return pref.getString(key, "");
        } else return null;

    }

    public static boolean getBoolean(String key, Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        return pref.getBoolean(key, true);
    }

    public static ArrayList<HashMap<String, String>> getNextWeekDay() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        HashMap<String, String> hashMap = new HashMap<>();
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat sdfDay = new SimpleDateFormat("EEE", Locale.getDefault());
        SimpleDateFormat sdfDateSend = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        for (int i = 0; i < 8; i++) {
            hashMap = new HashMap<>();

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, i);
            String date = sdfDate.format(calendar.getTime());
            String day = sdfDay.format(calendar.getTime());
            String dateSend = sdfDateSend.format(calendar.getTime());

            hashMap.put("date", date);
            hashMap.put("day", day);
            hashMap.put("dateSend", dateSend);

            list.add(hashMap);

        }
        return list;
    }


    public static String getDayFromDate(String incomingDate) {
        String inputPattern = "dd/MM/yyyy";
        String outputPattern = "EEE";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(incomingDate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    public static JSONObject objectToJSONObject(Object object){
        Object json = null;
        JSONObject jsonObject = null;
        try {
            json = new JSONTokener(object.toString()).nextValue();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (json instanceof JSONObject) {
            jsonObject = (JSONObject) json;
        }
        return jsonObject;
    }

    public static JSONArray objectToJSONArray(Object object){
        Object json = null;
        JSONArray jsonArray = null;
        try {
            json = new JSONTokener(object.toString()).nextValue();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (json instanceof JSONArray) {
            jsonArray = (JSONArray) json;
        }
        return jsonArray;
    }

    public static String sdfFromTimeStamp(String outPattern) {
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        SimpleDateFormat formatter = new SimpleDateFormat(outPattern);
        System.out.println(formatter.format(ts));
        return formatter.format(ts);
    }

    public static String getMonthFromDate(String incomingDate) {
        String inputPattern = "dd/MM/yyyy";
        String outputPattern = "MMM";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(incomingDate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String getDateFromDate(String incomingDate) {
        String inputPattern = "dd/MM/yyyy";
        String outputPattern = "dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(incomingDate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            time *= 1000;
        }
        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }

    public static String getDateInDMY(long timestamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d, ''yy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis((int) timestamp);
        return formatter.format(calendar.getTime());
    }


    public static void getFcmToken(final Activity activity) {
/*
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
            String newToken = instanceIdResult.getToken();
            setString(fcmToken, newToken, activity);
            Log.e("newToken2", newToken);
        });*/

    }


    public static FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static FirebaseFirestore getFirestoreReference() {
        return FirebaseFirestore.getInstance();
    }

    public static String getUid() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (null != user)
            return user.getUid();
        else return null;
    }

    public static String getCurrencyFormat(double num) {
        String COUNTRY = "IN";
        String LANGUAGE = "en";
        return NumberFormat.getCurrencyInstance(new Locale(LANGUAGE, COUNTRY)).format(num);
    }

    public static String getCurrencyFormat(long num) {
        String COUNTRY = "IN";
        String LANGUAGE = "en";
        return NumberFormat.getCurrencyInstance(new Locale(LANGUAGE, COUNTRY)).format(num);
    }

    public static String getCurrencyFormat(String num) {
        Double number = Double.parseDouble(num);
        String COUNTRY = "IN";
        String LANGUAGE = "en";
        return NumberFormat.getCurrencyInstance(new Locale(LANGUAGE, COUNTRY)).format(number);

    }

    public static void showToolbar(Activity activity) {
        Objects.requireNonNull(((AppCompatActivity) activity).getSupportActionBar()).show();
    }

    public static void hideToolbar(Activity activity) {
        Objects.requireNonNull(((AppCompatActivity) activity).getSupportActionBar()).hide();
    }
}