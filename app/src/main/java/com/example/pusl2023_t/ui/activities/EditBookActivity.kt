package com.example.pusl2023_t.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.pusl2023_t.R
import com.example.pusl2023_t.firestore.FirestoreClass
import com.example.pusl2023_t.models.Book
import com.example.pusl2023_t.utils.Constants
import com.example.pusl2023_t.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_edit_book.*

class EditBookActivity : BaseActivity() {
    private lateinit var mBookDetails: Book
    private var mBookOwnerId: String = ""

    private var mBookId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_book)
        if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID)) {
            mBookId =
                intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
            Log.i("details Book Id", mBookId)
        }
        //var bookOwnerId: String = ""
        if (intent.hasExtra(Constants.EXTRA_PRODUCT_OWNER_ID)) {
            mBookOwnerId = intent.getStringExtra(Constants.EXTRA_PRODUCT_OWNER_ID)!!
        }
        btn_update.setOnClickListener {
            updateBookDetails()
        }

        setupActionBar()
        getBookDetails()
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

    private fun getBookDetails() {

        // Show the book dialog
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of FirestoreClass to get the book details.
        //FirestoreClass().getBookDetails(this@BookDetailsActivity, mBookId)
        FirestoreClass().getBookDetails(this@EditBookActivity, mBookId)

    }
    fun bookDetailsSuccess(book: Book){
        mBookDetails = book
        GlideLoader(this@EditBookActivity).loadBookPicture(book.image, iv_book_image)

        et_book_title.setText(mBookDetails.title)
        et_book_description.setText(mBookDetails.description)
        hideProgressDialog()

    }
    private fun updateBookDetails(){
        val bookHashMap = HashMap<String, Any>()
        val title = et_book_title.text.toString().trim { it <= ' ' }
        val description = et_book_description.text.toString().trim { it <= ' ' }
        bookHashMap["title"] = title
        bookHashMap["description"] = description
        FirestoreClass().requestBook(this@EditBookActivity,bookHashMap,mBookId)


    }

    fun updateBookDetailsSuccess(){
        hideProgressDialog()
        Toast.makeText(this@EditBookActivity, "book updated", Toast.LENGTH_SHORT).show()
        finish()

    }
}