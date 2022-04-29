package com.example.pusl2023_t.firestore


import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.pusl2023_t.models.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

import com.example.pusl2023_t.ui.activities.*
import com.example.pusl2023_t.ui.fragments.AllBooksFragment
import com.example.pusl2023_t.ui.fragments.RequestsFragment
import com.example.pusl2023_t.ui.fragments.MyBooksFragment
import com.example.pusl2023_t.ui.fragments.BorrowedBooksFragment
import com.example.pusl2023_t.utils.Constants
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class FirestoreClass {

    // Access a Cloud Firestore instance.
    private val mFireStore = FirebaseFirestore.getInstance()

    /**
     * A function to make an entry of the registered user in the FireStore database.
     */
    fun registerUser(activity: RegisterActivity, userInfo: User) {

        // The "users" is collection name. If the collection is already created then it will not create the same one again.
        mFireStore.collection(Constants.USERS)
            // Document ID for users fields. Here the document it is the User ID.
            .document(userInfo.id)
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge later on instead of replacing the fields.
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

                // Here call a function of base activity for transferring the result to it.
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user.",
                    e
                )
            }
    }


    fun getCurrentUserID(): String {
        // An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }
    // END

    fun getUserDetails(activity: Activity) {

        // Here we pass the collection name from which we wants the data.
        mFireStore.collection(Constants.USERS)
            // The document id to get the Fields of user.
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                Log.i(activity.javaClass.simpleName, document.toString())

                // Here we have received the document snapshot which is converted into the User Data model object.
                val user = document.toObject(User::class.java)!!
                val sharedPreferences = activity.getSharedPreferences(Constants.BKSTORE_PREFERENCES, Context.MODE_PRIVATE)

                // Create an instance of the editor which is help us to edit the SharedPreference.//STORING NAME IN PREFS
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(Constants.LOGGED_IN_USERNAME, "${user.firstName} ${user.lastName}")
                editor.apply()

                when (activity) {
                    is LoginActivity -> {
                        // Call a function of base activity for transferring the result to it.
                        activity.userLoggedInSuccess(user)
                    }
                    is BookDetailsActivity -> {
                        // Call a function of base activity for transferring the result to it.
                        activity.userDetailsSuccess(user)
                    }
                    is SettingsActivity ->{

                        activity.userDetailsSuccess(user)
                        // END
                    }
                }
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error. And print the error in log.
                when (activity) {
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }
                    is BookDetailsActivity ->{

                        activity.hideProgressDialog()
                        // END
                    }
                    is SettingsActivity ->{

                        activity.hideProgressDialog()
                        // END
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting user details.",
                    e
                )
            }
    }
    fun getOwnerDetails(activity: BorrowedBookDetailsActivity,owner_id:String) {

        // Here we pass the collection name from which we wants the data.
        mFireStore.collection(Constants.USERS)
            // The document id to get the Fields of user.
            .document(owner_id)
            .get()
            .addOnSuccessListener { document ->

                Log.i(activity.javaClass.simpleName, document.toString())

                // Here we have received the document snapshot which is converted into the User Data model object.
                val user = document.toObject(User::class.java)!!



                when (activity) {
                    is BorrowedBookDetailsActivity -> {
                        // Call a function of base activity for transferring the result to it.
                        activity.getOwnerDetailsSuccess(user)
                    }
                }
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error. And print the error in log.
                when (activity) {
                    is BorrowedBookDetailsActivity -> {
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting user details.",
                    e
                )
            }
    }

    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>) {
        // Collection Name
        mFireStore.collection(Constants.USERS)
            // Document ID against which the data to be updated. Here the document id is the current logged in user id.
            .document(getCurrentUserID())
            // A HashMap of fields which are to be updated.
            .update(userHashMap)
            .addOnSuccessListener {


                // Notify the success result.
                when (activity) {
                    is UserProfileActivity -> {
                        // Call a function of base activity for transferring the result to it.
                        //will also hide progress dialog
                        activity.userProfileUpdateSuccess()
                    }
                }
                // END
            }
            .addOnFailureListener { e ->

                when (activity) {
                    is UserProfileActivity -> {
                        // Hide the progress dialog if there is any error. And print the error in log.
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the user details.",
                    e
                )
            }
    }
    fun requestBook(activity: Activity, bookHashMap: HashMap<String, Any>,book_id:String) {
        // Collection Name
        mFireStore.collection("books")
            // Document ID against which the data to be updated. Here the document id is the current logged in user id.
            .document(book_id)
            // A HashMap of fields which are to be updated.
            .update(bookHashMap)
            .addOnSuccessListener {


                // Notify the success result.
                when (activity) {
                    is BookDetailsActivity -> {
                        // Call a function of base activity for transferring the result to it.
                        //will also hide progress dialog
                        activity.requestBookSuccess()
                    }
                    is EditBookActivity -> {
                        // Hide the progress dialog if there is any error. And print the error in log.
                        activity.updateBookDetailsSuccess()
                    }
                }
                // END
            }
            .addOnFailureListener { e ->

                when (activity) {
                    is BookDetailsActivity -> {
                        // Hide the progress dialog if there is any error. And print the error in log.
                        activity.hideProgressDialog()
                    }
                    is EditBookActivity -> {
                        // Hide the progress dialog if there is any error. And print the error in log.
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the user details.",
                    e
                )
            }
    }
    fun updateRequestBook(fragment: Fragment ,book_id: String, status: String) {
        // Collection Name
        mFireStore.collection("books")
            // Document ID against which the data to be updated. Here the document id is the current logged in user id.
            .document(book_id)
            // A HashMap of fields which are to be updated.
            .update("status",status)
            .addOnSuccessListener {


                // Notify the success result.
                when (fragment) {
                    is RequestsFragment -> {
                        // Call a function of base activity for transferring the result to it.
                        //will also hide progress dialog
                        fragment.updateRequestBookSuccess()
                    }
                    is BorrowedBooksFragment -> {
                        // Call a function of base activity for transferring the result to it.
                        //will also hide progress dialog
                        fragment.updateRequestBookSuccess()
                    }

                }
                // END
            }
            .addOnFailureListener { e ->

                when (fragment) {
                    is RequestsFragment -> {
                        // Hide the progress dialog if there is any error. And print the error in log.
                        fragment.hideProgressDialog()
                    }
                    is BorrowedBooksFragment -> {
                        // Hide the progress dialog if there is any error. And print the error in log.
                        fragment.hideProgressDialog()
                    }
                }

                Log.e(
                    fragment.javaClass.simpleName,
                    "Error while updating the user details.",
                    e
                )
            }
    }
    fun updateRequestBook(activity: Activity ,book_id: String, status: String) {
        // Collection Name
        mFireStore.collection("books")
            // Document ID against which the data to be updated. Here the document id is the current logged in user id.
            .document(book_id)
            // A HashMap of fields which are to be updated.
            .update("status",status)
            .addOnSuccessListener {


                // Notify the success result.
                when (activity) {
                    is RequestDetailsActivity -> {
                        // Call a function of base activity for transferring the result to it.
                        //will also hide progress dialog
                        activity.updateRequestBookSuccess()
                    }
                    is BorrowedBookDetailsActivity -> {
                        // Call a function of base activity for transferring the result to it.
                        //will also hide progress dialog
                        activity.updateRequestBookSuccess()
                    }

                }
                // END
            }
            .addOnFailureListener { e ->

                when (activity) {
                    is RequestDetailsActivity -> {
                        // Hide the progress dialog if there is any error. And print the error in log.
                        activity.hideProgressDialog()
                    }
                    is BorrowedBookDetailsActivity -> {
                        // Hide the progress dialog if there is any error. And print the error in log.
                        activity.hideProgressDialog()
                    }

                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the user details.",
                    e
                )
            }
    }

    //___________________________2

    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?, imageType: String) {

        //getting the storage reference
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(imageType + System.currentTimeMillis() + "." + Constants.getFileExtension(
                activity, imageFileURI))

        //adding the file to reference
        sRef.putFile(imageFileURI!!).addOnSuccessListener { taskSnapshot ->
                // The image upload is success
                Log.e("Firebase Image URL", taskSnapshot.metadata!!.reference!!.downloadUrl.toString())

                // Get the downloadable url from the task snapshot
                taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                        Log.e("Downloadable Image URL", uri.toString())


                        // Here call a function of base activity for transferring the result to it.
                        when (activity) {
                            is UserProfileActivity -> {
                                activity.imageUploadSuccess(uri.toString())
                            }
                            is AddBookActivity -> {
                                activity.imageUploadSuccess(uri.toString())
                            }
                        }
                    }
            }
            .addOnFailureListener { exception ->

                // Hide the progress dialog if there is any error. And print the error in log.
                when (activity) {
                    is UserProfileActivity -> {
                        activity.hideProgressDialog()
                    }

                    is AddBookActivity -> {
                        activity.hideProgressDialog()
                    }
                }

                Log.e(activity.javaClass.simpleName, exception.message, exception
                )
            }
    }


    fun uploadBookDetails(activity: AddBookActivity, bookInfo: Book) {

        mFireStore.collection("books")
            .document()
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(bookInfo, SetOptions.merge())
            .addOnSuccessListener {

                // Here call a function of base activity for transferring the result to it.
                activity.bookUploadSuccess()
            }
            .addOnFailureListener { e ->

                activity.hideProgressDialog()

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while uploading the book details.",
                    e
                )
            }
    }


    fun getBooksList(fragment: Fragment) {
        // The collection name for PRODUCTS
        mFireStore.collection("books").whereEqualTo(Constants.USER_ID, getCurrentUserID()).get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->

                // Here we get the list of boards in the form of documents.
                Log.e("Books List///////////", document.documents.toString())

                // Here we have created a new instance for Books ArrayList.
                val booksList: ArrayList<Book> = ArrayList()

                // A for loop as per the list of documents to convert them into Books ArrayList.
                for (i in document.documents) {

                    val book = i.toObject(Book::class.java)
                    book!!.book_id = i.id

                    booksList.add(book)
                }

                when (fragment) {
                    is MyBooksFragment -> {
                        fragment.successBooksListFromFireStore(booksList)
                    }
                }
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error based on the base class instance.
                when (fragment) {
                    is MyBooksFragment -> {
                        fragment.hideProgressDialog()
                    }
                }
                Log.e("Get Book List", "Error while getting book list.", e)
            }
    }

    fun getDashboardItemsList(fragment: AllBooksFragment) {
        // The collection name for PRODUCTS
        mFireStore.collection("books").get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->

                // Here we get the list of boards in the form of documents.
                Log.e(fragment.javaClass.simpleName, document.documents.toString())

                // Here we have created a new instance for Books ArrayList.
                val booksList: ArrayList<Book> = ArrayList()

                // A for loop as per the list of documents to convert them into Books ArrayList.
                for (i in document.documents) {

                    val book = i.toObject(Book::class.java)!!
                    book.book_id = i.id
                    booksList.add(book)
                }

                // Pass the success result to the base fragment.
                fragment.successDashboardItemsList(booksList)
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error which getting the dashboard items list.
                fragment.hideProgressDialog()
                Log.e(fragment.javaClass.simpleName, "Error while getting dashboard items list.", e)
            }
    }

    fun deleteBook(fragment: MyBooksFragment, bookId: String) {

        mFireStore.collection(Constants.PRODUCTS).document(bookId).delete()
            .addOnSuccessListener {


                // Notify the success result to the base class.
                fragment.bookDeleteSuccess()
                // END
            }
            .addOnFailureListener { e ->

                // Hide the progress dialog if there is an error.
                fragment.hideProgressDialog()

                Log.e(fragment.requireActivity().javaClass.simpleName, "Error while deleting the book.", e)
            }
    }
    fun removeRequest(activity: SentRequestListActivity, book_id: String) {

        mFireStore.collection("books")
            // Document ID against which the data to be updated. Here the document id is the current logged in user id.
            .document(book_id)
            // A HashMap of fields which are to be updated.
            .update("status","")
            .addOnSuccessListener {


                // Notify the success result to the base class.
                activity.removeRequestSuccess()
                // END
            }
            .addOnFailureListener { e ->

                // Hide the progress dialog if there is an error.
                activity.hideProgressDialog()

                Log.e(activity.javaClass.simpleName, "Error while deleting the book.", e)
            }
    }


    fun getBookDetails(activity: Activity, bookId: String) {

        // The collection name for PRODUCTS
        mFireStore.collection("books")
            .document(bookId)
            .get() // Will get the document snapshots.
            .addOnSuccessListener { document ->

                // Here we get the book details in the form of document.
                Log.e(activity.javaClass.simpleName, document.toString())

                // Convert the snapshot to the object of Book data model class.
                val book = document.toObject(Book::class.java)!!
                book.book_id = document.id

                when (activity) {
                    is BookDetailsActivity -> {
                        activity.bookDetailsSuccess(book)
                    }

                    is EditBookActivity -> {
                        activity.bookDetailsSuccess(book)
                    }
                    // END
                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is BookDetailsActivity -> {
                        activity.hideProgressDialog()
                    }

                    is EditBookActivity -> {
                        //activity.successCartItemsList(list)
                        activity.hideProgressDialog()

                    }
                    // END
                }
                // Hide the progress dialog if there is an error.

                Log.e(activity.javaClass.simpleName, "Error while getting the book details.", e)
            }
    }



    fun getSentRequestList(activity: Activity) {
        // The collection name for PRODUCTS
        mFireStore.collection("books")
            .whereEqualTo("borrower_id", getCurrentUserID())
            .whereEqualTo("status", "requested")
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->
                // Here we get the list of cart items in the form of documents.
                Log.e(activity.javaClass.simpleName, document.documents.toString())
                // Here we have created a new instance for Cart Items ArrayList.
                val list: ArrayList<Book> = ArrayList()
                // A for loop as per the list of documents to convert them into Cart Items ArrayList.
                for (i in document.documents) {
                    val cartItem = i.toObject(Book::class.java)!!
                    cartItem.book_id = i.id
                    list.add(cartItem)
                }
                when (activity) {
                    is SentRequestListActivity -> {
                        activity.successSentRequestList(list)
                    }


                    // END
                }
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is an error based on the activity instance.
                when (activity) {
                    is SentRequestListActivity -> {
                        activity.hideProgressDialog()
                    }


                    // END
                }

                Log.e(activity.javaClass.simpleName, "Error while getting the cart list items.", e)
            }
    }





    fun getMyRequestsList(fragment: RequestsFragment) {
        mFireStore.collection("books")
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .whereIn("status", listOf("requested", "borrowed","returning"))
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->
                Log.e(fragment.javaClass.simpleName, document.documents.toString())
                val list: ArrayList<Book> = ArrayList()

                for (i in document.documents) {

                    val orderItem = i.toObject(Book::class.java)!!
                    orderItem.book_id = i.id

                    list.add(orderItem)
                }


                fragment.populateRequestsListInUI(list)
                // END
            }
            .addOnFailureListener { e ->
                // Here call a function of base activity for transferring the result to it.

                fragment.hideProgressDialog()

                Log.e(fragment.javaClass.simpleName, "Error while getting the orders list.", e)
            }
    }



    fun getBorrowedList(fragment: BorrowedBooksFragment) {
        // The collection name for SOLD PRODUCTS
        mFireStore.collection("books")
            .whereEqualTo("borrower_id", getCurrentUserID())
            .whereEqualTo("status", "borrowed")
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->
                // Here we get the list of sold books in the form of documents.
                Log.e(fragment.javaClass.simpleName, document.documents.toString())

                // Here we have created a new instance for Sold Books ArrayList.
                val list: ArrayList<Book> = ArrayList()

                // A for loop as per the list of documents to convert them into Sold Books ArrayList.
                for (i in document.documents) {

                    val soldBook = i.toObject(Book::class.java)!!
                    soldBook.book_id = i.id

                    list.add(soldBook)
                }

                fragment.successGetBorrowedList(list)
                // END
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error.
                fragment.hideProgressDialog()

                Log.e(
                    fragment.javaClass.simpleName,
                    "Error while getting the list of sold books.",
                    e
                )
            }
    }


}