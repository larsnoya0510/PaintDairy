package com.example.paintdairy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class DateActivity : AppCompatActivity() {
    lateinit var fragmentPool : FragmentPool
    var mDate=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date)
        mDate= intent.getStringExtra("Date") ?: ""
        fragmentPool = FragmentPool()
        initStartFragment()
    }

    private fun initStartFragment() {
        val homeFragment = supportFragmentManager.findFragmentByTag("DrawListFragment")
        if (null == homeFragment) {
            var mTargetFragment = fragmentPool.mDrawListFragment
            val transaction = this.supportFragmentManager.beginTransaction()
            transaction.replace(
                R.id.fragmentContainer,
                fragmentPool.mDrawListFragment,
                "DrawListFragment"
            )
            transaction.commit()
        } else {
            val transaction = this.supportFragmentManager.beginTransaction()
            transaction.replace(
                R.id.fragmentContainer,
                homeFragment,
                "DrawListFragment"
            )
            transaction.commit()
        }
    }
}
