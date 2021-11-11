package com.example.buswitcher

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.salesforce.marketingcloud.sfmcsdk.SFMCSdk
import com.salesforce.marketingcloud.sfmcsdk.SFMCSdkModuleConfig

class MainActivity : AppCompatActivity() {

    private var isConfigA = true
    private lateinit var appId: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button).apply {
            setOnClickListener {
                reconfigureSdk(application)
            }
        }

        appId = findViewById(R.id.tv_config_app_id)
        updateUI()

    }

    private fun reconfigureSdk(context: Application) {
        SFMCSdk.requestSdk {
            it.mp {
                Log.v("~#PushBefore", it.toString())
                SFMCSdk.configure(context, SFMCSdkModuleConfig.build {
                    pushModuleConfig = if (isConfigA) MarketingCloudConfigs.configB(context) else
                        MarketingCloudConfigs.configA(context)
                }.also {
                    isConfigA = !isConfigA
                }) {
                    SFMCSdk.requestSdk {
                        it.mp {
                            Log.v("~#PushDuring", it.toString())
                        }
                    }
                    updateUI()
                }
            }
        }
        SFMCSdk.requestSdk {
            it.mp {
                // Initializing Push will empty the pending request queue & this will never be seen
                Log.v("~#PushAfter", it.toString())
            }
        }
    }

    private fun updateUI() {
        SFMCSdk.requestSdk { uSdk ->
            uSdk.mp { pushSdk ->
                appId.text = pushSdk.state["initConfig"].toString()
                Log.v("~#PushUpdateUI", pushSdk.toString())
            }
        }
    }
}