package hbs.com.linememo.ui.core

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import hbs.com.linememo.R
import hbs.com.linememo.databinding.ActivityMainBinding

abstract class BaseActivity : AppCompatActivity(){
    fun initToolbar(toolbar: Toolbar, title:String) {
        setSupportActionBar(toolbar)
        with(supportActionBar) {
            this?.title = title
            this?.setDisplayShowHomeEnabled(true)
            this?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}