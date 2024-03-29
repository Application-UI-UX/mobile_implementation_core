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

package com.github.mobile_implementation_core_tutorial_services;

import com.github.namespace.GraphName;
import com.github.node.AbstractNodeMain;
import com.github.node.ConnectedNode;
import com.github.node.NodeMain;
import com.github.node.service.ServiceResponseBuilder;
import com.github.node.service.ServiceServer;

/**
 * This is a simple {@link ServiceServer} {@link NodeMain}.
 * 
 * @author damonkohler@google.com (Damon Kohler)
 */
public class Server extends AbstractNodeMain {

  @Override
  public GraphName getDefaultNodeName() {
    return GraphName.of("mobile_implementation_core_tutorial_services/server");
  }

  @Override
  public void onStart(ConnectedNode connectedNode) {
    connectedNode.newServiceServer("add_two_ints", mobile_implementation_core_test_msgs.AddTwoInts._TYPE,
        new ServiceResponseBuilder<mobile_implementation_core_test_msgs.AddTwoIntsRequest, mobile_implementation_core_test_msgs.AddTwoIntsResponse>() {
          @Override
          public void
              build(mobile_implementation_core_test_msgs.AddTwoIntsRequest request, mobile_implementation_core_test_msgs.AddTwoIntsResponse response) {
            response.setSum(request.getA() + request.getB());
          }
        });
  }
}
