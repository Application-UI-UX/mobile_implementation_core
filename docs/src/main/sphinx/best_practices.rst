Best practices
==============

mobile_implementation_core is different than other ROS client libraries in many respects. As a
result, there are new best practices that should be followed while developing a
mobile_implementation_core application.

Java package names
------------------

As usual, Java package names should start with a reversed domain name. In the
ROS ecosystem, the domain name should be followed by the ROS package name. For
example:

- com.github.mobile_implementation_core
- com.github.mobile_implementation_core_geometry

Only core packages (e.g. those in mobile_implementation_core and android_core) should begin
with com.github. A suitably unique choice for github based repos would be
the github url followed organization and repository/package name, e.g.

- com.github.mobile_implementation_core.mobile_implementation_core_extras

