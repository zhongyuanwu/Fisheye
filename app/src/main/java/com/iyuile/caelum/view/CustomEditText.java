package com.iyuile.caelum.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iyuile.caelum.R;
import com.iyuile.caelum.utils.MyApplication;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 自定义EditTtext
 *
 * @author WangYao
 * @version 1
 * @Description 自定义EditText
 * @date 2015-06-28 16:45:52
 */

public class CustomEditText extends LinearLayout {

    private Context mContext;

    public static final int PROMPT_TYPE_ERROR = 0x000001; // 错误提示
    public static final int PROMPT_TYPE_WARNING = 0x000002;// 警告提示
    public static final int PROMPT_TYPE_SUCCESS = 0x000003;// 成功提示

    private TextView txt_icon;
    private EditText edit_text;
    private TextView tv_txt_prompt;//提示text
    private View hr;
    private String iconText, hintText, text;
    private int iconColor = 0x00000000, textColor, hintColor, hrColor;
    private int maxLength;
    private int inputType;
    private float iconSize, textSize;
    private boolean isEditState = true;

    private Animation mShakeAnim;

    public CustomEditText(Context context) {
        super(context);
        mContext = context;
    }

    /**
     * @hide
     */
    @IntDef({PROMPT_TYPE_ERROR, PROMPT_TYPE_WARNING, PROMPT_TYPE_SUCCESS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
    }

    /**
     * @param context
     * @param attrs
     */
    @SuppressLint("InlinedApi")
    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_edittext, this);

        txt_icon = (TextView) findViewById(R.id.txt_icon);
        txt_icon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (iconOnClickListener != null)
                    iconOnClickListener.onClick();
            }
        });

        edit_text = (EditText) findViewById(R.id.edit_txt);
//        edit_text.setTypeface(MyApplication.getInstance().getWoodBodyStyleFont());
        tv_txt_prompt = (TextView) findViewById(R.id.tv_txt_prompt);
//        tv_txt_prompt.setTypeface(MyApplication.getInstance().getWoodBodyStyleFont());
        hr = findViewById(R.id.hr);

        // 将字体文件保存在assets/fonts/目录下，创建Typeface对象
//		Typeface typeFace = Typeface.createFromAsset(context.getAssets(),"fonts/iconfont.ttf");
        // 使用字体
        txt_icon.setTypeface(MyApplication.getInstance().getIconStyleFont());

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.custom_edittext);
        //
        iconText = a.getString(R.styleable.custom_edittext_iconText);
        if (iconText != null)
            txt_icon.setText(iconText);

        //获取默认字体大小(TextView=14sp,EditText=18sp)
