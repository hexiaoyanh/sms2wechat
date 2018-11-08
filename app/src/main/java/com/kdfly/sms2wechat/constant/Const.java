package com.kdfly.sms2wechat.constant;

public interface Const {

    String PROJECT_SOURCE_CODE_URL = "https://github.com/tianma8023/SmsCodeExtractor";

    String ALIPAY_PACKAGE_NAME = "com.eg.android.AlipayGphone";
    String ALIPAY_QRCODE_URI_PREFIX = "alipayqr://platformapi/startapp?saId=10000007&qrcode=";
    String ALIPAY_QRCODE_URL = "HTTPS://QR.ALIPAY.COM/FKX074142EKXD0OIMV8B60";

    String WECHAT_PACKAGE_NAME = "com.tencent.mm";
    String WECHAT_LAUNCHER_UI = WECHAT_PACKAGE_NAME + ".ui.LauncherUI";
    String WECHAT_QR_REWARD_SELECT_MONEY_UI = WECHAT_PACKAGE_NAME +
            ".plugin.collect.reward.ui.QrRewardSelectMoneyUI";
    String WECHAT_KEY_EXTRA_DONATE = "TianmaDonate";
    String WECHAT_QRCODE_URL = "m01pPa@:hEyGJ5P*a1@$xPI";

    String PROJECT_DOC_BASE_URL = "https://tianma8023.github.io/SmsCodeExtractor";
    String DOC_APPOPS_ADB_HELP = "appops_adb_help";
    String DOC_SMS_CODE_RULE_HELP ="sms_code_rule_help";
}
