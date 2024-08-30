package com.msaggik.playlistmaker.create_playlist.presentation.ui

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.msaggik.playlistmaker.R
import com.msaggik.playlistmaker.create_playlist.domain.models.Playlist
import com.msaggik.playlistmaker.create_playlist.domain.models.Track
import com.msaggik.playlistmaker.create_playlist.presentation.view_model.CreatePlaylistViewModel
import com.msaggik.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.msaggik.playlistmaker.util.Utils
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePlaylistFragment : Fragment() {

    companion object {
        private const val TRACK_INSTANCE = "track_instance"
        private const val TRACK_IS_INPUT = "track_is_input"
        fun createArgs(isInputTrack: Boolean, track: Track? = null): Bundle {
            return if(isInputTrack) {
                bundleOf(
                    TRACK_IS_INPUT to isInputTrack,
                    TRACK_INSTANCE to track
                )
            } else {
                return bundleOf(
                    TRACK_IS_INPUT to isInputTrack
                )
            }
        }
    }

    // view-model
    private val createPlaylistViewModel: CreatePlaylistViewModel by viewModel()

    // view
    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding: FragmentCreatePlaylistBinding get() = _binding!!

    // track
    private val track by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(TRACK_INSTANCE, Track::class.java)
        } else {
            requireArguments().getParcelable(TRACK_INSTANCE)
        }
    }

    // date playlist
    private val namesPlaylist: MutableList<String> = mutableListOf()
    private var uriImageToPrivateStorage: Uri? = null

    // parameter for selecting a picture
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    // parameter for dialog window
    private val backAddPlaylistDialog: MaterialAlertDialogBuilder by lazy {
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle(requireActivity().getString(R.string.finish_creating_the_playlist))
            .setMessage(requireActivity().getString(R.string.all_unsaved_data_will_be_lost))
            .setNeutralButton(requireActivity().getString(R.string.cancel)) { dialog, which -> }
            .setPositiveButton(requireActivity().getString(R.string.finish)) { dialog, which ->
                findNavController().popBackStack()
            }
    }

    // fragment lifecycle methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createPlaylistViewModel.isInputTrack = requireArguments().getBoolean(TRACK_IS_INPUT)

        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                createPlaylistViewModel.saveImageToPrivateStorage(uri)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createPlaylistViewModel.getNamesPlaylist()

        createPlaylistViewModel.getNamesPlaylistLiveData().observe(viewLifecycleOwner) {
            namesPlaylist.clear()
            namesPlaylist.addAll(it)
        }

        createPlaylistViewModel.getSuccessAddTrackInPlaylistLiveData()
            .observe(viewLifecycleOwner) { values ->
                val (idPlaylist, namePlaylist) = values
                val hasSuccessAddTrack = idPlaylist != -1L
                val message = requireContext()
                    .getString(
                        if (hasSuccessAddTrack) R.string.add_track_in_playlist else R.string.no_add_track_in_playlist,
                        namePlaylist
                    )
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }

        createPlaylistViewModel.getUriImageToPrivateStorageLiveData().observe(viewLifecycleOwner) {
            Glide.with(binding.root)
                .load(it)
                .placeholder(R.drawable.ic_placeholder)
                .transform(CenterCrop(), RoundedCorners(Utils.doToPx(8f, binding.root.context)))
                .transform()
                .into(binding.albumPlaylist)
            uriImageToPrivateStorage = it
        }

        binding.nameTrackInput.addTextChangedListener(inputSearchWatcher)
        binding.buttonBack.setOnClickListener(listener)
        binding.albumPlaylist.setOnClickListener(listener)
        binding.addPlaylist.setOnClickListener(listener)

        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (uriImageToPrivateStorage != null || !binding.nameTrackInput.text.isNullOrEmpty() || !binding.descriptionTrackInput.text.isNullOrEmpty()) {
                    backAddPlaylistDialog.show()
                } else {
                    findNavController().popBackStack()
                }
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        onBackPressedCallback.isEnabled = false
        onBackPressedCallback.remove()
        pickMedia.unregister()
        inputSearchWatcher.let { binding.nameTrackInput.removeTextChangedListener(it) }
        _binding = null
    }

    private val listener: View.OnClickListener = object : View.OnClickListener {
        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(p0: View?) {
            when (p0?.id) {
                R.id.button_back -> {
                    if (uriImageToPrivateStorage != null || !binding.nameTrackInput.text.isNullOrEmpty() || !binding.descriptionTrackInput.text.isNullOrEmpty()) {
                        backAddPlaylistDialog.show()
                    } else {
                        findNavController().popBackStack()
                    }
                }

                R.id.album_playlist -> {
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }

                R.id.add_playlist -> {
                    val playlist = Playlist(
                        playlistName = binding.nameTrackInput.text.toString(),
                        playlistDescription = binding.descriptionTrackInput.text.toString(),
                        playlistUriAlbum = uriImageToPrivateStorage.toString()
                    )
                    if (createPlaylistViewModel.isInputTrack) {
                        createPlaylistViewModel.addTrackInPlaylist(playlist, track!!)
                    } else {
                        createPlaylistViewModel.addPlaylist(playlist)
                        findNavController().popBackStack()
                    }
                    messageSuccessAddPlaylist(binding.nameTrackInput.text.toString())
                }
            }
        }
    }

    private fun messageSuccessAddPlaylist(namePlaylist: String) {
        Toast.makeText(
            requireActivity(),
            requireActivity().getString(R.string.message_success_add_playlist, namePlaylist),
            Toast.LENGTH_SHORT
        ).show()
    }

    private val inputSearchWatcher = object : TextWatcher {
        override fun beforeTextChanged(oldText: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(inputText: CharSequence?, p1: Int, p2: Int, p3: Int) {
            stateButtonAddPlaylist(inputText.toString())
        }

        override fun afterTextChanged(resultText: Editable?) {
            stateButtonAddPlaylist(resultText.toString())
        }
    }

    private fun stateButtonAddPlaylist(namePlaylist: String) {
        val hasNamesPlaylist = namesPlaylist.contains(namePlaylist)
        binding.addPlaylist.isEnabled = when {
            !hasNamesPlaylist && !namePlaylist.isNullOrEmpty() -> {
                binding.nameTrack.isErrorEnabled = false
                true
            }

            hasNamesPlaylist -> {
                binding.nameTrack.error =
                    requireActivity().getString(R.string.has_duplicate_playlist)
                false
            }

            else -> {
                binding.nameTrack.isErrorEnabled = false
                false
            }
        }
    }
}