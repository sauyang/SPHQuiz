package com.sauyang.androidsample.ui.widget

import android.annotation.TargetApi
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.sauyang.androidsample.R


class STYLEditText : LinearLayout {


    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs){
        init(context,attrs)
    }
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        init(context,attrs)
    }
    @TargetApi(21)
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int, defStyleRes: Int) :
            super(context, attrs, defStyleAttr, defStyleRes){
        init(context,attrs)
    }

    private var tvTitle  = TextView(context)
    private var etDescription = EditText(context)


    fun init(context: Context, attrs: AttributeSet?) {

        // set param
        orientation = VERTICAL


        var params = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )

        tvTitle.layoutParams = params
        etDescription.layoutParams = params

        addView(tvTitle)
        addView(etDescription)

        // description
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.styl_field, 0, 0)
            val title = resources.getText(typedArray.getResourceId(R.styleable.styl_field_header,R.string.app_name))
            val description = resources.getText(typedArray.getResourceId(R.styleable.styl_field_desc,R.string.emptystring))
            val isEdit = typedArray.getBoolean(R.styleable.styl_field_is_edit,true);

            tvTitle.setText(title)

            if(!isEdit){
                etDescription.setKeyListener(null);
            }
            etDescription.setText(description)


            typedArray.recycle()
        }
    }

    fun getEtDescription() : EditText {
        return etDescription
    }

    fun disableKeyListener(){
        etDescription.setKeyListener(null)
    }
    fun enableKeyListener(){
        etDescription.setKeyListener(EditText(context).getKeyListener())
    }


    fun setTitle(title : String){
        this.tvTitle.setText(title)
    }

    interface TextChanged : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {

        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }
    }

}