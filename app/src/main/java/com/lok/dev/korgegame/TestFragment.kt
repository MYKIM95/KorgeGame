package com.lok.dev.korgegame

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lok.dev.korgegame.databinding.FragmentTestBinding
import com.lok.dev.korgegame.scene.SceneCatDog
import com.lok.dev.korgegame.scene.SceneDraw

class TestFragment : Fragment() {

    private lateinit var binding : FragmentTestBinding

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

        when(arguments?.getString("type") ?: "draw") {
            "draw" -> {
                binding.testGameView.loadModule(CustomModule(width = 2000, height = 2000, kClass = SceneDraw::class, callback = {
                    Log.d("123123123", "Callback from android app")
                }))
            }
            "catDog" -> {
                binding.testGameView.loadModule(CustomModule(width = 2000, height = 2000, kClass = SceneCatDog::class, callback = {
                    Log.d("123123123", "Callback from android app")
                }))
            }

        }

    }



}