package com.rizky.bengkelin.ui.analysis

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.rizky.bengkelin.R
import com.rizky.bengkelin.databinding.FragmentAnalysisBinding
import com.rizky.bengkelin.utils.createCustomTempFile
import com.rizky.bengkelin.utils.resizeImageFile
import java.io.File

class AnalysisFragment : Fragment() {

    private var _binding: FragmentAnalysisBinding? = null
    private val binding get() = _binding!!

    private lateinit var currentPhotoPath: String
    private var getFile: File? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalysisBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnCamera.setOnClickListener { startCamera() }
            btnGallery.setOnClickListener { openGallery() }
            btnAnalyze.setOnClickListener { startAnalyze() }
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            cropImage(myFile.toUri())
        }
    }

    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(requireActivity().packageManager)

        createCustomTempFile(requireActivity().application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                requireActivity(), "com.rizky.bengkelin", it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            cropImage(selectedImg)
        }
    }

    private fun openGallery() {
        val intent = Intent().apply {
            action = ACTION_GET_CONTENT
            type = "image/*"
        }
        val chooser = Intent.createChooser(intent, getString(R.string.select_image))
        launcherIntentGallery.launch(chooser)
    }

    private val launcherCropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            val path = result.getUriFilePath(requireContext()) as String
            val file = File(path)
            getFile = file.resizeImageFile(256, 256).also {
                val bitmap = BitmapFactory.decodeFile(it.path)
                binding.ivPreview.setImageBitmap(bitmap)
                Toast.makeText(
                    requireActivity(),
                    "Image resized to ${bitmap.width} x ${bitmap.height}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun cropImage(uri: Uri) {
        launcherCropImage.launch(
            CropImageContractOptions(
                uri, CropImageOptions(
                    fixAspectRatio = true,
                )
            )
        )
    }

    private fun startAnalyze() {
        Toast.makeText(requireActivity(), "Comingsoon", Toast.LENGTH_SHORT).show()
    }

}