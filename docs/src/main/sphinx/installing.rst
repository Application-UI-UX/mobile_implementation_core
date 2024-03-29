.. _installing:

Installing
==========

Prerequisites
-------------

You will need a java implementation - this package has been well tested with openjdk-6,
but is also likely to work equally as well with other implementations (oraclejdk has
also had minimal testing).

If you would like to build the mobile_implementation_core documentation, you will also need
Pygments 1.5+ and Sphinx 1.1.3+. If you don't have native binaries, then

.. code-block:: bash

  sudo pip install --upgrade sphinx Pygments


Non-ROS Installation
--------------------

This java package no longer requires a ros environment to be installed. In this case,
you simply need to clone the github repository or you can use the publish link in Application-UI-UX

.. code-block:: bash

  git clone https://github.com/mobile_implementation_core/mobile_implementation_core
  cd mobile_implementation_core
  git checkout -b kinetic origin/kinetic

and proceed immediately to the section on :ref:`building`.

ROS Installation
----------------

If you would like a full ros environment backending your installation (you might
be generating code for your own custom messages, sequencing builds of multiple mobile_implementation_core
repositories or using mixed packages, e.g. java + python) then refer to the `RosWiki`_
for more details.

.. _RosWiki: http://wiki.ros.org/mobile_implementation_core

