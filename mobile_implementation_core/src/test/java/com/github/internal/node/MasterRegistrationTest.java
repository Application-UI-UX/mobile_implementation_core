// Copyright 2011 Google Inc. All Rights Reserved.

package com.github.internal.node;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import com.github.RosCore;
import com.github.RosTest;
import com.github.namespace.GraphName;
import com.github.node.AbstractNodeMain;
import com.github.node.ConnectedNode;
import com.github.node.topic.CountDownPublisherListener;
import com.github.node.topic.Publisher;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

/**
 * @author damonkohler@google.com (Damon Kohler)
 */
public class MasterRegistrationTest extends RosTest {

  private CountDownPublisherListener<std_msgs.String> publisherListener;
  private Publisher<std_msgs.String> publisher;

  @Test
  public void testRegisterPublisher() throws InterruptedException {
    publisherListener = CountDownPublisherListener.newDefault();
    nodeMainExecutor.execute(new AbstractNodeMain() {
      @Override
      public GraphName getDefaultNodeName() {
        return GraphName.of("node");
      }

      @Override
      public void onStart(ConnectedNode connectedNode) {
        publisher = connectedNode.newPublisher("topic", std_msgs.String._TYPE);
        publisher.addListener(publisherListener);
      }
    }, nodeConfiguration);
    assertTrue(publisherListener.awaitMasterRegistrationSuccess(1, TimeUnit.SECONDS));
    publisher.shutdown();
    assertTrue(publisherListener.awaitMasterUnregistrationSuccess(1, TimeUnit.SECONDS));
  }

  @Test
  public void testRegisterPublisherRetries() throws InterruptedException, IOException,
      URISyntaxException {
    int port = rosCore.getUri().getPort();
    publisherListener = CountDownPublisherListener.newDefault();
    nodeMainExecutor.execute(new AbstractNodeMain() {
      @Override
      public GraphName getDefaultNodeName() {
        return GraphName.of("node");
      }

      @Override
      public void onStart(ConnectedNode connectedNode) {
        rosCore.shutdown();
        ((DefaultNode) connectedNode).getRegistrar().setRetryDelay(1, TimeUnit.MILLISECONDS);
        publisher = connectedNode.newPublisher("topic", std_msgs.String._TYPE);
        publisher.addListener(publisherListener);
      }
    }, nodeConfiguration);

    assertTrue(publisherListener.awaitMasterRegistrationFailure(1, TimeUnit.SECONDS));
    rosCore = RosCore.newPrivate(port);
    rosCore.start();
    assertTrue(rosCore.awaitStart(1, TimeUnit.SECONDS));
    assertTrue(publisherListener.awaitMasterRegistrationSuccess(1, TimeUnit.SECONDS));
    publisher.shutdown();
    assertTrue(publisherListener.awaitMasterUnregistrationSuccess(1, TimeUnit.SECONDS));
  }

  @Test
  public void testUnregisterPublisherFailure() throws InterruptedException {
    publisherListener = CountDownPublisherListener.newDefault();
    nodeMainExecutor.execute(new AbstractNodeMain() {
      @Override
      public GraphName getDefaultNodeName() {
        return GraphName.of("node");
      }

      @Override
      public void onStart(ConnectedNode connectedNode) {
        publisher = connectedNode.newPublisher("topic", std_msgs.String._TYPE);
        publisher.addListener(publisherListener);
      }
    }, nodeConfiguration);
    assertTrue(publisherListener.awaitMasterRegistrationSuccess(1, TimeUnit.SECONDS));
    rosCore.shutdown();
    publisher.shutdown();
    assertTrue(publisherListener.awaitMasterUnregistrationFailure(6, TimeUnit.SECONDS));
  }
}
