package com.martial.soundplay.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.martial.soundplay.audio.AudioPlayerManager
import com.martial.soundplay.data.SoundRepository
import com.martial.soundplay.databinding.FragmentSanctuaryBinding
import com.martial.soundplay.MainActivity
import java.util.Calendar

class SanctuaryFragment : Fragment() {

    private var _binding: FragmentSanctuaryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSanctuaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Dynamic greeting
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        binding.greeting.text = when {
            hour < 12 -> "Good Morning"
            hour < 17 -> "Good Afternoon"
            else -> "Good Evening"
        }

        // Sound grid
        val adapter = SoundGridAdapter(SoundRepository.sounds) { sound ->
            AudioPlayerManager.getInstance().play(requireContext(), sound)
            (activity as? MainActivity)?.showMiniPlayer()
            startActivity(Intent(requireContext(), PlayerActivity::class.java))
        }
        val spacing = (12 * resources.displayMetrics.density).toInt()
        binding.soundGrid.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.soundGrid.addItemDecoration(GridSpacingDecoration(2, spacing))
        binding.soundGrid.adapter = adapter

        // Daily ritual
        binding.btnStartSession.setOnClickListener {
            val first = SoundRepository.sounds.first()
            AudioPlayerManager.getInstance().play(requireContext(), first)
            AudioPlayerManager.getInstance().setTimer(10)
            (activity as? MainActivity)?.showMiniPlayer()
            startActivity(Intent(requireContext(), PlayerActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
