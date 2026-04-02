package com.martial.soundplay.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.martial.soundplay.audio.AudioPlayerManager
import com.martial.soundplay.data.SoundRepository
import com.martial.soundplay.databinding.FragmentLibraryBinding
import com.martial.soundplay.MainActivity

class LibraryFragment : Fragment() {

    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!
    private lateinit var soundAdapter: LibrarySoundAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        soundAdapter = LibrarySoundAdapter(SoundRepository.sounds) { sound ->
            AudioPlayerManager.getInstance().play(requireContext(), sound)
            (activity as? MainActivity)?.showMiniPlayer()
            startActivity(Intent(requireContext(), PlayerActivity::class.java))
        }

        binding.libraryList.layoutManager = LinearLayoutManager(requireContext())
        binding.libraryList.adapter = soundAdapter

        val chipAdapter = CategoryChipAdapter(SoundRepository.categories) { category ->
            val filtered = if (category == "All") SoundRepository.sounds
            else SoundRepository.getByCategory(category)
            soundAdapter.updateList(filtered)
        }
        binding.categoryChips.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.categoryChips.adapter = chipAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
