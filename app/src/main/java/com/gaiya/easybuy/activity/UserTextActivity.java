
package com.gaiya.easybuy.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaiya.android.async.Arguments;
import com.gaiya.android.async.ICallback;
import com.gaiya.android.json.JSONUtil;
import com.gaiya.android.util.Logger;
import com.gaiya.android.view.ImageViewAdapter;
import com.gaiya.easybuy.R;
import com.gaiya.easybuy.application.GApplication;
import com.gaiya.easybuy.constant.ApiUrl;
import com.gaiya.easybuy.constant.ErrorCode;
import com.gaiya.easybuy.model.BitmapModel;
import com.gaiya.easybuy.model.UserModel;
import com.gaiya.easybuy.util.FileUtils;
import com.gaiya.easybuy.util.PreferencesConstant;
import com.gaiya.easybuy.util.PreferencesUtils;
import com.gaiya.easybuy.util.SystemUtil;
import com.gaiya.easybuy.util.ToastUtil;
import com.gaiya.easybuy.util.ViewUtil;
import com.gaiya.easybuy.view.CircleImageView;
import com.gaiya.easybuy.view.LoadingDialog;
import com.gaiya.easybuy.view.SelectPicPopupWindow;
import com.gaiya.easybuy.view.SelectSexPopupWindow;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;

/**
 * Created by dengt on 15-11-11.
 */
