package hbs.com.linememo.ui.core

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import hbs.com.linememo.R
import hbs.com.linememo.util.ResourceKeys
import io.reactivex.disposables.CompositeDisposable
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

interface DataSender {
    fun sendImage(imageUri: String)
}

abstract class BaseActivity : AppCompatActivity() {
    lateinit var dataSender: DataSender
    lateinit var currentPhotoPath: String
    val compositeDisposable = CompositeDisposable()

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    fun initToolbar(toolbar: Toolbar, title: String, isUseHomeButton: Boolean = false) {
        setSupportActionBar(toolbar)
        with(supportActionBar) {
            this?.title = title
            this?.setDisplayShowHomeEnabled(isUseHomeButton)
            this?.setDisplayHomeAsUpEnabled(isUseHomeButton)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ResourceKeys.TAKE_CAMERA && resultCode == Activity.RESULT_OK
        ) {
            dataSender.sendImage(currentPhotoPath)
            notifyNewPictureForBroadcast()
        } else if (requestCode == ResourceKeys.PICK_GALLERY && resultCode == Activity.RESULT_OK) {
            data?.dataString?.let { dataSender.sendImage(it) }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun checkPermissions(permissions: Array<String>, permissionCode: Int) {
        if (
        // 인자에 해당하는 권한이 있는지를 리턴
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 권한 팝업에 한번이라도 거부를 했을 경우 true
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // WRITE에 대한 설명이 필요한 경우
                showToast(R.string.all_text_not_permission)
            }
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                // CAMERA에 대한 설명이 필요한 경우
                showToast(R.string.all_text_not_permission)
            }
            // 권한 팝업을 요철하는 메소드
            requestPermissions(permissions, permissionCode)
        } else {
            requestPermissions(permissions, permissionCode)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == ResourceKeys.CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty()) {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED
                ) {
                    takeCameraIntent()
                }
            }
        } else if (requestCode == ResourceKeys.STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                pickGalleryIntent()
            }
        }
    }

    fun showToast(toastStringRef: Int) {
        Toast.makeText(this, getString(toastStringRef), Toast.LENGTH_SHORT).show()
    }

    private fun takeCameraIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }

                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "hbs.com.linememo.provider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, ResourceKeys.TAKE_CAMERA)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val isExistDir = storageDir?.exists() ?: false
        if (!isExistDir) {
            storageDir?.mkdirs()
        }
        Log.d("storageDir", storageDir?.path)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    fun showSelectionThumbnailDialog() {
        AlertDialog
            .Builder(this)
            .setTitle(R.string.thumbnail_selection_title)
            .setItems(R.array.thumbnail_selection_items)
            { dialogInterface, position ->
                if (position == 0) {
                    checkPermissions(
                        ResourceKeys.CAMERA_PERMISSIONS,
                        ResourceKeys.CAMERA_PERMISSION_CODE
                    )
                } else if (position == 1) {
                    checkPermissions(
                        ResourceKeys.STORAGE_PERMISSIONS,
                        ResourceKeys.STORAGE_PERMISSION_CODE
                    )
                }
            }.show()
    }

    private fun notifyNewPictureForBroadcast() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val newPhotoFile = File(currentPhotoPath)
            mediaScanIntent.data = Uri.fromFile(newPhotoFile)
            sendBroadcast(mediaScanIntent)
        }
    }

    private fun pickGalleryIntent() {
        val pictureActionIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pictureActionIntent, ResourceKeys.PICK_GALLERY);
    }
}