package com.hotniao.livelibrary.ui.beauty;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
/**pitu pkg start, P图动效打包专用，只编译，不打包源码，不要在此区域添加代码**/

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
/**pitu pkg end**/

import com.hotniao.livelibrary.R;
import com.hotniao.livelibrary.ui.beauty.utils.HnBeautyUtils;
import com.hotniao.livelibrary.ui.beauty.utils.VideoUtil1;
import com.hotniao.livelibrary.ui.beauty.view.TCHorizontalScrollView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@SuppressLint("ValidFragment")
public class BeautyDialogFragment extends DialogFragment implements SeekBar.OnSeekBarChangeListener {

    private static final String TAG = BeautyDialogFragment.class.getSimpleName();

    public static final int BEAUTYPARAM_BEAUTY = 1;
    public static final int BEAUTYPARAM_WHITE = 2;
    public static final int BEAUTYPARAM_RUDDY = 8;
    public static final int BEAUTYPARAM_FACE_LIFT = 3;
    public static final int BEAUTYPARAM_BIG_EYE = 4;
    public static final int BEAUTYPARAM_FILTER = 5;
    public static final int BEAUTYPARAM_MOTION_TMPL = 6;
    public static final int BEAUTYPARAM_GREEN = 7;

    private String mType = "Live";

    public BeautyDialogFragment() {
    }

    public BeautyDialogFragment(String mType) {
        this.mType = mType;
    }

    static public class BeautyParams {
        public int mBeautyProgress = 8;
        public int mWhiteProgress = 7;
        public int mRuddyProgress = 4;
        public int mFaceLiftProgress;
        public int mBigEyeProgress;
        public int mFilterIdx;
        public String mMotionTmplPath;
        public int mGreenIdx;
    }

    public interface OnBeautyParamsChangeListener {
        void onBeautyParamsChange(BeautyParams params, int key);

        void dismiss();
    }

    private View mLayoutBeauty;
    private View mLayoutPitu;

    private LinearLayout mBeautyLayout;
    private LinearLayout mWhitenLayout;
    private LinearLayout mFaceLiftLayout;
    private LinearLayout mBigEyeLayout;
    private LinearLayout mRuddyLayout;
    private SeekBar mBeautySeekbar;
    private SeekBar mFaceLiftSeekbar;
    private SeekBar mBigEyeSeekbar;
    private SeekBar mWhitenSeekbar;
    private SeekBar mRuddySeekbar;
    private TextView mTVBeauty;
    private TextView mTVFilter;
    private TextView mTVPitu;
    private TextView mTVGreens;
    private TCHorizontalScrollView mFilterPicker;
    private ArrayList<Integer> mFilterIDList;
    private ArrayAdapter<Integer> mFilterAdapter;

    private TCHorizontalScrollView mGreenPicker;
    private ArrayList<Integer> mGreenIDList;
    private ArrayAdapter<Integer> mGreenAdapter;

    private BeautyParams mBeautyParams;
    private OnBeautyParamsChangeListener mBeautyParamsChangeListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getActivity(), R.style.dialog);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.live_fragment_beauty_area);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消

        Log.d(TAG, "create fragment");
        mBeautyLayout = (LinearLayout) dialog.findViewById(R.id.layoutBeauty);
        mWhitenLayout = (LinearLayout) dialog.findViewById(R.id.layoutWhiten);
        mRuddyLayout = (LinearLayout) dialog.findViewById(R.id.layoutRuddy);
        mFaceLiftLayout = (LinearLayout) dialog.findViewById(R.id.layoutFacelift);
        mBigEyeLayout = (LinearLayout) dialog.findViewById(R.id.layoutBigEye);
        mFilterPicker = (TCHorizontalScrollView) dialog.findViewById(R.id.filterPicker);
        mGreenPicker = (TCHorizontalScrollView) dialog.findViewById(R.id.greenPicker);
        mTVPitu = (TextView) dialog.findViewById(R.id.tv_dynamic_effect);
        mTVPitu.setSelected(false);
        mLayoutPitu = dialog.findViewById(R.id.material_recycler_view);
        mLayoutBeauty = dialog.findViewById(R.id.layoutFaceBeauty);

        mFilterPicker.setVisibility(View.GONE);
        mLayoutPitu.setVisibility(View.GONE);
        mGreenPicker.setVisibility(View.GONE);

