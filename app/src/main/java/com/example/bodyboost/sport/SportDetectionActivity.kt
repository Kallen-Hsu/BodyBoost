package com.example.bodyboost.sport

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Chronometer
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.bodyboost.R
import com.example.bodyboost.R.*
import com.example.bodyboost.databinding.ActivityMainBinding
import com.example.bodyboost.databinding.ActivitySportDetectionBinding
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import org.w3c.dom.Text
import java.nio.ByteBuffer
import com.example.bodyboost.ml.Squat
import com.example.bodyboost.sport.fragment.CameraFragment
import com.google.mediapipe.formats.proto.LandmarkProto
import com.google.mediapipe.framework.ProtoUtil

class SportDetectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySportDetectionBinding
    private var cameraFragment: CameraFragment? = null
    private var testFrame: FrameLayout? = null
    private var testView: View? = null
    private lateinit var poseLandmarkerHelper: PoseLandmarkerHelper
    private lateinit var animationView: ImageView
    private lateinit var cm : Chronometer
    private lateinit var finishSportBtn : ImageButton
    private lateinit var sportClass:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySportDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        ProtoUtil.registerTypeName(
            LandmarkProto.NormalizedLandmarkList::class.java,
            "mediapipe.NormalizedLandmarkList"
        )
        cameraFragment = supportFragmentManager.findFragmentById(R.id.camera_fragment) as? CameraFragment

        testFrame = findViewById(R.id.test_fragment_container)
        val layoutInflater = LayoutInflater.from(this)
        testView = layoutInflater.inflate(R.layout.state, null)
        testFrame?.addView(testView)
        animationView = testView?.findViewById(R.id.sport_detection_animation)!!
        sportClass = findViewById(R.id.sportClass)
        // get sport data from SportFragment
        val sportId  = intent.getIntExtra("id", 0)
        val sportName = intent.getStringExtra("name").toString()
        val sportType = intent.getStringExtra("sport_type").toString()
        val sportIsCount = intent.getBooleanExtra("is_count", false)
        var time = 0
        var counting = 0
        if (sportType == "timing") {
            time = intent.getIntExtra("time", 0)
        } else if (sportType == "counting") {
            counting = intent.getIntExtra("counting", 0)
        }
        val sportAnimation = intent.getStringExtra("animation").toString()
        val sportMet = intent.getFloatExtra("met", 0.0f)
        val sportInflater:LayoutInflater  = layoutInflater

        if (cameraFragment == null) {

            cameraFragment = CameraFragment()
            cameraFragment!!.setListener { result ->
                processResult(result)
            }
            fragmentTransaction.replace(R.id.camera_fragment_container, cameraFragment!!)
            fragmentTransaction.commit()
        }


        Glide.with(animationView).load(sportAnimation).into(animationView)
        cm = findViewById(R.id.timer)
        cm.start()


        finishSportBtn = testView!!.findViewById(R.id.finishSportBtn)
        finishSportBtn.setOnClickListener {
            // Use the NavController to navigate to the desired destination
//                findNavController().navigate(R.id.finishedFragment) // Replace "action_camera_to_finished" with the actual action name in your navigation graph
            val intent = Intent(applicationContext, SportFinishedActivity::class.java)
            cm.stop()
            val seconds = cm.text.toString()
            intent.putExtra("seconds", seconds)
            intent.putExtra("name", sportName)
            intent.putExtra("met", sportMet)
            startActivity(intent)
        }
    }
    private fun processResult(poseLandmarkerResult: PoseLandmarkerResult) {
        val landmarks = poseLandmarkerResult.landmarks()

        if (landmarks != null) {
            val model = Squat.newInstance(this)

            if(model != null){
                // Create ByteBuffer for model input
                val inputBuffer = ByteBuffer.allocateDirect(1 * 33 * 4 * 4) // 1x33x4 float32 values (4 bytes each)

                for (i in landmarks.indices) {
                    val landmarkList = landmarks[i]
                    println("PoseLandmarkerResult:")
                    println("Landmarks for Pose #$i:")

                    for (j in landmarkList.indices) {
                        val landmark = landmarkList[j]
                        println(j)
                        // Convert Landmark data to ByteBuffer
                        inputBuffer.putFloat(landmark.x().toFloat())
                        inputBuffer.putFloat(landmark.y().toFloat())
                        inputBuffer.putFloat(landmark.z().toFloat())

                        val visibility = landmark.visibility()
                        if (visibility != null && visibility.isPresent) {
                            inputBuffer.putFloat(visibility.get())
                        } else {
                            println("visibility unavailable")
                        }
                    }
                }

                inputBuffer.rewind()
                val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 33 * 4), DataType.FLOAT32)
                inputFeature0.loadBuffer(inputBuffer)

                // Runs model inference and gets result.
                val outputs = model.process(inputFeature0)
                val outputFeature0 = outputs.outputFeature0AsTensorBuffer

                // Get the model's prediction (0 or 1)
                val prediction = if (outputFeature0.getFloatValue(0) > outputFeature0.getFloatValue(1)) {
                    "down"
                } else {
                    "up"
                }
                sportClass.text = prediction
                println("Model Prediction: $prediction")

                // Releases model resources if no longer used.
                model.close()
            }
            else{
                println("Model is Null!")
            }

        }
    }

    override fun onBackPressed() {
        finish()
    }
}


