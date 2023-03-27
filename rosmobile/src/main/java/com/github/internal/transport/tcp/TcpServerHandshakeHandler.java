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

package com.github.internal.transport.tcp;

import com.google.common.base.Preconditions;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import com.github.exception.RosRuntimeException;
import com.github.internal.node.server.NodeIdentifier;
import com.github.internal.node.service.DefaultServiceServer;
import com.github.internal.node.service.ServiceManager;
import com.github.internal.node.service.ServiceResponseEncoder;
import com.github.internal.node.topic.DefaultPublisher;
import com.github.internal.node.topic.SubscriberIdentifier;
import com.github.internal.node.topic.TopicIdentifier;
import com.github.internal.node.topic.TopicParticipantManager;
import com.github.internal.transport.ConnectionHeader;
import com.github.internal.transport.ConnectionHeaderFields;
import com.github.namespace.GraphName;

/**
 * A {@link ChannelHandler} which will process the TCP server handshake.
 * 
 * @author damonkohler@google.com (Damon Kohler)
 * @author kwc@willowgarage.com (Ken Conley)
 */
public class TcpServerHandshakeHandler extends SimpleChannelHandler {

  private final TopicParticipantManager topicParticipantManager;
  private final ServiceManager serviceManager;

  public TcpServerHandshakeHandler(TopicParticipantManager topicParticipantManager,
      ServiceManager serviceManager) {
    this.topicParticipantManager = topicParticipantManager;
    this.serviceManager = serviceManager;
  }

  @Override
  public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
    ChannelBuffer incomingBuffer = (ChannelBuffer) e.getMessage();
    ChannelPipeline pipeline = e.getChannel().getPipeline();
    ConnectionHeader incomingHeader = ConnectionHeader.decode(incomingBuffer);
    if (incomingHeader.hasField(ConnectionHeaderFields.SERVICE)) {
      handleServiceHandshake(e, pipeline, incomingHeader);
    } else {
      handleSubscriberHandshake(ctx, e, pipeline, incomingHeader);
    }
  }

  private void handleServiceHandshake(MessageEvent e, ChannelPipeline pipeline,
      ConnectionHeader incomingHeader) {
    GraphName serviceName = GraphName.of(incomingHeader.getField(ConnectionHeaderFields.SERVICE));
    Preconditions.checkState(serviceManager.hasServer(serviceName));
    DefaultServiceServer<?, ?> serviceServer = serviceManager.getServer(serviceName);
    e.getChannel().write(serviceServer.finishHandshake(incomingHeader));
    String probe = incomingHeader.getField(ConnectionHeaderFields.PROBE);
    if (probe != null && probe.equals("1")) {
      e.getChannel().close();
    } else {
      pipeline.replace(TcpServerPipelineFactory.LENGTH_FIELD_PREPENDER, "ServiceResponseEncoder",
          new ServiceResponseEncoder());
      pipeline.replace(this, "ServiceRequestHandler", serviceServer.newRequestHandler());
    }
  }

  private void handleSubscriberHandshake(ChannelHandlerContext ctx, MessageEvent e,
      ChannelPipeline pipeline, ConnectionHeader incomingConnectionHeader)
      throws InterruptedException, Exception {
    Preconditions.checkState(incomingConnectionHeader.hasField(ConnectionHeaderFields.TOPIC),
        "Handshake header missing field: " + ConnectionHeaderFields.TOPIC);
    GraphName topicName =
        GraphName.of(incomingConnectionHeader.getField(ConnectionHeaderFields.TOPIC));
    Preconditions.checkState(topicParticipantManager.hasPublisher(topicName),
        "No publisher for topic: " + topicName);
    DefaultPublisher<?> publisher = topicParticipantManager.getPublisher(topicName);
    ChannelBuffer outgoingBuffer = publisher.finishHandshake(incomingConnectionHeader);
    Channel channel = ctx.getChannel();
    if (incomingConnectionHeader.hasField(ConnectionHeaderFields.TCP_NODELAY)) {
      boolean tcpNoDelay = "1".equals(incomingConnectionHeader.getField(ConnectionHeaderFields.TCP_NODELAY));
      channel.getConfig().setOption("tcpNoDelay", tcpNoDelay);
    }
    ChannelFuture future = channel.write(outgoingBuffer).await();
    if (!future.isSuccess()) {
      throw new RosRuntimeException(future.getCause());
    }
    String nodeName = incomingConnectionHeader.getField(ConnectionHeaderFields.CALLER_ID);
    publisher.addSubscriber(new SubscriberIdentifier(NodeIdentifier.forName(nodeName),
        new TopicIdentifier(topicName)), channel);

    // Once the handshake is complete, there will be nothing incoming on the
    // channel. So, we replace the handshake handler with a handler which will
    // drop everything.
    pipeline.replace(this, "DiscardHandler", new SimpleChannelHandler());
  }
}