//        mBeautyLayout.setVisibility(View.VISIBLE);
//        mWhitenLayout.setVisibility(View.VISIBLE);
//        mFaceLiftLayout.setVisibility(View.GONE);
//        mBigEyeLayout.setVisibility(View.GONE);

        mBeautySeekbar = (SeekBar) dialog.findViewById(R.id.beauty_seekbar);
        mBeautySeekbar.setOnSeekBarChangeListener(this);
        mBeautySeekbar.setProgress(mBeautyParams.mBeautyProgress * mBeautySeekbar.getMax() / 9);

        mWhitenSeekbar = (SeekBar) dialog.findViewById(R.id.whiten_seekbar);
        mWhitenSeekbar.setOnSeekBarChangeListener(this);
        mWhitenSeekbar.setProgress(mBeautyParams.mWhiteProgress * mWhitenSeekbar.getMax() / 9);

        mRuddySeekbar = (SeekBar) dialog.findViewById(R.id.ruddy_seekbar);
        mRuddySeekbar.setOnSeekBarChangeListener(this);
        mRuddySeekbar.setProgress(mBeautyParams.mRuddyProgress * mRuddySeekbar.getMax() / 9);

        mFaceLiftSeekbar = (SeekBar) dialog.findViewById(R.id.facelift_seekbar);
        mFaceLiftSeekbar.setOnSeekBarChangeListener(this);
        mFaceLiftSeekbar.setProgress(mBeautyParams.mFaceLiftProgress * mFaceLiftSeekbar.getMax() / 9);

        mBigEyeSeekbar = (SeekBar) dialog.findViewById(R.id.bigeye_seekbar);
        mBigEyeSeekbar.setOnSeekBarChangeListener(this);
        mBigEyeSeekbar.setProgress(mBeautyParams.mBigEyeProgress * mBigEyeSeekbar.getMax() / 9);


        mFilterIDList = new ArrayList<Integer>();
        mFilterIDList.add(R.drawable.orginal);
        mFilterIDList.add(R.drawable.langman);
        mFilterIDList.add(R.drawable.qingxin);
        mFilterIDList.add(R.drawable.weimei);
        mFilterIDList.add(R.drawable.fennen);
        mFilterIDList.add(R.drawable.huaijiu);
        mFilterIDList.add(R.drawable.landiao);
        mFilterIDList.add(R.drawable.qingliang);
        mFilterIDList.add(R.drawable.rixi);
        mFilterAdapter = new ArrayAdapter<Integer>(dialog.getContext(), 0, mFilterIDList) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    convertView = inflater.inflate(R.layout.live_filter_layout, null);
                }
                ImageView view = (ImageView) convertView.findViewById(R.id.filter_image);
                if (position == 0) {
                    ImageView view_tint = (ImageView) convertView.findViewById(R.id.filter_image_tint);
                    if (view_tint != null)
                        view_tint.setVisibility(View.VISIBLE);
                }
                view.setTag(position);
                view.setImageDrawable(getResources().getDrawable(getItem(position)));
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int index = (int) view.getTag();
                        mBeautyParams.mFilterIdx = index;
                        selectFilter(mBeautyParams.mFilterIdx);
                        if (mBeautyParamsChangeListener instanceof OnBeautyParamsChangeListener) {
                            mBeautyParamsChangeListener.onBeautyParamsChange(mBeautyParams, BEAUTYPARAM_FILTER);
                        }
                    }
                });
                return convertView;

            }
        };
        mFilterPicker.setAdapter(mFilterAdapter);
        if (mBeautyParams.mFilterIdx >= 0 && mBeautyParams.mFilterIdx < mFilterAdapter.getCount()) {
            mFilterPicker.setClicked(mBeautyParams.mFilterIdx);
            selectFilter(mBeautyParams.mFilterIdx);
        } else {
            mFilterPicker.setClicked(0);
        }


        mGreenIDList = new ArrayList<Integer>();
        mGreenIDList.add(R.drawable.greens_no);
        mGreenIDList.add(R.drawable.greens_1);
        mGreenIDList.add(R.drawable.greens_2);
        mGreenAdapter = new ArrayAdapter<Integer>(dialog.getContext(), 0, mGreenIDList) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    convertView = inflater.inflate(R.layout.live_green_layout, null);
                }
                ImageView view = (ImageView) convertView.findViewById(R.id.green_image);
                if (position == 0) {
                    ImageView view_tint = (ImageView) convertView.findViewById(R.id.green_image_tint);
                    if (view_tint != null)
                        view_tint.setVisibility(View.VISIBLE);
                }
                view.setTag(position);
                view.setImageDrawable(getResources().getDrawable(getItem(position)));
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int index = (int) view.getTag();
                        mBeautyParams.mGreenIdx = index;
                        selectGreen(mBeautyParams.mGreenIdx);
                        if (mBeautyParamsChangeListener instanceof OnBeautyParamsChangeListener) {
                            mBeautyParamsChangeListener.onBeautyParamsChange(mBeautyParams, BEAUTYPARAM_GREEN);
                        }
                    }
                });
                return convertView;

            }
        };
        mGreenPicker.setAdapter(mGreenAdapter);
        if (mBeautyParams.mGreenIdx >= 0 && mBeautyParams.mGreenIdx < mGreenAdapter.getCount()) {
            mGreenPicker.setClicked(mBeautyParams.mGreenIdx);
            selectGreen(mBeautyParams.mGreenIdx);
        } else {
            mGreenPicker.setClicked(0);
        }


        mTVBeauty = (TextView) dialog.findViewById(R.id.tv_face_beauty);
        mTVFilter = (TextView) dialog.findViewById(R.id.tv_face_filter);
        mTVGreens = (TextView) dialog.findViewById(R.id.tv_green);
        mTVBeauty.setSelected(true);
        mTVFilter.setSelected(false);
        mTVGreens.setSelected(false);


        if ("Video".equals(mType)) {
            mLayoutBeauty.setVisibility(View.GONE);
            mTVBeauty.setVisibility(View.GONE);
            mTVFilter.setVisibility(View.VISIBLE);
            mFilterPicker.setVisibility(View.VISIBLE);
        } else if ("Chat".equals(mType)) {
            mLayoutBeauty.setVisibility(View.VISIBLE);
            mTVBeauty.setVisibility(View.VISIBLE);
            mTVFilter.setVisibility(View.GONE);
            mFilterPicker.setVisibility(View.GONE);
        } else {
            mLayoutBeauty.setVisibility(View.VISIBLE);
            mTVBeauty.setVisibility(View.VISIBLE);
            mTVFilter.setVisibility(View.VISIBLE);
            mFilterPicker.setVisibility(View.GONE);
        }

        mTVBeauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTVBeauty.setSelected(true);
                mTVFilter.setSelected(false);
                mTVPitu.setSelected(false);
                mTVGreens.setSelected(false);

                mLayoutBeauty.setVisibility(View.VISIBLE);
                mFilterPicker.setVisibility(View.GONE);
                mLayoutPitu.setVisibility(View.GONE);
                mGreenPicker.setVisibility(View.GONE);

                mBeautySeekbar.setProgress(mBeautyParams.mBeautyProgress * mBeautySeekbar.getMax() / 9);
                mWhitenSeekbar.setProgress(mBeautyParams.mWhiteProgress * mWhitenSeekbar.getMax() / 9);
                mRuddySeekbar.setProgress(mBeautyParams.mRuddyProgress * mRuddySeekbar.getMax() / 9);
