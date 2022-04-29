package com.example.pusl2023_t.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pusl2023_t.R
import com.example.pusl2023_t.firestore.FirestoreClass
import com.example.pusl2023_t.models.Book
import com.example.pusl2023_t.ui.activities.BookDetailsActivity
import com.example.pusl2023_t.ui.activities.SentRequestListActivity
import com.example.pusl2023_t.ui.activities.SettingsActivity
import com.example.pusl2023_t.ui.adapters.DashboardItemsListAdapter
import com.example.pusl2023_t.utils.Constants
import kotlinx.android.synthetic.main.fragment_dashboard.*

class AllBooksFragment : BaseFragment() {

    //private lateinit var dashboardViewModel: DashboardViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // If we want to use the option menu in fragment we need to add it.
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        //dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)


        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)


        //dashboardViewModel.text.observe(viewLifecycleOwner, Observer { textView.text = it })
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {

            R.id.action_settings -> {


                startActivity(Intent(activity, SettingsActivity::class.java))
                // END
                return true
            }

            R.id.action_cart -> {
                startActivity(Intent(activity, SentRequestListActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()

        getDashboardItemsList()
    }

    private fun getDashboardItemsList() {
        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getDashboardItemsList(this@AllBooksFragment)
    }

    fun successDashboardItemsList(dashboardItemsList: ArrayList<Book>) {

        // Hide the progress dialog.
        hideProgressDialog()

        if (dashboardItemsList.size > 0) {

            rv_dashboard_items.visibility = View.VISIBLE
            tv_no_dashboard_items_found.visibility = View.GONE

            rv_dashboard_items.layoutManager = GridLayoutManager(activity, 2)
            rv_dashboard_items.setHasFixedSize(true)

            val adapter = DashboardItemsListAdapter(requireActivity(), dashboardItemsList)
            rv_dashboard_items.adapter = adapter

            adapter.setOnClickListener(object :
                DashboardItemsListAdapter.OnClickListener {
                override fun onClick(position: Int, book: Book) {
                    val intent = Intent(context, BookDetailsActivity::class.java)
                    intent.putExtra(Constants.EXTRA_PRODUCT_ID, book.book_id)
                    intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID, book.user_id)

                    startActivity(intent)
                    // END
                }
            })
        } else {
            rv_dashboard_items.visibility = View.GONE
            tv_no_dashboard_items_found.visibility = View.VISIBLE
        }

        for (i in dashboardItemsList){
            Log.i("item title",i.title)


        }
    }
}