
package com.gaiya.easybuy.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.gaiya.easybuy.view.SelectPicPopupWindow;
import com.gaiya.easybuy.view.SelectSexPopupWindow;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

/**
 * Created by dengt on 15-9-24.
 */
public class UserInformationActivity extends BaseActivity implements View.OnClickListener,
        SelectSexPopupWindow.SelectResult, ICallback, SelectPicPopupWindow.SelectPictureResult {
    private EditText userName;
    private EditText userNick;
    private TextView userSex;
    private TextView userBrith;
    private TextView userComname;
    private EditText userSubject;
    private TextView userWorkdate;
    private TextView userPhone;
    private boolean editFlag;
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
        // registerBtn = ViewUtil.findViewById(this, R.id.commit_btn);
    }

    private void initEvent() {
        leftBtn.setOnClickListener(this);
        // registerBtn.setOnClickListener(this);
      //  userName.setOnClickListener(this);
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
                        ApiUrl.GET_COMPANY_IMG+"?"+"id="+userModel.getImageId(),
                        R.drawable.user_default, R.drawable.user_default,true);
            PreferencesUtils.putString(gApplication,
                                  PreferencesConstant.USER_IMAGEURL, ApiUrl.GET_COMPANY_IMG+"?"+"id="+userModel.getImageId());
//                BitmapModel.imageGet(userModel.getImageId()).done(new ICallback() {
//                    @Override
//                    public void call(Arguments arguments) {
//                        JSONObject jsonObject = arguments.get(0);
//                        String code = jsonObject.optString("status");
//
//                        Logger.e("uploadimg", jsonObject.toString());
//                        if (code.equals("000000")) {
//                            BitmapModel bitmapModel = JSONUtil
//                                    .load(BitmapModel.class,
//                                            jsonObject.optJSONObject("result"));
//                            ImageViewAdapter.adapt(userImg,
//                                    bitmapModel.getUri(),
//                                    R.drawable.touxiang, R.drawable.touxiang);
//                            PreferencesUtils.putString(gApplication,
//                                    PreferencesConstant.USER_IMAGEURL, bitmapModel.getUri());
//                        }
//                    }
//                });

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
                        UserInformationActivity.this, new DatePickerDialog.OnDateSetListener()
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
                        UserInformationActivity.this, new DatePickerDialog.OnDateSetListener()
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
                String name = userName.getText().toString().trim();
                String nickname = userNick.getText().toString().trim();
                // todo

                String sex = userSex.getText().toString().trim();
                if ("男".equals(sex)) {
                    sex = "MALE";
                } else {
                    sex = "FEMALE";
                }
                String imageId = userModel.getImageId();
                String birthDay = userBrith.getText().toString().trim();
                String position = userSubject.getText().toString().trim();
                String entryDate = userWorkdate.getText().toString().trim();
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
                            gApplication.updateApiToken();
                        } else {
                            ToastUtil.showMessage(ErrorCode.getCodeName(code));
                        }
                    }
                });
                finish();
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

    @Override
    public void selectPhoto() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, selectCode);
    }

    @Override
    public void resultCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, resultPictureCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }

        if (selectCode == requestCode) {
            uri = data.getData();
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 60);
            intent.putExtra("outputY", 60);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            intent.putExtra("return-data", true);// 设置为不返回数据
            startActivityForResult(intent, requestCropIcon);
        } else if (requestCropIcon == requestCode) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                if (uri == null) {
                    return;
                }
                Bitmap photo = null;
             //   try {
                   // photo = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    photo = data.getParcelableExtra("data");
                    if (photo != null) {
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
                                            ApiUrl.GET_COMPANY_IMG+"?"+"id="+bitmapModel.getId(),
                                            R.drawable.user_default, R.drawable.user_default,true);
                                    PreferencesUtils.putString(gApplication,
                                            PreferencesConstant.USER_IMAGEURL, ApiUrl.GET_COMPANY_IMG+"?"+"id="+bitmapModel.getId());
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
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

            }
        } else if (resultPictureCode == requestCode) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                if (photo != null) {
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
                                        ApiUrl.GET_COMPANY_IMG+"?"+"id="+bitmapModel.getId(),
                                        R.drawable.user_default, R.drawable.user_default,true);
                                PreferencesUtils.putString(gApplication,
                                        PreferencesConstant.USER_IMAGEURL, ApiUrl.GET_COMPANY_IMG+"?"+"id="+bitmapModel.getId());
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
                    }).fail(new ICallback() {
                        @Override
                        public void call(Arguments arguments) {

                        }
                    });
                }
            }
        }
    }
}
