package com.lok.dev.korgegame

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.lok.dev.korgegame.databinding.ActivityMainBinding
import com.soywiz.korge.android.KorgeAndroidView

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var korgeAndroidView : KorgeAndroidView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        korgeAndroidView = KorgeAndroidView(this)
        binding.toolContainer.addView(korgeAndroidView)

        binding.loadViewButton.setOnClickListener {
            loadToolModule()
        }

        binding.unloadViewButton.setOnClickListener {
            unloadToolModule()
        }

        binding.addFragment.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.testContainer, TestFragment())
            transaction.commitAllowingStateLoss()
        }

    }

    override fun onPause() {
        super.onPause()
        unloadToolModule()
    }

    private fun loadToolModule() {
        korgeAndroidView.loadModule(CustomModule(width = 1080, height = 1920, callback = {
            Log.d("123123123", "Callback from android app")
        }))
    }

    private fun unloadToolModule() {
        korgeAndroidView.unloadModule()
    }

}
