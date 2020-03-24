package com.sauyang.androidsample.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import androidx.appcompat.widget.AppCompatEditText;
import com.sauyang.androidsample.R;
import com.sauyang.androidsample.utils.Utils;
import java.lang.ref.WeakReference;

public class SSEditText extends AppCompatEditText {

    public interface SSEditTextBackButtonPressedListener{
        public void backButtonPressed(SSEditText editText);
    }
    String customFont = null;

    public SSEditTextListener listener = null;
    private String title = "";
    private WeakReference<SSEditTextBackButtonPressedListener> backButtonPressedListener = null;
    private boolean shouldShowClearButton = true;

    //This is main to turn off the clear button, the other is not main remain existing.
    private boolean shouldShowClearButtonMain = true;

    public void setBackButtonPressedListener(SSEditTextBackButtonPressedListener backBtnPressedListener) {
        this.backButtonPressedListener = new WeakReference<SSEditTextBackButtonPressedListener>(backBtnPressedListener);
    }

    public SSEditText(Context context) {
        super(context);
    }

    public SSEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SSEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /***SET FONT METHOD **/

    private void init(AttributeSet attrs)
    {
        if(attrs != null){
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SSEditTextAttr);
            shouldShowClearButtonMain = typedArray.getBoolean(R.styleable.SSEditTextAttr_editTextShowClear, shouldShowClearButtonMain);
            typedArray.recycle();
        }

        if (getHint() != null && getHint().toString() != null)
            setCustomHintText(getHint().toString());
        // clear button should be invisible at init
        setClearButtonVisible(false);

    }


    private void setCustomFont(Context context, AttributeSet attrs)
    {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SSEditText);
        customFont = a.getString(R.styleable.SSEditText_customFont);
        setCustomFont(context, customFont);
        a.recycle();
    }

    public boolean setCustomFont(Context context, String asset)
    {
        //Edit mode used to stop IDE from executing this code
        if(isInEditMode()) return false;

        Typeface tf = Utils.getCachedFont(context, "fonts/" + asset + ".ttf");

        if(tf==null)

            return false;

        if (getTypeface() != null)setTypeface(tf, getTypeface().getStyle());
        else setTypeface(tf);
        return true;
    }

    /******* TEXT MANIPULATION METHODS ******/


    @SuppressWarnings("ResourceType")
    public void setCustomHintText(String text)
    {
        if (text.endsWith("*"))
        {
            String colour = getResources().getString(R.color.general_theme_color);

            if (colour == null)
                return;

            //clip alpha value
            if (colour.length() == 9)
                colour = "#" + colour.substring(3);
            else if (colour.length() != 7)
                return;

            setHint(Html.fromHtml(text.substring(0, text.length() - 1) + "<font color=\"" + colour
                    + "\">*</font>"));
        }
    }

    /***SET ALPHA****/

    @Override
    public boolean onSetAlpha(int alpha)
    {
        setTextColor(getTextColors().withAlpha(alpha));
        setHintTextColor(getHintTextColors().withAlpha(alpha));
        setLinkTextColor(getLinkTextColors().withAlpha(alpha));
        return true;
    }

    /******* CLEARABLE METHODS ******/

    final int DRAWABLE_LEFT = 0;
    final int DRAWABLE_TOP = 1;
    final int DRAWABLE_RIGHT = 2;
    final int DRAWABLE_BOTTOM = 3;

    private Drawable drawableRight;
    private boolean drawableRightVisible = false;
    private boolean alwaysShowRightButton = false;
    private boolean clearable;

    public void setAlwaysShowRightButton(boolean show) {
        this.alwaysShowRightButton = show;
    }

    @Override
    public void setCompoundDrawables(Drawable left, Drawable top,
                                     Drawable right, Drawable bottom)
    {
        drawableRight = right;
        clearable = (right != null);

        super.setCompoundDrawables(left, top, right, bottom);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        if(shouldShowClearButton&&!alwaysShowRightButton)
            setClearButtonVisible(text.length() > 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(isEnabled())
            if (event.getAction() == MotionEvent.ACTION_UP)
            {
                //Extending the right edge of 20 width beyond the cancel icon to better capture the intention to clear text
                if (clearable && drawableRightVisible  && event.getX() >= (getWidth() - drawableRight.getBounds().width() - 20))
                {
                    onRightCompoundPressed(event);
                    return true;
                }
            }
        return super.onTouchEvent(event);
    }

    protected void onRightCompoundPressed(MotionEvent event)
    {
        //clear Text
        setText(null);

        //Request focus for avoiding arrow keep appear on every edittext if clicked
        requestFocus();

        if(listener != null)
            listener.onRightCompoundPressed();
    }

    public void setClearButtonVisible(boolean visible)
    {
        drawableRightVisible = visible;
        final Drawable[] drawables = getCompoundDrawables();

        Drawable show = null;
        if(visible && shouldShowClearButtonMain){
            show = drawableRight;
        }else
            show = null;

        super.setCompoundDrawables(drawables[DRAWABLE_LEFT], drawables[DRAWABLE_TOP], show, drawables[DRAWABLE_BOTTOM]);
    }

    public void setClearButtonNeverVisible(boolean visible){
        shouldShowClearButton = !visible;
        setClearButtonVisible(shouldShowClearButton);
    }

    public void setMaxLength(int count){
        setFilters(new InputFilter[] {new InputFilter.LengthFilter(count)});
    }


    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (backButtonPressedListener != null && backButtonPressedListener.get() != null){
                backButtonPressedListener.get().backButtonPressed(SSEditText.this);
            }
        }
        return super.dispatchKeyEventPreIme(event);
    }
}
