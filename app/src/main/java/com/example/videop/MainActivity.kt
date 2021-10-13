package com.example.videop

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint

import android.content.pm.PackageManager

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Telephony.Mms.Part._DATA
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.videop.databinding.ActivityMainBinding
import java.io.File
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle

    companion object{
        lateinit var videoList : ArrayList<Video>
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // for nav drawer
        toggle= ActionBarDrawerToggle(this,binding.root,R.string.open,R.string.close)
        binding.root.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if ( requestRuntimePermission()){
            videoList =getAllVideo()
            setFragment(VideoFragment())
        }


        binding.bottomNV.setOnItemSelectedListener {
           when(it.itemId){
               R.id.video_view -> setFragment(VideoFragment())
               R.id.folder_view -> setFragment(FolderFragment())
           }
            return@setOnItemSelectedListener true
        }
       binding.navView.setNavigationItemSelectedListener {
           when (it.itemId) {
               R.id.feedbackNav -> Toast.makeText(this,"Feedback",Toast.LENGTH_SHORT).show()
               R.id.themesNav -> Toast.makeText(this,"Theme",Toast.LENGTH_SHORT).show()
               R.id.sortOrderNav -> Toast.makeText(this,"Sort order",Toast.LENGTH_SHORT).show()
               R.id.aboutNav -> Toast.makeText(this,"About",Toast.LENGTH_SHORT).show()
               R.id.exitNav -> exitProcess(1)

           }
           return@setNavigationItemSelectedListener true
       }
    }


    private fun setFragment(fragment: Fragment) {
        val transaction =supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentFL, fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()
    }
    //for requesting permission
    private fun requestRuntimePermission(): Boolean{
        if (ActivityCompat.checkSelfPermission(this,WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf( WRITE_EXTERNAL_STORAGE),13)
            return false
        }
            return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 13){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show()
    } else
            ActivityCompat.requestPermissions(this, arrayOf( WRITE_EXTERNAL_STORAGE),13)
    }

    // toggle bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("Range")
    private fun getAllVideo(): ArrayList<Video>{
        val tempList = ArrayList<Video>()
        val projection = arrayOf(MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.DURATION)

        //is line me error dikha raha h
        applicationContext.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,null,null,
            MediaStore.Video.Media.DISPLAY_NAME
        )?.use {cursor->
            while (cursor.moveToNext()) {

                    val titleC =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE))
                    val idC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID))
                    val folderC =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
                    val sizeC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE))
                    val pathC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
                    val durationC =
                        cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION))
                    try {
                        val file = File(pathC)
                        val artUriC = Uri.fromFile(file)
                        val video = Video(
                            title = titleC, id = idC, folderName = folderC, duration = durationC,
                            size = sizeC, path = pathC, arUri = artUriC
                        )
                        if (file.exists()) tempList.add(video)
                    } catch (e: Exception) {
                    }

            }
            cursor.close()

        }
        return tempList
    }

}