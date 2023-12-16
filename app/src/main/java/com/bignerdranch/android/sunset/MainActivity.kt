package com.bignerdranch.android.sunset

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bignerdranch.android.sunset.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sunsetAnimatorSet: AnimatorSet
    private lateinit var sunriseAnimatorSet: AnimatorSet

    private val blueSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.blue_sky)
    }
    private val sunsetSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.sunset_sky)
    }
    private val nightSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.night_sky)
    }

    // keep track of the current state
    private var isSunset = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeSunsetAnimation()
        initializeSunriseAnimation()

        // view toggles btwn sunset & sunrise animations (using current state) to start the appropriate animator
        binding.scene.setOnClickListener {
            if (isSunset) {
                sunriseAnimatorSet.start()
            } else {
                sunsetAnimatorSet.start()
            }
            isSunset = !isSunset
        }
    }

    // individual sunset animation
    private fun initializeSunsetAnimation() {
        val sunYStart = binding.sun.translationY
        val sunYEnd = binding.sky.height.toFloat() - binding.sun.height.toFloat()

        val heightAnimator = ObjectAnimator
            .ofFloat(binding.sun, View.TRANSLATION_Y, sunYStart, sunYEnd)
            .setDuration(3000)
        heightAnimator.interpolator = AccelerateInterpolator()

        val sunsetSkyAnimator = ObjectAnimator
            .ofInt(binding.sky, "backgroundColor", blueSkyColor, sunsetSkyColor)
            .setDuration(3000)
        sunsetSkyAnimator.setEvaluator(ArgbEvaluator())

        val nightSkyAnimator = ObjectAnimator
            .ofInt(binding.sky, "backgroundColor", sunsetSkyColor, nightSkyColor)
            .setDuration(1500)
        nightSkyAnimator.setEvaluator(ArgbEvaluator())

        sunsetAnimatorSet = AnimatorSet()
        sunsetAnimatorSet.play(heightAnimator)
            .with(sunsetSkyAnimator)
            .before(nightSkyAnimator)
    }

    // individual sunrise animation
    private fun initializeSunriseAnimation() {
        val sunYStart = binding.sky.height.toFloat() - binding.sun.height.toFloat()
        val sunYEnd = binding.sun.translationY

        val heightAnimator = ObjectAnimator
            .ofFloat(binding.sun, View.TRANSLATION_Y, sunYStart, sunYEnd)
            .setDuration(3000)
        heightAnimator.interpolator = AccelerateInterpolator()

        val sunriseSkyAnimator = ObjectAnimator
            .ofInt(binding.sky, "backgroundColor", nightSkyColor, sunsetSkyColor)
            .setDuration(3000)
        sunriseSkyAnimator.setEvaluator(ArgbEvaluator())

        val blueSkyAnimator = ObjectAnimator
            .ofInt(binding.sky, "backgroundColor", sunsetSkyColor, blueSkyColor)
            .setDuration(1500)
        blueSkyAnimator.setEvaluator(ArgbEvaluator())

        sunriseAnimatorSet = AnimatorSet()
        sunriseAnimatorSet.play(heightAnimator)
            .with(sunriseSkyAnimator)
            .before(blueSkyAnimator)
    }
}