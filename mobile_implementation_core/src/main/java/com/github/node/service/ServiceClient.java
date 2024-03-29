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

package com.github.node.service;

import com.github.namespace.GraphName;

import java.net.URI;

/**
 * Provides a connection to a ROS service.
 * 
 * @author damonkohler@google.com (Damon Kohler)
 * 
 * @param <T>
 *          the {@link ServiceServer} responds to requests of this type
 * @param <S>
 *          the {@link ServiceServer} returns responses of this type
 */
public interface ServiceClient<T, S> {

  /**
   * Connects to a {@link ServiceServer}.
   * 
   * @param uri
   *          the {@link URI} of the {@link ServiceServer} to connect to
   */
  void connect(URI uri);

  /**
   * @return {@code true} if the {@link ServiceClient} is connected
   */
  boolean isConnected();

  /**
   * Calls a method on the {@link ServiceServer}.
   * 
   * @param request
   *          the request message
   * @param listener
   *          the {@link ServiceResponseListener} that will handle the response
   *          to this request
   */
  void call(T request, ServiceResponseListener<S> listener);

  /**
   * @return the name of the service this {@link ServiceClient} is connected to
   */
  GraphName getName();

  /**
   * Stops the client (e.g. disconnect a persistent service connection).
   */
  void shutdown();

  /**
   * @return a new request message
   */
  T newMessage();
}
