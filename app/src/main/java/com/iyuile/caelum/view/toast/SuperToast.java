/**
 *  Copyright 2014 John Persano
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 *
 */

package com.iyuile.caelum.view.toast;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iyuile.caelum.R;
import com.iyuile.caelum.utils.ScreenUtils;


/**
 * SuperToasts are designed to replace stock Android Toasts.
 * If you need to display a SuperToast inside of an Activity
 * please see {@link SuperToast}.
 *
 *
	final SuperToast superToast = new SuperToast(getActivity());

    superToast.setAnimations(SuperToast.Animations.FLYIN);
    superToast.setDuration(SuperToast.Duration.SHORT);
    superToast.setBackground(SuperToast.Background.GREEN);
    superToast.setTextSize(SuperToast.TextSize.SMALL);
    superToast.setIconSuperToast.Icon.Resource.YES, SuperToast.IconPosition.LEFT);
//    superToast.setGravity(Gravity.RIGHT|Gravity.TOP, 0, 48);
    superToast.setText("上传");
    superToast.show();

	SuperToast.create(getActivity(), "登录成功!", SuperToast.Duration.SHORT).setIcon(SuperToast.Icon.Resource.YES, SuperToast.IconPosition.LEFT).setBackground(SuperToast.Background.GREEN).show();

 *
 *
 */
public class SuperToast {

    private static final String TAG = "SuperToast";

    private static final String ERROR_CONTEXTNULL = " - You cannot use a null context.";
    private static final String ERROR_DURATIONTOOLONG = " - You should NEVER specify a duration greater than " + "four and a half seconds for a SuperToast.";

    /**
     * Custom OnClickListener to be used with SuperActivityToasts/SuperCardToasts. Note that
     * SuperActivityToasts/SuperCardToasts must use this with an
     * {@link SuperToast.OnClickListener}
     */
    public interface OnClickListener {

        void onClick(View view);

    }

    /**
     * Custom OnDismissListener to be used with any type of SuperToasts. Note that
     * SuperActivityToasts/SuperCardToasts must use this with an
     * {@link SuperToast.OnClickListener}
     */
    public interface OnDismissListener {

        void onDismiss(View view);

    }

    /**
     * Backgrounds for all types of SuperToasts.
     */
    public static class Background {

        public static final int BLACK = Style.getBackground(Style.BLACK);
        public static final int BLUE = Style.getBackground(Style.BLUE);
        public static final int GRAY = Style.getBackground(Style.GRAY);
        public static final int GREEN = Style.getBackground(Style.GREEN);
        public static final int ORANGE = Style.getBackground(Style.ORANGE);
        public static final int PURPLE = Style.getBackground(Style.PURPLE);
        public static final int RED = Style.getBackground(Style.RED);
        public static final int WHITE = Style.getBackground(Style.WHITE);
        public static final int YELLOW = Style.getBackground(Style.YELLOW);

    }

    /**
     * Animations for all types of SuperToasts.
     */
    public enum Animations {

        FADE,
        FLYIN,
        SCALE,
        POPUP,
        PULL

    }

    /**
     * Icons for all types of SuperToasts.
     */
    public static class Icon {

        /**
         * Icons for all types of SuperToasts with a light background.
         */
        public static class Color {

//            public static final int RED = (R.drawable.mark_red);
//            public static final int GREEN = (R.drawable.mark_green);
//            public static final int YELLOW = (R.drawable.mark_yellow);

        }

        public static class Resource {

        	public static final int YES = (R.drawable.mark_yes);
        	public static final int ERROR = (R.drawable.mark_no);
        	public static final int WARNING = (R.drawable.mark_warning);
        	public static final int INFO = (R.drawable.mark_info);
        	public static final int UPLOAD = (R.drawable.iconfont_upload_white);

        }

    }

    /**
     * Durations for all types of SuperToasts.
     */
    public static class Duration {

        public static final int VERY_SHORT = (1500);
        public static final int SHORT = (2000);
        public static final int MEDIUM = (2750);
        public static final int LONG = (3500);
        public static final int EXTRA_LONG = (4500);

    }

    /**
     * Text sizes for all types of SuperToasts.
     */
    public static class TextSize {

        public static final int EXTRA_SMALL = (12);
        public static final int SMALL = (14);
        public static final int MEDIUM = (16);
        public static final int LARGE = (18);

    }

    /**
     * Positions for icons used in all types of SuperToasts.
     */
    public enum IconPosition {

        /**
         * Set the icon to the left of the text.
         */
        LEFT,

        /**
         * Set the icon to the right of the text.
         */
        RIGHT,

        /**
         * Set the icon on top of the text.
         */
        TOP,

        /**
         * Set the icon on the bottom of the text.
         */
        BOTTOM

    }

