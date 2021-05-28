package com.andersenlab.andersen.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andersenlab.andersen.R
import com.andersenlab.andersen.adapter.ContactsAdapter
import com.andersenlab.andersen.model.Person
import com.andersenlab.andersen.retrofit.ContactsData
import com.andersenlab.andersen.utils.ItemTouchHelperCallback
import com.andersenlab.andersen.viewModel.ContactsListViewModel
import java.util.*


class ContactsListFragment : Fragment(), ContactsAdapter.Interaction {

    private var currentPosition = 1
    private var isLandscape: Boolean = false
    private lateinit var contactsList: RecyclerView
    private var filterData: List<Person>? = ArrayList<Person>()
    private lateinit var itemTouchHelper: ItemTouchHelper

    companion object {
        var contactsData = arrayListOf<Person>()
        fun newInstance() =
            ContactsListFragment()
    }

    private val viewModel: ContactsListViewModel by lazy {
        ViewModelProvider(this).get(ContactsListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (savedInstanceState != null) {
            contactsData =
                savedInstanceState.getParcelableArrayList<Person>("data") as ArrayList<Person>
            currentPosition = savedInstanceState.getInt("currentPosition")
        }
        if (contactsData.isNullOrEmpty()) {
            viewModel.getData()
                .observe(viewLifecycleOwner, { renderData(it) })
        }
        return inflater.inflate(R.layout.fragment_contacts_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contactsList = view.findViewById(R.id.contactsRecyclerView)
        init()
    }

    private fun init() {
        val dividerItemDecoration = DividerItemDecoration(context, RecyclerView.VERTICAL)
        dividerItemDecoration.setDrawable(
            ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.divider
            )!!
        )
        contactsList.addItemDecoration(dividerItemDecoration)
        updateAdapter(contactsData)
        val search: EditText = view!!.findViewById(R.id.search)
        search.setText("")
        search.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun afterTextChanged(s: Editable) {
                    if (s.toString() != "") {
                        filterData = getFilter(s.toString())
                        updateAdapter(filterData!!)
                    } else {
                        updateAdapter(contactsData)
                    }
                }
            })
    }

    private fun getFilter(charSequence: CharSequence?): ArrayList<Person>? {
        val filterResultsData: ArrayList<Person> = ArrayList<Person>()
        if (charSequence == null || charSequence.isEmpty()) {
            return null
        } else {
            for (contact in contactsData) {
                if (contact.name.toLowerCase(Locale.ROOT).contains(charSequence)) {
                    filterResultsData.add(contact)
                }
            }
        }
        return filterResultsData
    }

    fun updateAdapter(data: List<Person>) {
        val adapter = ContactsAdapter(
            data as MutableList<Person>,
            requireContext()
        )
        contactsList.layoutManager = LinearLayoutManager(activity!!.baseContext)
        contactsList.adapter = adapter
        itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(adapter))
        itemTouchHelper.attachToRecyclerView(contactsList)
        adapter.onClick = { _, position ->
            currentPosition = position
            showDetails(currentPosition)
        }
        adapter.onLongClick = { person, position ->
            AlertDialog.Builder(requireContext())
                .setTitle("Do you really want do delete ${person.name}?")
                .setNegativeButton(
                    "Cancel"
                ) { dialog: DialogInterface, _: Int -> dialog.cancel() }
                .setPositiveButton(
                    "Ok"
                ) { _: DialogInterface?, _: Int ->
                    adapter.onItemDismiss(position)
                }
                .create()
                .show()
        }
        adapter.notifyDataSetChanged()
    }

    private fun renderData(data: ContactsData) {
        when (data) {
            is ContactsData.Success -> {
                val serverResponseData = data.serverResponseData
                val list = serverResponseData.results
                if (list != null) {
                    for (item in list) {
                        val person = Person(
                            item.name.first + " " + item.name.last,
                            item.phone,
                            item.picture.large
                        )
                        contactsData.add(person)
                    }
                }
                init()
            }
            is ContactsData.Loading -> {
                //showLoading()
            }
            is ContactsData.Error -> {
                Toast.makeText(context, data.error.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onItemSelected(position: Int, item: Person) {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        isLandscape = (resources.configuration.orientation
                == Configuration.ORIENTATION_LANDSCAPE)

        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt("currentPosition", 0)
        }
    }

    private fun showDetails(index: Int) {
        if (isLandscape) {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(
                    R.id.details,
                    ContactDetailsFragment.newInstance(contactsData[index], index)
                )
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit()
        } else {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(
                    R.id.contacts,
                    ContactDetailsFragment.newInstance(contactsData[index], index)
                )
                .addToBackStack("")
                .commit()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("currentContact", currentPosition)
        outState.putParcelableArrayList("data", ArrayList(contactsData))
        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            contactsData =
                savedInstanceState.getParcelableArrayList<Person>("data") as ArrayList<Person>
            currentPosition = savedInstanceState.getInt("currentPosition")
        }
    }
}