Advanced topics
===============

Listeners
---------

Because mobile_implementation_core provides a primarily asynchronous API, many classes which allow
you to provide event listeners. For example,
:javadoc:`com.github.node.topic.PublisherListener`\s allow you to react to
lifecycle events of a :javadoc:`com.github.node.topic.Publisher`. The snippet
below adds a :javadoc:`com.github.node.topic.PublisherListener` that will log a
warning message if the :javadoc:`com.github.node.topic.Publisher` fails to
register with the master. ::

  Node node;
  Publisher<std_msgs.String> publisher;

  ...

  publisher.addListener(new DefaultPublisherListener() {
    @Override
    public void onMasterRegistrationFailure(Publisher<std_msgs.String> registrant) {
      node.getLog().warn("Publisher failed to register: " + registrant);
    }
  });

Messages as BLOBs
-----------------

If you need to deserialize a ROS message BLOB, it is important to remember that
Java is a big endian virtual machine. When supplying the ``ByteBuffer`` to the
:javadoc:`com.github.message.MessageDeserializer`, make sure that order is set to
little endian. ::

  Node node;
  byte[] messageData;

  ...

  ByteBuffer buffer = ByteBuffer.wrap(messageData);
  buffer.order(ByteOrder.LITTLE_ENDIAN);
  PointCloud2 msg = node.getMessageSerializationFactory()
      .newMessageDeserializer(sensor_msgs.PointCloud._TYPE)
          .deserialize(buffer);

