package com.blueventor.menu.driver.driverdetailsdashboard

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.blueventor.R
import com.blueventor.databinding.FragmentDriverPersonalInfoBinding
import com.blueventor.session.SessionManager
import com.blueventor.util.loadImage
import com.blueventor.util.setOnclick
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@AndroidEntryPoint
class DriverProofInfoFragment : Fragment() {
    private val binding get() = _binding!!
    var driver_licence = ""
    var driver_licence_back_side = ""
    var taxi_image = ""
    var vehicle_registration_license = ""
    private var selfieBitmap: Bitmap? = null
    private lateinit var selfieImageView: ImageView
    private lateinit var selfieDialog: AlertDialog
    private lateinit var selectedImageUri: Uri
    private lateinit var currentPhotoPath: String

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            selectedImageUri = it
            binding.image1.setImageURI(it)
        }
    }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                selectedImageUri?.let { uri ->
                    val bitmap = BitmapFactory.decodeFile(currentPhotoPath)
                    selfieBitmap = bitmap
                    selfieImageView.setImageBitmap(bitmap)
                }
            }
        }


    @Inject
    lateinit var sessionManager: SessionManager
    private var _binding: FragmentDriverPersonalInfoBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDriverPersonalInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        driver_licence = sessionManager.getString("driver_licence", "Not Found")
        driver_licence_back_side = sessionManager.getString("driver_licence_back_side", "Not Found")
        taxi_image = sessionManager.getString("taxi_image", "Not Found")
        vehicle_registration_license =
            sessionManager.getString("vehicle_registration_license", "Not Found")

        binding.image1.loadImage(
            url = driver_licence,
            placeholder = R.drawable.ic_car_placeholder,
            onSuccess = { Log.d("Glide", "Image loaded successfully!") },
            onError = { Log.e("Glide", "Failed to load image") }
        )


        binding.image2.loadImage(
            url = driver_licence_back_side,
            placeholder = R.drawable.ic_car_placeholder,
            onSuccess = { Log.d("Glide", "Image loaded successfully!") },
            onError = { Log.e("Glide", "Failed to load image") }
        )


        binding.image3.loadImage(
            url = taxi_image,
            placeholder = R.drawable.ic_car_placeholder,
            onSuccess = { Log.d("Glide", "Image loaded successfully!") },
            onError = { Log.e("Glide", "Failed to load image") }
        )


        binding.image4.loadImage(
            url = vehicle_registration_license,
            placeholder = R.drawable.ic_car_placeholder,
            onSuccess = { Log.d("Glide", "Image loaded successfully!") },
            onError = { Log.e("Glide", "Failed to load image") }
        )

        setOnclick(binding.button1)
        {
            if (selfieBitmap != null) {
                selectedImageUri?.let { uri ->
                    val imageFile = getFileFromUri(requireActivity(), uri)
                    if (imageFile != null) {
                        val compressedFile = compressImage(requireActivity(), selfieBitmap!!)


//                        uploadImage(
//                            compressedFile,
//                            SessionSave.getSession("Id", this@HomeScreenActivity)
//                        )

                    } else {
                        Toast.makeText(requireActivity(), "Unable to get image file", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireActivity(), "Please capture selfie first", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun compressImage(context: Context, bitmap: Bitmap): File {
        val compressedFile = File.createTempFile("compressed_", ".jpg", context.cacheDir)
        FileOutputStream(compressedFile).use { out ->
            // 80% quality is usually a good balance (you can tweak this 50–100)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)
        }
        return compressedFile
    }
    private fun getFileFromUri(context: Context, uri: Uri): File? {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val tempFile = File.createTempFile("upload_", ".jpg", context.cacheDir)
        inputStream.use { input ->
            FileOutputStream(tempFile).use { output ->
                input.copyTo(output)
            }
        }
        return tempFile
    }
    private fun openFrontCamera() {
        val photoFile = File.createTempFile(
            "selfie_", ".jpg",
            requireActivity().externalCacheDir
        )
        currentPhotoPath = photoFile.absolutePath

        selectedImageUri = FileProvider.getUriForFile(
            requireActivity(),
            "${requireActivity().applicationContext.packageName}.fileprovider", // Make sure it matches manifest
            photoFile
        )

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUri)
            putExtra("android.intent.extras.CAMERA_FACING", 1)
            putExtra("android.intent.extras.LENS_FACING_FRONT", 1)
            putExtra("android.intent.extra.USE_FRONT_CAMERA", true)
        }
        cameraLauncher.launch(intent)
    }
}