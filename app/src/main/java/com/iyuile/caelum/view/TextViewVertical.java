package com.iyuile.caelum.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.iyuile.caelum.R;
import com.iyuile.caelum.utils.Log;
import com.iyuile.caelum.utils.PixelUtil;

/**
 * 垂直文字
 * <p/>
 * {@links https://github.com/liufsd/TextViewVertical}
 * Created by WangYao on 2016/12/5.
 */
public class TextViewVertical extends View {

    public static final int LAYOUT_CHANGED = 1;
    private Paint paint;
    private int mTextPosx = 0;// x坐标
    private int mTextPosy = 0;// y坐标
    private int mTextWidth = 0;// 绘制宽度
    private int mTextHeight = 0;// 绘制高度
    private int mFontHeight = 0;// 绘制字体高度
    private float mFontSize = 24;// 字体大小
    private int mRealLine = 0;// 字符串真实的行数
    private int mLineWidth = 0;//列宽度
    private int mLineCount = 0;//列数
    private int mLimitLineCount = 0;//限制行数
    private int TextLength = 0;//字符串长度
    private int oldwidth = 0;//存储久的width
    private String text = "";//待显示的文字
    private Handler mHandler = null;
    private Matrix matrix;
    private Align textStartAlign = Align.RIGHT;//draw start left or right.//default right
    BitmapDrawable drawable = (BitmapDrawable) getBackground();

