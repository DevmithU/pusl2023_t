package com.example.pusl2023_t.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.pusl2023_t.R
import com.example.pusl2023_t.firestore.FirestoreClass
import com.example.pusl2023_t.models.User
import com.example.pusl2023_t.utils.Constants
import com.example.pusl2023_t.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.io.IOException

class UserProfileActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mUserDetails: User

    private var mSelectedImageFileUri: Uri? = null

    private var mUserProfileImageURL: String = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)


        if(intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            // Get the user details from intent as a ParcelableExtra.
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }
        // END

        // TODO Step 6: After receiving the user details from intent set it to the UI.
        // START
        // Here, the some of the edittext components are disabled because it is added at a time of Registration.
        et_first_name.isEnabled = false
        et_first_name.setText(mUserDetails.firstName)

        et_last_name.isEnabled = false
        et_last_name.setText(mUserDetails.lastName)

        et_email.isEnabled = false
        et_email.setText(mUserDetails.email)


        iv_user_photo.setOnClickListener(this@UserProfileActivity)
        btn_save.setOnClickListener(this@UserProfileActivity)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                R.id.iv_user_photo -> {

                    if (ContextCompat.checkSelfPermission(this,  Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED  ) {

                        //showErrorSnackBar("You already have the storage permission.", false)
                        Constants.showImageChooser(this@UserProfileActivity)

                    } else {

                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),  Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }
                R.id.btn_save ->{


                    if (validateUserProfileDetails()) {


                        // Show the progress dialog.
                        showProgressDialog(resources.getString(R.string.please_wait))
                        // END

                        if (mSelectedImageFileUri != null) {

                            FirestoreClass().uploadImageToCloudStorage(this@UserProfileActivity, mSelectedImageFileUri)
                        } else {

                            /*
                            val userHashMap = HashMap<String, Any>()

                            // Here the field which are not editable needs no update. So, we will update user Mobile Number and Gender for now.

                            // Here we get the text from editText and trim the space
                            val mobileNumber = et_mobile_number.text.toString().trim { it <= ' ' }

                            val gender = if (rb_male.isChecked) {
                                Constants.MALE
                            } else {
                                Constants.FEMALE
                            }

                            if (mobileNumber.isNotEmpty()) {
                                userHashMap[Constants.MOBILE] = mobileNumber.toLong()
                            }

                            userHashMap[Constants.GENDER] = gender

                            // Show the progress dialog.
                            showProgressDialog(resources.getString(R.string.please_wait))

                            // call the registerUser function of FireStore class to make an entry in the database.
                            FirestoreClass().updateUserProfileData(
                                this@UserProfileActivity,
                                userHashMap
                            )*/


                            updateUserProfileDetails()
                        }
                    }
                }//keep


           }
       }
   }


//overide fucntion to show custom toast
   override fun onRequestPermissionsResult(
       requestCode: Int,
       permissions: Array<out String>,
       grantResults: IntArray
   ) {
       super.onRequestPermissionsResult(requestCode, permissions, grantResults)
       if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
           //If permission is granted
           if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

               //showErrorSnackBar("The storage permission is granted.", false)
               Constants.showImageChooser(this@UserProfileActivity)

           } else {
               //Displaying another toast if permission is not granted
               Toast.makeText( this,  resources.getString(R.string.read_storage_permission_denied), Toast.LENGTH_LONG  ).show()
           }
       }
   }

   public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
       super.onActivityResult(requestCode, resultCode, data)
       if (resultCode == Activity.RESULT_OK) {
           if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
               if (data != null) {
                   try {
                       // The uri of selected image from phone storage.
                       mSelectedImageFileUri = data.data!!

                       //iv_user_photo.setImageURI(Uri.parse(selectedImageFileUri.toString()))
                       GlideLoader(this).loadUserPicture(mSelectedImageFileUri!!,iv_user_photo)
                   } catch (e: IOException) {
                       e.printStackTrace()
                       Toast.makeText(this@UserProfileActivity, resources.getString(R.string.image_selection_failed), Toast.LENGTH_SHORT).show()
                   }
               }
           }
       } else if (resultCode == Activity.RESULT_CANCELED) {
           // A log is printed when user close or cancel the image selection.
           Log.e("Request Cancelled", "Image selection cancelled")
       }
   }

   private fun validateUserProfileDetails(): Boolean {
       return when {

           // We have kept the user profile picture is optional.
           // The FirstName, LastName, and Email Id are not editable when they come from the login screen.
           // The Radio button for Gender always has the default selected value.

           // Check if the mobile number is not empty as it is mandatory to enter.
           TextUtils.isEmpty(et_mobile_number.text.toString().trim { it <= ' ' }) -> {
               showErrorSnackBar(resources.getString(R.string.err_msg_enter_mobile_number), true)
               false
           }
           else -> {
               true
           }
       }
   }
    private fun updateUserProfileDetails() {

        val userHashMap = HashMap<String, Any>()

        // Here the field which are not editable needs no update. So, we will update user Mobile Number and Gender for now.

        // Here we get the text from editText and trim the space
        val mobileNumber = et_mobile_number.text.toString().trim { it <= ' ' }

        val gender = if (rb_male.isChecked) {
            Constants.MALE
        } else {
            Constants.FEMALE
        }


        if (mUserProfileImageURL.isNotEmpty()) {
            userHashMap[Constants.IMAGE] = mUserProfileImageURL
        }

        if (mobileNumber.isNotEmpty()) {
            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
        }

        userHashMap[Constants.GENDER] = gender

        userHashMap[Constants.COMPLETE_PROFILE] = 1

        // Show the progress dialog.
        /*showProgressDialog(resources.getString(R.string.please_wait))*/

        // call the registerUser function of FireStore class to make an entry in the database.
        FirestoreClass().updateUserProfileData(
            this@UserProfileActivity,
            userHashMap
        )
    }
   fun userProfileUpdateSuccess() {

       // Hide the progress dialog
       hideProgressDialog()

       Toast.makeText(this@UserProfileActivity, resources.getString(R.string.msg_profile_update_success), Toast.LENGTH_SHORT).show()


       // Redirect to the Main Screen after profile completion.
       startActivity(Intent(this@UserProfileActivity, MainActivity::class.java))
       finish()
   }

   fun imageUploadSuccess(imageURL: String) {

       // Hide the progress dialog
       //hideProgressDialog()

       //Toast.makeText(this@UserProfileActivity, "Your image is uploaded successfully. Image URL is $imageURL", Toast.LENGTH_SHORT).show()

       mUserProfileImageURL = imageURL

       updateUserProfileDetails()



   }
}