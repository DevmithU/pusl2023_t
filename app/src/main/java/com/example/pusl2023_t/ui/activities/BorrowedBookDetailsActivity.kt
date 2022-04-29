package com.example.pusl2023_t.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.pusl2023_t.R
import com.example.pusl2023_t.firestore.FirestoreClass
import com.example.pusl2023_t.models.Book
import com.example.pusl2023_t.models.User
import com.example.pusl2023_t.utils.Constants
import com.example.pusl2023_t.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_borrowed_book_details.*


import java.text.SimpleDateFormat
import java.util.*

class BorrowedBookDetailsActivity : BaseActivity() {
    var mborrowedDetails: Book = Book()
    var mUser: User = User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_borrowed_book_details)

        //var bookDetails: SoldBook = SoldBook()

        if (intent.hasExtra(Constants.EXTRA_SOLD_PRODUCT_DETAILS)) {
            mborrowedDetails = intent.getParcelableExtra<Book>(Constants.EXTRA_SOLD_PRODUCT_DETAILS)!!
        }
        btn_returned.setOnClickListener {
            updateRequestBook(mborrowedDetails.book_id,"returning")

        }

        setupActionBar()
        getOwnerDetails()
        //

    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_book_details_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbar_book_details_activity.setNavigationOnClickListener { onBackPressed() }
    }
    private fun setupUI(){
        GlideLoader(this@BorrowedBookDetailsActivity).loadBookPicture(mborrowedDetails.image, iv_book_detail_image)
        tv_book_details_title.text=mborrowedDetails.title
        tv_book_status.text=mborrowedDetails.status
        tv_address.text=mUser.address
        tv_mobile_number.text="${mUser.mobile}"
        hideProgressDialog()

    }
    fun getOwnerDetails(){
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getOwnerDetails(this@BorrowedBookDetailsActivity,mborrowedDetails.user_id)


    }
    fun getOwnerDetailsSuccess(owner:User){
        mUser=owner

        setupUI()

    }

    fun updateRequestBook(book_id: String, status: String) {
        showProgressDialog(resources.getString(R.string.please_wait))

        // Here we will call the delete function of the FirestoreClass. But, for now lets display the Toast message and call this function from adapter class.
        //showAlertDialogToDeleteBook(bookID)
        FirestoreClass().updateRequestBook(this@BorrowedBookDetailsActivity,book_id,status)

        //Toast.makeText(requireActivity(), "You can now delete the book. $bookID", Toast.LENGTH_SHORT).show()
    }

    fun updateRequestBookSuccess() {


        hideProgressDialog()
        finish()

    }


}