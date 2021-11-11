package com.example.buswitcher

import android.app.Application
import com.salesforce.marketingcloud.MarketingCloudConfig
import com.salesforce.marketingcloud.notifications.NotificationCustomizationOptions

object MarketingCloudConfigs {

    fun configA(context: Application): MarketingCloudConfig = MarketingCloudConfig.builder()
        .setApplicationId("5c65b582-6eb9-4da4-9b3e-77285e0e4d67")
        .setAccessToken("012345678901234567891234")
        .setMarketingCloudServerUrl("https://consumer.exacttargetapis.com")
        .setNotificationCustomizationOptions(NotificationCustomizationOptions.create(R.mipmap.ic_launcher))
        .build(context)

    fun configB(context: Application): MarketingCloudConfig = MarketingCloudConfig.builder()
        .setApplicationId("7efcdef4-1f19-420f-9c4a-3d3d60f84ef7")
        .setAccessToken("987654312098765432109876")
        .setMarketingCloudServerUrl("https://consumer.exacttargetapis.com")
        .setNotificationCustomizationOptions(NotificationCustomizationOptions.create(R.mipmap.ic_launcher))
        .build(context)

}