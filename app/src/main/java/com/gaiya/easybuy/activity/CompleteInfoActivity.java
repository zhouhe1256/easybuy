
package com.gaiya.easybuy.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.gaiya.android.async.Arguments;
import com.gaiya.android.async.ICallback;
import com.gaiya.android.json.JSONUtil;
import com.gaiya.android.util.Logger;
import com.gaiya.android.view.ImageViewAdapter;
import com.gaiya.easybuy.R;
import com.gaiya.easybuy.adapter.CatalogAdapter;
import com.gaiya.easybuy.application.GApplication;
import com.gaiya.easybuy.constant.ApiUrl;
import com.gaiya.easybuy.constant.ErrorCode;
import com.gaiya.easybuy.model.BitmapModel;
import com.gaiya.easybuy.model.CataLogListModel;
import com.gaiya.easybuy.model.CataLogModel;
import com.gaiya.easybuy.model.CompanyModel;
import com.gaiya.easybuy.model.UserModel;
import com.gaiya.easybuy.util.FileUtils;
import com.gaiya.easybuy.util.PreferencesConstant;
import com.gaiya.easybuy.util.PreferencesUtils;
import com.gaiya.easybuy.util.ToastUtil;
import com.gaiya.easybuy.util.ViewUtil;
import com.gaiya.easybuy.view.SelectPicPopupWindow;
import com.gaiya.easybuy.view.TextViewDelete;
import com.gaiya.easybuy.view.XCFlowLayout;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengt on 15-9-25.
 */
