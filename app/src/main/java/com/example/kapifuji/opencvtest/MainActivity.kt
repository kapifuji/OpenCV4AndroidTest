package com.example.kapifuji.opencvtest

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity

import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log

import org.opencv.android.BaseLoaderCallback
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader
import org.opencv.core.Mat
import org.opencv.android.CameraBridgeViewBase


import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val logTag = "MainActivity"
    val CAMERA = 0

    private val loaderCallback = object :BaseLoaderCallback(this){
        override fun onManagerConnected(status: Int) = when(status) {
            LoaderCallbackInterface.SUCCESS -> {
                Log.i(logTag, "OpenCV load success")
                camera_view.enableView()
            }
            else -> super.onManagerConnected(status)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        camera_view.setCvCameraViewListener(cameraViewListener)
    }

    override fun onResume() {
        super.onResume()
        when (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
            PackageManager.PERMISSION_GRANTED -> {}
            PackageManager.PERMISSION_DENIED -> {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA)
            }
        }
        if(OpenCVLoader.initDebug() == true){
            Log.d(logTag, "use internal OpenCV")
            loaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
        }
        else{
            Log.d(logTag, "use OpenCV Manager")
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, loaderCallback)
        }
    }

    override fun onPause() {
        super.onPause()
        camera_view.disableView()
    }

    override fun onStop() {
        super.onStop()
        camera_view.disableView()
    }

    override fun onDestroy() {
        super.onDestroy()
        camera_view.disableView()
    }

    private val cameraViewListener = object : CameraBridgeViewBase.CvCameraViewListener2 {
        override fun onCameraViewStarted(width: Int, height: Int) {
        }

        override fun onCameraViewStopped() {
        }

        override fun onCameraFrame(frame: CameraBridgeViewBase.CvCameraViewFrame): Mat {
            Log.i(logTag, "kita")
            var a = arrayOf(frame.rgba(), frame.gray())
            return a[1]
        }
    }

    
}
