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

package com.github.time;

import com.google.common.base.Preconditions;

import com.github.Topics;
import com.github.internal.node.DefaultNode;
import com.github.message.MessageListener;
import com.github.message.Time;
import com.github.node.topic.Subscriber;
import rosgraph_msgs.Clock;

/**
 * A {@link TimeProvider} for use when the ROS graph is configured for
 * simulation.
 * 
 * @author damonkohler@google.com (Damon Kohler)
 */
public class ClockTopicTimeProvider implements TimeProvider {

  private final Subscriber<rosgraph_msgs.Clock> subscriber;

  private Object mutex;
  private rosgraph_msgs.Clock clock;

  public ClockTopicTimeProvider(DefaultNode defaultNode) {
    subscriber = defaultNode.newSubscriber(Topics.CLOCK, rosgraph_msgs.Clock._TYPE);
    mutex = new Object();
    subscriber.addMessageListener(new MessageListener<Clock>() {
      @Override
      public void onNewMessage(Clock message) {
        synchronized (mutex) {
          clock = message;
        }
      }
    });
  }

  public Subscriber<rosgraph_msgs.Clock> getSubscriber() {
    return subscriber;
  }

  @Override
  public Time getCurrentTime() {
    // When using simulation time, the ROS Time API will return time=0 until it has
    // received a
    // message from the /clock topic.
    if (clock == null) {
      return new Time();
    }

    synchronized (mutex) {
      return new Time(clock.getClock());
    }
  }
}
