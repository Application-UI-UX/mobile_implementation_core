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

package com.github;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import com.github.node.DefaultNodeMainExecutor;
import com.github.node.NodeConfiguration;
import com.github.node.NodeMainExecutor;

import java.util.concurrent.TimeUnit;

/**
 * This is a base class for tests that sets up and tears down a {@link RosCore}
 * and a {@link NodeMainExecutor}.
 * 
 * @author damonkohler@google.com (Damon Kohler)
 */
@Ignore
public abstract class RosTest {

  protected RosCore rosCore;
  protected NodeConfiguration nodeConfiguration;
  protected NodeMainExecutor nodeMainExecutor;

  @Before
  public void setUp() throws InterruptedException {
    rosCore = RosCore.newPrivate();
    rosCore.start();
    assertTrue(rosCore.awaitStart(1, TimeUnit.SECONDS));
    nodeMainExecutor = DefaultNodeMainExecutor.newDefault();
    nodeConfiguration = NodeConfiguration.newPrivate(rosCore.getUri());
  }

  @After
  public void tearDown() {
    nodeMainExecutor.shutdown();
    rosCore.shutdown();
  }
}
