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

package com.github.internal.node.client;

import com.github.internal.node.response.IntegerResultFactory;
import com.github.internal.node.response.Response;
import com.github.internal.node.response.SystemStateResultFactory;
import com.github.internal.node.response.TopicListResultFactory;
import com.github.internal.node.response.TopicTypeListResultFactory;
import com.github.internal.node.response.UriListResultFactory;
import com.github.internal.node.response.UriResultFactory;
import com.github.internal.node.response.VoidResultFactory;
import com.github.internal.node.server.NodeIdentifier;
import com.github.internal.node.server.SlaveServer;
import com.github.internal.node.server.master.MasterServer;
import com.github.internal.node.topic.PublisherDeclaration;
import com.github.internal.node.topic.PublisherIdentifier;
import com.github.internal.node.topic.TopicDeclaration;
import com.github.internal.node.xmlrpc.MasterXmlRpcEndpoint;
import com.github.master.client.SystemState;
import com.github.master.client.TopicSystemState;
import com.github.master.client.TopicType;
import com.github.namespace.GraphName;
import com.github.node.service.ServiceServer;
import com.github.node.topic.Publisher;
import com.github.node.topic.Subscriber;

import java.net.URI;
import java.util.List;

/**
 * Provides access to the XML-RPC API exposed by a {@link MasterServer}.
 * 
 * @author damonkohler@google.com (Damon Kohler)
 */
public class MasterClient extends Client<MasterXmlRpcEndpoint> {

  /**
   * Create a new {@link MasterClient} connected to the specified
   * {@link MasterServer} URI.
   * 
   * @param uri
   *          the {@link URI} of the {@link MasterServer} to connect to
   */
  public MasterClient(URI uri) {
    super(uri, MasterXmlRpcEndpoint.class);
  }

  /**
   * Registers the given {@link ServiceServer}.
   * 
   * @param slave
   *          the {@link NodeIdentifier} where the {@link ServiceServer} is
   *          running
   * @param service
   *          the {@link ServiceServer} to register
   * @return a void {@link Response}
   */
  public Response<Void> registerService(NodeIdentifier slave, ServiceServer<?, ?> service) {
    return Response.fromListChecked(xmlRpcEndpoint.registerService(slave.getName().toString(),
        service.getName().toString(), service.getUri().toString(), slave.getUri().toString()),
        new VoidResultFactory());
  }

  /**
   * Unregisters the specified {@link ServiceServer}.
   * 
   * @param slave
   *          the {@link NodeIdentifier} where the {@link ServiceServer} is
   *          running
   * @param service
   *          the {@link ServiceServer} to unregister
   * @return the number of unregistered services
   */
  public Response<Integer> unregisterService(NodeIdentifier slave, ServiceServer<?, ?> service) {
    return Response.fromListChecked(xmlRpcEndpoint.unregisterService(
        slave.getName().toString(), service.getName().toString(), service.getUri().toString()),
        new IntegerResultFactory());
  }

  /**
   * Registers the given {@link Subscriber}. In addition to receiving a list of
   * current {@link Publisher}s, the {@link Subscriber}s {@link SlaveServer}
   * will also receive notifications of new {@link Publisher}s via the
   * publisherUpdate API.
   * 
   * @param slave
   *          the {@link NodeIdentifier} that the {@link Subscriber} is running
   *          on
   * @param subscriber
   *          the {@link Subscriber} to register
   * @return a {@link List} or {@link SlaveServer} XML-RPC API URIs for nodes
   *         currently publishing the specified topic
   */
  public Response<List<URI>> registerSubscriber(NodeIdentifier slave, Subscriber<?> subscriber) {
    return Response.fromListChecked(xmlRpcEndpoint.registerSubscriber(slave.getName()
        .toString(), subscriber.getTopicName().toString(), subscriber.getTopicMessageType(), slave
        .getUri().toString()), new UriListResultFactory());
  }

  /**
   * Unregisters the specified {@link Subscriber}.
   * 
   * @param slave
   *          the {@link NodeIdentifier} where the subscriber is running
   * @param subscriber
   *          the {@link Subscriber} to unregister
   * @return the number of unregistered {@link Subscriber}s
   */
  public Response<Integer> unregisterSubscriber(NodeIdentifier slave, Subscriber<?> subscriber) {
    return Response.fromListChecked(xmlRpcEndpoint.unregisterSubscriber(slave.getName()
        .toString(), subscriber.getTopicName().toString(), slave.getUri().toString()),
        new IntegerResultFactory());
  }