public class CompleteInfoActivity extends BaseActivity implements View.OnClickListener, ICallback,
        SelectPicPopupWindow.SelectPictureResult {

    private ImageView uploadImage;

    private GApplication gApplication;
    private CompanyModel companyModel;
    private EditText comName;
    private Spinner comtype;
    private ImageView addSubject;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;
    // 业务
    private Spinner spinner1;
    private Spinner spinner2;
    private Spinner spinner3;
    private LinearLayout linear;
    private LinearLayout ll;
    private CatalogAdapter catalogAdapter1;
    private CatalogAdapter catalogAdapter2;
    private CatalogAdapter catalogAdapter3;
    private List<CataLogModel> items1;
    private List<CataLogModel> items2;
    private List<CataLogModel> items3;
    private CataLogModel addid;
    private Button backButton;
    ViewGroup.MarginLayoutParams lp;
    private XCFlowLayout mFlowLayout;
    String type = "SUPPLIER";
    private SelectPicPopupWindow menuWindow;
    private ArrayList<CataLogModel> n = new ArrayList<CataLogModel>();
    Uri uri;
    private int selectCode = 1;
    private int requestCropIcon = 2;
    private int resultPictureCode = 3;
    private int p = 0;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                for(int i=0;i<n.size();i++){
                    addid = n.get(i);

                Logger.i("namess", mFlowLayout.getChildCount() + "");
                CataLogListModel.addCatalog(addid.getId()).done(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        JSONObject jsonObject = arguments.get(0);
                        String code = jsonObject.optString("status");

                        Logger.e("addCatalog", jsonObject.toString());
                        if (code.equals("000000")) {
                            TextViewDelete view = new TextViewDelete(CompleteInfoActivity.this);
                            view.setText(addid);
                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CataLogListModel.delCatalog(((TextViewDelete) v).getid()).done(
                                            new ICallback() {
                                                @Override
                                                public void call(Arguments arguments) {
                                                    JSONObject jsonObject = arguments.get(0);
                                                    String code = jsonObject.optString("status");

                                                    Logger.e("delCatalog", jsonObject.toString());
                                                    if (code.equals("000000")) {

                                                    }
                                                }
                                            });

                                    mFlowLayout.removeView(v);
                                }
                            });
                            mFlowLayout.addView(view, lp);
                        }else{
                            ToastUtil.showMessage(ErrorCode.getCodeName(code));
                        }
                    }
                });
            }}
        }
    };
    @Override
    protected void doInit() {
        gApplication = GApplication.getInstance();
        initView();
        initDate();
        initEvent();
    }

    private void initView() {
        //backButton = ViewUtil.findViewById(this,R.id.left_btn);
        uploadImage = ViewUtil.findViewById(this, R.id.upload_image);

        comName = ViewUtil.findViewById(this, R.id.info_com_name);
        comtype = ViewUtil.findViewById(this, R.id.info_com_type);

        addSubject = ViewUtil.findViewById(this, R.id.add_subject);
        spinner1 = ViewUtil.findViewById(this, R.id.spinner1);
        spinner2 = ViewUtil.findViewById(this, R.id.spinner2);
        spinner3 = ViewUtil.findViewById(this, R.id.spinner3);
        ll = ViewUtil.findViewById(this,R.id.ll);
        mFlowLayout = (XCFlowLayout) findViewById(R.id.flowlayout);
        linear = (LinearLayout) findViewById(R.id.linear);
        //backButton.setVisibility(View.GONE);
    }

    private void initEvent() {
        rightText.setOnClickListener(this);
        leftBtn.setOnClickListener(this);
        uploadImage.setOnClickListener(this);
       // comName.setOnClickListener(this);
        // 添加事件Spinner事件监听
        comtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(data_list.get(position).equals("采购商")){
                    linear.setVisibility(View.GONE);
                }else{
                    linear.setVisibility(View.VISIBLE);
                }
                if (companyModel != null) {
                    type = arr_adapter.getItem(position);
                    if ("供应商(原厂)".equals(data_list.get(position))) {
                        // comtype.setSelection(0);
                        type = "SUPPLIER";
                    }
                    else if ("供应商(代理)".equals(data_list.get(position))) {
                        // comtype.setSelection(1);
                        type = "AGENT";
                    } else if ("采购商".equals(data_list.get(position))) {
                        // comtype.setSelection(2);
                        type = "BUYER";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 取业务
                CataLogListModel.getCatalog(
                        Long.toString(((CataLogModel) catalogAdapter1.getItem(position)).getId()))
                        .done(new ICallback() {
                            @Override
                            public void call(Arguments arguments) {
                                JSONObject jsonObject = arguments.get(0);
                                String code = jsonObject
                                        .optString("status");
                                Logger.e("getCatalogData2",
                                        jsonObject.toString());
                                if (code.equals("000000")) {
                                    List<CataLogModel> cataLogListModel = JSONUtil
                                            .load(CataLogModel.class,
                                                    jsonObject
                                                            .optJSONArray("result"));
                                    catalogAdapter2
                                            .updateListView(cataLogListModel);
                                    // 取业务
                                    CataLogListModel.getCatalog(
                                            Long.toString(cataLogListModel.get(0).getId()))
                                            .done(new ICallback() {
                                                @Override
                                                public void call(Arguments arguments) {
                                                    JSONObject jsonObject = arguments.get(0);
                                                    String code = jsonObject
                                                            .optString("status");
                                                    Logger.e("getCatalogData3",
                                                            jsonObject.toString());
                                                    if (code.equals("000000")) {
                                                        List<CataLogModel> cataLogListModel = JSONUtil
                                                                .load(CataLogModel.class,
                                                                        jsonObject
                                                                                .optJSONArray("result"));
                                                        catalogAdapter3
                                                                .updateListView(cataLogListModel);
                                                        addid = ((CataLogModel) catalogAdapter3
                                                                .getItem(0));
                                                        Logger.i("iddd",addid.getId()+"");
                                                    }
                                                }
                                            });

                                }
                            }
                        });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 取业务
                CataLogListModel.getCatalog(
                        Long.toString(((CataLogModel) catalogAdapter2.getItem(position)).getId()))
                        .done(new ICallback() {
                            @Override
                            public void call(Arguments arguments) {
                                JSONObject jsonObject = arguments.get(0);
                                String code = jsonObject
                                        .optString("status");
                                Logger.e("getCatalogData3",
                                        jsonObject.toString());
                                if (code.equals("000000")) {
                                    List<CataLogModel> cataLogListModel = JSONUtil
                                            .load(CataLogModel.class,
                                                    jsonObject
                                                            .optJSONArray("result"));
                                    catalogAdapter3
                                            .updateListView(cataLogListModel);
                                    addid = ((CataLogModel) catalogAdapter3.getItem(0));
                                    Logger.i("iddd",addid.getId()+"");
                                }
                            }
                        });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 取业务
                addid = ((CataLogModel) catalogAdapter3.getItem(position));
                Logger.i("iddd",addid.getId()+"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        addSubject.setOnClickListener(this);

    }

    private void initDate() {
        setTitle("完善信息");
        rightText.setVisibility(View.VISIBLE);
        rightText.setText("提交");
        catalogAdapter1 = new CatalogAdapter(items1, this);
        catalogAdapter2 = new CatalogAdapter(items2, this);
        catalogAdapter3 = new CatalogAdapter(items3, this);
        spinner1.setAdapter(catalogAdapter1);
        spinner2.setAdapter(catalogAdapter2);
        spinner3.setAdapter(catalogAdapter3);
        lp = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 5;
        lp.rightMargin = 5;
        lp.topMargin = 5;
        lp.bottomMargin = 5;
        CompanyModel.get().done(this).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {

            }
        });
        // 数据
        data_list = new ArrayList<String>();
        data_list.add("供应商(原厂)");
        data_list.add("供应商(代理)");
        data_list.add("采购商");
        // 适配器
        arr_adapter = new ArrayAdapter<String>(this, R.layout.item_my_spinner,
                data_list);
        // 设置样式
        arr_adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        // 加载适配器
        comtype.setAdapter(arr_adapter);
        // 取业务
        CataLogListModel.getCatalog("0").done(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                JSONObject jsonObject = arguments.get(0);
                String code = jsonObject.optString("status");

                Logger.e("getCatalogData1", jsonObject.toString());
                if (code.equals("000000")) {
                    List<CataLogModel> cataLogListModel = JSONUtil
                            .load(CataLogModel.class, jsonObject.optJSONArray("result"));
                    catalogAdapter1.updateListView(cataLogListModel);
                    // spinner1.setSelection(0);
                    // 取业务
                    CataLogListModel.getCatalog(Long.toString(cataLogListModel.get(0).getId()))
                            .done(new ICallback() {
                                @Override
                                public void call(Arguments arguments) {
                                    JSONObject jsonObject = arguments.get(0);
                                    String code = jsonObject.optString("status");

                                    Logger.e("getCatalogData2", jsonObject.toString());
                                    if (code.equals("000000")) {
                                        List<CataLogModel> cataLogListModel = JSONUtil
                                                .load(CataLogModel.class,
                                                        jsonObject.optJSONArray("result"));
                                        catalogAdapter2.updateListView(cataLogListModel);
                                        // spinner2.setSelection(0);

                                        // 取业务
                                        CataLogListModel.getCatalog(
                                                Long.toString(cataLogListModel.get(0).getId()))
                                                .done(new ICallback() {
                                                    @Override
                                                    public void call(Arguments arguments) {
                                                        JSONObject jsonObject = arguments.get(0);
                                                        String code = jsonObject
                                                                .optString("status");
                                                        Logger.e("getCatalogData3",
                                                                jsonObject.toString());
                                                        if (code.equals("000000")) {
                                                            List<CataLogModel> cataLogListModel = JSONUtil
                                                                    .load(CataLogModel.class,
                                                                            jsonObject
                                                                                    .optJSONArray("result"));
                                                            catalogAdapter3
                                                                    .updateListView(cataLogListModel);
                                                            addid = ((CataLogModel) catalogAdapter3
                                                                    .getItem(0));

                                                        }
                                                    }
                                                });
                                    }
                                }
                            });
                }
            }
        });
    }

    @Override
    public void call(Arguments arguments) {
        JSONObject jsonObject = arguments.get(0);
        String code = jsonObject.optString("status");
        Logger.e("company", jsonObject.toString());
        if (code.equals("000000")) {
            companyModel = JSONUtil
                    .load(CompanyModel.class, jsonObject.optJSONObject("result"));
            String token = jsonObject.optString("newToken");
            PreferencesUtils.putString(gApplication, PreferencesConstant.API_TOKEN, token);
            gApplication.updateApiToken();

            comName.setText(companyModel.getName());
            if ("SUPPLIER".equals(companyModel.getType()))
                comtype.setSelection(0);
            else if ("AGENT".equals(companyModel.getType())) {
                comtype.setSelection(1);
            } else if ("BUYER".equals(companyModel.getType())) {
                comtype.setSelection(2);
            }
            // if (!"DRAFT".equals(companyModel.getStatus())) {
            // comName.setOnClickListener(null);
            // comtype.setEnabled(false);
            //
            // } else {
            // comName.setOnClickListener(this);
            // comtype.setEnabled(true);
            //
            // }
            if (!companyModel.getCatalogList().isEmpty())
                for (int i = 0; i < companyModel.getCatalogList().size(); i++) {
                    TextViewDelete view = new TextViewDelete(this);
                    view.setText(companyModel.getCatalogList().get(i));

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CataLogListModel.delCatalog(((TextViewDelete) v).getid()).done(
                                    new ICallback() {
                                        @Override
                                        public void call(Arguments arguments) {
                                            JSONObject jsonObject = arguments.get(0);
                                            String code = jsonObject.optString("status");

                                            Logger.e("delCatalog", jsonObject.toString());
                                            if (code.equals("000000")) {

                                            }
                                        }
                                    });

                            mFlowLayout.removeView(v);
                        }
                    });
                    mFlowLayout.addView(view, lp);
                }

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
        return R.layout.activity_complete_info;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upload_image:
                menuWindow = new SelectPicPopupWindow(this, this);
                menuWindow.showAtLocation(this.findViewById(R.id.info_com_name), Gravity.BOTTOM
                        | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
                break;
            case R.id.left_btn:
                Intent intent = new Intent(CompleteInfoActivity.this, TabActivity.class);
                ViewUtil.startTopActivity(CompleteInfoActivity.this, intent);
                MainActivity.PAGE_F = 3;
                finish();
                break;
            case R.id.add_subject:
                boolean f = true;
                for(int i=0;i<n.size();i++){
                    if(n.get(i).getName().equals(addid.getName())){
                        f = false;
                    }
                }
                if(f){

                final TextViewDelete view = new TextViewDelete(CompleteInfoActivity.this);
                view.setText(addid);
                view.setTextINtext(p);
                n.add(addid);
                p++;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mFlowLayout.removeView(v);
                        n.remove(view.getTextINtext());
                    }
                });
                mFlowLayout.addView(view, lp);

                }else{
                    ToastUtil.showMessage("不能添加相同的业务");
                }

                /*Logger.i("namess", mFlowLayout.getChildCount() + "");
                CataLogListModel.addCatalog(addid.getId()).done(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        JSONObject jsonObject = arguments.get(0);
                        String code = jsonObject.optString("status");

                        Logger.e("addCatalog", jsonObject.toString());
                        if (code.equals("000000")) {
                            TextViewDelete view = new TextViewDelete(CompleteInfoActivity.this);
                            view.setText(addid);
                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CataLogListModel.delCatalog(((TextViewDelete) v).getid()).done(
                                            new ICallback() {
                                                @Override
                                                public void call(Arguments arguments) {
                                                    JSONObject jsonObject = arguments.get(0);
                                                    String code = jsonObject.optString("status");

                                                    Logger.e("delCatalog", jsonObject.toString());
                                                    if (code.equals("000000")) {

                                                    }
                                                }
                                            });

                                    mFlowLayout.removeView(v);
                                }
                            });
                            mFlowLayout.addView(view, lp);
                        }else{
                            ToastUtil.showMessage(ErrorCode.getCodeName(code));
                        }
                    }
                });*/

                break;
            case R.id.right_text:
                String name = comName.getText().toString().trim();
                String businessLicenceImageId = companyModel.getBusinessLicenceImageId();
                if (name.isEmpty() || businessLicenceImageId.isEmpty()) {
                    ToastUtil.showMessage("请完善公司名称和营业执照");
                    return;
                }
                if((type.equals("AGENT")||type.equals("SUPPLIER"))&&mFlowLayout.getChildCount()==0){
                    ToastUtil.showMessage("请选择主营业务");
                    return;
                }
                CompanyModel.change(type, name, null, null, businessLicenceImageId,
                        null, null,
                        null).done(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        JSONObject jsonObject = arguments.get(0);
                        String code = jsonObject.optString("status");
                        Logger.e("userinfo", jsonObject.toString());
                        if (code.equals("000000")) {
                            String token = jsonObject.optString("newToken");
                            gApplication.updateApiToken();
                            CompanyModel.commit().done(new ICallback() {
                                @Override
                                public void call(Arguments arguments) {
                                    JSONObject jsonObject = arguments.get(0);
                                    String code = jsonObject.optString("status");
                                    Logger.e("userinfo", jsonObject.toString());
                                    if (code.equals("000000")) {
                                        Message msg = handler.obtainMessage();
                                        msg.what = 1;
                                        handler.sendMessage(msg);
                                        //// TODO: 15-11-24
                                        Intent intent = new Intent(CompleteInfoActivity.this,
                                                MainActivity.class);
                                        ViewUtil.startTopActivity(CompleteInfoActivity.this, intent);
                                        finish();
                                    }
                                }
                            });

                        } else {
                            ToastUtil.showMessage(ErrorCode.getCodeName(code));
                        }
                    }
                });
                break;
        }
    }

    @Override
    public void onBackPressed() {

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
            intent.putExtra("aspectX", 7);
            intent.putExtra("aspectY", 10);
            intent.putExtra("outputX", 140);
            intent.putExtra("outputY", 230);
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
                //Bitmap photo = null;
               // try {
                   // photo = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
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
                                    ImageViewAdapter.adapt(uploadImage,
                                            ApiUrl.GET_COMPANY_IMG+"?"+"id="+bitmapModel.getId(),
                                            R.drawable.exchange_default, R.drawable.exchange_default,false);
                                    companyModel.setBusinessLicenceImageId(bitmapModel.getId());
                                    BitmapModel.imageCom(bitmapModel.getId()).done(
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
                                ImageViewAdapter.adapt(uploadImage, ApiUrl.GET_COMPANY_IMG+"?"+"id="+bitmapModel.getId(),
                                        R.drawable.exchange_default, R.drawable.exchange_default,true);
                                companyModel.setBusinessLicenceImageId(bitmapModel.getId());
                                BitmapModel.imageCom(bitmapModel.getId()).done(
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
}
