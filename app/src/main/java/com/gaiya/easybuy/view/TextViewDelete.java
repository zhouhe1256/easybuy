
package com.gaiya.easybuy.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaiya.easybuy.R;
import com.gaiya.easybuy.model.CataLogModel;
import com.gaiya.easybuy.util.ViewUtil;

/**
 * Created by dengt on 15-9-25.
 */
public class TextViewDelete extends LinearLayout {
    private TextView textView;
    private ImageView textdel;
    private int intext;
    private long id;
    private CataLogModel text_;

    public long getid() {
        return id;
    }

    public TextViewDelete(Context context) {
        super(context);
        initView(context);
    }

    public TextViewDelete(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TextViewDelete(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.text_delete, this);
        textView = ViewUtil.findViewById(this, R.id.select_text);
        textdel = ViewUtil.findViewById(this,R.id.text_del);
    }
    public void setINVisible(boolean visible){
        if(visible){
            textdel.setVisibility(View.GONE);
        }
    }
    public void setText(CataLogModel text) {
        textView.setText(text.getName());
        this.id = text.getId();
        this.text_ = text;
    }
    public void setTextINtext(int text) {
        this.intext = text;
    }
    public int getTextINtext(){
        return  intext;
    }
    public CataLogModel getCatalog() {
        return text_;
    }
}
