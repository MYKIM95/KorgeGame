package com.lok.dev.korgegame

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.lok.dev.korgegame.databinding.ActivityMainBinding
import com.soywiz.korge.android.KorgeAndroidView

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var korgeAndroidView : KorgeAndroidView

    private var testFragment: TestFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        korgeAndroidView = KorgeAndroidView(this)

        binding.addDraw.setOnClickListener {
            testFragment = TestFragment().apply {
                arguments = bundleOf("type" to "draw")
            }
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.testContainer, testFragment!!)
            transaction.commitAllowingStateLoss()
        }

        binding.addCatDog.setOnClickListener {
            testFragment = TestFragment().apply {
                arguments = bundleOf("type" to "catDog")
            }
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.testContainer, testFragment!!)
            transaction.commitAllowingStateLoss()
        }

    }

    override fun onBackPressed() {
        if(testFragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.remove(testFragment!!)
            transaction.commitAllowingStateLoss()
            testFragment = null
        }else {
            finish()
        }
    }

}
