package com.hotniao.svideo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hn.library.utils.HnDimenUtil;
import com.hotniao.svideo.R;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：InputView
 * 类描述：仿支付宝实现密码输入框
 * 创建人：Administrator
 * 创建时间：2017/3/16 16:08
 * 修改人：Administrator
 * 修改时间：2017/3/16 16:08
 * 修改备注：
 * Version:  1.0.0
 */
public class InputView extends RelativeLayout{


    private  String TAG="InputView";

    //上下文
    private Context  mContext;
    //输入控件
    private EditText mEditText;
    //输入框父布局
    private LinearLayout mLinearLayout;
    //密码框
    private TextView[] mTextViews;


    //输入框的长度 默认6
    private int      length=4;
    //背景色
    private int      bgColor= R.drawable.edit_num_bg;
    //分割线的宽度  像素
    private float    segment_line_width=2.0f;
    //分割线的颜色
    private  int    segment_line_color=R.color.color_d9d9d9;
    //空布局高度
    private  float emptyViewHeight=0;
    //空布局宽度
    private  float emptyViewWidth=0;
    //颜色
    private  int    emptyViewColor=R.color.gray;
    //文本的字体大小
    private  float   textSize=12;
    //文本的颜色
    private  int     textColor=R.color.color_999999;


    //监听接口  检测用户是否输入完成
    private OnTextFinishListener onTextFinishListener;
    //是否显示明文  默认密文显示
    private boolean  isShow=false;

    private Handler  handler;




    /**
     * 构造函数
     * @param context
     */
    public InputView(Context context) {
        this(context,null);
    }

