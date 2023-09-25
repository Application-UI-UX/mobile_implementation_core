.. _building:

Building
========

Gradle
------

mobile_implementation_core uses the `Gradle`_ build system in tandem with an external maven
repository which supplies dependencies.

To build mobile_implementation_core and install it to your local `Maven`_ repository, execute
the `gradle wrapper`_:

.. code-block:: bash

  cd mobile_implementation_core
  ./gradlew install

To build the documentation, you may execute the docs task:

.. code-block:: bash

  ./gradlew docs

To run the tests, you may execute the test task:

.. code-block:: bash

  ./gradlew test

To generate Eclipse project files, you may execute the eclipse:

.. code-block:: bash

  ./gradlew eclipse

Catkin
------

It also has a very minimal catkin wrapper that relays build instructions to the
underlying gradle builder. This lets the repository be easily built and
deployed alongside other mobile_implementation_core repositories in a ros environment. Refer to
the `RosWiki`_ for more information.


.. _Gradle: http://www.gradle.org/
.. _rosmake: http://ros.org/wiki/rosmake/
.. _Maven: http://maven.apache.org/
.. _gradle wrapper: http://gradle.org/docs/current/userguide/gradle_wrapper.html
.. _RosWiki: http://wiki.ros.org/mobile_implementation_core
.. _Package: https://github.com/Application-UI-UX
