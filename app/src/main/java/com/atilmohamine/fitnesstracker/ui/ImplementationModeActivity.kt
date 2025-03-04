package com.atilmohamine.fitnesstracker.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.health.connect.client.PermissionController
import androidx.lifecycle.lifecycleScope
import com.atilmohamine.fitnesstracker.R
import com.atilmohamine.fitnesstracker.data.HealthConnectAvailability
import com.atilmohamine.fitnesstracker.data.ImplementationMode
import com.atilmohamine.fitnesstracker.utils.healthConnectPermissions
import com.atilmohamine.fitnesstracker.viewmodel.FitnessViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class ImplementationModeActivity : AppCompatActivity() {

    private lateinit var selectHealthConnectButton: Button
    private lateinit var selectFitButton: Button

    private val fitnessViewModel: FitnessViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_implementation_mode)
        selectHealthConnectButton = findViewById(R.id.switch_to_hc_button)
        selectFitButton = findViewById(R.id.maybe_later_button)

        if (healthConnectManager.availability.value == HealthConnectAvailability.NOT_SUPPORTED) {
            goToMainScreen(ImplementationMode.GOOGLE_FIT_API)
        } else {
            if (fitnessViewModel.loadImplementationMode() == ImplementationMode.GOOGLE_HEALTH_CONNECT.code) {
                lifecycleScope.launch {
                    if (healthConnectManager.hasAllPermissions(healthConnectPermissions)) {
                        selectHealthConnect()
                    } else {
                        setUpButtons()
                    }
                }
            } else {
                setUpButtons()
            }
        }
    }

    private fun setUpButtons() {
        selectHealthConnectButton.setOnClickListener {
            selectHealthConnect()
        }

        selectFitButton.setOnClickListener {
            goToMainScreen(ImplementationMode.GOOGLE_FIT_API)
        }
    }

    private fun goToMainScreen(implementationMode: ImplementationMode) {
        fitnessViewModel.saveImplementationMode(implementationMode)
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    }

    // Health Connect API
    private val healthConnectManager by lazy { (application as BaseApplication).healthConnectManager }

    private fun selectHealthConnect() {
        healthConnectManager.checkAvailability()
        if (healthConnectManager.availability.value == HealthConnectAvailability.NOT_INSTALLED) {
            val url = Uri.parse(getString(R.string.market_url))
                .buildUpon()
                .appendQueryParameter("id", getString(R.string.health_connect_package))
                .appendQueryParameter("url", getString(R.string.onboarding_url))
                .build()
            startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(url.toString()))
            )
        } else {
            lifecycleScope.launch {
                requestHealthPermissions()
            }
        }
    }

    private suspend fun requestHealthPermissions() {
        val permissionsGranted = healthConnectManager.hasAllPermissions(healthConnectPermissions)
        if (permissionsGranted) {
            goToMainScreen(ImplementationMode.GOOGLE_HEALTH_CONNECT)
        } else {
            requestPermissionsLauncher.launch(healthConnectPermissions)
        }
    }

    private val requestPermissionsLauncher = registerForActivityResult(
        PermissionController.createRequestPermissionResultContract()
    ) { result ->
        if (result.containsAll(healthConnectPermissions)) {
            Snackbar.make(
                findViewById(android.R.id.content),
                "All permissions granted!",
                Snackbar.LENGTH_INDEFINITE
            )
            goToMainScreen(ImplementationMode.GOOGLE_HEALTH_CONNECT)
        } else {
            Snackbar.make(
                findViewById(android.R.id.content),
                "Some permissions were denied",
                Snackbar.LENGTH_INDEFINITE
            )
        }
    }
}