//		float scale = getResources().getDisplayMetrics().density;
//	    int txtDefault = (int) (txt_icon.getTextSize() / scale);
//	    float editDefault = edit_text.getTextSize() / scale;
//	    
//	    iconSize = a.getDimensionPixelSize(R.styleable.custom_edittext_iconSize, PixelUtil.sp2px(txtDefault, context));
//	    txt_icon.setTextSize(iconSize/2);
//	    
//	    textSize = a.getDimensionPixelSize(R.styleable.custom_edittext_textSize, PixelUtil.sp2px(editDefault, context));
//	    edit_text.setTextSize(textSize/2);
//	    tv_txt_prompt.setTextSize(textSize/2);

        iconColor = a.getColor(R.styleable.custom_edittext_iconColor, Color.BLACK);
        txt_icon.setTextColor(iconColor);
        //是否可编辑
        isEditState = a.getBoolean(R.styleable.custom_edittext_isEdit, isEditState);
        isEdit(isEditState);

        //
        hintText = a.getString(R.styleable.custom_edittext_hintText);
        if (hintText != null)
            edit_text.setHint(hintText);
        //
        hintColor = a.getColor(R.styleable.custom_edittext_hintColor, ContextCompat.getColor(context, R.color.custom_default_hinttext));
        edit_text.setHintTextColor(hintColor);
        //
        text = a.getString(R.styleable.custom_edittext_text);
        if (text != null)
            edit_text.setText(text);
        //
        textColor = a.getColor(R.styleable.custom_edittext_textColor, Color.BLACK);
        edit_text.setTextColor(textColor);
        tv_txt_prompt.setTextColor(textColor);
        //字符长度限制
        maxLength = a.getInteger(R.styleable.custom_edittext_maxLength, 0);
        setTextMaxLength(maxLength);
        //
        hrColor = a.getColor(R.styleable.custom_edittext_hrColor, Color.BLACK);
        hr.setBackgroundColor(hrColor);
        //
        inputType = a.getInteger(R.styleable.custom_edittext_inputType, InputType.TYPE_NUMBER_VARIATION_NORMAL);
        switch (inputType) {
            case InputType.TYPE_NUMBER_VARIATION_NORMAL://@ersion2 加入代码设置了默认类型,因为xml默认是小数类型,
                edit_text.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL);
            case InputType.TYPE_CLASS_TEXT:
                edit_text.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS:
                edit_text.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                break;
            case InputType.TYPE_TEXT_FLAG_CAP_WORDS:
                edit_text.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                break;
            case InputType.TYPE_TEXT_FLAG_CAP_SENTENCES:
                edit_text.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                break;
            case InputType.TYPE_TEXT_FLAG_AUTO_CORRECT:
                edit_text.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
                break;
            case InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE:
                edit_text.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
                break;
            case InputType.TYPE_TEXT_FLAG_MULTI_LINE:
                edit_text.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                break;
            case InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE:
                edit_text.setInputType(InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
                break;
            case InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS:
                edit_text.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                break;
            case InputType.TYPE_TEXT_VARIATION_URI:
                edit_text.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
                break;
            case InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS:
                edit_text.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                break;
            case InputType.TYPE_TEXT_VARIATION_EMAIL_SUBJECT:
                edit_text.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_SUBJECT);
                break;
            case InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE:
                edit_text.setInputType(InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE);
                break;
            case InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE:
                edit_text.setInputType(InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE);
                break;
            case InputType.TYPE_TEXT_VARIATION_PERSON_NAME:
                edit_text.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                break;
            case InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS:
                edit_text.setInputType(InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
                break;
            case InputType.TYPE_TEXT_VARIATION_PASSWORD://密码
//			edit_text.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                edit_text.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
            case InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD:
                edit_text.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                break;
            case InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT:
                edit_text.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT);
                break;
            case InputType.TYPE_TEXT_VARIATION_FILTER:
                edit_text.setInputType(InputType.TYPE_TEXT_VARIATION_FILTER);
                break;
            case InputType.TYPE_TEXT_VARIATION_PHONETIC:
                edit_text.setInputType(InputType.TYPE_TEXT_VARIATION_PHONETIC);
                break;
            case InputType.TYPE_CLASS_NUMBER:
                edit_text.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case InputType.TYPE_CLASS_PHONE:
                edit_text.setInputType(InputType.TYPE_CLASS_PHONE);
                break;
            case InputType.TYPE_CLASS_DATETIME:
                edit_text.setInputType(InputType.TYPE_CLASS_DATETIME);
                break;
            case 0x00902000://小数,默认是这类型,
                setPricePoint(edit_text, a.getInteger(R.styleable.custom_edittext_decimalNumber, 0));//设置小数点后几位(:::默认0,不限制)
                break;
            // case InputType.TYPE_NUMBER_FLAG_SIGNED:
            //
            // break;
            // case InputType.TYPE_NUMBER_FLAG_DECIMAL:
            //
            // break;

            // case InputType.TYPE_DATETIME_VARIATION_DATE:
            //
            // break;
            // case InputType.TYPE_DATETIME_VARIATION_TIME:
            //
            // break;
            default:
                break;
        }

        a.recycle();
    }

    /**
     * 小数点后几位
     *
     * @param editText
     * @param number   小数点后几位
     */
    public void setPricePoint(final EditText editText, final int number) {
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (number != 0) {
                    if (s.toString().contains(".")) {
                        if (s.length() - 1 - s.toString().indexOf(".") > number) {
                            s = s.toString().subSequence(0, s.toString().indexOf(".") + number + 1);
                            editText.setText(s);
                            editText.setSelection(s.length());
                        }
                    }
                    if (s.toString().trim().substring(0).equals(".")) {
                        s = "0" + s;
                        editText.setText(s);
                        editText.setSelection(number);
                    }

                    if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                        if (!s.toString().substring(1, number).equals(".")) {
                            editText.setText(s.subSequence(0, 1));
                            editText.setSelection(1);
                            return;
                        }
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

        });

    }

    /**
     * 获取edittext对象
     *
     * @return
     */
    public EditText getEditText() {
        return this.edit_text;
    }

    /**
     * 获取icon文本
     *
     * @return
     */
    public String getIconText() {
        return txt_icon.getText().toString();
    }

    /**
     * @param iconText
     */
    public void setIconText(String iconText) {
        txt_icon.setText(iconText);
    }

    /**
     * 设置icon文本
     *
     * @param length
     */
    public void setTextMaxLength(int length) {
        this.maxLength = length;

        if (maxLength != 0) {//"0"的时候不限制

            if (edit_text.length() > maxLength) {//如果默认字符长度超出了设置的长度被切掉
                edit_text.setText(edit_text.getText().toString().substring(0, maxLength));
            }

            edit_text.setFilters(
                    new InputFilter[]{new InputFilter() {
                        @Override
                        public CharSequence filter(CharSequence source,
                                                   int start, int end, Spanned dest,
                                                   int dstart, int dend) {
                            int keep = maxLength - (dest.length() - (dend - dstart));
                            if (keep <= 0) {
                                prompt(PROMPT_TYPE_WARNING, "只能输入" + maxLength + "位!", false);
                                return "";
                            } else if (keep >= end - start) {
                                return null;
                            } else {
                                return source.subSequence(start, start + keep);
                            }
                        }
                    }});
        }
    }

    /**
     * 获取edit文本
     *
     * @return
     */
    public String getText() {
        return edit_text.getText().toString();
    }

    /**
     * 设置edit文本
     *
     * @param text
     */
    public void setText(String text) {
        edit_text.setText(text);
    }

    /**
     * 清空文本
     */
    public void emptyText() {
        edit_text.setText("");
    }

    /**
     * 是否可编辑
     *
     * @param state
     */
    public void isEdit(boolean state) {
        edit_text.setFocusable(state);
        edit_text.setFocusableInTouchMode(state);
    }

    private IconOnClickListener iconOnClickListener;//icon 点击事件

    public void setIconOnClickListener(IconOnClickListener iconOnClickListener) {
        this.iconOnClickListener = iconOnClickListener;
    }

    public interface IconOnClickListener {
        public void onClick();
    }

    /**
     * 提示 警告or错误
     *
     * @param type        类型
     * @param warningText 警告文本
     * @param isEmpty     是否提示完后清空
     */
    public void prompt(@Type int type, String warningText, final boolean isEmpty) {
        if (mShakeAnim == null) {
            mShakeAnim = AnimationUtils.loadAnimation(mContext, R.anim.shake);
        }

        if (type == PROMPT_TYPE_ERROR) {
            tv_txt_prompt.setTextColor(Color.RED);
            txt_icon.setTextColor(Color.RED);
            txt_icon.setText(R.string.custom_icon_error);
            hr.setBackgroundColor(Color.RED);
        } else if (type == PROMPT_TYPE_WARNING) {
            tv_txt_prompt.setTextColor(ContextCompat.getColor(mContext, R.color.custom_prompt_warning));
            txt_icon.setTextColor(ContextCompat.getColor(mContext, R.color.custom_prompt_warning));
            txt_icon.setText(R.string.custom_icon_warning);
            hr.setBackgroundColor(ContextCompat.getColor(mContext, R.color.custom_prompt_warning));
        } else if (type == PROMPT_TYPE_SUCCESS) {
            tv_txt_prompt.setTextColor(ContextCompat.getColor(mContext, R.color.custom_prompt_success));
            txt_icon.setTextColor(ContextCompat.getColor(mContext, R.color.custom_prompt_success));
            txt_icon.setText(R.string.custom_icon_success);
            hr.setBackgroundColor(ContextCompat.getColor(mContext, R.color.custom_prompt_success));
        }

        edit_text.setVisibility(View.GONE);
        tv_txt_prompt.setVisibility(View.VISIBLE);

        tv_txt_prompt.setText(warningText);

        txt_icon.startAnimation(mShakeAnim);

        mShakeAnim.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                edit_text.setFocusable(false);//失去焦点
                edit_text.setFocusableInTouchMode(false);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                edit_text.setFocusable(true);//得到焦点
                edit_text.setFocusableInTouchMode(true);

                edit_text.setVisibility(View.VISIBLE);
                tv_txt_prompt.setVisibility(View.GONE);

                txt_icon.setTextColor(iconColor);
                txt_icon.setText(iconText);
                hr.setBackgroundColor(hrColor);

                if (isEmpty)
                    edit_text.setText("");//清空
            }
        });
    }
}
