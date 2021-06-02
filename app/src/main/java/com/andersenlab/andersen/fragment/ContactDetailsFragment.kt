package com.andersenlab.andersen.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.andersenlab.andersen.R
import com.andersenlab.andersen.model.Person
import com.bumptech.glide.Glide

class ContactDetailsFragment : Fragment() {

    private lateinit var person: Person
    private lateinit var buttonEdit: Button
    private lateinit var buttonOk: Button
    private var index = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageView = view.findViewById<ImageView>(R.id.picture)
        Glide.with(requireContext())
            .load(person.image)
            .into(imageView)
        val nameTextView = view.findViewById<TextView>(R.id.name)
        nameTextView.text = person.name
        val editNameTextView = view.findViewById<TextView>(R.id.editName)
        val phoneTextView = view.findViewById<TextView>(R.id.phone)
        phoneTextView.text = person.phone
        val editPhoneTextView = view.findViewById<TextView>(R.id.editPhone)
        buttonEdit = view.findViewById(R.id.buttonEdit)
        buttonOk = view.findViewById(R.id.buttonOk)
        buttonEdit.setOnClickListener {
            nameTextView.visibility = View.GONE
            phoneTextView.visibility = View.GONE
            editNameTextView.visibility = View.VISIBLE
            editPhoneTextView.visibility = View.VISIBLE
            editNameTextView.text = person.name
            editPhoneTextView.text = person.phone
            buttonOk.visibility = View.VISIBLE
            buttonEdit.visibility = View.GONE
        }
        buttonOk.setOnClickListener {
            nameTextView.visibility = View.VISIBLE
            phoneTextView.visibility = View.VISIBLE
            editNameTextView.visibility = View.GONE
            editPhoneTextView.visibility = View.GONE
            buttonOk.visibility = View.GONE
            buttonEdit.visibility = View.VISIBLE

            val newName = editNameTextView.text.toString()
            val newPhone = editPhoneTextView.text.toString()
            person.name = newName
            person.phone = newPhone
            nameTextView.text = newName
            phoneTextView.text = newPhone
            ContactsListFragment.contactsData[index].name = newName
            ContactsListFragment.contactsData[index].phone = newPhone
            val fragment = requireActivity()
                    .supportFragmentManager
                    .findFragmentByTag("FirstFragment")
            requireActivity().supportFragmentManager.beginTransaction()
                    .detach(fragment!!)
                    .attach(fragment)
                    .commit()
        }
    }

    companion object {
        fun newInstance(person: Person, index: Int) =
            ContactDetailsFragment().apply {
                this.person = person
                this.index = index
            }
    }

    override fun onDetach() {
        super.onDetach()
        requireActivity().findViewById<FrameLayout>(R.id.contacts).visibility = View.VISIBLE
        requireActivity().findViewById<FrameLayout>(R.id.details).visibility = View.GONE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("person", person)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            this.person = savedInstanceState.getParcelable("person")!!
        }
    }
}