package com.example.buswitcher

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.salesforce.marketingcloud.MarketingCloudSdk
import com.salesforce.marketingcloud.sfmcsdk.SFMCSdk
import com.salesforce.marketingcloud.sfmcsdk.SFMCSdkModuleConfig

class MainActivity : AppCompatActivity() {

    private var isConfigA = true
    private lateinit var appId: TextView
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button).apply {
            setOnClickListener {
                reconfigureSdk(application)
                handler.postDelayed({ getFinalSDKStates() }, 2000L)
            }
        }
        findViewById<Button>(R.id.addTag).apply {
            setOnClickListener {
                SFMCSdk.requestSdk {
                    it.mp { push ->
                        val tag = "sssdsd" + System.currentTimeMillis();
                        val isCommitted =
                            push.registrationManager.edit()
                                .addTag(tag)
                                .commit()
                        Log.v("~#PushAddTag", "Tag Added: $tag + Is committed : $isCommitted")
                    }
                }
            }
        }

        appId = findViewById(R.id.tv_config_app_id)
        updateUI()
    }

    private fun reconfigureSdk(context: Application) {
        SFMCSdk.requestSdk {
            it.mp { prevSdk ->
                Log.v("~#PushBefore", prevSdk.toString())

                // Configure new ..
                SFMCSdk.configure(context, SFMCSdkModuleConfig.build {
                    pushModuleConfig = if (isConfigA) MarketingCloudConfigs.configB(context) else
                        MarketingCloudConfigs.configA(context)
                }.also {
                    isConfigA = !isConfigA
                }) {
                    Log.v("~PushStatus - SFMC", "Status : ${it.status}")
                    updateUI()
                }
            }
        }

        SFMCSdk.requestSdk {
            it.mp { sdk ->
                // Initializing Push will empty the pending request queue & this will never be seen
                Log.v("~#PushAfter", sdk.toString())
            }
        }
    }

    private fun updateUI() {
        SFMCSdk.requestSdk { uSdk ->
            uSdk.mp { pushSdk ->
                appId.text = pushSdk.state["initConfig"].toString()
                Log.v("~#PushInstance - SFMC", pushSdk.toString())
            }
        }
    }

    private fun getFinalSDKStates() {
        Log.v("~#PushInstance- Delay", "SDK States after 2000 ms")
        SFMCSdk.requestSdk {
            it.mp { mp ->
                Log.v("~#PushInstance-SFMC way", mp.toString())
            }
        }

        MarketingCloudSdk.requestSdk {
            Log.v("~#PushInstance-SDK way", it.toString())
        }
    }
}