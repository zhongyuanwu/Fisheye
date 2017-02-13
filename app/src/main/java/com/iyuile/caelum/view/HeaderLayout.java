package com.iyuile.caelum.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iyuile.caelum.R;
import com.iyuile.caelum.utils.MyApplication;
import com.iyuile.caelum.utils.PixelUtil;


/**
 * 自定义头部布局
 *
 * @author WangYao
 * @ClassName: {@link HeaderLayout}
 * @Description: 自定义actionBar
 * @date 2014-5-19 下午2:30:30
 * :::version 3
 */
public class HeaderLayout extends LinearLayout {

    private String textLeft, titleText, textRight;
    private Drawable textLeftImg, textRightImg;
    private int textColor, backgroundc, textColorCenter;
    private Drawable backgrounds;
    private float textSize, textSizeCenter;
    private int styleType;

    private float secondaryPadding = 0f, secondaryPaddingDefault = 6f;

    private LayoutInflater mInflater;
    private View mHeader;
    private LinearLayout mLayoutLeftContainer;
    private LinearLayout mLayoutMiddleContainer;
    private LinearLayout mLayoutRightContainer;
    private TextView mHtvSubTitle;

    private LinearLayout mLayoutRightImageButtonLayout;
    private Button mRightImageButton;
    private onRightImageButtonClickListener mRightImageButtonClickListener;

    private LinearLayout mLayoutLeftImageButtonLayout;
    private ImageButton mLeftImageButton;
    private onLeftImageButtonClickListener mLeftImageButtonClickListener;


    private LinearLayout mLayoutRightTxtButtonLayout;
    private TextView mRightTxtButton, mRightTxtButtonSecondary;

    private LinearLayout mLayoutLeftTxtButtonLayout;
    private TextView mLeftTxtButton, mLeftTxtButtonSecondary;

    private TextView mCenterTxtButton;

    public enum HeaderStyle {// 头部整体样式
        DEFAULT_TITLE, TITLE_LEFT_IMAGEBUTTON, TITLE_RIGHT_IMAGEBUTTON, TITLE_DOUBLE_IMAGEBUTTON, TITLE_LEFT_TXTBUTTON, TITLE_RIGHT_TXTBUTTON, TITLE_DOUBLE_TXTBUTTON;
    }

    public HeaderLayout(Context context) {
        super(context);
        init(context, null);
    }

    public HeaderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @SuppressLint("NewApi")
    public void init(Context context, AttributeSet attrs) {
        mInflater = LayoutInflater.from(context);
        mHeader = mInflater.inflate(R.layout.actionbar_common_header, null);
        mHeader.setOnClickListener(null);//:::不让点击事件透过bar
        addView(mHeader);
        initViews();
        //:::------------------------------------------------------
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomActionBar);

            styleType = a.getInteger(R.styleable.CustomActionBar_style, 0x00000099);