    public TextViewVertical(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TextViewVertical(Context context, AttributeSet attrs) {
        super(context, attrs);
        matrix = new Matrix();
        paint = new Paint();//新建画笔
        paint.setTextAlign(Align.CENTER);//文字居中
        paint.setAntiAlias(true);//平滑处理
        paint.setColor(Color.BLACK);//默认文字颜色

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextViewVertical);

            mFontSize = a.getDimension(R.styleable.TextViewVertical_textSize_wy, PixelUtil.sp2px(16f));
        }
    }

    /*
    //获取整数值
    private final int getAttributeIntValue(AttributeSet attrs,String field) {
    	int intVal = 0;
    	//TODO
    	//应该可以直接用attrs.getAttributeIntValue()获取对应的数值的，
    	//但不知道为什么一直无法获得只好临时写个函数凑合着用,没有写完整，暂时只支持px作为单位，其它单位的转换有空再写
    	String tempText=attrs.getAttributeValue(androidns, field);
    	intVal = (int)Math.ceil(Float.parseFloat(tempText.replaceAll("px","")));
		return intVal;
    }*/
    //设置文字
    public final void setText(String text) {
        this.text = text;
        this.TextLength = text.length();
        if (mTextHeight > 0) GetTextInfo();
    }

    /**
     *  限制行数
     *
     * @param mLimitLineCount
     */
    public final void setLimitLineCount(int  mLimitLineCount) {
        this.mLimitLineCount = mLimitLineCount;
        if (mLimitLineCount > 0) GetTextInfo();
    }

    /**
     * //设置文字位置
     *
     * @param align
     */
    public final void setTextAlign(Align align) {
        paint.setTextAlign(align);
    }

    //设置字体大小
    public final void setTextSize(float size) {
        if (size != paint.getTextSize()) {
            mFontSize = size;
            if (mTextHeight > 0) GetTextInfo();
        }
    }

    //设置字体颜色
    public final void setTextColor(int color) {
        paint.setColor(color);
    }

    //设置字体颜色
    public final void setTextARGB(int a, int r, int g, int b) {
        paint.setARGB(a, r, g, b);
    }

    //设置字体
    public void setTypeface(Typeface tf) {
        if (this.paint.getTypeface() != tf) {
            this.paint.setTypeface(tf);
        }
    }

    //设置行宽
    public void setLineWidth(int LineWidth) {
        mLineWidth = LineWidth;
    }

    //获取实际宽度
    public int getTextWidth() {
        return mTextWidth;
    }

    //设置Handler，用以发送事件
    public void setHandler(Handler handler) {
        mHandler = handler;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.v("TextViewVertical", "onDraw");
        if (drawable != null) {
            //画背景
            Bitmap b = Bitmap.createBitmap(drawable.getBitmap(), 0, 0, mTextWidth, mTextHeight);
            canvas.drawBitmap(b, matrix, paint);
        }
        //画字
        draw(canvas, this.text);
    }

    private void draw(Canvas canvas, String thetext) {
        char ch;
        mTextPosy = 0;//初始化y坐标
        mTextPosx = textStartAlign == Align.LEFT ? mLineWidth : mTextWidth - mLineWidth;//初始化x坐标
        for (int i = 0; i < this.TextLength; i++) {
            ch = thetext.charAt(i);
            if (ch == '\n') {
                if (textStartAlign == Align.LEFT) {
                    mTextPosx += mLineWidth;// 换列
                } else {
                    mTextPosx -= mLineWidth;// 换列
                }
                mLineCount++;
                mTextPosy = 0;
            } else {
                mTextPosy += mFontHeight;
                if (mTextPosy > this.mTextHeight) {
                    if (textStartAlign == Align.LEFT) {
                        mTextPosx += mLineWidth;// 换列
                    } else {
                        mTextPosx -= mLineWidth;// 换列
                    }
                    mLineCount++;
                    i--;
                    mTextPosy = 0;
                } else {
                    if (mLimitLineCount == 0 || mLineCount < mLimitLineCount) {
                        canvas.drawText(String.valueOf(ch), mTextPosx, mTextPosy, paint);
                    } else {
                        break;
                    }

                }
            }
        }

        //调用接口方法
        //activity.getHandler().sendEmptyMessage(TestFontActivity.UPDATE);
    }

    //计算文字行数和总宽
    private void GetTextInfo() {
        Log.v("TextViewVertical", "GetTextInfo");
        char ch;
        int h = 0;
        paint.setTextSize(mFontSize);
        //获得字宽
        if (mLineWidth == 0) {
            float[] widths = new float[1];
            paint.getTextWidths("正", widths);//获取单个汉字的宽度
            mLineWidth = (int) Math.ceil(widths[0] * 1.1 + 2);
        }

        FontMetrics fm = paint.getFontMetrics();
        mFontHeight = (int) (Math.ceil(fm.descent - fm.top) * 0.9);// 获得字体高度

        //计算文字行数
        mRealLine = 0;
        for (int i = 0; i < this.TextLength; i++) {
            if (mLimitLineCount == 0 || mRealLine < mLimitLineCount) {
                ch = this.text.charAt(i);
                if (ch == '\n') {
                    mRealLine++;// 真实的行数加一
                    h = 0;
                } else {
                    h += mFontHeight;
                    if (h > this.mTextHeight) {
                        mRealLine++;// 真实的行数加一
                        i--;
                        h = 0;
                    } else {
                        if (i == this.TextLength - 1) {
                            mRealLine++;// 真实的行数加一
                        }
                    }
                }
            } else {
                break;
            }
        }
        mRealLine++;//额外增加一行
        mTextWidth = mLineWidth * mRealLine;//计算文字总宽度
        measure(mTextWidth, getHeight());//重新调整大小
        layout(getLeft(), getTop(), getLeft() + mTextWidth, getBottom());//重新绘制容器
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredHeight = measureHeight(heightMeasureSpec);
        //int measuredWidth = measureWidth(widthMeasureSpec);
        if (mTextWidth == 0) GetTextInfo();
        setMeasuredDimension(mTextWidth, measuredHeight);
        if (oldwidth != getWidth()) {//
            oldwidth = getWidth();
            if (mHandler != null) mHandler.sendEmptyMessage(LAYOUT_CHANGED);
        }
    }

    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = 500;
        if (specMode == MeasureSpec.AT_MOST) {
            result = specSize;
        } else if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        mTextHeight = result;//设置文本高度
        return result;
    }
    /*
    private int measureWidth(int measureSpec) {
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		int result = 500;
		if (specMode == MeasureSpec.AT_MOST){
			result = specSize;
		}else if (specMode == MeasureSpec.EXACTLY){
			result = specSize;
		}
		return result;
	}  */
}