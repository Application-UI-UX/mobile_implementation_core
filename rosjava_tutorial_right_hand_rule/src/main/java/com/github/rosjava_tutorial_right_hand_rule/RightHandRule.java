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

package com.github.rosjava_tutorial_right_hand_rule;

import com.github.message.MessageListener;
import com.github.namespace.GraphName;
import com.github.node.AbstractNodeMain;
import com.github.node.ConnectedNode;
import com.github.node.topic.Publisher;
import com.github.node.topic.Subscriber;

/**
 * @author damonkohler@google.com (Damon Kohler)
 */
public class RightHandRule extends AbstractNodeMain {

  @Override
  public GraphName getDefaultNodeName() {
    return GraphName.of("right_hand_rule");
  }

  @Override
  public void onStart(ConnectedNode connectedNode) {
    final Publisher<geometry_msgs.Twist> publisher =
        connectedNode.newPublisher("cmd_vel", geometry_msgs.Twist._TYPE);
    final geometry_msgs.Twist twist = publisher.newMessage();
    final Subscriber<sensor_msgs.LaserScan> subscriber =
        connectedNode.newSubscriber("base_scan", sensor_msgs.LaserScan._TYPE);
    subscriber.addMessageListener(new MessageListener<sensor_msgs.LaserScan>() {
      @Override
      public void onNewMessage(sensor_msgs.LaserScan message) {
        float[] ranges = message.getRanges();
        float northRange = ranges[ranges.length / 2];
        float northEastRange = ranges[ranges.length / 3];
        double linearVelocity = 0.5;
        double angularVelocity = -0.5;
        if (northRange < 1. || northEastRange < 1.) {
          linearVelocity = 0;
          angularVelocity = 0.5;
        }
        twist.getAngular().setZ(angularVelocity);
        twist.getLinear().setX(linearVelocity);
        publisher.publish(twist);
      }
    });
  }
}
