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

import com.github.exception.RemoteException;
import com.github.exception.RosRuntimeException;
import com.github.exception.ServiceNotFoundException;
import com.github.namespace.GraphName;
import com.github.node.AbstractNodeMain;
import com.github.node.ConnectedNode;
import com.github.node.NodeMain;
import com.github.node.service.ServiceClient;
import com.github.node.service.ServiceResponseListener;

/**
 * A simple {@link ServiceClient} {@link NodeMain}.
 * 
 * @author damonkohler@google.com (Damon Kohler)
 */
public class Client extends AbstractNodeMain {

  @Override
  public GraphName getDefaultNodeName() {
    return GraphName.of("mobile_implementation_core_tutorial_services/client");
  }

  @Override
  public void onStart(final ConnectedNode connectedNode) {
    ServiceClient<mobile_implementation_core_test_msgs.AddTwoIntsRequest, mobile_implementation_core_test_msgs.AddTwoIntsResponse> serviceClient;
    try {
      serviceClient = connectedNode.newServiceClient("add_two_ints", mobile_implementation_core_test_msgs.AddTwoInts._TYPE);
    } catch (ServiceNotFoundException e) {
      throw new RosRuntimeException(e);
    }
    final mobile_implementation_core_test_msgs.AddTwoIntsRequest request = serviceClient.newMessage();
    request.setA(2);
    request.setB(2);
    serviceClient.call(request, new ServiceResponseListener<mobile_implementation_core_test_msgs.AddTwoIntsResponse>() {
      @Override
      public void onSuccess(mobile_implementation_core_test_msgs.AddTwoIntsResponse response) {
        connectedNode.getLog().info(
            String.format("%d + %d = %d", request.getA(), request.getB(), response.getSum()));
      }

      @Override
      public void onFailure(RemoteException e) {
        throw new RosRuntimeException(e);
      }
    });
  }
}
