package com.msaggik.playlistmaker.create_playlist.presentation.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.msaggik.playlistmaker.R
import com.msaggik.playlistmaker.create_playlist.domain.models.Playlist
import com.msaggik.playlistmaker.create_playlist.domain.models.Track
import com.msaggik.playlistmaker.create_playlist.presentation.view_model.CreatePlaylistViewModel
import com.msaggik.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.msaggik.playlistmaker.util.Utils
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class CreatePlaylistFragment : Fragment() {

    companion object {

        private const val TRACK_INSTANCE = "track_instance"
        private var hasCreateArgs = false

        fun createArgs(track: Track): Bundle {
            hasCreateArgs = true
            return bundleOf(
                TRACK_INSTANCE to track
            )
        }

    }

    // track
    private val track by lazy {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(TRACK_INSTANCE, Track::class.java)
        } else {
            requireArguments().getParcelable(TRACK_INSTANCE)
        }
    }

    private val createPlaylistViewModel: CreatePlaylistViewModel by viewModel()

    private val namesPlaylist: MutableList<String> = mutableListOf()

    private lateinit var defaultUriImageToPrivateStorage: Uri
    private var uriImageToPrivateStorage: Uri? = null
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    private val backAddPlaylistDialog: MaterialAlertDialogBuilder by lazy {
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle(requireActivity().getString(R.string.finish_creating_the_playlist))
            .setMessage(requireActivity().getString(R.string.all_unsaved_data_will_be_lost))
            .setNeutralButton(requireActivity().getString(R.string.cancel)) { dialog, which ->
            }.setPositiveButton("Завершить") { dialog, which ->
                findNavController().popBackStack()
            }
    }

    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding: FragmentCreatePlaylistBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        defaultUriImageToPrivateStorage = Uri.parse("android.resource://${requireActivity().packageName}/${R.drawable.ic_placeholder}")

        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                binding.albumTrack.setImageURI(uri)
                uriImageToPrivateStorage = saveImageToPrivateStorage(uri)
            } else {
                binding.albumTrack.setImageURI(defaultUriImageToPrivateStorage)
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

        binding.nameTrackInput.addTextChangedListener(inputSearchWatcher)
        binding.buttonBack.setOnClickListener(listener)
        binding.albumTrack.setOnClickListener(listener)
        binding.addPlaylist.setOnClickListener(listener)

        requireActivity().onBackPressedDispatcher.addCallback(object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                backAddPlaylistDialog.show()
            }
        })
    }

    private fun saveImageToPrivateStorage(uri: Uri) : Uri {
        // read image
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        // create file
        val filePath = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlist_covers")
        if (!filePath.exists()) filePath.mkdirs() // read/create directory
        val fileName = Utils.dateFormatNameAlbumPlaylist(System.currentTimeMillis())
        val file = File(filePath, fileName)
        val outputStream = FileOutputStream(file)
        val privateUri = file.toUri()
        // save image
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return privateUri
    }

    private val listener: View.OnClickListener = object : View.OnClickListener {
        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(p0: View?) {
            when (p0?.id) {
                R.id.button_back -> {
                    if(uriImageToPrivateStorage != null && (binding.nameTrackInput.text.isNullOrEmpty() || binding.descriptionTrackInput.text.isNullOrEmpty())) {
                        backAddPlaylistDialog.show()
                    } else {
                        findNavController().popBackStack()
                    }
                }

                R.id.album_track -> {
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
                R.id.add_playlist -> {
                    val playlist = Playlist(
                        playlistName = binding.nameTrackInput.text.toString(),
                        playlistDescription = binding.descriptionTrackInput.text.toString(),
                        playlistUriAlbum = uriImageToPrivateStorage.toString()
                    )
                    if(hasCreateArgs) {
                        createPlaylistViewModel.addTrackInPlaylist(playlist, track!!)
                    } else {
                        createPlaylistViewModel.addPlaylist(playlist)
                    }
                    findNavController().popBackStack()
                    messageSuccessAddPlaylist(binding.nameTrackInput.text.toString())
                }
            }
        }
    }

    private fun messageSuccessAddPlaylist(namePlaylist: String) {
        Toast.makeText(requireActivity(), requireActivity().getString(R.string.message_success_add_playlist, namePlaylist), Toast.LENGTH_LONG).show()

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
                binding.nameTrack.error = requireActivity().getString(R.string.has_duplicate_playlist)
                false
            }
            else -> {
                binding.nameTrack.isErrorEnabled = false
                false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        pickMedia.unregister()
        inputSearchWatcher.let { binding.nameTrackInput.removeTextChangedListener(it) }
        _binding = null
    }
}