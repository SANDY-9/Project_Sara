package com.xten.sara.ui

import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View.*
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.xten.sara.R
import com.xten.sara.databinding.ActivityMainBinding
import com.xten.sara.util.*
import com.xten.sara.util.constants.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomNavigation()
    }

    private fun initBottomNavigation() = binding.navBottom.apply {
        setupWithNavController(getNavController())
        setOnItemReselectedListener { /* NO-OP */ }
    }
    private fun getNavController() = run {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.navController.apply {
            graph.setStartDestination(R.id.homeFragment)
            addOnDestinationChangedListener { _, destination, _ ->
                setNavigateDestinationChangeAction(destination.label)
            }
        }
    }
    private fun setNavigateDestinationChangeAction(label: CharSequence?) {
        setWindowTransparent(label)
        controlBottomNavVisibility(label)
    }

    @Suppress("DEPRECATION")
    private fun setWindowTransparent(label: CharSequence?) = label?.let {
        window?.apply {
            statusBarColor = when (it) {
                com.example.common.LABEL_IMAGE_RESULT_ -> Color.TRANSPARENT
                else -> Color.WHITE
            }
            decorView.systemUiVisibility = when(label) {
                com.example.common.LABEL_IMAGE_RESULT_ -> SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LAYOUT_STABLE or SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                else -> SYSTEM_UI_FLAG_LAYOUT_STABLE or SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }


    private fun controlBottomNavVisibility(label: CharSequence?) = label?.let {
        with(binding.navBottom) {
            visibility = when(label) {
                com.example.common.LABEL_HOME_, com.example.common.LABEL_GALLERY_, com.example.common.LABEL_MY_ -> VISIBLE.also { binding.shadow.visibility = VISIBLE }
                else -> GONE.also { binding.shadow.visibility = GONE }
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        ev?.run {
            currentFocus?.let {
                val rect = Rect()
                it.getGlobalVisibleRect(rect)
                if(!rect.contains(x.toInt(), y.toInt())) it.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

}