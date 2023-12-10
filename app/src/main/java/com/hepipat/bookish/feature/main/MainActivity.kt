package com.hepipat.bookish.feature.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.hepipat.bookish.R
import com.hepipat.bookish.databinding.ActivityMainBinding
import com.hepipat.bookish.databinding.DialogCloseAppBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.container)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment, R.id.myBooksFragment
                -> {
                    binding.bottomNavigation.setupWithNavController(navController)
                    binding.bottomNavigation.visibility = View.VISIBLE
                }
                else -> {
                    binding.bottomNavigation.visibility = View.GONE
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
            }
        }
        return super.onContextItemSelected(item)
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        var backState = false
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment,
                R.id.loginFragment-> {
                    backState = false
                }
                else -> {
                    backState = true
                }
            }
        }
        if (backState) {
            onBackPressedDispatcher.onBackPressed()
        } else {
            quitDialog()
        }
    }

    private fun quitDialog() {
        val quitApps = DialogCloseAppBinding.inflate(LayoutInflater.from(this))
        val quitDialogBuilder = AlertDialog.Builder(this, R.style.RoundedCornerDialog)
            .setView(quitApps.root)
        quitDialogBuilder.setCancelable(true)
        val showQuitDialog = quitDialogBuilder.show()
        quitApps.btnClose.setOnClickListener {
            finish()
        }
        quitApps.btnDismiss.setOnClickListener {
            showQuitDialog.cancel()
        }

    }
}