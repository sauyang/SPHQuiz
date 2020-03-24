package com.sauyang.androidsample.module.register

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.sauyang.androidsample.R
import com.sauyang.androidsample.module.graph.GraphDataActivity
import com.sauyang.androidsample.ui.SampleBaseActivity
import kotlinx.android.synthetic.main.activity_mainmenu.*

class MainMenuActivity : SampleBaseActivity(),View.OnClickListener{
    override fun contentViewID(): Int {
        return R.layout.activity_mainmenu
    }

    override fun populateLayout(v: View?) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        tvGotoMobileData.setOnClickListener(this)
        tvGoRegisteredList.setOnClickListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {

            android.R.id.home ->{
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu_recipe, menu)

        return true
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tvGoRegisteredList -> {

                val intent = Intent(this, RegisterListActivity::class.java)
                startActivity(intent)
            }

            R.id.tvGotoMobileData -> {
                val intent = Intent(this, GraphDataActivity::class.java)
                startActivity(intent)
            }
        }

    }
}