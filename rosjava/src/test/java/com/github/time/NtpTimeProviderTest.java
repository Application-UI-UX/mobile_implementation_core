/*
 * Copyright (C) 2011 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.github.time;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import com.github.RosTest;
import com.github.address.InetAddressFactory;
import com.github.namespace.GraphName;
import com.github.node.AbstractNodeMain;
import com.github.node.ConnectedNode;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author damonkohler@google.com (Damon Kohler)
 */
public class NtpTimeProviderTest extends RosTest {

  @Test
  public void testNtpUbuntuCom() throws InterruptedException {
    final NtpTimeProvider ntpTimeProvider =
        new NtpTimeProvider(InetAddressFactory.newFromHostString("ntp.ubuntu.com"),
            Executors.newScheduledThreadPool(Integer.MAX_VALUE));
    final CountDownLatch latch = new CountDownLatch(1);
    nodeConfiguration.setTimeProvider(ntpTimeProvider);
    nodeMainExecutor.execute(new AbstractNodeMain() {
      @Override
      public GraphName getDefaultNodeName() {
        return GraphName.of("ntp_time_provider");
      }

      @Override
      public void onStart(ConnectedNode connectedNode) {
        try {
          ntpTimeProvider.updateTime();
        } catch (IOException e) {
          // Ignored. This is only a sanity check.
        }
        ntpTimeProvider.getCurrentTime();
        System.out.println("System time: " + System.currentTimeMillis());
        System.out.println("NTP time: " + ntpTimeProvider.getCurrentTime().totalNsecs() / 1000000);
        latch.countDown();
      }
    }, nodeConfiguration);
    boolean result = latch.await(10, TimeUnit.SECONDS);
    //System.out.println("Latch waiting : " + latch.getCount() + " " + result + " [" + System.currentTimeMillis() + "]");
    assertTrue(result);
  }
}
