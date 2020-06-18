package com.kds.just.segmentview;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import java.io.File;

public class EnhancedHalper {

    public static final String INI_FILENAME_CONFIG = "enhanced_font_path";

    public static String NAME_LIGHT = null;
    public static String NAME_REGULAR = null;
    public static String NAME_BOLD = null;

    public static final String FONT_PATH_LIGHT = "FONT_PATH_LIGHT";
    public static final String FONT_PATH_REGULAR = "FONT_PATH_REGULAR";
    public static final String FONT_PATH_BOLD = "FONT_PATH_BOLD";

    public static Typeface mFont_Light;
    public static Typeface mFont_Regular;
    public static Typeface mFont_Bold;

    public static enum FontType {
        Font_None(0),
        Font_Light(1),
        Font_Regular(2),
        Font_Bold(3);

        private int mId;

        private FontType(int id) {
            mId = id;
        }

        public static FontType fromId(int id) {
            for (FontType t: values()) {
                if (t.mId == id) {
                    return t;
                }
            }
            return Font_None;
        }
    }

    public static void initFontAssetPath(Context ctx, String light,String regular, String bold) {
        NAME_LIGHT = light;
        NAME_REGULAR = regular;
        NAME_BOLD = bold;
        put(ctx,FONT_PATH_LIGHT,NAME_LIGHT);
        put(ctx,FONT_PATH_REGULAR,NAME_REGULAR);
        put(ctx,FONT_PATH_BOLD,NAME_BOLD);
    }

    public static String getLightPath(Context ctx) {
        if (TextUtils.isEmpty(NAME_LIGHT)) {
            NAME_LIGHT = get(ctx,FONT_PATH_LIGHT);
        }
        return NAME_LIGHT;
    }
    public static String getRegularPath(Context ctx) {
        if (TextUtils.isEmpty(NAME_REGULAR)) {
            NAME_REGULAR = get(ctx,FONT_PATH_REGULAR);
        }
        return NAME_REGULAR;
    }
    public static String getBoldPath(Context ctx) {
        if (TextUtils.isEmpty(NAME_BOLD)) {
            NAME_BOLD = get(ctx,FONT_PATH_BOLD);
        }
        return NAME_BOLD;
    }

    public static FontType getFontType(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.SegmentedView);
        return FontType.fromId(a.getInt(R.styleable.SegmentedView_cfont, 0));
    }

    public static void setFont(Context ctx, TextView v, AttributeSet attrs) {
        FontType fontType = getFontType(ctx,attrs);
        boolean isFontChanged = false;
        switch (fontType) {
            case Font_Light:
                isFontChanged = setTextNanumLight(ctx, v);
                break;
            case Font_Regular:
                isFontChanged = setTextNanum(ctx, v);
                break;
            case Font_Bold:
                isFontChanged = setTextNaumeBold(ctx, v);
                break;
            default:
        }
        if (isFontChanged) {
            v.setIncludeFontPadding(false);
        }
    }

    public static void setFont(TextView v, FontType fontType) {
        boolean isFontChanged = false;
        switch (fontType) {
            case Font_Light:
                isFontChanged = setTextNanumLight(v.getContext(), v);
                break;
            case Font_Regular:
                isFontChanged = setTextNanum(v.getContext(), v);
                break;
            case Font_Bold:
                isFontChanged = setTextNaumeBold(v.getContext(), v);
                break;
            default:
        }
        if (isFontChanged) {
            v.setIncludeFontPadding(false);
        }
    }

    public static boolean setTextNanumLight(Context ctx, TextView v) {
        if (mFont_Light == null) {
            try {
                mFont_Light = Typeface.createFromAsset(ctx.getAssets(), NAME_LIGHT);
                v.setTypeface(mFont_Light);
                return true;
            } catch(Exception e) {
                try {
                    mFont_Light = Typeface.createFromFile(new File(Environment.getExternalStorageDirectory(), NAME_LIGHT));
                    v.setTypeface(mFont_Light);
                    return true;
                } catch(Exception e1) {

                }
                return false;
            }
        } else {
            v.setTypeface(mFont_Light);
            return true;
        }
    }

    public static boolean setTextNanum(Context ctx, TextView v) {
        if (mFont_Regular == null) {
            try {
                mFont_Regular = Typeface.createFromAsset(ctx.getAssets(), NAME_REGULAR);
                v.setTypeface(mFont_Regular);
                return true;
            } catch(Exception e) {
                try {
                    mFont_Regular = Typeface.createFromFile(new File(Environment.getExternalStorageDirectory(), NAME_REGULAR));
                    v.setTypeface(mFont_Regular);
                    return true;
                } catch(Exception e1) {

                }
                return false;
            }
        } else {
            v.setTypeface(mFont_Regular);
            return true;
        }
    }

    public static boolean setTextNaumeBold(Context ctx, TextView v) {
        if (mFont_Bold == null) {
            try {
                mFont_Bold = Typeface.createFromAsset(ctx.getAssets(), NAME_BOLD);
                v.setTypeface(mFont_Bold);
                return true;
            } catch(Exception e) {
                try {
                    mFont_Bold = Typeface.createFromFile(new File(Environment.getExternalStorageDirectory(), NAME_BOLD));
                    v.setTypeface(mFont_Bold);
                    return true;
                } catch(Exception e1) {

                }
                return false;
            }
        } else {
            v.setTypeface(mFont_Bold);
            return true;
        }
    }


    public static void put(Context ctx, String key, String value) {
        SharedPreferences pref = ctx.getSharedPreferences(INI_FILENAME_CONFIG, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(key, value);
        commit(editor);
    }

    public static String get(Context ctx, String key) {
        SharedPreferences pref = ctx.getSharedPreferences(INI_FILENAME_CONFIG, Activity.MODE_PRIVATE);
        try {
            String result = pref.getString(key,"");
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public static void commit(SharedPreferences.Editor editor) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD) {
            editor.apply();
        } else {
            editor.commit();
        }
    }
}
