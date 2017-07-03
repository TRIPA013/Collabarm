package com.abhirishi.personal.collabarm.util

import android.content.Context
import android.provider.ContactsContract
import com.abhirishi.personal.collabarm.models.Friend
import java.util.*

class ContactsUtil {

    companion object {
        fun getAllFriends(context: Context): ArrayList<Friend> {
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
            return friendsList
        }

    }

}