package com.roman.movieApp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.roman.movieApp.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
        setSupportActionBar(findViewById(R.id.toolbar))
    }

    fun openFragment(
        fragment: Fragment, name: String = fragment.javaClass.name, addToBackStack: Boolean = true,
        transition: Int = FragmentTransaction.TRANSIT_FRAGMENT_OPEN
    ) {
        val transaction = supportFragmentManager.beginTransaction().add(R.id.container, fragment, fragment.javaClass.name)
        if (addToBackStack) {
            transaction.addToBackStack(name)
        }
        transaction.setTransition(transition).commit()
    }
}