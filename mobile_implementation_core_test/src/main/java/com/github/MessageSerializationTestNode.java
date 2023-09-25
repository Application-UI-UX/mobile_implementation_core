/*
 * Copyright (C) 2012 Google Inc.
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

import nav_msgs.Odometry;
import com.github.message.MessageListener;
import com.github.namespace.GraphName;
import com.github.node.ConnectedNode;
import com.github.node.Node;
import com.github.node.NodeMain;
import com.github.node.topic.Publisher;
import com.github.node.topic.Subscriber;

/**
 * @author damonkohler@google.com (Damon Kohler)
 */
public class MessageSerializationTestNode implements NodeMain {

  @Override
  public GraphName getDefaultNodeName() {
    return GraphName.of("message_serialization_test_node");
  }

  @Override
  public void onStart(ConnectedNode connectedNode) {
    final Publisher<nav_msgs.Odometry> publisher =
        connectedNode.newPublisher("odom_echo", nav_msgs.Odometry._TYPE);
    Subscriber<nav_msgs.Odometry> subscriber =
        connectedNode.newSubscriber("odom", nav_msgs.Odometry._TYPE);
    subscriber.addMessageListener(new MessageListener<Odometry>() {
      @Override
      public void onNewMessage(Odometry message) {
        publisher.publish(message);
      }
    });
  }

  @Override
  public void onShutdown(Node node) {
  }

  @Override
  public void onShutdownComplete(Node node) {
  }

  @Override
  public void onError(Node node, Throwable throwable) {
  }
}
