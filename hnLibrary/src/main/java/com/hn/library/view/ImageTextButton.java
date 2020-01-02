package com.hn.library.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hn.library.R;


public class ImageTextButton extends RelativeLayout {
    private ImageView icon;
    private TextView title;
    public ImageTextButton(Context context) {
        super(context);
    }

    public ImageTextButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public ImageTextButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }
    @TargetApi(21)
    public ImageTextButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);
    }
    /**
     * 初始化布局
     */
    public void init(Context context, AttributeSet attrs) {
        // 根据属性配置，加载属性值导航栏高度为65dp
        if (attrs != null) {
            //初始化状态View
            TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ImageTextButton);
            //图标资源
            int iconId = attributes.getResourceId(R.styleable.ImageTextButton_iconId, 0);
            if (iconId> 0) {
                icon=new ImageView(context);
                icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                icon.setImageResource(iconId);
                icon.setId(iconId);
                icon.setClickable(false);
            }
            //图标大小
            float iconSize=attributes.getDimension(R.styleable.ImageTextButton_iconSize,0);
            //上边距
            float mTop=attributes.getDimension(R.styleable.ImageTextButton_iMarginTop,0);

            //下边距
            float mBottom=attributes.getDimension(R.styleable.ImageTextButton_iMarginBottom,0);

            //图标与文字间隔
            float divider=attributes.getDimension(R.styleable.ImageTextButton_iDivider,0);

            //文字
            String text=attributes.getString(R.styleable.ImageTextButton_text);

            //文字颜色
            int color=attributes.getColor(R.styleable.ImageTextButton_textColor,0);

            //mTitles =new TextView(context);
            title=(TextView)LayoutInflater.from(context).inflate(R.layout.button_image_text,null);
            //mTitles.setIncludeFontPadding(false);
            //mTitles.setTextColor(color);
            //mTitles.setGravity(Gravity.CENTER);
            title.setText(text);
            //mTitles.setSingleLine();
            //mTitles.setClickable(false);
            attributes.recycle();

            LayoutParams params=new LayoutParams((int)iconSize,(int)iconSize);
            params.setMargins(0,(int)mTop,0,0);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
            addView(icon, params);
            params=new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //添加位置信息 -1表示相对于父控件的位置 ，如果要相对某个平级控件则参数是该控件的ID
            params.setMargins(0,(int)divider,0,(int)mBottom);
            params.addRule(RelativeLayout.BELOW, icon.getId());
            params.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
            addView(title, params);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    /**
     * 点击时
     */
    public void changeState(int iconId,int color){
            icon.setImageResource(iconId);
            title.setTextColor(color);
            postInvalidate();
    }
}
