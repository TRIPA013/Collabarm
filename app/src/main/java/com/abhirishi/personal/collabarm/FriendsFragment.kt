package com.abhirishi.personal.collabarm

import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.abhirishi.personal.collabarm.models.Friend
import com.abhirishi.personal.collabarm.util.FirebaseUtil
import com.abhirishi.personal.collabarm.util.FirebaseUtil.Companion.checkForFriends
import java.util.*

/**
 * A fragment representing a list of Items.
 *
 *
 * Activities containing this fragment MUST implement the [OnListFragmentInteractionListener]
 * interface.
 */
/**
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
class FriendsFragment : Fragment(), FriendsListener {

    var friendsRecyclerViewAdapter: FriendsRecyclerViewAdapter? = null
    override fun onFriendsChange() {
        friendsRecyclerViewAdapter?.notifyDataSetChanged()
    }

    // TODO: Customize parameters
    private var mColumnCount = 1
    private var mListener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            mColumnCount = arguments.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_friends_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            val context = view.getContext()
            val recyclerView = view
            if (mColumnCount <= 1) {
                recyclerView.layoutManager = LinearLayoutManager(context)
            } else {
                val verticalLayoutmanager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

                recyclerView.layoutManager = verticalLayoutmanager
            }
            friendsRecyclerViewAdapter = FriendsRecyclerViewAdapter(getAllContacts(context), mListener)
            recyclerView.adapter = friendsRecyclerViewAdapter
        }
        return view
    }

    private fun getAllContacts(context: Context): List<Friend> {
        val friendsList = ArrayList<Friend>()
        var Friend: Friend

        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC")
        if (cursor!!.count > 0) {
            while (cursor.moveToNext()) {

                val hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)))
                if (hasPhoneNumber > 0) {
                    val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

                    Friend = Friend()
                    Friend.name = name

                    val phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            arrayOf(id), null)
                    if (phoneCursor!!.moveToNext()) {
                        val phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        Friend.phoneNumber = phoneNumber
                    }

                    phoneCursor.close()

                    val emailCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            arrayOf(id), null)
                    while (emailCursor!!.moveToNext()) {
                        val emailId = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))
                    }
                    friendsList.add(Friend)
                }
            }
        }
        checkForFriends(context, friendsList, FirebaseUtil.getUsername(), this)

        return FirebaseUtil.availableFriends
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            mListener = context as OnListFragmentInteractionListener?
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: Friend)
    }

    companion object {

        // TODO: Customize parameter argument names
        private val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        fun newInstance(columnCount: Int): FriendsFragment {
            val fragment = FriendsFragment()
            val args = Bundle()
            args.putInt(ARG_COLUMN_COUNT, columnCount)
            fragment.arguments = args
            return fragment
        }
    }
}
