package com.lok.dev.korgegame

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lok.dev.korgegame.databinding.FragmentTestBinding
import com.soywiz.korge.android.KorgeAndroidView
import com.soywiz.korgw.AndroidGameWindowNoActivity

class TestFragment : Fragment() {

    private lateinit var binding : FragmentTestBinding

    private lateinit var gameWindow : AndroidGameWindowNoActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        binding.testGameView.loadModule(CustomModule(width = 100, height = 100, callback = {
            Log.d("123123123", "Callback from android app")
        }))

        /*gameWindow = AndroidGameWindowNoActivity(

        )*/

    }



}