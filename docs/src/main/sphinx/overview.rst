Overview
========

While mobile_implementation_core is mostly feature complete, it is currently under active
development. Consider all APIs and documentation to be volatile.

`Javadoc <javadoc/index.html>`_ is used extensively and cross referenced from
this documentation.

Android friendly
----------------

One of the primary goals of mobile_implementation_core is to bring ROS to Android. See the
android_core :ref:`android-core:getting-started` documentation for more
information.

Asynchronous
------------

Because ROS is heavily dependent on network communication, mobile_implementation_core is
asynchronous. No attempt is made to hide asynchronous behavior behind a
synchronous API. As a result, the mobile_implementation_core APIs may feel unfamiliar.

More threads, fewer processes
-----------------------------

You will not find a ``spin()`` method in rosjav in mobile_implementation_core. Unlike other client
libraries, many mobile_implementation_core nodes can run in a thread pool within a single JVM
process. In this way, mobile_implementation_core nodes are similar to C++ nodlets. In the future,
mobile_implementation_core nodes will support in memory communication in the same way that C++
nodelets do today.

