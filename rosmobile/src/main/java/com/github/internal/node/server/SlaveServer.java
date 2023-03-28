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

package com.github.internal.node.server;

import com.google.common.collect.Lists;

import com.github.address.AdvertiseAddress;
import com.github.address.BindAddress;
import com.github.internal.node.client.MasterClient;
import com.github.internal.node.parameter.ParameterManager;
import com.github.internal.node.service.ServiceManager;
import com.github.internal.node.topic.DefaultPublisher;
import com.github.internal.node.topic.DefaultSubscriber;
import com.github.internal.node.topic.PublisherIdentifier;
import com.github.internal.node.topic.SubscriberIdentifier;
import com.github.internal.node.topic.TopicDeclaration;
import com.github.internal.node.topic.TopicParticipantManager;
import com.github.internal.node.xmlrpc.SlaveXmlRpcEndpointImpl;
import com.github.internal.system.Process;
import com.github.internal.transport.ProtocolDescription;
import com.github.internal.transport.ProtocolNames;
import com.github.internal.transport.tcp.TcpRosProtocolDescription;
import com.github.internal.transport.tcp.TcpRosServer;
import com.github.namespace.GraphName;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author damonkohler@google.com (Damon Kohler)
 */
public class SlaveServer extends XmlRpcServer {

  private final GraphName nodeName;
  private final MasterClient masterClient;
  private final TopicParticipantManager topicParticipantManager;
  private final ParameterManager parameterManager;
  private final TcpRosServer tcpRosServer;

  public SlaveServer(GraphName nodeName, BindAddress tcpRosBindAddress,
      AdvertiseAddress tcpRosAdvertiseAddress, BindAddress xmlRpcBindAddress,
      AdvertiseAddress xmlRpcAdvertiseAddress, MasterClient master,
      TopicParticipantManager topicParticipantManager, ServiceManager serviceManager,
      ParameterManager parameterManager, ScheduledExecutorService executorService) {
    super(xmlRpcBindAddress, xmlRpcAdvertiseAddress);
    this.nodeName = nodeName;
    this.masterClient = master;
    this.topicParticipantManager = topicParticipantManager;
    this.parameterManager = parameterManager;
    this.tcpRosServer =
        new TcpRosServer(tcpRosBindAddress, tcpRosAdvertiseAddress, topicParticipantManager,
            serviceManager, executorService);
  }

  public AdvertiseAddress getTcpRosAdvertiseAddress() {
    return tcpRosServer.getAdvertiseAddress();
  }

  /**
   * Start the XML-RPC server. This start() routine requires that the
   * {@link TcpRosServer} is initialized first so that the slave server returns
   * correct information when topics are requested.
   */
  public void start() {
    super.start(com.github.internal.node.xmlrpc.SlaveXmlRpcEndpointImpl.class,
        new SlaveXmlRpcEndpointImpl(this));
    tcpRosServer.start();
  }

  // TODO(damonkohler): This should also shut down the Node.
  @Override
  public void shutdown() {
    super.shutdown();
    tcpRosServer.shutdown();
  }

  public List<Object> getBusStats(String callerId) {
    throw new UnsupportedOperationException();
  }

  public List<Object> getBusInfo(String callerId) {
    List<Object> busInfo = Lists.newArrayList();
    // The connection ID field is opaque to the user. A monotonically increasing
    // integer for now is sufficient.
    int id = 0;
    for (DefaultPublisher<?> publisher : getPublications()) {
      for (SubscriberIdentifier subscriberIdentifier : topicParticipantManager
          .getPublisherConnections(publisher)) {
        List<String> publisherBusInfo = Lists.newArrayList();
        publisherBusInfo.add(Integer.toString(id));
        publisherBusInfo.add(subscriberIdentifier.getNodeIdentifier().getName().toString());
        // TODO(damonkohler): Pull out BusInfo constants.
        publisherBusInfo.add("o");
        // TODO(damonkohler): Add getter for protocol to topic participants.
        publisherBusInfo.add(ProtocolNames.TCPROS);
        publisherBusInfo.add(publisher.getTopicName().toString());
        busInfo.add(publisherBusInfo);
        id++;
      }
    }
    for (DefaultSubscriber<?> subscriber : getSubscriptions()) {
      for (PublisherIdentifier publisherIdentifer : topicParticipantManager
          .getSubscriberConnections(subscriber)) {
        List<String> subscriberBusInfo = Lists.newArrayList();
        subscriberBusInfo.add(Integer.toString(id));
        // Subscriber connection PublisherIdentifiers are populated with node
        // URIs instead of names. As a result, the only identifier information
        // available is the URI.
        subscriberBusInfo.add(publisherIdentifer.getNodeIdentifier().getUri().toString());
        // TODO(damonkohler): Pull out BusInfo constants.
        subscriberBusInfo.add("i");
        // TODO(damonkohler): Add getter for protocol to topic participants.
        subscriberBusInfo.add(ProtocolNames.TCPROS);
        subscriberBusInfo.add(publisherIdentifer.getTopicName().toString());
        busInfo.add(subscriberBusInfo);
        id++;
      }
    }
    return busInfo;
  }

  public URI getMasterUri() {
    return masterClient.getRemoteUri();
  }

  /**
   * @return PID of this process if available, throws
   *         {@link UnsupportedOperationException} otherwise.
   */
  @Override
  public int getPid() {
    return Process.getPid();
  }

  public Collection<DefaultSubscriber<?>> getSubscriptions() {
    return topicParticipantManager.getSubscribers();
  }

  public Collection<DefaultPublisher<?>> getPublications() {
    return topicParticipantManager.getPublishers();
  }

  /**
   * @param parameterName
   * @param parameterValue
   * @return the number of parameter subscribers that received the update
   */
  public int paramUpdate(GraphName parameterName, Object parameterValue) {
    return parameterManager.updateParameter(parameterName, parameterValue);
  }

  public void publisherUpdate(String callerId, String topicName, Collection<URI> publisherUris) {
    GraphName graphName = GraphName.of(topicName);
    if (topicParticipantManager.hasSubscriber(graphName)) {
      DefaultSubscriber<?> subscriber = topicParticipantManager.getSubscriber(graphName);
      TopicDeclaration topicDeclaration = subscriber.getTopicDeclaration();
      Collection<PublisherIdentifier> identifiers =
          PublisherIdentifier.newCollectionFromUris(publisherUris, topicDeclaration);
      subscriber.updatePublishers(identifiers);
    }
  }

  public ProtocolDescription requestTopic(String topicName, Collection<String> protocols)
      throws ServerException {
    // TODO(damonkohler): Use NameResolver.
    // Canonicalize topic name.
    GraphName graphName = GraphName.of(topicName).toGlobal();
    if (!topicParticipantManager.hasPublisher(graphName)) {
      throw new ServerException("No publishers for topic: " + graphName);
    }
    for (String protocol : protocols) {
      if (protocol.equals(ProtocolNames.TCPROS)) {
        try {
          return new TcpRosProtocolDescription(tcpRosServer.getAdvertiseAddress());
        } catch (Exception e) {
          throw new ServerException(e);
        }
      }
    }
    throw new ServerException("No supported protocols specified.");
  }

  /**
   * @return a {@link NodeIdentifier} for this {@link SlaveServer}
   */
  public NodeIdentifier toNodeIdentifier() {
    return new NodeIdentifier(nodeName, getUri());
  }
}
