.. _getting-started:

Getting started
===============

Creating a new Java package
---------------------------

Please refer to the `RosWiki`_ for more information on how to create a new
gradle/catkin project and subprojects. You might also wish to read the 
Gradle `Java tutorial`_ for more details about building Java projects with
Gradle in general.

.. _RosWiki: http://wiki.ros.org/mobile_implementation_core
.. _Maven plugin: http://gradle.org/docs/current/userguide/maven_plugin.html
.. _Application plugin: http://gradle.org/docs/current/userguide/application_plugin.html
.. _Java tutorial: http://gradle.org/docs/current/userguide/tutorial_java_projects.html
.. _Package: https://github.com/Application-UI-UX

Creating nodes
--------------

Typically ROS nodes are synonymous with processes. In mobile_implementation_core, however, nodes
are more like :roswiki:`nodelet`\s in that many nodes can run in a single
process, the Java VM.

Users, like yourself, do not create :javadoc:`com.github.node.Node`\s. Instead,
programs are defined as implementations of :javadoc:`com.github.node.NodeMain`
which are executed by the aptly named :javadoc:`com.github.node.NodeMainExecutor`.

Let's consider the following mostly empty :javadoc:`com.github.node.NodeMain`
implementation:

.. code-block:: java

  import com.github.namespace.GraphName;
  import com.github.node.Node;
  import com.github.node.NodeMain;

  public class MyNode implements NodeMain {

    @Override
    public GraphName getDefaultNodeName() {
      return GraphName.of("my_node");
    }

    @Override
    public void onStart(ConnectedNode node) {
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

The :javadoc:`com.github.node.NodeMain#getDefaultNodeName()` method returns the
default name of the node. This name will be used unless a node name is
specified in the :javadoc:`com.github.node.NodeConfiguration` (more on that
later). :javadoc:`com.github.namespace.GraphName`\s are used throughout mobile_implementation_core
when refering to nodes, topics, and parameters. Most methods which accept a
:javadoc:`com.github.namespace.GraphName` will also accept a string for
convenience.

The :javadoc:`com.github.node.NodeListener#onStart(com.github.node.ConnectedNode)`
method is the entry point for your program (or node). The
:javadoc:`com.github.node.ConnectedNode` parameter is the factory we use to build
things like :javadoc:`com.github.topic.Publisher`\s and
:javadoc:`com.github.topic.Subscriber`\s.

The :javadoc:`com.github.node.NodeListener#onShutdown(com.github.node.Node)` method is
the first exit point for your program. It will be executed as soon as shutdown
is started (i.e. before all publishers, subscribers, etc. have been shutdown).
The shutdown of all created publishers, subscribers, etc. will be delayed until
this method returns or the shutdown timeout expires.

The :javadoc:`com.github.node.NodeListener#onShutdownComplete(com.github.node.Node)`
method is the final exit point for your program. It will be executed after all
publishers, subscribers, etc. have been shutdown. This is the preferred place
to handle clean up since it will not delay shutdown.

The :javadoc:`com.github.node.NodeListener#onError(com.github.node.Node,
java.lang.Throwable)` method is called when an error occurs in the
:javadoc:`com.github.node.Node` itself. These errors are typically fatal.  The
:javadoc:`com.github.node.NodeListener#onShutdown(com.github.node.Node)` and
:javadoc:`com.github.node.NodeListener#onShutdownComplete(com.github.node.Node)`
methods will be called following the call to
:javadoc:`com.github.node.NodeListener#onError(com.github.node.Node,
java.lang.Throwable)`.

Publishers and subscribers
--------------------------

The following class (:javadoc:`com.github.mobile_implementation_core_tutorial_pubsub.Talker`) is
available from the mobile_implementation_core_tutorial_pubsub package. In this example, we create
a publisher for the chatter topic. This should feel relatively familiar if
you're a ROS veteran. The :javadoc:`com.github.topic.Publisher` publishes
``std_msgs.String`` messages to the ``/chatter`` topic.

.. literalinclude:: ../../../../mobile_implementation_core_tutorial_pubsub/src/main/java/com.github/mobile_implementation_core_tutorial_pubsub/Talker.java
  :language: java
  :linenos:
  :lines: 17-
  :emphasize-lines: 28,38

Line 28 will probably feel unfamailiar even to ROS veterans. This is one
example of mobile_implementation_core's asynchornous API. The intent of our
:javadoc:`com.github.mobile_implementation_core_tutorial_pubsub.Talker` class is to publish a hello
world message to anyone who will listen once per second. One way to accomplish
this is to publish the message and sleep in a loop. However, we don't want to
block the :javadoc:`com.github.node.NodeListener#onStart(com.github.node.Node)`
method. So, we create a :javadoc:`com.github.concurrent.CancellableLoop` and ask
the :javadoc:`com.github.node.Node` to execute it. The loop will be interrupted
automatically when the :javadoc:`com.github.node.Node` exits.

On line 38 we create a new ``std_msgs.String`` message to publish using the
:javadoc:`com.github.node.topic.Publisher#newMessage()` method. Messages in
mobile_implementation_core cannot be instantiated directly. More on that later.

Now lets take a look at the :javadoc:`com.github.mobile_implementation_core_tutorial_pubsub.Listener`
class.

.. literalinclude:: ../../../../mobile_implementation_core_tutorial_pubsub/src/main/java/com.github/mobile_implementation_core_tutorial_pubsub/Listener.java
  :language: java
  :linenos:
  :lines: 17-
  :emphasize-lines: 27-32

In lines 27-32 we see another example of mobile_implementation_core's asynchornous API. We can add
as many :javadoc:`com.github.message.MessageListener`\s to our
:javadoc:`com.github.node.topic.Subscriber` as we like. When a new message is
received, all of our :javadoc:`com.github.message.MessageListener`\s will be
called with the incoming message as an argument to
:javadoc:`com.github.message.MessageListener#onNewMessage(T)`.

Executing nodes
---------------

When packaging your application into jar, you can use :javadoc:`com.github.RosRun`
as the main class. :javadoc:`com.github.RosRun` provides a
:javadoc:`com.github.node.NodeMainExecutor` and a command line interface that will
be familiar to ROS veterans. For example, the following steps will build and
execute the :javadoc:`com.github.mobile_implementation_core_tutorial_pubsub.Talker` and
:javadoc:`com.github.mobile_implementation_core_tutorial_pubsub.Listener` nodes in separate
processes:

.. code-block:: bash

  # source your devel/setup.bash
  roscd mobile_implementation_core/mobile_implementation_core_tutorial_pubsub
  ../gradlew installDist
  roscore &
  ./build/install/mobile_implementation_core_tutorial_pubsub/bin/mobile_implementation_core_tutorial_pubsub com.github.mobile_implementation_core_tutorial_pubsub.Talker &
  ./build/install/mobile_implementation_core_tutorial_pubsub/bin/mobile_implementation_core_tutorial_pubsub com.github.mobile_implementation_core_tutorial_pubsub.Listener

.. note:: The above example launches roscore and the Talker node in the
  background. You could instead launch each in a separate terminal. Also, you
  may notice that rosrun cannot find the installed executable. This is a known
  issue that will be addressed in the future.

At this point, you should see the familiar "Hello, world!" messages start
appearing in your terminal. You can also echo the topic using the
:roswiki:`rostopic` command line tool:

.. code-block:: bash

  rostopic echo chatter

You can configure the executed nodes from the command line in the same way you
would any other ROS executable. For example, the following commands will remap
the default topic /chatter to /foo.

.. code-block:: bash

  ./build/install/mobile_implementation_core_tutorial_pubsub/bin/mobile_implementation_core_tutorial_pubsub com.github.mobile_implementation_core_tutorial_pubsub.Talker chatter:=/foo &
  ./build/install/mobile_implementation_core_tutorial_pubsub/bin/mobile_implementation_core_tutorial_pubsub com.github.mobile_implementation_core_tutorial_pubsub.Listener chatter:=/foo

See :roswiki:`Remapping%20Arguments` for more information on passing arguments
to ROS executables.

.. note:: Only the arguments described in :roswiki:`Remapping%20Arguments` are
  supported. Support for arbitrary command line arguments (i.e. argv) will be
  added in the future.

Services
--------

The following class (:javadoc:`com.github.mobile_implementation_core_tutorial_services.Server`) is
available from the mobile_implementation_core_tutorial_services package. In this example, we
create a :javadoc:`com.github.node.service.ServiceServer` for the
``mobile_implementation_core_test_msgs.AddTwoInts`` service. This should feel relatively familiar if you're
a ROS veteran.

.. literalinclude:: ../../../../mobile_implementation_core_tutorial_services/src/main/java/com.github/mobile_implementation_core_tutorial_services/Server.java
  :language: java
  :linenos:
  :lines: 17-
  :emphasize-lines: 29

The :javadoc:`com.github.node.service.ServiceResponseBuilder` is called
asynchronously for each incoming request. On line 29 we modify the response
output parameter to contain the sum of the two integers in the request. The
response will be sent once the
:javadoc:`com.github.node.service.ServiceResponseBuilder#build(T, S)` returns.

Now lets take a look at the :javadoc:`com.github.mobile_implementation_core_tutorial_services.Client`
class.

.. literalinclude:: ../../../../mobile_implementation_core_tutorial_services/src/main/java/com.github/mobile_implementation_core_tutorial_services/Client.java
  :language: java
  :linenos:
  :lines: 17-
  :emphasize-lines: 36-47

In lines 36-47 we see another example of mobile_implementation_core's asynchornous API. When the
response is received, our
:javadoc:`com.github.node.service.ServiceResponseListener` will be called with the
incoming response as an argument to
:javadoc:`com.github.node.service.ServiceResponseListener#onSuccess(T)`. In the
event that the server thows a :javadoc:`com.github.exception.ServiceException`
while building the response,
:javadoc:`com.github.node.service.ServiceResponseListener#onFailure(RemoteException)`
will be called. The :javadoc:`com.github.exception.RemoteException` will contain
the error message from the server.

Building and executing these nodes works in the same manner as described above:

.. code-block:: bash

  # source your devel/setup.bash
  roscd mobile_implementation_core/mobile_implementation_core_tutorial_pubsub
  ../gradlew installDist
  roscore &
  ./build/install/mobile_implementation_core_tutorial_services/bin/mobile_implementation_core_tutorial_services com.github.mobile_implementation_core_tutorial_services.Server &
  ./build/install/mobile_implementation_core_tutorial_services/bin/mobile_implementation_core_tutorial_services com.github.mobile_implementation_core_tutorial_services.Client

At this point, you should see the log message "2 + 2 = 4" appear in your
terminal. You can also access the service using the :roswiki:`rosservice`
command line tool:

.. code-block:: bash

  rosservice add_two_ints 2 2

Just as before, you can configure the executed nodes from the command line in
the same way you would any other ROS executable. See
:roswiki:`Remapping%20Arguments` for more information on passing arguments to
ROS executables.

Messages
--------

Messages are defined as interfaces. Since this makes it impossible to
instantiate the message directly, it's necessary to use a
:javadoc:`com.github.message.MessageFactory` or helper methods such as
:javadoc:`com.github.node.topic.Publisher#newMessage()`. This indirection allows
the underlying message implementation to change in the future. ::

  Node node;

  ...

  PointCloud2 msg = node.getTopicMessageFactory()
      .newMessage(sensor_msgs.PointCloud._TYPE);

If you want to use messages that you define, whether they are officially released packages or your
own custom packages, follow the instructions on the roswiki (refer to :roswiki:`mobile_implementation_core/Messages`).

Parameters
----------

mobile_implementation_core offers full access to the ROS :roswiki:`Parameter Server`. The
:roswiki:`Parameter Server` is a shared dictionary of configuration parameters
accessible to all the nodes at runtime. It is meant to store configuration
parameters that are easy to inspect and modify.

Parameters are accessible via :javadoc:`com.github.node.parameter.ParameterTree`\s
(provided by :javadoc:`com.github.node.Node`\s). ::

  ParameterTree params = node.newParameterTree();

Accessing Parameters
~~~~~~~~~~~~~~~~~~~~

The :javadoc:`com.github.node.parameter.ParameterTree` API allows you to set and
query lists, maps, and single objects of integers, strings and floats.

Unlike typical ROS :roswiki:`Client Libraries`, mobile_implementation_core requires that the type
of the parameter be known when you retrieve it. If the actual parameter type
doesn't match the expected type, an exception will be thrown. ::

  boolean foo = params.getBoolean("/foo");
  int bar = params.getInteger("/bar", 42 /* default value */);
  double baz = params.getDouble("/foo/baz");

  params.set("/bloop", "Hello, world!");
  String helloWorld = params.getString("/bloop");

  List<Integer> numbers = params.getList("/numbers");
  Map<String, String> strings = params.getMap("/strings");

As with other ROS client libraries, it is possible to retrieve a subtree of
parameters. However, you will be responsible for casting the values to their
appropriate types. ::

  Map<String, Object> subtree = params.getMap("/subtree");

Using a ParameterListener
~~~~~~~~~~~~~~~~~~~~~~~~~

It is also possible to subscribe to a particular parameter using a
:javadoc:`com.github.node.parameter.ParameterListener`. Note that this does not
work for parameter subtrees. ::

  params.addParameterListener("/foo/bar", new ParameterListener() {
    @Override
    public void onNewValue(Object value) {
      ...
    }
  });

Currently, ParameterListeners are not generic. Instead, you are responsible for
casting the value appropriately.

Logging
-------

The logging interface for mobile_implementation_core is accessed through
:javadoc:`com.github.node.Node` objects via the
:javadoc:`com.github.node.Node#getLog()` method. This object returns an `Apache
Commons Log`_ object which handles the debug, info, error, warning, and fatal
logging outputs for ROS. ::

  node.getLog.debug("debug message");
  node.getLog.info(" informative message");

  node.getLog.warn("warning message");

  // report an error message
  node.getLog.error("error message");

  // error message with an exception
  // so that it can print the stack trace
  node.getLog.error("error message", e);

  node.fatal("message informing user of a fatal occurrence");

.. _Apache Commons Log: http://commons.apache.org/logging/commons-logging-1.1.1/apidocs/index.html