            switch (styleType) {
                case 0x00000001:
                    defaultTitle();
                    break;

                case 0x00000002:
                    defaultTitle();
                    titleLeftImageButton();
                    break;

                case 0x00000003:
                    defaultTitle();
                    titleRightImageButton();
                    break;

                case 0x00000004:
                    defaultTitle();
                    titleLeftImageButton();
                    titleRightImageButton();
                    break;
                // :::
                case 0x00000005:
                    defaultTitle();
                    titleLeftTxtButton();
                    break;

                case 0x00000006:
                    defaultTitle();
                    titleRightTxtButton();
                    break;

                case 0x00000007:
                    defaultTitle();
                    titleLeftTxtButton();
                    titleRightTxtButton();
                    break;
            }
            if (styleType != 0x00000099) {

                titleText = a.getString(R.styleable.CustomActionBar_titleText);
                if (titleText != null && mCenterTxtButton != null)
                    mCenterTxtButton.setText(titleText);

                textLeft = a.getString(R.styleable.CustomActionBar_textLeft);
                if (textLeft != null && mLeftTxtButton != null)
                    mLeftTxtButton.setText(textLeft);

                textRight = a.getString(R.styleable.CustomActionBar_textRight);
                if (textRight != null && mRightTxtButton != null)
                    mRightTxtButton.setText(textRight);

                textLeftImg = a.getDrawable(R.styleable.CustomActionBar_textLeftImg);
                if (textLeftImg != null && mLeftImageButton != null)
                    mLeftImageButton.setImageDrawable(textLeftImg);

                textRightImg = a.getDrawable(R.styleable.CustomActionBar_textRightImg);
                if (textRightImg != null && mRightImageButton != null)
                    mRightImageButton.setBackground(textRightImg);

                backgrounds = a.getDrawable(R.styleable.CustomActionBar_backgrounds);
                if (backgrounds != null && mHeader != null)
                    mHeader.setBackground(backgrounds);

                textColor = a.getColor(R.styleable.CustomActionBar_textColorc, getResources().getColor(R.color.custom_actionbar_base_color_text));
                if (textLeft != null && mLeftTxtButton != null) {
                    mLeftTxtButton.setTextColor(textColor);
                    mLeftTxtButtonSecondary.setTextColor(textColor);
                }
                if (textRight != null && mRightTxtButton != null) {
                    mRightTxtButton.setTextColor(textColor);
                    mRightTxtButtonSecondary.setTextColor(textColor);
                }

                backgroundc = a.getColor(R.styleable.CustomActionBar_backgroundc, getResources().getColor(R.color.custom_actionbar_base_color_background));
                if (mHeader != null)
                    mHeader.setBackgroundColor(backgroundc);

                textColorCenter = a.getColor(R.styleable.CustomActionBar_textColorCenter, getResources().getColor(R.color.custom_actionbar_base_color_text));
                if (mCenterTxtButton != null)
                    mCenterTxtButton.setTextColor(textColorCenter);

                //TODO :::version 2
                if (mLeftTxtButton != null) {
                    textSize = a.getDimension(R.styleable.CustomActionBar_textSizes, 0);
                    if (textSizeCenter != 0) {
                        mLeftTxtButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                        mLeftTxtButtonSecondary.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                    }
                } else if (mRightTxtButton != null) {
                    textSize = a.getDimension(R.styleable.CustomActionBar_textSizes, 0);
                    if (textSizeCenter != 0) {
                        mRightTxtButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                        mRightTxtButtonSecondary.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                    }
                }

                if (mCenterTxtButton != null) {
                    textSizeCenter = a.getDimension(R.styleable.CustomActionBar_textSizeCenter, 0);
                    if (textSizeCenter != 0)
                        mCenterTxtButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeCenter);
                }
            }

            a.recycle();

        }

    }

    /**
     * 设置actionBar背景
     *
     * @param color
     */
    public void setActionBarBackgroundColor(int color) {
        mHeader.setBackgroundColor(color);
    }

    /**
     * 设置actionBar背景
     *
     * @param resid
     */
    public void setActionBarBackgroundResource(int resid) {
        mHeader.setBackgroundResource(resid);
    }

    public void initViews() {
        mLayoutLeftContainer = (LinearLayout) findViewByHeaderId(R.id.header_layout_leftview_container);
        mLayoutMiddleContainer = (LinearLayout) findViewByHeaderId(R.id.header_layout_middleview_container);//中间部分添加搜索或者其他按钮时可打开:::
        mCenterTxtButton = (TextView) findViewById(R.id.header_htv_subtitle);
        mLayoutRightContainer = (LinearLayout) findViewByHeaderId(R.id.header_layout_rightview_container);
        mHtvSubTitle = (TextView) findViewByHeaderId(R.id.header_htv_subtitle);
        if (!isInEditMode()) {
            //TODO :::version 1 根据项目需要,默认设置标题字体
            mHtvSubTitle.setTypeface(MyApplication.getInstance().getYeGenyouStyleFont());
        }
    }

    /**
     * 设置字体
     *
     * @param tf
     */
    public void setTitleTypeface(Typeface tf) {
        mHtvSubTitle.setTypeface(tf);
    }

    /**
     * 设置文字大小
     *
     * @param size
     */
    public void setTitleSize(float size) {
        mHtvSubTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    public View findViewByHeaderId(int id) {
        return mHeader.findViewById(id);
    }

    public void init(HeaderStyle hStyle) {
        switch (hStyle) {
            case DEFAULT_TITLE:
                defaultTitle();
                break;

            case TITLE_LEFT_IMAGEBUTTON:
                defaultTitle();
                titleLeftImageButton();
                break;

            case TITLE_RIGHT_IMAGEBUTTON:
                defaultTitle();
                titleRightImageButton();
                break;

            case TITLE_DOUBLE_IMAGEBUTTON:
                defaultTitle();
                titleLeftImageButton();
                titleRightImageButton();
                break;
            //:::
            case TITLE_LEFT_TXTBUTTON:
                defaultTitle();
                titleLeftTxtButton();
                break;

            case TITLE_RIGHT_TXTBUTTON:
                defaultTitle();
                titleRightTxtButton();
                break;

            case TITLE_DOUBLE_TXTBUTTON:
                defaultTitle();
                titleLeftTxtButton();
                titleRightTxtButton();
                break;
        }
    }

    // 默认文字标题
    private void defaultTitle() {
        mLayoutLeftContainer.removeAllViews();
        mLayoutRightContainer.removeAllViews();
    }

    // 左侧自定义按钮
    private void titleLeftImageButton() {
        View mleftImageButtonView = mInflater.inflate(R.layout.actionbar_common_header_left_button, null);
        mLayoutLeftContainer.addView(mleftImageButtonView);
        mLayoutLeftImageButtonLayout = (LinearLayout) mleftImageButtonView.findViewById(R.id.header_layout_imagebuttonlayout);
        mLeftImageButton = (ImageButton) mleftImageButtonView.findViewById(R.id.header_ib_imagebutton);
        mLayoutLeftContainer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mLeftImageButtonClickListener != null) {
                    mLeftImageButtonClickListener.onClick();
                }
            }
        });
    }

    // :::左侧自定义文字按钮
    private void titleLeftTxtButton() {
        View mleftTxtButtonView = mInflater.inflate(R.layout.actionbar_common_header_left_txt, null);
        mLayoutLeftContainer.addView(mleftTxtButtonView);
        mLayoutLeftTxtButtonLayout = (LinearLayout) mleftTxtButtonView.findViewById(R.id.header_layout_txtbuttonlayout);
        mLeftTxtButton = (TextView) mleftTxtButtonView.findViewById(R.id.header_ib_txtbutton);
        mLeftTxtButtonSecondary = (TextView) mleftTxtButtonView.findViewById(R.id.header_ib_txtbutton_secondary);
        mLayoutLeftContainer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mLeftImageButtonClickListener != null) {
                    mLeftImageButtonClickListener.onClick();
                }
            }
        });
    }

    // 右侧自定义按钮
    private void titleRightImageButton() {
        View mRightImageButtonView = mInflater.inflate(R.layout.actionbar_common_header_right_button, null);
        mLayoutRightContainer.addView(mRightImageButtonView);
        mLayoutRightImageButtonLayout = (LinearLayout) mRightImageButtonView.findViewById(R.id.header_layout_imagebuttonlayout);
        mRightImageButton = (Button) mRightImageButtonView.findViewById(R.id.header_ib_imagebutton);
        mLayoutRightContainer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mRightImageButtonClickListener != null) {
                    mRightImageButtonClickListener.onClick();
                }
            }
        });
    }

    //::: 右侧自定义文字按钮
    private void titleRightTxtButton() {
        View mRightTxtButtonView = mInflater.inflate(R.layout.actionbar_common_header_right_txt, null);
        mLayoutRightContainer.addView(mRightTxtButtonView);
        mLayoutRightTxtButtonLayout = (LinearLayout) mRightTxtButtonView.findViewById(R.id.header_layout_txtbuttonlayout);
        mRightTxtButton = (TextView) mRightTxtButtonView.findViewById(R.id.header_ib_txtbutton);
        mRightTxtButtonSecondary = (TextView) mRightTxtButtonView.findViewById(R.id.header_ib_txtbutton_secondary);
        mLayoutRightContainer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mRightImageButtonClickListener != null) {
                    mRightImageButtonClickListener.onClick();
                }
            }
        });
    }

    /**
     * 获取右边按钮
     *
     * @param @return
     * @return Button
     * @throws
     * @Title: getRightImageButton
     */
    public Button getRightImageButton() {
        if (mRightImageButton != null) {
            return mRightImageButton;
        }
        return null;
    }

    /**
     * 获取右边文字按钮
     * //:::
     *
     * @param @return
     * @return Txt
     * @throws
     * @Title: getRightTxtButton
     */
    public TextView getRightTxtButton() {
        if (mRightTxtButton != null) {
            return mRightTxtButton;
        }
        return null;
    }

    /**
     * 副文本
     *
     * @return
     */
    public TextView getRightTxtButtonSecondary() {
        if (mRightTxtButtonSecondary != null) {
            return mRightTxtButtonSecondary;
        }
        return null;
    }

    public void setDefaultTitle(CharSequence title) {
        if (title != null) {
            if (!title.equals(""))
                mHtvSubTitle.setText(title);
        } else {
            mHtvSubTitle.setVisibility(View.GONE);
        }
    }

    public TextView getTitleCenter() {
        return mHtvSubTitle;
    }

    /**
     * 设置标题颜色
     *
     * @param color
     */
    public void setTitleColor(int color) {
        mHtvSubTitle.setTextColor(color);
    }

    public void setTitleAndRightButton(CharSequence title, int backid,
                                       String text,
                                       onRightImageButtonClickListener onRightImageButtonClickListener) {
        setDefaultTitle(title);
        mLayoutRightContainer.setVisibility(View.VISIBLE);
        if (mRightImageButton != null && backid > 0) {
            mRightImageButton.setWidth(PixelUtil.dp2px(45));
            mRightImageButton.setHeight(PixelUtil.dp2px(40));
            mRightImageButton.setBackgroundResource(backid);
            mRightImageButton.setText(text);
            setOnRightImageButtonClickListener(onRightImageButtonClickListener);
        }
    }

    //:::
    public void setTitleAndRightTxt(CharSequence title, int backid, Typeface typeFace,
                                    String text,
                                    onRightImageButtonClickListener onRightImageButtonClickListener) {
        setDefaultTitle(title);
        mLayoutRightContainer.setVisibility(View.VISIBLE);
        if (mRightTxtButton != null && backid > 0) {
            mRightTxtButton.setWidth(PixelUtil.dp2px(45));
            mRightTxtButton.setHeight(PixelUtil.dp2px(40));
            mRightTxtButton.setBackgroundResource(backid);
            mRightTxtButton.setText(text);
            if (typeFace != null) {
                mRightTxtButton.setTypeface(typeFace);
            }
            setOnRightImageButtonClickListener(onRightImageButtonClickListener);
        }
    }

    //:::
    public void setTitleAndRightSecondaryTxt(CharSequence title, int backid, Typeface typeFace,
                                             String text,
                                             onRightImageButtonClickListener onRightImageButtonClickListener) {
        setDefaultTitle(title);
        mLayoutRightContainer.setVisibility(View.VISIBLE);
        if (mRightTxtButtonSecondary != null && backid > 0) {
            mRightTxtButton.setPadding(PixelUtil.dp2px(secondaryPadding), mRightTxtButton.getPaddingTop(), mRightTxtButton.getPaddingRight(), mRightTxtButton.getPaddingBottom());
            mRightTxtButtonSecondary.setVisibility(View.VISIBLE);
            mRightTxtButtonSecondary.setWidth(PixelUtil.dp2px(45));
            mRightTxtButtonSecondary.setHeight(PixelUtil.dp2px(40));
            mRightTxtButtonSecondary.setBackgroundResource(backid);
            mRightTxtButtonSecondary.setText(text);
            if (typeFace != null) {
                mRightTxtButtonSecondary.setTypeface(typeFace);
            }
            setOnRightImageButtonClickListener(onRightImageButtonClickListener);
        } else {
            mRightTxtButton.setPadding(PixelUtil.dp2px(secondaryPaddingDefault), mRightTxtButton.getPaddingTop(), mRightTxtButton.getPaddingRight(), mRightTxtButton.getPaddingBottom());
            mRightTxtButtonSecondary.setVisibility(View.GONE);
        }
    }

    public void setTitleAndRightImageButton(CharSequence title, int backid,
                                            onRightImageButtonClickListener onRightImageButtonClickListener) {
        setDefaultTitle(title);
        mLayoutRightContainer.setVisibility(View.VISIBLE);
        if (mRightImageButton != null && backid > 0) {
            mRightImageButton.setWidth(PixelUtil.dp2px(30));
            mRightImageButton.setHeight(PixelUtil.dp2px(30));
            mRightImageButton.setTextColor(Color.TRANSPARENT);// :::getResources().getColor(R.color.transparent)
            mRightImageButton.setBackgroundResource(backid);
            setOnRightImageButtonClickListener(onRightImageButtonClickListener);
        }
    }

    //:::
    public void setTitleAndRightTxtButton(CharSequence title, int txtId, Typeface typeFace,
                                          onRightImageButtonClickListener onRightImageButtonClickListener) {
        setDefaultTitle(title);
        mLayoutRightContainer.setVisibility(View.VISIBLE);
        if (mRightTxtButton != null && txtId > 0) {
            mRightTxtButton.setText(txtId);
            if (typeFace != null) {
                mRightTxtButton.setTypeface(typeFace);
            }
            setOnRightImageButtonClickListener(onRightImageButtonClickListener);
        }
    }

    //:::
    public void setTitleAndRightSecondaryTxtButton(CharSequence title, int txtId, Typeface typeFace,
                                                   onRightImageButtonClickListener onRightImageButtonClickListener) {
        setDefaultTitle(title);
        mLayoutRightContainer.setVisibility(View.VISIBLE);
        if (mRightTxtButtonSecondary != null && txtId > 0) {
            mRightTxtButton.setPadding(PixelUtil.dp2px(secondaryPadding), mRightTxtButton.getPaddingTop(), mRightTxtButton.getPaddingRight(), mRightTxtButton.getPaddingBottom());
            mRightTxtButtonSecondary.setVisibility(View.VISIBLE);
            mRightTxtButtonSecondary.setText(txtId);
            if (typeFace != null) {
                mRightTxtButtonSecondary.setTypeface(typeFace);
            }
            setOnRightImageButtonClickListener(onRightImageButtonClickListener);
        } else {
            mRightTxtButton.setPadding(PixelUtil.dp2px(secondaryPaddingDefault), mRightTxtButton.getPaddingTop(), mRightTxtButton.getPaddingRight(), mRightTxtButton.getPaddingBottom());
            mRightTxtButtonSecondary.setVisibility(View.GONE);
        }
    }

    //:::
    public void setTitleAndRightTxtButtonAndTextSize(CharSequence title, int txtId, Typeface typeFace, float txtSize,
                                                     onRightImageButtonClickListener onRightImageButtonClickListener) {
        setDefaultTitle(title);
        mLayoutRightContainer.setVisibility(View.VISIBLE);
        if (mRightTxtButton != null && txtId > 0) {
            mRightTxtButton.setText(txtId);
            if (typeFace != null) {
                mRightTxtButton.setTypeface(typeFace);
            }
            if (txtSize != 0) {
                mRightTxtButton.setTextSize(txtSize);
            }
            setOnRightImageButtonClickListener(onRightImageButtonClickListener);
        }
    }

    //:::
    public void setTitleAndRightSecondaryTxtButtonAndTextSize(CharSequence title, int txtId, Typeface typeFace, float txtSize,
                                                              onRightImageButtonClickListener onRightImageButtonClickListener) {
        setDefaultTitle(title);
        mLayoutRightContainer.setVisibility(View.VISIBLE);
        if (mRightTxtButtonSecondary != null && txtId > 0) {
            mRightTxtButton.setPadding(PixelUtil.dp2px(secondaryPadding), mRightTxtButton.getPaddingTop(), mRightTxtButton.getPaddingRight(), mRightTxtButton.getPaddingBottom());
            mRightTxtButtonSecondary.setVisibility(View.VISIBLE);
            mRightTxtButtonSecondary.setText(txtId);
            if (typeFace != null) {
                mRightTxtButtonSecondary.setTypeface(typeFace);
            }
            if (txtSize != 0) {
                mRightTxtButtonSecondary.setTextSize(txtSize);
            }
            setOnRightImageButtonClickListener(onRightImageButtonClickListener);
        } else {
            mRightTxtButton.setPadding(PixelUtil.dp2px(secondaryPaddingDefault), mRightTxtButton.getPaddingTop(), mRightTxtButton.getPaddingRight(), mRightTxtButton.getPaddingBottom());
            mRightTxtButtonSecondary.setVisibility(View.GONE);
        }
    }

    public void setTitleAndLeftImageButton(CharSequence title, int id, onLeftImageButtonClickListener listener) {
        setDefaultTitle(title);
        if (mLeftImageButton != null && id > 0) {
            mLeftImageButton.setImageResource(id);
            setOnLeftImageButtonClickListener(listener);
        }
        mLayoutRightContainer.setVisibility(View.INVISIBLE);
    }

    public void setDoubleListener(onLeftImageButtonClickListener leftListener, onRightImageButtonClickListener rightListener) {
        setOnLeftImageButtonClickListener(leftListener);
        setOnRightImageButtonClickListener(rightListener);
    }

    public void setLeftListener(onLeftImageButtonClickListener leftListener) {
        setOnLeftImageButtonClickListener(leftListener);
    }

    public void setRightListener(onRightImageButtonClickListener rightListener) {
        setOnRightImageButtonClickListener(rightListener);
    }

    //:::
    public void setTitleAndLeftTxtButton(CharSequence title, int txtId, Typeface typeFace, onLeftImageButtonClickListener listener) {
        setDefaultTitle(title);
        if (mLeftTxtButton != null && txtId > 0) {
            mLeftTxtButton.setText(txtId);
            if (typeFace != null) {
                mLeftTxtButton.setTypeface(typeFace);
            }
            setOnLeftImageButtonClickListener(listener);
        }
        mLayoutRightContainer.setVisibility(View.INVISIBLE);
    }

    //:::
    public void setTitleAndLeftSecondaryTxtButton(CharSequence title, int txtId, Typeface typeFace, onLeftImageButtonClickListener listener) {
        setDefaultTitle(title);
        if (mLeftTxtButtonSecondary != null && txtId > 0) {
            mLeftTxtButton.setPadding(mLeftTxtButton.getPaddingLeft(), mLeftTxtButton.getPaddingTop(), PixelUtil.dp2px(secondaryPadding), mLeftTxtButton.getPaddingBottom());
            mLeftTxtButtonSecondary.setVisibility(View.VISIBLE);
            mLeftTxtButtonSecondary.setText(txtId);
            if (typeFace != null) {
                mLeftTxtButtonSecondary.setTypeface(typeFace);
            }
            setOnLeftImageButtonClickListener(listener);
        } else {
            mLeftTxtButton.setPadding(mLeftTxtButton.getPaddingLeft(), mLeftTxtButton.getPaddingTop(), PixelUtil.dp2px(secondaryPaddingDefault), mLeftTxtButton.getPaddingBottom());
            mLeftTxtButtonSecondary.setVisibility(View.GONE);
        }
        mLayoutRightContainer.setVisibility(View.INVISIBLE);
    }

    public void setOnRightImageButtonClickListener(
            onRightImageButtonClickListener listener) {
        mRightImageButtonClickListener = listener;
    }

    public interface onRightImageButtonClickListener {
        void onClick();
    }

    public void setOnLeftImageButtonClickListener(
            onLeftImageButtonClickListener listener) {
        mLeftImageButtonClickListener = listener;
    }

    public interface onLeftImageButtonClickListener {
        void onClick();
    }

}
