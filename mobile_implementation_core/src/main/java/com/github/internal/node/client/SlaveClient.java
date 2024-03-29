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

import com.google.common.collect.Lists;

import com.github.internal.node.response.IntegerResultFactory;
import com.github.internal.node.response.ProtocolDescriptionResultFactory;
import com.github.internal.node.response.Response;
import com.github.internal.node.response.TopicListResultFactory;
import com.github.internal.node.response.UriResultFactory;
import com.github.internal.node.response.VoidResultFactory;
import com.github.internal.node.topic.TopicDeclaration;
import com.github.internal.node.xmlrpc.SlaveXmlRpcEndpoint;
import com.github.internal.transport.ProtocolDescription;
import com.github.namespace.GraphName;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author damonkohler@google.com (Damon Kohler)
 */
public class SlaveClient extends Client<SlaveXmlRpcEndpoint> {

  private final GraphName nodeName;

  public SlaveClient(GraphName nodeName, URI uri) {
    super(uri, SlaveXmlRpcEndpoint.class);
    this.nodeName = nodeName;
  }

  public List<Object> getBusStats() {
    throw new UnsupportedOperationException();
  }

  public List<Object> getBusInfo() {
    throw new UnsupportedOperationException();
  }

  public Response<URI> getMasterUri() {
    return Response.fromListChecked(xmlRpcEndpoint.getMasterUri(nodeName.toString()), new UriResultFactory());
  }

  public Response<Void> shutdown(String message) {
    return Response.fromListChecked(xmlRpcEndpoint.shutdown("/master", message), new VoidResultFactory());
  }

  public Response<Integer> getPid() {
    return Response.fromListChecked(xmlRpcEndpoint.getPid(nodeName.toString()), new IntegerResultFactory());
  }

  public Response<List<TopicDeclaration>> getSubscriptions() {
    return Response.fromListChecked(xmlRpcEndpoint.getSubscriptions(nodeName.toString()),
        new TopicListResultFactory());
  }

  public Response<List<TopicDeclaration>> getPublications() {
    return Response.fromListChecked(xmlRpcEndpoint.getPublications(nodeName.toString()),
        new TopicListResultFactory());
  }

  public Response<Void> paramUpdate(GraphName name, boolean value) {
    return Response.fromListChecked(xmlRpcEndpoint.paramUpdate(nodeName.toString(), name.toString(), value),
        new VoidResultFactory());
  }

  public Response<Void> paramUpdate(GraphName name, char value) {
    return Response.fromListChecked(xmlRpcEndpoint.paramUpdate(nodeName.toString(), name.toString(), value),
        new VoidResultFactory());
  }

  public Response<Void> paramUpdate(GraphName name, int value) {
    return Response.fromListChecked(xmlRpcEndpoint.paramUpdate(nodeName.toString(), name.toString(), value),
        new VoidResultFactory());
  }

  public Response<Void> paramUpdate(GraphName name, double value) {
    return Response.fromListChecked(xmlRpcEndpoint.paramUpdate(nodeName.toString(), name.toString(), value),
        new VoidResultFactory());
  }

  public Response<Void> paramUpdate(GraphName name, String value) {
    return Response.fromListChecked(xmlRpcEndpoint.paramUpdate(nodeName.toString(), name.toString(), value),
        new VoidResultFactory());
  }

  public Response<Void> paramUpdate(GraphName name, List<?> value) {
    return Response.fromListChecked(xmlRpcEndpoint.paramUpdate(nodeName.toString(), name.toString(), value),
        new VoidResultFactory());
  }

  public Response<Void> paramUpdate(GraphName name, Map<?, ?> value) {
    return Response.fromListChecked(xmlRpcEndpoint.paramUpdate(nodeName.toString(), name.toString(), value),
        new VoidResultFactory());
  }

  public Response<Void> publisherUpdate(GraphName topic, Collection<URI> publisherUris) {
    List<String> publishers = Lists.newArrayList();
    for (URI uri : publisherUris) {
      publishers.add(uri.toString());
    }
    return Response.fromListChecked(
        xmlRpcEndpoint.publisherUpdate(nodeName.toString(), topic.toString(), publishers.toArray()),
        new VoidResultFactory());
  }

  public Response<ProtocolDescription> requestTopic(GraphName topic,
      Collection<String> requestedProtocols) {
    return Response.fromListChecked(xmlRpcEndpoint.requestTopic(nodeName.toString(), topic.toString(),
        new Object[][] { requestedProtocols.toArray() }), new ProtocolDescriptionResultFactory());
  }
}
