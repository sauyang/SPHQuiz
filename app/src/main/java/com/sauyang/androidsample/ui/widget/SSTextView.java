package com.sauyang.androidsample.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatTextView;

import com.sauyang.androidsample.R;
import com.sauyang.androidsample.utils.Utils;

import java.lang.ref.WeakReference;

public class SSTextView extends AppCompatTextView {

    public interface SSTextViewStateChangedListener{
        public void stageChanged(SSTextView textView);
    }

    private WeakReference<SSTextViewStateChangedListener> stateChangedListener = null;
    public void setStateChangedListener(SSTextViewStateChangedListener stageChangedListener) {
        this.stateChangedListener = new WeakReference<>(stageChangedListener);
    }

    String customFont = null;

    public WeakReference<SSEditTextListener> listener = null;

    public SSTextView(Context context) {
        super(context);
    }

    public SSTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SSTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /******* FONT METHODS ******/

    private void init()
    {
        if (getHint() != null && getHint().toString() != null)
            setCustomHintText(getHint().toString());
    }


    private void setCustomFont(Context context, AttributeSet attrs)
    {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SSTextView);
        String customFont = a.getString(R.styleable.SSTextView_customFont);
        setCustomFont(context, customFont);
        a.recycle();
    }

    public boolean setCustomFont(Context context, String asset)
    {
        //Edit mode used to stop IDE from executing this code
        if(isInEditMode()) {
            return false;
        }

        if(asset == null || asset.equalsIgnoreCase("null")){
            return false;
        }

        Typeface tf = Utils.getCachedFont(context, "fonts/" + asset + ".ttf");

        if( tf == null ) {
            return false;
        }

        if (getTypeface() != null) {
            setTypeface(tf, getTypeface().getStyle());
        }
        else {
            setTypeface(tf);
        }
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

    @SuppressWarnings("ResourceType")
    public void makeStarsRed()
    {
        String colour = getResources().getString(R.color.general_theme_color);

        if (colour == null)
            return;

        //clip alpha value
        if (colour.length() == 9)
            colour = "#" + colour.substring(3);
        else if (colour.length() != 7)
            return;

        //make stars red
        String html = getText().toString().replace("*", "<font color=\"" + colour + "\">*</font>");
        //make new line work. can also use &nbsp
        html = html.replace("\n","<br>");
        setText(Html.fromHtml(html));
    }

    /******* ALPHA METHODS ******/

    @Override
    public boolean onSetAlpha(int alpha)
    {
        setTextColor(getTextColors().withAlpha(alpha));
        setHintTextColor(getHintTextColors().withAlpha(alpha));
        setLinkTextColor(getLinkTextColors().withAlpha(alpha));
        return true;
    }

    /******* COLOUR METHODS *****/

    public void setColourForAmount(Number amount)
    {
        if(amount == null)
            setColourForAmount(0);
        else
            setColourForAmount(amount.doubleValue());
    }
    public void setColourForAmount(double amount)
    {
        setTextColor(getResources().getColor((amount < 0) ? R.color.text_negative_number_color : R.color.text_black_color));
    }


    /******* CLEARABLE METHODS ******/

    final int DRAWABLE_LEFT = 0;
    final int DRAWABLE_TOP = 1;
    final int DRAWABLE_RIGHT = 2;
    final int DRAWABLE_BOTTOM = 3;

    private Drawable drawableRight;
    private boolean drawableRightVisible = false;
    public boolean clearable = false;

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

        setClearButtonVisible(text.length() > 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(isEnabled())
            if (event.getAction() == MotionEvent.ACTION_UP)
            {
                if (clearable && drawableRightVisible  && event.getRawX() >= (getRight() - drawableRight.getBounds().width() - 20))
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

        if(listener != null && listener.get() != null)
            listener.get().onRightCompoundPressed();
    }

    public void setClearButtonVisible(boolean visible)
    {
        drawableRightVisible = visible;
        final Drawable[] drawables = getCompoundDrawables();
        Drawable show = visible ? drawableRight : null ;
        super.setCompoundDrawables(drawables[DRAWABLE_LEFT], drawables[DRAWABLE_TOP], show, drawables[DRAWABLE_BOTTOM]);
    }

    public void setClearable (boolean flag){
        clearable = flag;
    }
    public void setTextWithImages(String text, int... imageRes){

        SpannableString span = new SpannableString(text);

        for(int i = 0; i < imageRes.length; i++){

            String find = "%"+(i+1); //place holders in string look like %1 %2 etc

            int start = text.indexOf(find);
            int end = start + find.length();

            span.setSpan(new ImageSpan(getContext(), imageRes[i]), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        this.setText(span);
    }

    public void setTextWithDrawableImages(String text, Drawable... imageRes){

        SpannableString ss = new SpannableString(text);

        for(int i = 0; i < imageRes.length; i++){
            String find = "%"+(i+1); //place holders in string look like %1 %2 etc

            int start = text.indexOf(find);
            int end = start + find.length();

            imageRes[i].setBounds(0, 0, imageRes[i].getIntrinsicWidth(), imageRes[i].getIntrinsicHeight());
            ImageSpan span = new ImageSpan(imageRes[i], ImageSpan.ALIGN_BASELINE);
            ss.setSpan(span, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        this.setText(ss);
    }

    public void setStateChanged(){
        setSelected(!isSelected());

        if (isSelected()) {
            setBackgroundResource(R.drawable.button_positive);
        } else {
            setBackgroundResource(R.color.light_grey);
        }

        if (stateChangedListener != null && stateChangedListener.get() != null){
            stateChangedListener.get().stageChanged(SSTextView.this);
        }
    }

}
