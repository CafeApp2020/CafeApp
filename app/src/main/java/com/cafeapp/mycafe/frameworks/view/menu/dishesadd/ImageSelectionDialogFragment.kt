package com.cafeapp.mycafe.frameworks.view.menu.dishesadd

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.cafeapp.mycafe.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_add_photo.*
import kotlinx.android.synthetic.main.fragment_dishesadd.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ImageSelectionDialogFragment : BottomSheetDialogFragment() {
    companion object {
        fun newInstance() = ImageSelectionDialogFragment()

        private const val REQUEST_CODE_CAMERA = 100
        private const val REQUEST_CODE_GALLERY = 200
        private const val REQUEST_CODE_DELETE_IMAGE = 300
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_add_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        get_from_gallery_textView.setOnClickListener { selectImageFromGallery() }
        get_from_camera_textView.setOnClickListener { takePhotoFromCamera() }
        delete_image_textView.setOnClickListener { deleteImage() }
    }

    //метод выбора изображения с фотокамеры
    private fun takePhotoFromCamera() {
        this.dismiss()
        dispatchTakePictureIntent()
    }

    //метод выбора изображения из галереи
    private fun selectImageFromGallery() {
        this.dismiss()
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        parentFragment?.startActivityForResult(intent, REQUEST_CODE_GALLERY)
    }

    private fun deleteImage() {
        this.dismiss()
        parentFragment?.onActivityResult(REQUEST_CODE_DELETE_IMAGE, AppCompatActivity.RESULT_OK,null)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        //mCurrentPhotoPath = image.absolutePath
        return image
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        if (activity?.packageManager?.let { takePictureIntent.resolveActivity(it) } != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                Log.d("tag", "dispatchTakePictureIntent: Error!")
            }
            if (photoFile != null) {
                val photoURI = context?.let {
                    FileProvider.getUriForFile(
                        it,
                        "com.cafeapp.mycafe.provider",
                        photoFile
                    )
                }
                parentFragment?.fragment_dish_image_imageview?.tag = photoURI.toString()
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                parentFragment?.startActivityForResult(takePictureIntent,REQUEST_CODE_CAMERA)
            }
        }
    }
}