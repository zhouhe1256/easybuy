
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
import android.widget.TextView;

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
 * Created by dengt on 15-9-24.
 */
public class ComInformationActivity extends BaseActivity implements View.OnClickListener,
        ICallback, SelectPicPopupWindow.SelectPictureResult {
    private EditText comName;
    private Spinner comtype;
    private EditText comintroduce;
    private EditText comaddress;
    private EditText userName;
    private EditText useremail;
    private EditText userPhone;
    private GApplication gApplication;
    private CompanyModel companyModel;
    private Button commit;
    private TextView notify;
    private ImageView imageView;
    private ImageView picture;

    private ImageView addSubject;
    private LinearLayout linearLayout;

    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;

    // 业务
    private Spinner spinner1;
    private Spinner spinner2;
    private Spinner spinner3;
    private LinearLayout spinnerLinear;
    private CatalogAdapter catalogAdapter1;
    private CatalogAdapter catalogAdapter2;
    private CatalogAdapter catalogAdapter3;
    private List<CataLogModel> items1;
    private List<CataLogModel> items2;
    private List<CataLogModel> items3;
    private CataLogModel addid;
    private LinearLayout ll;
    private boolean del;
    private List<CataLogModel> temp;
    String type = "SUPPLIER";
    InputMethodManager imm;
    private int p = 0;
    private ArrayList<CataLogModel> n = new ArrayList<CataLogModel>();
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
                                TextViewDelete view = new TextViewDelete(ComInformationActivity.this);
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
        comName = ViewUtil.findViewById(this, R.id.info_com_name);
        comtype = ViewUtil.findViewById(this, R.id.info_com_type);
        comintroduce = ViewUtil.findViewById(this, R.id.info_com_introduce);
        comaddress = ViewUtil.findViewById(this, R.id.info_com_address);
        userName = ViewUtil.findViewById(this, R.id.info_name);
        useremail = ViewUtil.findViewById(this, R.id.info_email);
        userPhone = ViewUtil.findViewById(this, R.id.info_phone);
        commit = ViewUtil.findViewById(this, R.id.register_btn);
        notify = ViewUtil.findViewById(this, R.id.com_notify);
        imageView = ViewUtil.findViewById(this, R.id.arror);
        addSubject = ViewUtil.findViewById(this, R.id.add_subject);
        spinner1 = ViewUtil.findViewById(this, R.id.spinner1);
        spinner2 = ViewUtil.findViewById(this, R.id.spinner2);
        spinner3 = ViewUtil.findViewById(this, R.id.spinner3);
        linearLayout = ViewUtil.findViewById(this, R.id.catalog_com);
        mFlowLayout = (XCFlowLayout) findViewById(R.id.flowlayout);
        picture = ViewUtil.findViewById(this, R.id.info_com_img);
        spinnerLinear = ViewUtil.findViewById(this,R.id.spinner_linear);
        ll = (LinearLayout) findViewById(R.id.ll);
        // registerBtn = ViewUtil.findViewById(this, R.id.commit_btn);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    private void initEvent() {
        leftBtn.setOnClickListener(this);
        // comName.setOnClickListener(this);
        // comtype.setOnClickListener(this);
        // comintroduce.setOnClickListener(this);
        // comaddress.setOnClickListener(this);
        // userName.setOnClickListener(this);
        // useremail.setOnClickListener(this);
        // userPhone.setOnClickListener(this);
        commit.setOnClickListener(this);
        // 添加事件Spinner事件监听
        comtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(data_list.get(position).equals("采购商")){
                    linearLayout.setVisibility(View.GONE);
                }else{
                    linearLayout.setVisibility(View.VISIBLE);
                }
                if (companyModel != null) {
                    type = arr_adapter.getItem(position);
                    if ("供应商(原厂)".equals(data_list.get(position))) {
                        // comtype.setSelection(0);
                        type = "SUPPLIER";
                        linearLayout.setVisibility(View.VISIBLE);
                    }
                    else if ("供应商(代理)".equals(data_list.get(position))) {
                        // comtype.setSelection(1);
                        type = "AGENT";
                        linearLayout.setVisibility(View.VISIBLE);
                    } else if ("采购商".equals(data_list.get(position))) {
                        // comtype.setSelection(2);
                        linearLayout.setVisibility(View.GONE);
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        addSubject.setOnClickListener(this);
    }

    ViewGroup.MarginLayoutParams lp;

    private void initDate() {
        setTitle("企业信息");
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

    private XCFlowLayout mFlowLayout;

    @Override
    public void call(Arguments arguments) {
        JSONObject jsonObject = arguments.get(0);
        String code = jsonObject.optString("status");

        // Logger.e("company", jsonObject.toString());
        if (code.equals("000000")) {
            companyModel = JSONUtil
                    .load(CompanyModel.class, jsonObject.optJSONObject("result"));
            String token = jsonObject.optString("newToken");
            PreferencesUtils.putString(gApplication, PreferencesConstant.API_TOKEN, token);
            gApplication.updateApiToken();
            temp = companyModel.getCatalogList();
            if (temp == null) {
                temp = new ArrayList<CataLogModel>();
            }
            comName.setText(companyModel.getName());
            if ("SUPPLIER".equals(companyModel.getType())){
                comtype.setSelection(0);
                linearLayout.setVisibility(View.VISIBLE);}
            else if ("AGENT".equals(companyModel.getType())) {
                comtype.setSelection(1);
                linearLayout.setVisibility(View.VISIBLE);
            } else if ("BUYER".equals(companyModel.getType())) {
                comtype.setSelection(2);
                linearLayout.setVisibility(View.GONE);
            }
            if ("PENDING".equals(companyModel.getStatus())||"ACTIVE".equals(companyModel.getStatus())) {
                // comName.setOnClickListener(null);
                del = true;
                comName.setEnabled(false);
                comtype.setEnabled(false);
                spinner1.setEnabled(false);
                spinner2.setEnabled(false);
                spinner3.setEnabled(false);
                imageView.setVisibility(View.GONE);
                picture.setOnClickListener(null);
                addSubject.setVisibility(View.GONE);
                commit.setVisibility(View.GONE);
                notify.setVisibility(View.GONE);
                spinnerLinear.setVisibility(View.GONE);
                // commit.setText("保存");
                // commit.setOnClickListener(null);
            } else {
                del = false;
                // comName.setOnClickListener(this);
                comtype.setEnabled(true);
                imageView.setVisibility(View.VISIBLE);
                commit.setText("提交审核");
                commit.setVisibility(View.VISIBLE);
                notify.setVisibility(View.VISIBLE);
                addSubject.setVisibility(View.VISIBLE);
                spinnerLinear.setVisibility(View.VISIBLE);
                spinner1.setEnabled(true);
                spinner2.setEnabled(true);
                spinner3.setEnabled(true);
            }
            comintroduce.setText(companyModel.getDescription());
            comaddress.setText(companyModel.getWebsite());
            userName.setText(companyModel.getLinkman());
            userPhone.setText(companyModel.getContactNumber());
            useremail.setText(companyModel.getEmail());
            if (!companyModel.getBusinessLicenceImageId().isEmpty())
                ImageViewAdapter.adapt(
                        picture,
                        ApiUrl.GET_COMPANY_IMG + "?" + "id="
                                + companyModel.getBusinessLicenceImageId(),
                        R.drawable.exchange_default, R.drawable.exchange_default, true);
            // BitmapModel.imageGet(companyModel.getBusinessLicenceImageId()).done(
            // new ICallback() {
            // @Override
            // public void call(Arguments arguments) {
            // JSONObject jsonObject = arguments.get(0);
            // String code = jsonObject.optString("status");
            //
            // // Logger.e("uploadimg", jsonObject.toString());
            // if (code.equals("000000")) {
            // BitmapModel bitmapModel = JSONUtil
            // .load(BitmapModel.class,
            // jsonObject.optJSONObject("result"));
            // ImageViewAdapter.adapt(picture,
            // bitmapModel.getUri(),
            // R.drawable.exchange_default,
            // R.drawable.exchange_default);
            // }
            // }
            // });
            for (int i = 0; i < companyModel.getCatalogList().size(); i++) {
                final TextViewDelete view = new TextViewDelete(this);
                view.setText(companyModel.getCatalogList().get(i));
                if(del){
                    view.setINVisible(true);
                }else{
                    view.setINVisible(false);
                }
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!del){

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
                            temp.remove(((TextViewDelete) v).getCatalog());
                            mFlowLayout.removeView(v);
                        }
                    }});
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
        return R.layout.activity_com_info;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_btn:
                String name = comName.getText().toString().trim();
                String description = comintroduce.getText().toString().trim();
                // todo
                // String type = "SUPPLIER";
                String website = comaddress.getText().toString().trim();
                String businessLicenceImageId = null;
                if(companyModel==null){
                    ViewUtil.finish(ComInformationActivity.this);
                }else{
                    businessLicenceImageId = companyModel.getBusinessLicenceImageId();
                }

                String contactNumber = userPhone.getText().toString().trim();
                String linkman = userName.getText().toString().trim();
                String email = useremail.getText().toString().trim();

                CompanyModel.change(type, name, description, website, businessLicenceImageId,
                        contactNumber, linkman,
                        email).done(new ICallback() {
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
                }).fail(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        ToastUtil.showMessage("网络异常");
                    }
                });
                ViewUtil.finish(ComInformationActivity.this);
                break;
            case R.id.add_subject:
                if (!temp.contains(addid)) {
                    boolean f = true;
                    for(int i=0;i<n.size();i++){
                        if(n.get(i).getName().equals(addid.getName())){
                            f = false;
                        }
                    }
                    if(f){

                        final TextViewDelete view = new TextViewDelete(ComInformationActivity.this);
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




                   /* CataLogListModel.addCatalog(addid.getId()).done(new ICallback() {
                        @Override
                        public void call(Arguments arguments) {
                            JSONObject jsonObject = arguments.get(0);
                            String code = jsonObject.optString("status");

                            Logger.e("addCatalog", jsonObject.toString());
                            if (code.equals("000000")) {
                                TextViewDelete view = new TextViewDelete(
                                        ComInformationActivity.this);
                                view.setText(addid);
                                view.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(!del){
                                            Logger.i("fff","fff");
                                        CataLogListModel.delCatalog(((TextViewDelete) v).getid())
                                                .done(
                                                        new ICallback() {
                                                            @Override
                                                            public void call(Arguments arguments) {
                                                                JSONObject jsonObject = arguments
                                                                        .get(0);
                                                                String code = jsonObject
                                                                        .optString("status");

                                                                Logger.e("delCatalog",
                                                                        jsonObject.toString());
                                                                if (code.equals("000000")) {

                                                                }
                                                            }
                                                        });
                                        temp.remove(((TextViewDelete) v).getCatalog());
                                        mFlowLayout.removeView(v);
                                    } }
                                });
                                mFlowLayout.addView(view, lp);
                                temp.add(((TextViewDelete) view).getCatalog());
                            }else{
                                ToastUtil.showMessage(ErrorCode.getCodeName(code));
                            }
                        }
                    });*/
                } else {
                    ToastUtil.showMessage("该业务已添加");
                }

                break;

            case R.id.info_com_img:
                menuWindow = new SelectPicPopupWindow(this, this);
                menuWindow.showAtLocation(this.findViewById(R.id.info_com_name), Gravity.BOTTOM
                        | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置

                break;

            case R.id.register_btn:
                String name_commit = comName.getText().toString().trim();
                String description_commit = comintroduce.getText().toString().trim();
                // todo
                // String type = "SUPPLIER";
                String website_commit = comaddress.getText().toString().trim();
                String businessLicenceImageId_commit = companyModel.getBusinessLicenceImageId();
                String contactNumber_commit = userPhone.getText().toString().trim();
                String linkman_commit = userName.getText().toString().trim();
                String email_commit = useremail.getText().toString().trim();
                if (name_commit.isEmpty() || businessLicenceImageId_commit.isEmpty()) {
                    ToastUtil.showMessage("请完善公司名称和营业执照");
                    return;
                }
                Logger.i("namessss", mFlowLayout.getChildCount() + "," + type);
                if((type.equals("AGENT")||type.equals("SUPPLIER"))&&mFlowLayout.getChildCount()==0){
                    ToastUtil.showMessage("请选择主营业务");
                    return;
                }
                CompanyModel.change(type, name_commit, description_commit, website_commit,
                        businessLicenceImageId_commit,
                        contactNumber_commit, linkman_commit,
                        email_commit).done(new ICallback() {
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
                                        ToastUtil.showMessage("提交成功");
                                        Message msg = handler.obtainMessage();
                                        msg.what = 1;
                                        handler.sendMessage(msg);


                                        comName.setOnClickListener(null);
                                        comtype.setEnabled(false);
                                        imageView.setVisibility(View.GONE);
                                        picture.setOnClickListener(null);
                                        commit.setVisibility(View.GONE);
                                        notify.setVisibility(View.GONE);
                                        // finish();
                                    }
                                }
                            });

                        } else {
                            ToastUtil.showMessage(ErrorCode.getCodeName(code));
                        }
                    }
                });
                ViewUtil.finish(ComInformationActivity.this);
                break;
        }
    }

    @Override
    public void selectPhoto() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, selectCode);
    }

    private SelectPicPopupWindow menuWindow;
    Uri uri;
    private int selectCode = 1;
    private int requestCropIcon = 2;
    private int resultPictureCode = 3;

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
                Bitmap photo = null;
                // try {
                // photo =
                // MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
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
                                                                                        ImageViewAdapter.adapt(picture,
                                                                                                ApiUrl.GET_COMPANY_IMG + "?" + "id=" + bitmapModel.getId(),
                                                                                                R.drawable.exchange_default, R.drawable.exchange_default, true);
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
                // } catch (IOException e) {
                // e.printStackTrace();
                // }

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
                                ImageViewAdapter.adapt(picture, ApiUrl.GET_COMPANY_IMG + "?" + "id=" + bitmapModel.getId(),
                                        R.drawable.exchange_default, R.drawable.exchange_default, true);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ViewUtil.finish(ComInformationActivity.this);
    }
}
