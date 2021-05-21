package com.andersenlab.andersen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.andersenlab.andersen.fragment.ContactsListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.contacts, ContactsListFragment.newInstance())
                .commitNow()
        }
    }
}