public class UserTextActivity extends BaseActivity implements View.OnClickListener,
        SelectSexPopupWindow.SelectResult, ICallback, SelectPicPopupWindow.SelectPictureResult {
    private EditText userName;
    private EditText userNick;
    private TextView userSex;
    private TextView userBrith;
    private TextView userComname;
    private EditText userSubject;
    private TextView userWorkdate;
    private TextView userPhone;
    private LinearLayout ll;
    private boolean editFlag;
    private LoadingDialog progress;
    private CircleImageView userImg;
    private UserModel userModel;
    private SelectSexPopupWindow menuWindow;
    private GApplication gApplication;
    private int year, monthOfYear, dayOfMonth, hourOfDay, minute;

    private SelectPicPopupWindow menupicWindow;
    Uri uri;
    private int selectCode = 1;
    private int requestCropIcon = 2;
    private int resultPictureCode = 3;
    private File tempFile;
    // private Button registerBtn;

    @Override
    protected void doInit() {
        gApplication = GApplication.getInstance();
        initView();
        initDate();
        initEvent();
    }

    private void initView() {
        userName = ViewUtil.findViewById(this, R.id.info_name);
        userNick = ViewUtil.findViewById(this, R.id.info_nick_name);
        userSex = ViewUtil.findViewById(this, R.id.info_sex);
        userBrith = ViewUtil.findViewById(this, R.id.info_brith);
        userComname = ViewUtil.findViewById(this, R.id.info_com_name);
        userSubject = ViewUtil.findViewById(this, R.id.info_subject);
        userWorkdate = ViewUtil.findViewById(this, R.id.info_work_date);
        userPhone = ViewUtil.findViewById(this, R.id.info_phone);
        userImg = ViewUtil.findViewById(this, R.id.info_img);
        ll = ViewUtil.findViewById(this,R.id.ll);
        // registerBtn = ViewUtil.findViewById(this, R.id.commit_btn);
    }

    private void initEvent() {
        leftBtn.setOnClickListener(this);
        // registerBtn.setOnClickListener(this);
        // userName.setOnClickListener(this);
        // userNick.setOnClickListener(this);
        userSex.setOnClickListener(this);
        userBrith.setOnClickListener(this);
        // userComname.setOnClickListener(this);
        // userSubject.setOnClickListener(this);
        userWorkdate.setOnClickListener(this);
        userImg.setOnClickListener(this);
        // userPhone.setOnClickListener(this);
    }

    private void initDate() {
        setTitle("基本信息");
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        monthOfYear = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        UserModel.get().done(this).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {

            }
        });
    }

    @Override
    public void call(Arguments arguments) {
        JSONObject jsonObject = arguments.get(0);
        String code = jsonObject.optString("status");

        Logger.e("userinfo", jsonObject.toString());
        if (code.equals("000000")) {
            userModel = JSONUtil
                    .load(UserModel.class, jsonObject.optJSONObject("result"));
            String token = jsonObject.optString("newToken");
            String userid = userModel.getUserId();
            PreferencesUtils.putString(gApplication, PreferencesConstant.API_TOKEN, token);
            PreferencesUtils.putString(gApplication, PreferencesConstant.USER_ID, userid);
            PreferencesUtils.putString(gApplication, PreferencesConstant.USER_IMGID,
                    userModel.getImageId());

            PreferencesUtils.putString(gApplication, PreferencesConstant.NICK_NAME,
                    userModel.getNickName());
            gApplication.updateApiToken();
            gApplication.updateUserID();
            userName.setText(userModel.getName());
            userNick.setText(userModel.getNickName());
            userSex.setText(userModel.getSex().equals("MALE") ? "男" : "女");
            if (!userModel.getBirthday().isEmpty())
                userBrith.setText(SystemUtil.getFormatedDateTime("yyyy-MM-dd",
                        Long.valueOf(userModel.getBirthday())));
            userComname.setText(userModel.getCompanyName());
            userSubject.setText(userModel.getPosition());
            if (!userModel.getEntryDate().isEmpty())
                userWorkdate.setText(SystemUtil.getFormatedDateTime("yyyy-MM-dd",
                        Long.valueOf(userModel.getEntryDate())));
            userPhone.setText(userModel.getPhone());
            if (!userModel.getImageId().isEmpty())
                ImageViewAdapter.adapt(userImg,
                        ApiUrl.GET_COMPANY_IMG + "?" + "id=" + userModel.getImageId(),
                        R.drawable.user_default, R.drawable.user_default,true);
            PreferencesUtils.putString(gApplication,
                    PreferencesConstant.USER_IMAGEURL, ApiUrl.GET_COMPANY_IMG + "?" + "id="
                            + userModel.getImageId());
        } else {
            ToastUtil.showMessage(ErrorCode.getCodeName(code));
        }
    }

    @Override
    protected boolean isDisplayTitle() {
        return true;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_user_info;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.info_img:
                menupicWindow = new SelectPicPopupWindow(this, this);
                menupicWindow.showAtLocation(this.findViewById(R.id.info_name), Gravity.BOTTOM
                        | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
                break;
            case R.id.info_sex:
                menuWindow = new SelectSexPopupWindow(this, this);
                menuWindow.showAtLocation(this.findViewById(R.id.info_img_relative), Gravity.BOTTOM
                        | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置

                break;
            case R.id.info_brith:
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        this, new DatePickerDialog.OnDateSetListener()
                        {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                    int dayOfMonth) {
                                userBrith.setText(year
                                        + "-"
                                        + ((monthOfYear + 1) >= 10 ? (monthOfYear + 1) : "0"
                                                + (monthOfYear + 1)) + "-"
                                        + (dayOfMonth >= 10 ? dayOfMonth : "0" + dayOfMonth));
                            }
                        }, year, monthOfYear, dayOfMonth);

                datePickerDialog.show();
                break;
            case R.id.info_work_date:
                DatePickerDialog datePickerDialog_ = new DatePickerDialog(
                        this, new DatePickerDialog.OnDateSetListener()
                        {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                    int dayOfMonth) {
                                userWorkdate.setText(year
                                        + "-"
                                        + ((monthOfYear + 1) >= 10 ? (monthOfYear + 1) : "0"
                                                + (monthOfYear + 1)) + "-"
                                        + (dayOfMonth >= 10 ? dayOfMonth : "0" + dayOfMonth));
                            }
                        }, year, monthOfYear, dayOfMonth);

                datePickerDialog_.show();

                break;
            case R.id.left_btn:
                //todo
                if(progress == null){
                    progress = new LoadingDialog(this);
                }
                progress.show();
                String name = userName.getText().toString().trim();
                final String nickname = userNick.getText().toString().trim();


                String sex = userSex.getText().toString().trim();
                Logger.i("sex","sex="+sex);
                if ("男".equals(sex)) {
                    sex = "MALE";
                } else {
                    sex = "FEMALE";
                }
                String imageId = null;
                String birthDay = null;
                String position = null;
                String entryDate = null;
                if(userModel==null){
                    ViewUtil.finish(UserTextActivity.this);
                }else {
                    imageId = userModel.getImageId();
                    birthDay = userBrith.getText().toString().trim();
                    position = userSubject.getText().toString().trim();
                    entryDate = userWorkdate.getText().toString().trim();
                }
                if (nickname.equals(userModel.getNickName())) {
                    PreferencesUtils.putString(gApplication, PreferencesConstant.NICK_NAME,
                            nickname);
                }
                UserModel.updateUserInfo(name, nickname, sex, imageId, birthDay, position,
                        entryDate).done(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        JSONObject jsonObject = arguments.get(0);
                        String code = jsonObject.optString("status");

                        Logger.e("userinfo", jsonObject.toString());
                        if (code.equals("000000")) {
                            String token = jsonObject.optString("newToken");
                            PreferencesUtils.putString(gApplication, PreferencesConstant.API_TOKEN, token);
                            gApplication.updateApiToken();
                            PreferencesUtils.putString(UserTextActivity.this,PreferencesConstant.NICK_NAME,nickname);
                        } else {
                            ToastUtil.showMessage(ErrorCode.getCodeName(code));
                        }
                        progress.dismiss();
                        ViewUtil.finish(UserTextActivity.this);
                    }
                }).fail(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        ToastUtil.showMessage("网络异常");
                        //progress.dismiss();
                        ViewUtil.finish(UserTextActivity.this);
                    }
                });

                break;
            default:
                break;
        }
    }

    @Override
    public void selectFemale() {
        userSex.setText("女");
    }

    @Override
    public void selectMale() {

        userSex.setText("男");
    }

    private static final String IMAGE_FILE_NAME = "myFile.jpg";
    /** 请求码 */
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;

    @Override
    public void selectPhoto() {
        /*Intent intentFromGallery = new Intent();
        //intentFromGallery.setType("image*//*"); // 设置文件类型
        intentFromGallery
                .setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentFromGallery,
                IMAGE_REQUEST_CODE);*/
        startPhotoZoom(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    }

    @Override
    public void resultCamera() {
        Intent intentFromCapture = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intentFromCapture,
                CAMERA_REQUEST_CODE);
    }

    public void startPhotoZoom(Uri uri) {

        Intent intent = new Intent(Intent.ACTION_PICK,null);
        intent.setDataAndType(uri, "image/*");
        /*tempFile=new File("/sdcard/ll1x/"+Calendar.getInstance().getTimeInMillis()+".jpg"); // 以时间秒为文件名
        File temp = new File("/sdcard/ll1x/");//自已项目 文件夹
        if (!temp.exists()) {
            temp.mkdir();
        }
        intent.putExtra("output", Uri.fromFile(tempFile));  // 专入目标文件
        intent.putExtra("outputFormat", "JPEG"); //输入文件格式
*/
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 60);
        intent.putExtra("outputY", 60);
        intent.putExtra("return-data", true);


        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    /**
     * 保存裁剪之后的图片数据
     */

    private void getImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);
            UserModel.setAvatar(FileUtils.Bitmap2Bytes(photo)).done(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    JSONObject jsonObject = arguments.get(0);
                    String code = jsonObject.optString("status");

                    Logger.e("uploadimg", jsonObject.toString());
                    if (code.equals("000000")) {
                        BitmapModel bitmapModel = JSONUtil
                                .load(BitmapModel.class,
                                        jsonObject.optJSONObject("result"));
                        ImageViewAdapter.adapt(userImg,
                                ApiUrl.GET_COMPANY_IMG + "?" + "id=" + bitmapModel.getId(),
                                R.drawable.user_default, R.drawable.user_default,true);
                        PreferencesUtils.putString(gApplication,
                                PreferencesConstant.USER_IMAGEURL, ApiUrl.GET_COMPANY_IMG + "?"
                                        + "id=" + bitmapModel.getId());
                        userModel.setImageId(bitmapModel.getId());
                        BitmapModel.imageUser(bitmapModel.getId()).done(
                                new ICallback() {
                                    @Override
                                    public void call(Arguments arguments) {
                                        JSONObject jsonObject = arguments.get(0);
                                        String code = jsonObject.optString("status");

                                        Logger.e("uploadimgid", jsonObject.toString());
                                        if (code.equals("000000")) {
                                        }
                                    }
                                }

                                );
                        PreferencesUtils.putString(gApplication,
                                PreferencesConstant.USER_IMGID, bitmapModel.getId());

                    }
                }
            }
                    );
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case IMAGE_REQUEST_CODE:
               // startPhotoZoom(data.getData());
                break;
            case CAMERA_REQUEST_CODE:
                // 判断存储卡是否可以用，可用进行存储
                String state = Environment.getExternalStorageState();
                if (state.equals(Environment.MEDIA_MOUNTED)) {
                  /*  File path = Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                    File tempFile = new File(path, IMAGE_FILE_NAME);
                    startPhotoZoom(Uri.fromFile(tempFile));*/
                    getImageToView(data);

                } else {
                    ToastUtil.showMessage("未找到存储卡，无法存储照片！");
                }
                break;
            case RESULT_REQUEST_CODE:// 图片缩放
                if (data != null) {
                    getImageToView(data);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE );
                if (imm != null ) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    ll.requestFocus();
                }
            }
            return super .dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true ;
        }
        return onTouchEvent(ev);
    }
    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false ;
            } else {
                return true ;
            }
        }
        return false ;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ViewUtil.finish(UserTextActivity.this);
    }
}
