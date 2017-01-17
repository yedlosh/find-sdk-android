package com.orchestral.findsdk.wifi;

import com.orchestral.findsdk.wifi.model.WifiAccessPoint;

import java.util.List;

/**
 * Copyright © 2017. Orion Health. All rights reserved.
 */

public interface WifiFingerprintAgent {

    List<WifiAccessPoint> getWifiFingerprint();

}
