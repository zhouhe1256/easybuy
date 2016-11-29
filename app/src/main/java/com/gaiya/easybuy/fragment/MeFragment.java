
package com.gaiya.easybuy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaiya.android.async.Arguments;
import com.gaiya.android.async.ICallback;
import com.gaiya.android.json.JSONUtil;
import com.gaiya.android.view.ImageViewAdapter;
import com.gaiya.easybuy.R;
import com.gaiya.easybuy.activity.ChangePhoneActivity;
import com.gaiya.easybuy.activity.ChangePwdActivity;
import com.gaiya.easybuy.activity.ComInformationActivity;
import com.gaiya.easybuy.activity.FeedbackActivity;
import com.gaiya.easybuy.activity.SettingActivity;
import com.gaiya.easybuy.activity.UserTextActivity;
import com.gaiya.easybuy.constant.ApiUrl;
import com.gaiya.easybuy.constant.ErrorCode;
import com.gaiya.easybuy.model.UserModel;
import com.gaiya.easybuy.util.PreferencesConstant;
import com.gaiya.easybuy.util.PreferencesUtils;
import com.gaiya.easybuy.util.ToastUtil;
import com.gaiya.easybuy.util.ViewUtil;
import com.gaiya.easybuy.view.CircleImageView;

import org.json.JSONObject;

/**
 * Created by dengt on 15-9-24.
 */
public class MeFragment extends Fragment {
    private CircleImageView useImg;
    private TextView nick;
    private UserModel userModel;
    private void initDate() {
        UserModel.get().done(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                JSONObject jsonObject = arguments.get(0);
                String code = jsonObject.optString("status");
                if (code.equals("000000")) {
                    userModel = JSONUtil
                            .load(UserModel.class, jsonObject.optJSONObject("result"));
                    String token = jsonObject.optString("newToken");

                    nick.setText(userModel.getNickName());
                    if (!userModel.getImageId().isEmpty())
                        ImageViewAdapter.adapt(useImg,
                                ApiUrl.GET_COMPANY_IMG + "?" + "id=" + userModel.getImageId(),
                                R.drawable.user_default, R.drawable.user_default,true);
                } else {
                    ToastUtil.showMessage(ErrorCode.getCodeName(code));
                }
            }
        });

        String userImg = PreferencesUtils.getString(getActivity(),
                PreferencesConstant.USER_IMAGEURL,
                "");
        String userNick = PreferencesUtils.getString(getActivity(),
                PreferencesConstant.NICK_NAME, "");
       /* if (!userImg.isEmpty())
            ImageViewAdapter.adapt(useImg, userImg, R.drawable.login_default,
                    R.drawable.login_default,false);*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = null;
        rootView = inflater.inflate(R.layout.fragment_me, null);
        useImg = ViewUtil.findViewById(rootView, R.id.user_img);
        nick = ViewUtil.findViewById(rootView, R.id.user_nick);
        //initDate();
        return rootView;
    }

    public void doClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.user_img:
                // intent = new Intent(getActivity(),
                // CompleteInfoActivity.class);
                // ViewUtil.startActivity(getActivity(), intent);
                break;
            case R.id.user_info:
                // DialogUtil.hintMessage(getActivity(), "1");
                intent = new Intent(getActivity(), UserTextActivity.class);
                ViewUtil.startActivity(getActivity(), intent);
                getActivity().overridePendingTransition(0, R.anim.base_slide_right_in);
                break;
            case R.id.com_info:
                intent = new Intent(getActivity(), ComInformationActivity.class);
                ViewUtil.startActivity(getActivity(), intent);
                getActivity().overridePendingTransition(0, R.anim.base_slide_right_in);
                break;
            case R.id.edit_pwd:
                intent = new Intent(getActivity(), ChangePwdActivity.class);
                ViewUtil.startActivity(getActivity(), intent);
                getActivity().overridePendingTransition(0, R.anim.base_slide_right_in);

                break;
            case R.id.change_phone:
                intent = new Intent(getActivity(), ChangePhoneActivity.class);
                ViewUtil.startActivity(getActivity(), intent);
                getActivity().overridePendingTransition(0, R.anim.base_slide_right_in);

                break;
            case R.id.feedback:
                intent = new Intent(getActivity(), FeedbackActivity.class);
                ViewUtil.startActivity(getActivity(), intent);
                getActivity().overridePendingTransition(0, R.anim.base_slide_right_in);

                break;
            case R.id.setting:
                intent = new Intent(getActivity(), SettingActivity.class);
                ViewUtil.startActivity(getActivity(), intent);
                getActivity().overridePendingTransition(0, R.anim.base_slide_right_in);

                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initDate();
    }
}
