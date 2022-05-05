package com.example.pusl2023_t.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.pusl2023_t.R
import com.example.pusl2023_t.firestore.FirestoreClass
import com.example.pusl2023_t.models.Book
import com.example.pusl2023_t.utils.Constants
import com.example.pusl2023_t.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_add_book.*
import java.io.IOException

class AddBookActivity : BaseActivity(), View.OnClickListener {

    // A global variable for URI of a selected image from phone storage.
    private var mSelectedImageFileUri: Uri? = null

    // A global variable for uploaded book image URL.
    private var mBookImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)

        setupActionBar()

        iv_add_update_book.setOnClickListener(this)

        btn_submit.setOnClickListener(this)


    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_add_book_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbar_add_book_activity.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onClick(v: View?) {

        if (v != null) {
            when (v.id) {

                // The permission code is similar to the user profile image selection.
                R.id.iv_add_update_book -> {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Constants.showImageChooser(this@AddBookActivity)
                    } else {
                        /*Requests permissions to be granted to this application. These permissions
                         must be requested in your manifest, they should not be granted to your app,
                         and they should have protection level*/
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), Constants.READ_STORAGE_PERMISSION_CODE)
                    }
                }

                R.id.btn_submit -> {
                    if (validateBookDetails()) {

                        //___________________________1
                        uploadBookImage()
                    }
                }
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this@AddBookActivity)
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, resources.getString(R.string.read_storage_permission_denied), Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.PICK_IMAGE_REQUEST_CODE && data!!.data != null
        ) {

            // Replace the add icon with edit icon once the image is selected.
            iv_add_update_book.setImageDrawable(ContextCompat.getDrawable(this@AddBookActivity, R.drawable.ic_vector_edit))

            // The uri of selection image from phone storage.
            mSelectedImageFileUri = data.data!!

            try {
                // Load the book image in the ImageView.
                GlideLoader(this@AddBookActivity).loadBookPicture(mSelectedImageFileUri!!, iv_book_image)

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    private fun validateBookDetails(): Boolean {
        return when {

            mSelectedImageFileUri == null -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_select_book_image), true)
                false
            }

            TextUtils.isEmpty(et_book_title.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_book_title), true)
                false
            }

            TextUtils.isEmpty(et_book_description.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_book_description), true)
                false
            }

            else -> {
                true
            }
        }
    }

    fun imageUploadSuccess(imageURL: String) {

        // Initialize the global image url variable.
        mBookImageURL = imageURL

        uploadBookDetails()
    }

    private fun uploadBookImage() {

        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().uploadImageToCloudStorage(
            this@AddBookActivity,
            mSelectedImageFileUri,
            Constants.PRODUCT_IMAGE
        )
    }

    //___________________________4
    private fun uploadBookDetails() {

        // Get the logged in username from the SharedPreferences that we have stored at a time of login.
        val username =
            this.getSharedPreferences(Constants.BKSTORE_PREFERENCES, Context.MODE_PRIVATE).getString(Constants.LOGGED_IN_USERNAME, "")!!

        // Here we get the text from editText and trim the space
        val book = Book(
            FirestoreClass().getCurrentUserID(),
            username,
            "",
            "",
            et_book_title.text.toString().trim { it <= ' ' },
            et_book_description.text.toString().trim { it <= ' ' },
            mBookImageURL
        )

        FirestoreClass().uploadBookDetails(this@AddBookActivity, book)

    }
    fun bookUploadSuccess() {

        // Hide the progress dialog
        hideProgressDialog()

        Toast.makeText(this@AddBookActivity, resources.getString(R.string.book_uploaded_success_message), Toast.LENGTH_SHORT).show()

        finish()
    }




}