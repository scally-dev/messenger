package com.example.mymessenger.utilits

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mymessenger.MainActivity
import com.example.mymessenger.R
import com.example.mymessenger.models.CommonModel
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

fun showToast(message:String){
    Toast.makeText(APP_ACTIVITY,message,Toast.LENGTH_SHORT).show()
}

fun restartActivity(){
    val intent = Intent(APP_ACTIVITY, MainActivity::class.java)
    APP_ACTIVITY.startActivity(intent)
    APP_ACTIVITY.finish()
}

fun replaceFragment(fragment: Fragment, addStack:Boolean = true){
    if (addStack){
        APP_ACTIVITY.supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.data_container,
                fragment
            ).commit()
    } else {
        APP_ACTIVITY.supportFragmentManager.beginTransaction()
            .replace(R.id.data_container,
                fragment
            ).commit()
    }
}



fun hideKeyboard(){
    val imm: InputMethodManager = APP_ACTIVITY.getSystemService(Context.INPUT_METHOD_SERVICE)
            as InputMethodManager
    imm.hideSoftInputFromWindow(APP_ACTIVITY.window.decorView.windowToken,0)
}

fun ImageView.downloadAndSetImage(url:String) {
    Picasso.get()
        .load(url)
        .fit()
        .placeholder(R.drawable.default_photo)
        .into(this)
}

fun initContacts() {
    /*if (checkPermission(READ_CONTACTS)){
        showToast("Чтение контактов")
    }*/
    var arrayContacts = arrayListOf<CommonModel>()
    var username = ""
    var login = ""
    val newModel = CommonModel()
    newModel.username = username
    newModel.login = login
    arrayContacts.add(newModel)

    /*updatePhonesToDatabase(arrayContacts)*/
}
fun String.asTime(): String {
    val time = Date(this.toLong())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(time)
}

@SuppressLint("Range")
fun getFilenameFromUri(uri: Uri): String {
    var result = ""
    val cursor = APP_ACTIVITY.contentResolver.query(uri, null, null, null, null)
    try {
        if (cursor != null && cursor.moveToFirst()) {
            result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        }
    } catch (e: Exception) {
        showToast(e.message.toString())
    } finally {
        cursor?.close()
        return result
    }
}

fun getPlurals(count:Int) = APP_ACTIVITY.resources.getQuantityString(
    R.plurals.count_members,count,count
)