    public InputView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public InputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
         this.mContext=context;
         handler=new Handler();
         init(attrs);
         initStyle();
    }


    /**
     * 从xml中获取配置属性
     * @param attrs
     */
    private void init(AttributeSet attrs) {
        TypedArray typedArray=mContext.obtainStyledAttributes(attrs,R.styleable.InputView);
        if(typedArray==null){
            return;
        }
        //是否显示
        isShow=typedArray.getBoolean(R.styleable.InputView_isShow,isShow);
         //设置输入框的个数
         length= typedArray.getInteger(R.styleable.InputView_length,length);
         //获取背景色
         bgColor=typedArray.getResourceId(R.styleable.InputView_bgColor,bgColor);
         //获取字体大小
         textSize=typedArray.getDimension(R.styleable.InputView_textSize,textSize);
         //字体颜色
         textColor=typedArray.getResourceId(R.styleable.InputView_text_Color,textColor);
         //分割线的宽度
          segment_line_width=typedArray.getDimension(R.styleable.InputView_segment_line_width,segment_line_width);
         //分割线的颜色
         segment_line_color=typedArray.getResourceId(R.styleable.InputView_segment_line_color,segment_line_color);
         //空布局高度
         emptyViewHeight=typedArray.getDimension(R.styleable.InputView_empty_view_heigh,emptyViewHeight);
         //空布局宽度
         emptyViewWidth=typedArray.getDimension(R.styleable.InputView_empty_view_width,emptyViewWidth);
         //空布局颜色
         emptyViewColor=typedArray.getResourceId(R.styleable.InputView_empty_view_bg,emptyViewColor);
         //释放资源
         typedArray.recycle();

    }


    /**
     * 初始化eidttext
     */
    private void initEditView() {
        mEditText=new EditText(mContext);
        //背景色
        mEditText.setBackgroundResource(bgColor);

        //设置光标是否可见
        mEditText.setCursorVisible(false);
        //设置字体大小
        mEditText.setTextSize(0);
        //设置输入的文本类型   输入类型为数字文本  密文
        mEditText.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD|InputType.TYPE_CLASS_NUMBER);
        //设置过滤器  可以用于限制长度以及输入类型
        mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
        //添加文本输入监听
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Editable  edit=mEditText.getText();
                Selection.setSelection(edit,edit.length());

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                initTextData(s);
                Log.i(TAG,"输入的数据:"+s.toString()+"-->lenght:"+s.length());
                if(s.length() == length)
                {
                    //避免绘制没有完成，就已经调用接口，从而导致视图显示为最后一个输入框没有设置数据
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(onTextFinishListener != null)
                            {
                                onTextFinishListener.onFinish(s.toString().trim());
                            }
                        }
                    },50);
                }else  if(s.length()>0&&s.length()<length){
                    if(onTextFinishListener != null)
                    {
                        onTextFinishListener.inputing(s.toString().trim());
                    }
                }
            }
        });
        //动态这只eidttext居中  动态设置布局的属性，在不同的条件下设置不同的布局排列方式
         LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
         lp.addRule(RelativeLayout.TEXT_ALIGNMENT_CENTER, RelativeLayout.TRUE);
         addView(mEditText, lp);
    }


    /**
     * 初始化最上层视图
     */
    private void initView() {
        try {
        //添加输入框父布局
        mLinearLayout = new LinearLayout(mContext);
        //设置父布局背景
        mLinearLayout.setBackgroundResource(bgColor);

        //设置宽高
        LayoutParams layoutParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mLinearLayout.setLayoutParams(layoutParam);
        //方向
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(mLinearLayout);
        //添加密码框
        mTextViews = new TextView[length];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
        params.weight = 1;
        params.gravity = Gravity.CENTER;

//       LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(px2dip(mContext, segment_line_width), LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams emptyParam = new LinearLayout.LayoutParams((int) emptyViewWidth, LayoutParams.MATCH_PARENT);
        for (int i = 0; i < mTextViews.length; i++) {
            //添加textview
            TextView mTextView = new TextView(mContext);
            mTextView.setGravity(Gravity.CENTER);
            mTextView.setBackgroundResource(R.drawable.shape_white_bg_black_stroke_5_radius_recentage);

            mTextViews[i] = mTextView;
            mTextViews[i].setTextSize(textSize);
            mTextViews[i].setTextColor(mContext.getResources().getColor(textColor));
            if(isShow) {
                mTextViews[i].setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD | InputType.TYPE_CLASS_NUMBER);
            }else{
                mTextViews[i].setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD | InputType.TYPE_CLASS_NUMBER);
            }
            mLinearLayout.addView(mTextView, params);



            //添加分割线
//            if (i < mTextViews.length - 1) {
//               LinearLayout  linearLayout=new LinearLayout(mContext);
//                linearLayout.setBackgroundColor(Color.TRANSPARENT);
//                LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
//                linearLayout.setLayoutParams(layoutParams);
//                View view1 = new View(mContext);
//                view1.setBackgroundColor(mContext.getResources().getColor(segment_line_color));
//                linearLayout.addView(view1,param);
                if(i<mTextViews.length-1) {
                    View emptyView = new View(mContext);
                    emptyView.setBackgroundColor(mContext.getResources().getColor(emptyViewColor));
                    mLinearLayout.addView(emptyView, emptyParam);
                }
//                if(emptyViewWidth>0) {
//                    View view2 = new View(mContext);
//                    view2.setBackgroundColor(mContext.getResources().getColor(segment_line_color));
//                    linearLayout.addView(view2, param);
//                }
//                mLinearLayout.addView(linearLayout);
//            }

        }

         }catch (Exception  e){
            Log.i(TAG,"初始化布局出错:"+e.getMessage());
        }
    }


    /**
     * 当用户输入完成后
     * @param s
     */
    private void initTextData(Editable s) {
        int  strLength=s.length();
         if(strLength>0){
             for (int i = 0; i <length ; i++) {
                  if(i<strLength){
                      for(int j = 0; j < strLength; j++)
                      {
                          char ch = s.charAt(j);
                          mTextViews[j].setText(String.valueOf(ch));
                      }

                  }else{
                      mTextViews[i].setText("");
                  }


             }
         }else{//清空数据
             for (int i = 0; i <length ; i++) {
                   mTextViews[i].setText("");
             }

         }
    }

    private  void showOrHideText(){
        int length=mTextViews.length;
        if(length>0){
            for (int i = 0; i <length ; i++) {
                if (isShow) {//显示文字  明文
                    mTextViews[i].setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD|InputType.TYPE_CLASS_NUMBER);
                    mTextViews[i].setBackgroundResource(R.drawable.shape_white_bg_black_stroke_5_radius_recentage);
                } else {//隐藏显示  密文
                     mTextViews[i].setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD|InputType.TYPE_CLASS_NUMBER);
                    mTextViews[i].setBackgroundResource(R.drawable.shape_white_bg_black_stroke_5_radius_recentage);
                }
            }
        }

    }
    /**
     * dp 转px
     * @param context
     * @param dipValue
     * @return
     */
    private    int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    private   int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**************************************常用方法**********************************/


    /**
     * 初始化输入框的样式
     */
    public  void initStyle(){
        //1.在布局文件上添加一个edittext，用于用户输入
        initEditView();
//        //2.在布局文件上添加一个linearlayout，里面放着textview，当点击的时候显示数据
        initView();
    }

    /**
     * 设置输入框的的个数  需要重新绘制视图
     * @param length
     */
    public  void  setViewLength(int length){
        if(length>0) {
            this.length = length;
            removeAllViews();
            initStyle();
        }
    }

    /**
     * 设置分割线的样式
     * @param segment_line_width
     * @param segment_line_color
     */
    public   void  segment_line_style(float segment_line_width,int segment_line_color){
        if(segment_line_width!=-1){
            this.segment_line_width=segment_line_width;
        }
        if(segment_line_color!=-1) {
            this.segment_line_color = segment_line_color;
        }
        removeView(mLinearLayout);
        initView();
    }

    /**
     * 是否显示明文    默认密文
     * @param isShow
     */
    public void isShowText(boolean  isShow){
        this.isShow=isShow;
        showOrHideText();
    }


    /**
     * 设置文本显示类型
     * @param type
     */
    public  void  setInputType(int type){
        int length = mTextViews.length;
        for(int i = 0; i < length; i++) {
            mTextViews[i].setInputType(type);
        }
    }

    /**
     * 设置背景色
     * @param resColor
     */
    public  void  setBgColor(int resColor){
               this.bgColor=resColor;
               if(mLinearLayout!=null) {
                   //设置父布局背景
                   mLinearLayout.setBackgroundResource(bgColor);
               }
               if(mEditText!=null) {
                   mEditText.setBackgroundResource(bgColor);
               }
    }

    /**
     * 设置字体大小
     * @param textSize
     */
    public  void setTextSize(float textSize){
        this.textSize=textSize;
        for(int i = 0; i < length; i++) {
            mTextViews[i].setTextSize(textSize);
        }
    }

    /**
     * 设置字体颜色
     * @param textColor
     */
    public  void setTextColor(int textColor){
        this.textColor=textColor;
        for(int i = 0; i < length; i++) {
            mTextViews[i].setTextColor(mContext.getResources().getColor(textColor));
        }
    }

    /**
     * 设置左边距
     * @param margin
     */
    public  void setextViewRightMargin(int margin){
        for(int i = 0; i < length; i++) {
           if(i>=0){
               LinearLayout.LayoutParams textparam = (LinearLayout.LayoutParams) mTextViews[i] .getLayoutParams();
                textparam.leftMargin= HnDimenUtil.px2dip(mContext,margin);
                mTextViews[i].setLayoutParams(textparam);
            }
        }
    }
    /**
     * 设置左边距
     * @param margin
     */
    public  void setextViewLeftMargin(int margin){
        for(int i = 0; i < length; i++) {
            if(i<length-1){
                LinearLayout.LayoutParams textparam = (LinearLayout.LayoutParams) mTextViews[i] .getLayoutParams();
                textparam.rightMargin= HnDimenUtil.px2dip(mContext,margin);
                textparam.leftMargin= HnDimenUtil.px2dip(mContext,margin);
                mTextViews[i].setLayoutParams(textparam);
            }
        }
    }


    /**
     * 清除文本框
     */
    public void clearText()
    {
        mEditText.setText("");
        for(int i = 0; i < length; i++)
        {
            mTextViews[i].setText("");
        }
        if(onTextFinishListener!=null){
            onTextFinishListener.onFinish("");
        }
    }

    /**
     * 设置文本数据
     * @param result
     */

    public void setTextData(String result) {
        if(TextUtils.isEmpty(result))  return;
        if(result.length()<mTextViews.length)  return;
        for (int i = 0; i <mTextViews.length ; i++) {
            String data=result.substring(i,i+1);
            if(mTextViews[i]==null)  return;
            mTextViews[i].setText(data);

        }
        if(onTextFinishListener!=null){
            onTextFinishListener.onFinish(result);
        }
    }


    /*********************文本输入监听接口*************************************************************/

    public interface OnTextFinishListener
    {
        void inputing(String str);//输入中
        void onFinish(String str);//输入完成
    }

    /**
     * 设置监听
     * @param onTextFinishListener
     */
    public void setOnTextFinishListener(OnTextFinishListener onTextFinishListener) {
        this.onTextFinishListener = onTextFinishListener;
    }

}