  /**
   * Registers the specified {@link PublisherDeclaration}.
   * 
   * @param publisherDeclaration
   *          the {@link PublisherDeclaration} of the {@link Publisher} to
   *          register
   * @return a {@link List} of the current {@link SlaveServer} URIs which have
   *         {@link Subscriber}s for the published {@link TopicSystemState}
   */
  public Response<List<URI>> registerPublisher(PublisherDeclaration publisherDeclaration) {
    String slaveName = publisherDeclaration.getSlaveName().toString();
    String slaveUri = publisherDeclaration.getSlaveUri().toString();
    String topicName = publisherDeclaration.getTopicName().toString();
    String messageType = publisherDeclaration.getTopicMessageType();
    return Response.fromListChecked(
        xmlRpcEndpoint.registerPublisher(slaveName, topicName, messageType, slaveUri),
        new UriListResultFactory());
  }

  /**
   * Unregisters the specified {@link PublisherDeclaration}.
   * 
   * @param publisherIdentifier
   *          the {@link PublisherIdentifier} of the {@link Publisher} to
   *          unregister
   * @return the number of unregistered {@link Publisher}s
   */
  public Response<Integer> unregisterPublisher(PublisherIdentifier publisherIdentifier) {
    String slaveName = publisherIdentifier.getNodeName().toString();
    String slaveUri = publisherIdentifier.getNodeUri().toString();
    String topicName = publisherIdentifier.getTopicName().toString();
    return Response.fromListChecked(
        xmlRpcEndpoint.unregisterPublisher(slaveName, topicName, slaveUri),
        new IntegerResultFactory());
  }

  /**
   * @param slaveName
   *          the {@link GraphName} of the caller
   * @param nodeName
   *          the name of the {@link SlaveServer} to lookup
   * @return the {@link URI} of the {@link SlaveServer} with the given name
   */
  public Response<URI> lookupNode(GraphName slaveName, String nodeName) {
    return Response.fromListChecked(xmlRpcEndpoint.lookupNode(slaveName.toString(), nodeName),
        new UriResultFactory());
  }

  /**
   * @param slaveName
   *          the {@link NodeIdentifier} of the caller
   * @return the {@link URI} of the {@link MasterServer}
   */
  public Response<URI> getUri(GraphName slaveName) {
    return Response.fromListChecked(xmlRpcEndpoint.getUri(slaveName.toString()),
        new UriResultFactory());
  }

  /**
   * @param callerName
   *          the {@link GraphName} of the caller
   * @param serviceName
   *          the name of the {@link ServiceServer} to look up
   * @return the {@link URI} of the {@link ServiceServer} with the given name.
   *         {@link ServiceServer} as a result
   */
  public Response<URI> lookupService(GraphName callerName, String serviceName) {
    return Response.fromListCheckedFailure(
        xmlRpcEndpoint.lookupService(callerName.toString(), serviceName), new UriResultFactory());
  }

  /**
   * @param callerName
   *          the {@link GraphName} of the caller
   * @param subgraph
   *          the subgraph of the topics
   * @return the list of published {@link TopicDeclaration}s
   */
  public Response<List<TopicDeclaration>> getPublishedTopics(GraphName callerName, String subgraph) {
    return Response.fromListChecked(
        xmlRpcEndpoint.getPublishedTopics(callerName.toString(), subgraph),
        new TopicListResultFactory());
  }

  /**
   * Get a {@link List} of all {@link TopicSystemState} message types.
   * 
   * @param callerName
   *          the {@link GraphName} of the caller
   * @return a {@link List} of {@link TopicType}s
   */
  public Response<List<TopicType>> getTopicTypes(GraphName callerName) {
    return Response.fromListChecked(xmlRpcEndpoint.getTopicTypes(callerName.toString()),
        new TopicTypeListResultFactory());
  }

  /**
   * @param callerName
   *          the {@link GraphName} of the caller
   * @return the current {@link SystemState}
   */
  public Response<SystemState> getSystemState(GraphName callerName) {
    return Response.fromListChecked(xmlRpcEndpoint.getSystemState(callerName.toString()),
        new SystemStateResultFactory());
  }
}
