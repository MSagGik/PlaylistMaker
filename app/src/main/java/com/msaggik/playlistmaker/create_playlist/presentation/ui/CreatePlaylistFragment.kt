package com.msaggik.playlistmaker.create_playlist.presentation.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.msaggik.playlistmaker.databinding.FragmentCreatePlaylistBinding


class CreatePlaylistFragment : Fragment() {

    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding: FragmentCreatePlaylistBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.setDecorFitsSystemWindows(false)
            binding.root.setOnApplyWindowInsetsListener { _, insets ->
                val top = insets.getInsets(WindowInsets.Type.statusBars()).top
                val heightIme = insets.getInsets(WindowInsets.Type.ime()).bottom
                val bottom = if (heightIme != 0) heightIme else insets.getInsets(WindowInsets.Type.navigationBars()).bottom
                binding.root.setPadding(0, top, 0, bottom)
                insets
            }
        } else {
            requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nameTrackInput.addTextChangedListener(inputSearchWatcher)
    }

    private val inputSearchWatcher = object : TextWatcher {
        override fun beforeTextChanged(oldText: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(inputText: CharSequence?, p1: Int, p2: Int, p3: Int) {
            binding.addPlaylist.isEnabled = !inputText.isNullOrEmpty()
        }

        override fun afterTextChanged(resultText: Editable?) {
            binding.addPlaylist.isEnabled = resultText.toString().isNotEmpty()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        inputSearchWatcher.let { binding.nameTrackInput.removeTextChangedListener(it) }
        _binding = null
    }
}