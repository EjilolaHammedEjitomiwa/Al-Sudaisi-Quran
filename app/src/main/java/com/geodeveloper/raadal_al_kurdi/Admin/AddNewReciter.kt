package com.geodeveloper.raadal_al_kurdi.Admin

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.Toast
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.geodeveloper.newtobetting.Notifications.*
import com.geodeveloper.raadal_al_kurdi.APIservice
import com.geodeveloper.raadal_al_kurdi.Constants
import com.geodeveloper.raadal_al_kurdi.Model.UsersModel
import com.geodeveloper.raadal_al_kurdi.R
import com.geodeveloper.raadal_al_kurdi.helper.Utils
import com.geodeveloper.raadal_al_kurdi.helper.Utils.sendNotification
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.kaopiz.kprogresshud.KProgressHUD
import com.theartofdev.edmodo.cropper.CropImage
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_add_betting_site.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class AddNewReciter : AppCompatActivity() {
    private var progress: KProgressHUD? = null
    private var imageRef: StorageReference? = null

    private var imageBitmap: Bitmap? = null
    private var mediaDownloadUrl: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_betting_site)
        progress = KProgressHUD(this)

        imageRef = FirebaseStorage.getInstance().reference.child(Constants.newReciterImage)

        add_new_reciter_selectImage.setOnClickListener {
            cropImage()
        }
        add_new_reciter_publishBtn.setOnClickListener {
            val name = add_new_reciter_name.text.toString()
            val link = add_new_reciter_link.text.toString()
                when {
                    TextUtils.isEmpty(name) -> Toasty.error(this, "title cannot be empty", Toast.LENGTH_LONG).show()
                    TextUtils.isEmpty(link) -> Toasty.error(this, "link cannot be empty", Toast.LENGTH_LONG).show()
                    imageBitmap == null -> Toasty.error(this, "Please select an image", Toast.LENGTH_LONG).show()
                    else -> {
                        uploadSponsoredPostImage()
                    }
                }

        }
    }

    private fun cropImage() {
        CropImage.activity().setAspectRatio(1, 1).start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && data != null) {
            val cropedImage = CropImage.getActivityResult(data)
            val gameImageUri = cropedImage.uri
            imageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, gameImageUri)
            add_new_reciter_image.setImageBitmap(imageBitmap)
            Toast.makeText(this, "Selected successfully", Toast.LENGTH_LONG).show()
        }
    }

    private fun uploadSponsoredPostImage() {
        progress = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show()
        val randomKey = Utils.databaseRef().push().key
        try {
            val baos = ByteArrayOutputStream()
            imageBitmap!!.compress(Bitmap.CompressFormat.JPEG, 20, baos)
            val data = baos.toByteArray()
            val fileRef = imageRef!!.child("$randomKey.jpg")
            val uploadTask: StorageTask<*>
            uploadTask = fileRef.putBytes(data)
            uploadTask.continueWithTask(com.google.android.gms.tasks.Continuation<com.google.firebase.storage.UploadTask.TaskSnapshot, com.google.android.gms.tasks.Task<android.net.Uri>> {
                if (!it.isSuccessful) {
                    it.exception?.let { error ->
                        throw error
                    }
                }
                return@Continuation fileRef.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    mediaDownloadUrl = task.result.toString()
                    saveInfo()
                }
            }
        } catch (e: Exception) {
        }
    }

    private fun saveInfo() {
        val id = Utils.databaseRef().push().key
        val name = add_new_reciter_name.text.toString()
        val link = add_new_reciter_link.text.toString().trim()

        val map = HashMap<String, Any>()
        map["id"] = id!!
        map["name"] = name
        map["link"] = link
        map["image_url"] = mediaDownloadUrl
        try {
            Utils.databaseRef().child(Constants.newReciter).child(id).setValue(map)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Utils.databaseRef().child(Constants.users).addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(p0: DataSnapshot) {
                                    if (p0.exists()) {
                                        for (snapshot in p0.children){
                                            sendNotification(snapshot.key.toString(),"Reciter: ",name)
                                        }
                                        if(progress!!.isShowing){
                                            progress!!.dismiss()
                                            finish()
                                            Animatoo.animateDiagonal(this@AddNewReciter)
                                        }
                                    }
                                }
                                override fun onCancelled(p0: DatabaseError) {
                                }
                            })

                        } else {
                            progress!!.dismiss()
                            Toast.makeText(this, "Error occur", Toast.LENGTH_LONG).show()
                        }
                    }
        } catch (e: Exception) {
        }
    }
}