//                mFaceLiftSeekbar.setProgress(mBeautyParams.mFaceLiftProgress * mFaceLiftSeekbar.getMax() / 9);
            }
        });

        mTVFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTVBeauty.setSelected(false);
                mTVFilter.setSelected(true);
                mTVPitu.setSelected(false);
                mTVGreens.setSelected(false);

                mLayoutBeauty.setVisibility(View.GONE);
                mFilterPicker.setVisibility(View.VISIBLE);
                mLayoutPitu.setVisibility(View.GONE);
                mGreenPicker.setVisibility(View.GONE);
            }
        });

        mTVPitu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTVBeauty.setSelected(false);
                mTVFilter.setSelected(false);
                mTVPitu.setSelected(true);
                mTVGreens.setSelected(false);

                mLayoutBeauty.setVisibility(View.GONE);
                mFilterPicker.setVisibility(View.GONE);
                mLayoutPitu.setVisibility(View.VISIBLE);
                mGreenPicker.setVisibility(View.GONE);
            }
        });

        mTVGreens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTVBeauty.setSelected(false);
                mTVFilter.setSelected(false);
                mTVPitu.setSelected(false);
                mTVGreens.setSelected(true);

                mLayoutBeauty.setVisibility(View.GONE);
                mFilterPicker.setVisibility(View.GONE);
                mLayoutPitu.setVisibility(View.GONE);
                mGreenPicker.setVisibility(View.VISIBLE);
            }
        });


        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);

        //pitu
        /**pitu pkg start, P图动效打包专用，只编译，不打包源码，不要在此区域添加代码**/
        final String[] ONLINE_MATERIAL_IDS = new String[]{"video_fox", "video_cats", "video_guangmao", "video_zuanshitu", "video_guangxiong", "video_tuzi", "video_maonv", "video_totoro", "video_pig", "video_cat",
                "video_winter_cat", "video_heart_eye", "video_dahuzi", "video_xiaohuzi", "video_lamb", "video_lovely_eye", "video_huangguan", "video_zhinv", "video_jiaban_dog", "video_little_mouse",
                "video_520", "video_cangshu", "video_fawn", "video_guiguan", "video_heart_lips", "video_laughday", "video_raccoon", "video_liaomei", "video_ruhua", "video_wawalian",
                "video_aliens", "video_fangle2"};
        if (mMaterialAdapter == null) {
            List<VideoMaterialMetaData> materials = loadLocalMaterials();
            mMaterialAdapter = new MaterialAdapter(getActivity(), materials);
            ArrayList newMaterials = new ArrayList();
            newMaterials.add(new VideoMaterialMetaData("video_none", "assets://camera/camera_video/CameraVideoAnimal/video_none", "", "assets://camera/camera_video/CameraVideoAnimal/video_doodle/video_none.png"));
//                materials.add(new VideoMaterialMetaData("video_rabbit", "assets://camera/camera_video/CameraVideoAnimal/video_rabbit", "", "assets://camera/camera_video/CameraVideoAnimal/video_rabbit/video_rabbit.png"));
//                materials.add(new VideoMaterialMetaData("video_snow_white", "assets://camera/camera_video/CameraVideoAnimal/video_snow_white", "", "assets://camera/camera_video/CameraVideoAnimal/video_snow_white/video_snow_white.png"));
            String[] var3 = ONLINE_MATERIAL_IDS;
            int var4 = var3.length;
            for (int var5 = 0; var5 < var4; ++var5) {
                String id = var3[var5];
                String packageUrl = "http://st1.xiangji.qq.com/yunmaterials/" + id + "Android.zip";
                String imageUrl = "http://st1.xiangji.qq.com/yunmaterials/" + id + ".png";
                newMaterials.add(new VideoMaterialMetaData(id, "", packageUrl, imageUrl));
            }
            Iterator var1 = newMaterials.iterator();
            while (var1.hasNext()) {
                VideoMaterialMetaData material = (VideoMaterialMetaData) var1.next();
                if (TextUtils.isEmpty(material.path)) {
                    material.path = mPrefs.getString(material.id, "");
                }
            }
            mMaterialAdapter = new MaterialAdapter(getActivity(), newMaterials);
        }

        mMaterialAdapter.setOnItemClickListener(mFilterClickListener);
        RecyclerView recyclerView = (RecyclerView) mLayoutPitu;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(mMaterialAdapter);
        /**pitu pkg end**/
        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mBeautyParamsChangeListener != null) mBeautyParamsChangeListener.dismiss();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (R.id.beauty_seekbar == seekBar.getId()) {
            mBeautyParams.mBeautyProgress = HnBeautyUtils.filtNumber(9, mBeautySeekbar.getMax(), progress);
            if (mBeautyParamsChangeListener instanceof OnBeautyParamsChangeListener) {
                mBeautyParamsChangeListener.onBeautyParamsChange(mBeautyParams, BEAUTYPARAM_BEAUTY);
            }
        } else if (R.id.whiten_seekbar == seekBar.getId()) {
            mBeautyParams.mWhiteProgress = HnBeautyUtils.filtNumber(9, mWhitenSeekbar.getMax(), progress);
            if (mBeautyParamsChangeListener instanceof OnBeautyParamsChangeListener) {
                mBeautyParamsChangeListener.onBeautyParamsChange(mBeautyParams, BEAUTYPARAM_WHITE);
            }
        } else if (R.id.ruddy_seekbar == seekBar.getId()) {
            mBeautyParams.mRuddyProgress = HnBeautyUtils.filtNumber(9, mRuddySeekbar.getMax(), progress);
            if (mBeautyParamsChangeListener instanceof OnBeautyParamsChangeListener) {
                mBeautyParamsChangeListener.onBeautyParamsChange(mBeautyParams, BEAUTYPARAM_RUDDY);
            }
        } else if (R.id.facelift_seekbar == seekBar.getId()) {
            mBeautyParams.mFaceLiftProgress = HnBeautyUtils.filtNumber(9, mFaceLiftSeekbar.getMax(), progress);
            if (mBeautyParamsChangeListener instanceof OnBeautyParamsChangeListener) {
                mBeautyParamsChangeListener.onBeautyParamsChange(mBeautyParams, BEAUTYPARAM_FACE_LIFT);
            }
        } else if (R.id.bigeye_seekbar == seekBar.getId()) {
            mBeautyParams.mBigEyeProgress = HnBeautyUtils.filtNumber(9, mBigEyeSeekbar.getMax(), progress);
            if (mBeautyParamsChangeListener instanceof OnBeautyParamsChangeListener) {
                mBeautyParamsChangeListener.onBeautyParamsChange(mBeautyParams, BEAUTYPARAM_BIG_EYE);
            }
        }

    }


    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void selectFilter(int index) {
        ViewGroup group = (ViewGroup) mFilterPicker.getChildAt(0);
        for (int i = 0; i < mFilterAdapter.getCount(); i++) {
            View v = group.getChildAt(i);
            ImageView IVTint = (ImageView) v.findViewById(R.id.filter_image_tint);
            if (IVTint != null) {
                if (i == index) {
                    IVTint.setVisibility(View.VISIBLE);
                } else {
                    IVTint.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    private void selectGreen(int index) {
        ViewGroup group = (ViewGroup) mGreenPicker.getChildAt(0);
        for (int i = 0; i < mGreenAdapter.getCount(); i++) {
            View v = group.getChildAt(i);
            ImageView IVTint = (ImageView) v.findViewById(R.id.green_image_tint);
            if (IVTint != null) {
                if (i == index) {
                    IVTint.setVisibility(View.VISIBLE);
                } else {
                    IVTint.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    public void setBeautyParamsListner(BeautyParams params, OnBeautyParamsChangeListener listener) {
        mBeautyParams = params;
        mBeautyParamsChangeListener = listener;
        //当BeautyDialogFragment重置时，先刷新一遍配置
        if (mBeautyParamsChangeListener instanceof OnBeautyParamsChangeListener) {
            mBeautyParamsChangeListener.onBeautyParamsChange(mBeautyParams, BEAUTYPARAM_BEAUTY);
            mBeautyParamsChangeListener.onBeautyParamsChange(mBeautyParams, BEAUTYPARAM_WHITE);
            mBeautyParamsChangeListener.onBeautyParamsChange(mBeautyParams, BEAUTYPARAM_RUDDY);
            mBeautyParamsChangeListener.onBeautyParamsChange(mBeautyParams, BEAUTYPARAM_MOTION_TMPL);
        }
    }

    /**
     * pitu pkg start, P图动效打包专用，只编译，不打包源码，不要在此区域添加代码
     **/
    private MaterialAdapter mMaterialAdapter;
    public MaterialAdapter.OnItemClickListener mFilterClickListener = new MaterialAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(VideoMaterialMetaData materialMetaData) {
            mBeautyParams.mMotionTmplPath = "video_none".equals(materialMetaData.id) ? "" : materialMetaData.path;
            if (mBeautyParamsChangeListener instanceof OnBeautyParamsChangeListener) {
                mBeautyParamsChangeListener.onBeautyParamsChange(mBeautyParams, BEAUTYPARAM_MOTION_TMPL);
            }
        }
    };

    public List<VideoMaterialMetaData> loadLocalMaterials() {
        List materials = buildVideoMaterials();
        if (materials == null) {
            return new ArrayList();
        } else {
            Iterator var1 = materials.iterator();

            while (var1.hasNext()) {
                VideoMaterialMetaData material = (VideoMaterialMetaData) var1.next();
                if (TextUtils.isEmpty(material.path)) {
                    material.path = mPrefs.getString(material.id, "");
                }
            }

            return materials;
        }
    }

    public static List<VideoMaterialMetaData> buildVideoMaterials() {
        ArrayList materials = new ArrayList();
        materials.add(new VideoMaterialMetaData("video_rabbit", "assets://camera/camera_video/CameraVideoAnimal/video_rabbit", "", "assets://camera/camera_video/CameraVideoAnimal/video_rabbit/video_rabbit.png"));
        materials.add(new VideoMaterialMetaData("video_snow_white", "assets://camera/camera_video/CameraVideoAnimal/video_snow_white", "", "assets://camera/camera_video/CameraVideoAnimal/video_snow_white/video_snow_white.png"));
        String[] var3 = ONLINE_MATERIAL_IDS;
        int var4 = var3.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            String id = var3[var5];
            String packageUrl = "http://st1.xiangji.qq.com/yunmaterials/" + id + "Android.zip";
            String imageUrl = "http://st1.xiangji.qq.com/yunmaterials/" + id + ".png";
            materials.add(new VideoMaterialMetaData(id, "", packageUrl, imageUrl));
        }

        return materials;
    }

    private static String[] ONLINE_MATERIAL_IDS = new String[]{"video_jinmao", "video_fenlu", "video_leipen", "video_nethot", "video_fox", "video_water_ghost", "video_lamb", "video_xiaohuzi", "video_zhinv", "video_gentleman", "video_jiaban_dog", "video_little_mouse", "video_520", "video_zhipai", "video_cangshu", "video_huaduo", "video_wawalian", "video_aliens", "video_fangle2", "video_monalisa", "video_kangxi", "video_angrybird", "video_baby_milk", "video_dayuhaitang", "video_fawn", "video_guiguan", "video_heart_cheek", "video_heart_eye", "video_heart_lips", "video_huangguan", "video_laughday", "video_cat", "video_raccoon", "video_liaomei", "video_limao", "video_lovely_cat", "video_lovely_eye", "video_molihuaxian", "video_mothersday", "video_ogle", "video_ruhua", "video_snake_face", "video_zhenzi", "video_xiaoxuesheng", "video_xinqing", "video_yellow_dog"};
    private SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(VideoUtil1.getContext());
    ;
    /**pitu pkg end**/
}