package com.msaggik.playlistmaker.playlist_manager.presentation.ui

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
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.msaggik.playlistmaker.R
import com.msaggik.playlistmaker.playlist_manager.domain.models.Playlist
import com.msaggik.playlistmaker.playlist_manager.domain.models.Track
import com.msaggik.playlistmaker.playlist_manager.presentation.ui.state.PlaylistManagerState
import com.msaggik.playlistmaker.playlist_manager.presentation.view_model.PlaylistManagerViewModel
import com.msaggik.playlistmaker.databinding.FragmentPlaylistManagerBinding
import com.msaggik.playlistmaker.util.Utils
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistManagerFragment : Fragment() {

    companion object {
        private const val TRACK_INSTANCE = "track_instance"
        private const val PLAYLIST_INSTANCE = "playlist_instance"

        private const val ARGS_STATE = "args_state"

        private const val CREATE_PLAYLIST_STATE = 0
        private const val CREATE_PLAYLIST_WITH_TRACK_STATE = 1
        private const val EDIT_PLAYLIST_STATE = 2

        fun createArgs(playlistManagerState: PlaylistManagerState): Bundle {
            return when (playlistManagerState) {
                is PlaylistManagerState.EmptyArg -> {
                    bundleOf(
                        ARGS_STATE to CREATE_PLAYLIST_STATE
                    )
                }

                is PlaylistManagerState.TrackArg -> {
                    bundleOf(
                        ARGS_STATE to CREATE_PLAYLIST_WITH_TRACK_STATE,
                        TRACK_INSTANCE to playlistManagerState.track
                    )
                }

                is PlaylistManagerState.EditPlaylistArg -> {
                    bundleOf(
                        ARGS_STATE to EDIT_PLAYLIST_STATE,
                        PLAYLIST_INSTANCE to playlistManagerState.playlist
                    )
                }

            }
        }
    }

    // view-model
    private val playlistManagerViewModel: PlaylistManagerViewModel by viewModel()

    // view
    private var _binding: FragmentPlaylistManagerBinding? = null
    private val binding: FragmentPlaylistManagerBinding get() = _binding!!

    // input data
    private var track: Track? = null
    private var playlist: Playlist? = null

    // data playlist
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

        playlistManagerViewModel.stateCreateOrEditPlaylist = requireArguments().getInt(ARGS_STATE)

        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                playlistManagerViewModel.saveImageToPrivateStorage(uri)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistManagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDifStateFragment()

        playlistManagerViewModel.getNamesPlaylist()

        playlistManagerViewModel.getNamesPlaylistLiveData().observe(viewLifecycleOwner) {
            namesPlaylist.clear()
            namesPlaylist.addAll(it)
        }

        playlistManagerViewModel.getUriImageToPrivateStorageLiveData().observe(viewLifecycleOwner) {
            Glide.with(binding.root)
                .load(it)
                .placeholder(R.drawable.ic_placeholder)
                .transform(CenterCrop(), RoundedCorners(Utils.doToPx(8f, binding.root.context)))
                .transform()
                .into(binding.albumPlaylist)
            uriImageToPrivateStorage = it
        }

        if (playlistManagerViewModel.stateCreateOrEditPlaylist == EDIT_PLAYLIST_STATE) {
            playlistManagerViewModel.setUriImageToPrivateStorageLiveData(playlist!!.playlistUriAlbum.toUri())
            binding.nameTrackInput.setText(playlist!!.playlistName)
            binding.descriptionTrackInput.setText(playlist!!.playlistDescription)
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
                if(playlistManagerViewModel.stateCreateOrEditPlaylist == EDIT_PLAYLIST_STATE) {
                    findNavController().popBackStack()
                } else if (uriImageToPrivateStorage != null || !binding.nameTrackInput.text.isNullOrEmpty() || !binding.descriptionTrackInput.text.isNullOrEmpty()) {
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

    private fun initDifStateFragment() {
        when (playlistManagerViewModel.stateCreateOrEditPlaylist) {
            CREATE_PLAYLIST_STATE -> {
                binding.panelHeader.text = requireContext().getString(R.string.new_playlist)
                binding.addPlaylist.text = requireContext().getString(R.string.create)

            }

            CREATE_PLAYLIST_WITH_TRACK_STATE -> {
                binding.panelHeader.text = requireContext().getString(R.string.new_playlist)
                binding.addPlaylist.text = requireContext().getString(R.string.create)

                track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requireArguments().getParcelable(TRACK_INSTANCE, Track::class.java)
                } else {
                    requireArguments().getParcelable(TRACK_INSTANCE)
                }

                playlistManagerViewModel.getSuccessAddTrackInPlaylistLiveData()
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
            }

            EDIT_PLAYLIST_STATE -> {
                binding.panelHeader.text = requireContext().getString(R.string.edit_playlist)
                binding.addPlaylist.text = requireContext().getString(R.string.save_playlist)
                binding.addPlaylist.isEnabled = true

                playlist = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requireArguments().getParcelable(PLAYLIST_INSTANCE, Playlist::class.java)
                } else {
                    requireArguments().getParcelable(PLAYLIST_INSTANCE)
                }
            }
        }
    }

    private val listener: View.OnClickListener = object : View.OnClickListener {
        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(p0: View?) {
            when (p0?.id) {
                R.id.button_back -> {
                    if(playlistManagerViewModel.stateCreateOrEditPlaylist == EDIT_PLAYLIST_STATE) {
                        findNavController().popBackStack()
                    } else if (uriImageToPrivateStorage != null || !binding.nameTrackInput.text.isNullOrEmpty() || !binding.descriptionTrackInput.text.isNullOrEmpty()) {
                        backAddPlaylistDialog.show()
                    } else {
                        findNavController().popBackStack()
                    }
                }

                R.id.album_playlist -> {
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }

                R.id.add_playlist -> {
                    val newPlaylist = Playlist(
                        playlistName = binding.nameTrackInput.text.toString(),
                        playlistDescription = binding.descriptionTrackInput.text.toString(),
                        playlistUriAlbum = uriImageToPrivateStorage.toString()
                    )
                    when (playlistManagerViewModel.stateCreateOrEditPlaylist) {
                        CREATE_PLAYLIST_STATE -> {
                            playlistManagerViewModel.addPlaylist(newPlaylist)
                            findNavController().popBackStack()
                            messageSuccessAddPlaylist(binding.nameTrackInput.text.toString())
                        }

                        CREATE_PLAYLIST_WITH_TRACK_STATE -> {
                            playlistManagerViewModel.addTrackInPlaylist(newPlaylist, track!!)
                            messageSuccessAddPlaylist(binding.nameTrackInput.text.toString())
                        }

                        EDIT_PLAYLIST_STATE -> {
                            newPlaylist.apply { playlistId = playlist!!.playlistId }
                            playlistManagerViewModel.editTrackInPlaylist(newPlaylist)
                            findNavController().popBackStack()
                            messageSuccessEditPlaylist(binding.nameTrackInput.text.toString())
                        }
                    }
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

    private fun messageSuccessEditPlaylist(namePlaylist: String) {
        Toast.makeText(
            requireActivity(),
            requireActivity().getString(R.string.message_success_edit_playlist, namePlaylist),
            Toast.LENGTH_SHORT
        ).show()
    }

    private val inputSearchWatcher = object : TextWatcher {
        override fun beforeTextChanged(oldText: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(inputText: CharSequence?, p1: Int, p2: Int, p3: Int) {
            stateButtonPlaylist(inputText.toString())
        }

        override fun afterTextChanged(resultText: Editable?) {
            stateButtonPlaylist(resultText.toString())
        }
    }

    private fun stateButtonPlaylist(namePlaylist: String) {
        val hasNamesPlaylist = namesPlaylist.contains(namePlaylist)
        binding.addPlaylist.isEnabled = when {
            playlistManagerViewModel.stateCreateOrEditPlaylist == EDIT_PLAYLIST_STATE
                    && namePlaylist == playlist!!.playlistName -> {
                binding.nameTrack.isErrorEnabled = false
                true
            }

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