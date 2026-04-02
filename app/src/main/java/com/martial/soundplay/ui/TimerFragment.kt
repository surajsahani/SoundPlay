package com.martial.soundplay.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.martial.soundplay.R
import com.martial.soundplay.audio.AudioPlayerManager
import com.martial.soundplay.databinding.FragmentTimerBinding

class TimerFragment : Fragment() {

    private var _binding: FragmentTimerBinding? = null
    private val binding get() = _binding!!
    private var selectedMinutes = 10
    private var isTimerRunning = false
    private val audioManager = AudioPlayerManager.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateTimerDisplay(selectedMinutes * 60L * 1000L)

        binding.btn5min.setOnClickListener { selectDuration(5) }
        binding.btn10min.setOnClickListener { selectDuration(10) }
        binding.btn20min.setOnClickListener { selectDuration(20) }
        binding.btn30min.setOnClickListener { selectDuration(30) }

        binding.btnTimerToggle.setOnClickListener {
            if (isTimerRunning) {
                audioManager.cancelTimer()
                isTimerRunning = false
                binding.btnTimerToggle.text = "Start Timer"
                updateTimerDisplay(selectedMinutes * 60L * 1000L)
            } else {
                audioManager.setTimer(selectedMinutes)
                isTimerRunning = true
                binding.btnTimerToggle.text = "Stop Timer"
            }
        }

        audioManager.timerRemaining.observe(viewLifecycleOwner) { remaining ->
            if (remaining > 0) {
                updateTimerDisplay(remaining)
            } else if (isTimerRunning) {
                isTimerRunning = false
                binding.btnTimerToggle.text = "Start Timer"
                updateTimerDisplay(selectedMinutes * 60L * 1000L)
            }
        }
    }

    private fun selectDuration(minutes: Int) {
        if (isTimerRunning) return
        selectedMinutes = minutes
        updateTimerDisplay(minutes * 60L * 1000L)
    }

    private fun updateTimerDisplay(millis: Long) {
        val totalSeconds = millis / 1000
        val min = totalSeconds / 60
        val sec = totalSeconds % 60
        binding.timerDisplay.text = String.format("%d:%02d", min, sec)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