    private Animations mAnimations = Animations.PULL;
    private Context mContext;
    private int mGravity = Gravity.TOP;// Gravity.RIGHT | Gravity.TOP;
    private int mDuration = Duration.SHORT;
    private int mTypefaceStyle;
    private int mBackground;
    private int mXOffset = 0;
    private int mYOffset = 0;
    private LinearLayout mRootLayout;
    private OnDismissListener mOnDismissListener;
    private TextView mMessageTextView;
    private View mToastView;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowManagerParams;

    /**
     * Instantiates a new {@value #TAG}.
     *
     * @param context {@link Context}
     */
    public SuperToast(final Context context) {

        if (context == null) {

            throw new IllegalArgumentException(TAG + ERROR_CONTEXTNULL);

        }

        this.mContext = context;

        mYOffset = context.getResources().getDimensionPixelSize(
                R.dimen.toast_hover);

        final LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mToastView = layoutInflater.inflate(R.layout.supertoast, null);

        mWindowManager = (WindowManager) mToastView.getContext()
                .getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

        mRootLayout = (LinearLayout)mToastView.findViewById(R.id.root_layout);

        mMessageTextView = (TextView)
                mToastView.findViewById(R.id.message_textview);

        ViewTreeObserver vto = mMessageTextView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mMessageTextView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                LinearLayout.LayoutParams vBgLp = (LinearLayout.LayoutParams) mMessageTextView.getLayoutParams();
                vBgLp.width = ScreenUtils.getScreenWidth(context);
                mMessageTextView.setLayoutParams(vBgLp);
            }
        });

    }

    /**
     * Instantiates a new {@value #TAG} with a specified style.
     *
     * @param context {@link Context}
     * @param style   {@link Style}
     */
    public SuperToast(Context context, Style style) {

        if (context == null) {

            throw new IllegalArgumentException(TAG + ERROR_CONTEXTNULL);

        }

        this.mContext = context;

        mYOffset = context.getResources().getDimensionPixelSize(
                R.dimen.toast_hover);

        final LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mToastView = layoutInflater.inflate(R.layout.supertoast, null);

        mWindowManager = (WindowManager) mToastView.getContext()
                .getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

        mRootLayout = (LinearLayout)
                mToastView.findViewById(R.id.root_layout);

        mMessageTextView = (TextView)
                mToastView.findViewById(R.id.message_textview);

        this.setStyle(style);

    }

    /**
     * Shows the {@value #TAG}. If another {@value #TAG} is showing than
     * this one will be added to a queue and shown when the previous {@value #TAG}
     * is dismissed.
     */
    public void show() {

        mWindowManagerParams = new WindowManager.LayoutParams();

        mWindowManagerParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowManagerParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowManagerParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        mWindowManagerParams.format = PixelFormat.TRANSLUCENT;
        mWindowManagerParams.windowAnimations = getAnimation();
        mWindowManagerParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mWindowManagerParams.gravity = mGravity;
        mWindowManagerParams.x = mXOffset;
        mWindowManagerParams.y = mYOffset;

        ManagerSuperToast.getInstance().add(this);

    }

    /**
     * Sets the message text of the {@value #TAG}.
     *
     * @param text {@link CharSequence}
     */
    public void setText(CharSequence text) {

        mMessageTextView.setText(text);

    }

    /**
     * Returns the message text of the {@value #TAG}.
     *
     * @return {@link CharSequence}
     */
    public CharSequence getText() {

        return mMessageTextView.getText();

    }

    /**
     * Sets the message typeface style of the {@value #TAG}.
     *
     * @param typeface {@link android.graphics.Typeface} int
     */
    public void setTypefaceStyle(int typeface) {

        mTypefaceStyle = typeface;

        mMessageTextView.setTypeface(mMessageTextView.getTypeface(), typeface);

    }

    /**
     * Returns the message typeface style of the {@value #TAG}.
     *
     * @return {@link android.graphics.Typeface} int
     */
    public int getTypefaceStyle() {

        return mTypefaceStyle;

    }

    /**
     * Sets the message text color of the {@value #TAG}.
     *
     * @param textColor {@link android.graphics.Color}
     */
    public void setTextColor(int textColor) {

        mMessageTextView.setTextColor(textColor);

    }

    /**
     * Returns the message text color of the {@value #TAG}.
     *
     * @return int
     */
    public int getTextColor() {

        return mMessageTextView.getCurrentTextColor();

    }

    /**
     * Sets the text size of the {@value #TAG} message.
     *
     * @param textSize int
     */
    public void setTextSize(int textSize) {

        mMessageTextView.setTextSize(textSize);

    }

    /**
     * Returns the text size of the {@value #TAG} message in pixels.
     *
     * @return float
     */
    public float getTextSize() {

        return mMessageTextView.getTextSize();

    }

    /**
     * Sets the duration that the {@value #TAG} will show.
     *
     * @param duration {@link SuperToast.Duration}
     */
    public SuperToast setDuration(int duration) {
    	// :::
//        if(duration > Duration.EXTRA_LONG) {
//
//            Log.e(TAG, TAG + ERROR_DURATIONTOOLONG);
//
//            this.mDuration = Duration.EXTRA_LONG;
//
//        } else {

            this.mDuration = duration;

//        }

           return this;

    }

    /**
     * Returns the duration of the {@value #TAG}.
     *
     * @return int
     */
    public int getDuration() {

        return this.mDuration;

    }

    /**
     * Sets an icon resource to the {@value #TAG} with a specified position.
     *
     * @param iconResource {@link SuperToast.Icon}
     * @param iconPosition {@link SuperToast.IconPosition}
     */
    public SuperToast setIcon(int iconResource, IconPosition iconPosition) {

        if (iconPosition == IconPosition.BOTTOM) {
            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, mContext.getResources().getDrawable(iconResource));

        } else if (iconPosition == IconPosition.LEFT) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources()
                    .getDrawable(iconResource), null, null, null);

        } else if (iconPosition == IconPosition.RIGHT) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null, null,mContext.getResources().getDrawable(iconResource), null);

        } else if (iconPosition == IconPosition.TOP) {

            mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null,mContext.getResources().getDrawable(iconResource), null, null);

        }

        return this;
    }

    /**
     * Sets the background resource of the {@value #TAG}.
     *
     * @param background {@link SuperToast.Background}
     */
    public SuperToast setBackground(int background) {

        this.mBackground = background;

        mRootLayout.setBackgroundResource(background);

        return this;
    }

    /**
     * Returns the background resource of the {@value #TAG}.
     *
     * @return int
     */
    public int getBackground() {

        return this.mBackground;

    }

    /**
     * Sets the gravity of the {@value #TAG} along with x and y offsets.
     *
     * @param gravity {@link Gravity} int
     * @param xOffset int
     * @param yOffset int
     */
    public void setGravity(int gravity, int xOffset, int yOffset) {

        this.mGravity = gravity;
        this.mXOffset = xOffset;
        this.mYOffset = yOffset;

    }

    /**
     * Sets the show/hide animations of the {@value #TAG}.
     *
     * @param animations {@link SuperToast.Animations}
     */
    public void setAnimations(Animations animations) {

        this.mAnimations = animations;

    }

    /**
     * Returns the show/hide animations of the {@value #TAG}.
     *
     * @return {@link SuperToast.Animations}
     */
    public Animations getAnimations() {

        return this.mAnimations;

    }

    /**
     * Sets an OnDismissListener defined in this library
     * to the {@value #TAG}. Does not require wrapper.
     *
     * 在onDismiss()最后调用 SuperToast.superToast.setOnDismissListener(null);清除取消状态
     *
     * @param onDismissListener {@link SuperToast.OnDismissListener}
     */
    public SuperToast setOnDismissListener(OnDismissListener onDismissListener) {

        this.mOnDismissListener = onDismissListener;

        return this;

    }

    /**
     * Returns the OnDismissListener set to the {@value #TAG}.
     *
     * @return {@link SuperToast.OnDismissListener}
     */
    public OnDismissListener getOnDismissListener() {

        return mOnDismissListener;

    }

    /**
     * Dismisses the {@value #TAG}.
     */
    public void dismiss() {

        ManagerSuperToast.getInstance().removeSuperToast(this);

    }

    /**
     * Returns the {@value #TAG} message textview.
     *
     * @return {@link TextView}
     */
    public TextView getTextView() {

        return mMessageTextView;

    }

    /**
     * Returns the {@value #TAG} view.
     *
     * @return {@link View}
     */
    public View getView() {

        return mToastView;

    }

    /**
     * Returns true if the {@value #TAG} is showing.
     *
     * @return boolean
     */
    public boolean isShowing() {

        return mToastView != null && mToastView.isShown();

    }

    /**
     * Returns the window manager that the {@value #TAG} is attached to.
     *
     * @return {@link WindowManager}
     */
    public WindowManager getWindowManager() {

        return mWindowManager;

    }

    /**
     * Returns the window manager layout params of the {@value #TAG}.
     *
     * @return {@link WindowManager.LayoutParams}
     */
    public WindowManager.LayoutParams getWindowManagerParams() {

        return mWindowManagerParams;

    }

    /**
     * Private method used to return a specific animation for a animations enum
     */
    private int getAnimation() {

        if (mAnimations == Animations.FLYIN) {

            return android.R.style.Animation_Translucent;

        } else if (mAnimations == Animations.SCALE) {

            return android.R.style.Animation_Dialog;

        } else if (mAnimations == Animations.POPUP) {

            return android.R.style.Animation_InputMethod;

        } else if (mAnimations == Animations.PULL) {

            return R.style.Animation_Pull;

        } else {

            return android.R.style.Animation_Toast;

        }

    }

    /**
     * Private method used to set a default style to the {@value #TAG}
     */
    private void setStyle(Style style) {

        this.setAnimations(style.animations);
        this.setTypefaceStyle(style.typefaceStyle);
        this.setTextColor(style.textColor);
        this.setBackground(style.background);

    }

    public static SuperToast superToast;

    /**
     * Returns a standard {@value #TAG}.
     *
     * @param context          {@link Context}
     * @param textCharSequence {@link CharSequence}
     * @param durationInteger  {@link SuperToast.Duration}
     *
     * @return {@link SuperToast}
     */
    public static SuperToast makeText(Context context, CharSequence textCharSequence,
                                      int durationInteger) {
		if (superToast == null)
			superToast = new SuperToast(context);
		else
			cancelAllSuperToasts();

        superToast.setText(textCharSequence);
        superToast.setDuration(durationInteger);

        return superToast;

    }

    /**
     * Returns a standard {@value #TAG} with specified animations.
     *
     * @param context          {@link Context}
     * @param textCharSequence {@link CharSequence}
     * @param durationInteger  {@link SuperToast.Duration}
     * @param animations       {@link SuperToast.Animations}
     *
     * @return {@link SuperToast}
     */
    public static SuperToast makeText(Context context, CharSequence textCharSequence,
                                      int durationInteger, Animations animations) {

		if (superToast == null)
			superToast = new SuperToast(context);
		else
			cancelAllSuperToasts();

        superToast.setText(textCharSequence);
        superToast.setDuration(durationInteger);
        superToast.setAnimations(animations);

        return superToast;

    }

    /**
     * Returns a {@value #TAG} with a specified style.
     *
     * @param context          {@link Context}
     * @param textCharSequence {@link CharSequence}
     * @param durationInteger  {@link SuperToast.Duration}
     * @param style            {@link Style}
     *
     * @return SuperCardToast
     */
    public static SuperToast makeText(Context context, CharSequence textCharSequence, int durationInteger, Style style) {

    	if (superToast == null)
			superToast = new SuperToast(context);
		else
			cancelAllSuperToasts();

        superToast.setText(textCharSequence);
        superToast.setDuration(durationInteger);
        superToast.setStyle(style);

        return superToast;

    }

    /**
     * Dismisses and removes all showing/pending {@value #TAG}.
     */
    public static void cancelAllSuperToasts() {

        ManagerSuperToast.getInstance().cancelAllSuperToasts();

    }

    /**
     * @param context
     * @param textCharSequence text
     * @param icon  SuperToast.Icon.Color.GREEN
    			SuperToast.Icon.Resource.YES
     * @param background SuperToast.Background.ORANGE
     * @return
     */
    public static SuperToast makeText(Context context, CharSequence textCharSequence, int icon, int background){
		return SuperToast
				.makeText(context, textCharSequence, Duration.SHORT)
				.setIcon(icon, IconPosition.LEFT)
				.setBackground(background);
    }
    /*


		// warning
		SuperToast.makeText(context,context.getResources().getString(R.string.response_code_not_networkavailable_0),
						SuperToast.Icon.Resource.WARNING,
						SuperToast.Background.YELLOW).show();
						
		SuperToast.makeText(getActivity(),getString(R.string.fragment_sgin_ce_phone_hint),
					SuperToast.Icon.Resource.WARNING,
					SuperToast.Background.YELLOW).show();

		// yes
		SuperToast.makeText(context,context.getResources().getString(R.string.response_code_unauthorized_401),
					SuperToast.Icon.Resource.YES,
					SuperToast.Background.GREEN).show();
	
		SuperToast.makeText(this,getString(R.string.setting_cache_success),
					SuperToast.Icon.Resource.YES,
					SuperToast.Background.GREEN).show();
	
		// error
		SuperToast.makeText(context,context.getResources().getString(R.string.response_code_forbidden_403),
					SuperToast.Icon.Resource.ERROR,
					SuperToast.Background.RED).show();
	
		SuperToast.makeText(getActivity(),getString(R.string.fragment_sgin_tv_graph_identifying_code_fail),
					SuperToast.Icon.Resource.ERROR,
					SuperToast.Background.RED).show();

		// info
		SuperToast.makeText(context,context.getResources().getString(R.string.response_code_unauthorized_401),
					SuperToast.Icon.Resource.INFO,
					SuperToast.Background.BLUE).show();
					
		SuperToast.makeText(getActivity(),getString(R.string.response_code_signup_success_unauthorized),
					SuperToast.Icon.Resource.INFO,
					SuperToast.Background.BLUE).show();

     * 
     */

}


