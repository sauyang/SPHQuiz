package com.sauyang.androidsample.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.sauyang.androidsample.R
import com.sauyang.androidsample.module.graph.GraphDataActivity
import com.sauyang.androidsample.ui.widget.SSTextView
import com.sauyang.webservices.utils.SUtils.getMobileDataJSON
import kotlinx.android.synthetic.main.base_toolbar.*
import kotlinx.android.synthetic.main.sample_base_activity.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

abstract class SampleBaseActivity :BaseActivity() {

    protected abstract fun contentViewID() :Int
    protected abstract fun populateLayout(v :View?)
    protected open var isShowToolBar = true
        get() = llToolbar != null && llToolbar!!.visibility == View.VISIBLE

    private var llToolbar: LinearLayout? = null
    protected var flContainer: FrameLayout? = null
    private var flFloat: FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        populateData(savedInstanceState)
        super.setContentView(R.layout.sample_base_activity)

        llToolbar = findViewById(R.id.llToolbar)
        flContainer = findViewById(R.id.flContainer)
        flFloat = findViewById(R.id.flFloat)
        Log.i("HAHAMHAM", "ABCDEF")
        showTitle("")
        hideToolbarIcon1()
        hideToolbarIcon2()
        //Make Title centre more accurate
//        reformatMiddle()
        inflateToParent()
//        screenSizeHandle()
    }


    open fun populateData(savedInstanceState: Bundle?){

    }

    private fun inflateToParent(){
        flContainer?.removeAllViews()

        val inflater = LayoutInflater.from(this@SampleBaseActivity)
        Log.i("SeeUI", contentViewID().toString())
        if (contentViewID() == -1) {
            return
        }
        val view = inflater.inflate(contentViewID(), null)
        if (isScrollView()) {
            val scrollView = ScrollView(this)
            scrollView.addView(view)
            val flParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
            scrollView.layoutParams = flParams
            flContainer!!.addView(scrollView)
            Log.i("SeeUI", "Added")
        } else {
            flContainer!!.addView(view)
        }

        populateLayout(view)
    }

    open fun isOverScreenSize(): Boolean {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        return width > height
    }

    open fun screenSizeHandle(isPhoneView: Boolean = isOverScreenSize()) {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels

        var ratio = height / width.toDouble()


        if (isPhoneView) {
            val params = flContainer?.layoutParams as LinearLayout.LayoutParams
            params.height = height
//            params.width = (ratio * height).toInt()
            if ((ratio * height).toInt() > width / 2) {
                params.width = (ratio * height).toInt()
            } else {
                params.width = width / 2
            }

            params.gravity = Gravity.CENTER
            flContainer?.layoutParams = params

            if (llToolbar?.visibility == View.VISIBLE) {
                val paramsToolbar = toolbarBase?.layoutParams as LinearLayout.LayoutParams
                if ((ratio * height).toInt() > width / 2) {
                    paramsToolbar.width = (ratio * height).toInt()
                } else {
                    paramsToolbar.width = width / 2
                }
                paramsToolbar.gravity = Gravity.CENTER
                toolbarBase?.layoutParams = paramsToolbar
            }
        }

    }

    open fun isScrollView(): Boolean {
        return false
    }

    open fun setBackgoundDrawable(drawable: Int) {
        rlParent.background = ContextCompat.getDrawable(this, drawable)
    }

    open fun showToolbar(isShow: Boolean) {

        if (isShow) {
            llToolbar?.visibility = View.VISIBLE
        } else {
            llToolbar?.visibility = View.GONE
        }
    }

    open fun showTitle(title: String) {
        isShowToolBar = title.isNotEmpty()

        if (isShowToolBar) {
            val tvTitle: SSTextView = llToolbar!!.findViewById(R.id.tvTitle)
            val ivTitleIcon = llToolbar!!.findViewById<ImageView>(R.id.ivTitleIcon)
            if (title.isNotEmpty()) {
                tvTitle.visibility = View.VISIBLE
                ivTitleIcon.visibility = View.GONE
                tvTitle.text = title
            } else {
                tvTitle.visibility = View.GONE
                ivTitleIcon.visibility = View.VISIBLE
            }
        }
    }

    open fun hideToolbarIcon2() {
        showToolbarIcon2(null)
    }

    open fun showToolbarIcon2(onClickListener: View.OnClickListener?) {
        if (onClickListener != null) {
            llToolbar!!.findViewById<View>(R.id.ivIcon2).visibility = View.VISIBLE
            llToolbar!!.findViewById<View>(R.id.ivIcon2).setOnClickListener(onClickListener)
        } else {
            llToolbar!!.findViewById<View>(R.id.ivIcon2).visibility = View.GONE
            llToolbar!!.findViewById<View>(R.id.ivIcon2).setOnClickListener(null)
        }
    }

    open fun hideToolbarIcon1(){
        showToolbarIcon1(null)
    }

    open fun showToolbarIcon1(onClickListener: View.OnClickListener?){
        if(onClickListener != null){
            llToolbar!!.findViewById<View>(R.id.ivIcon1).visibility = View.VISIBLE
            llToolbar!!.findViewById<View>(R.id.ivIcon1).setOnClickListener(onClickListener)
        }else{
            llToolbar!!.findViewById<View>(R.id.ivIcon1).visibility = View.GONE
            llToolbar!!.findViewById<View>(R.id.ivIcon1).setOnClickListener(null)
        }
    }

    open fun addFragment(fragment : Fragment){
        addFragment(fragment,true)
    }

    open fun addFragment(fragment : Fragment, addToBackStack:Boolean){
        addFragment(fragment,addToBackStack,null)
    }

    open fun addFragment(fragment:Fragment, addToBackStack : Boolean, backStackID : String?){
        val containerId = R.id.flFloat

        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        val currentFragmentInContainer = fm.findFragmentById(containerId)
        if (currentFragmentInContainer != null) { //added to test for saveInstanceState
//            saveAllFragmentState(currentFragmentInContainer)
            ft.detach(currentFragmentInContainer)
        }
        if (addToBackStack) {
            ft.addToBackStack(backStackID)
        }
        ft.replace(containerId, fragment).attach(fragment).commitAllowingStateLoss()
    }

    open fun fragmentShow(boolean: Boolean) {
        if (boolean) {
            flFloat?.visibility = View.VISIBLE
        } else {
            flFloat?.visibility = View.GONE
        }
    }

    private fun reformatMiddle() {
        if (rlMiddle.visibility != View.VISIBLE) {
            return
        }
        var backCountSize = 0
        var frontCountSize = 0
        var titleCount = 0
        for (i in 0 until llToolbar!!.childCount) {
            val childAt = llToolbar?.getChildAt(i)!!

            if (childAt === rlMiddle) {
                titleCount = i
                break
            }

            if (childAt.visibility != View.VISIBLE) {
                continue
            }

            var sizeSum = 0
            val layoutParams = childAt.layoutParams as LinearLayout.LayoutParams
            sizeSum += layoutParams.width
            sizeSum += (layoutParams.leftMargin + layoutParams.rightMargin)

            sizeSum += (childAt.paddingStart)
            sizeSum += (childAt.paddingEnd)
            sizeSum += (childAt.paddingLeft)
            sizeSum += (childAt.paddingRight)

            frontCountSize += sizeSum
        }

        for (i in titleCount until llToolbar!!.childCount) {

            val childAt = llToolbar?.getChildAt(i)!!
            if (childAt.visibility != View.VISIBLE) {
                continue
            }
            var sizeSum = 0
            val layoutParams = childAt.layoutParams as LinearLayout.LayoutParams
            sizeSum += layoutParams.width
            sizeSum += (layoutParams.leftMargin + layoutParams.rightMargin)

            sizeSum += (childAt.paddingStart)
            sizeSum += (childAt.paddingEnd)
            sizeSum += (childAt.paddingLeft)
            sizeSum += (childAt.paddingRight)

            backCountSize += sizeSum
        }
        val layoutParams = rlMiddle.layoutParams as LinearLayout.LayoutParams

        if (frontCountSize > backCountSize) {
            layoutParams.setMargins(0, 0, frontCountSize - backCountSize, 0)
        } else {
            layoutParams.setMargins(backCountSize - frontCountSize, 0, 0, 0)
        }
    }


    open fun showBack(onClickListener: View.OnClickListener? = View.OnClickListener { finish() }) {
        showToolbar(true)
        if (isShowToolBar) {
            val ivBack = llToolbar!!.findViewById<ImageView>(R.id.ivBack)
            val ivHamburger = llToolbar!!.findViewById<ImageView>(R.id.ivHamburger)
            ivBack.visibility = View.VISIBLE
            ivHamburger.visibility = View.GONE
            ivBack.setOnClickListener(onClickListener)
            ivHamburger.setOnClickListener(null)
        }

    }

    open fun hideBack() {
        showToolbar(true)
        if (isShowToolBar) {
            val ivBack = llToolbar!!.findViewById<ImageView>(R.id.ivBack)
            val ivHamburger = llToolbar!!.findViewById<ImageView>(R.id.ivHamburger)
            ivBack.visibility = View.GONE
            ivHamburger.visibility = View.GONE
            ivBack.setOnClickListener(null)
        }

    }



    open fun hideTitle() {
        if (isShowToolBar) {
            val tvTitle: SSTextView = llToolbar!!.findViewById(R.id.tvTitle)
            val ivDbsIcon = llToolbar!!.findViewById<ImageView>(R.id.ivTitleIcon)
            tvTitle.visibility = View.GONE
            ivDbsIcon.visibility = View.GONE
        }

    }


}