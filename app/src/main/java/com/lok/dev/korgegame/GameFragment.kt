package com.lok.dev.korgegame

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lok.dev.korgegame.databinding.FragmentGameBinding
import com.lok.dev.korgegame.scene.SceneCatDog
import com.lok.dev.korgegame.scene.SceneCatDogStart

class GameFragment : Fragment() {

    private lateinit var binding : FragmentGameBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        when(arguments?.getString("type") ?: "catDog") {
            "catDog" -> {
                binding.gameView.loadModule(CustomModule(width = 2000, height = 2000, kClass = SceneCatDogStart::class, callback = {
                    Log.d("123123123", "Callback from android app")
                }))
            }
        }
    }


}