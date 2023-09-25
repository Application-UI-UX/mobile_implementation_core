cmake_minimum_required(VERSION 3.0.2)
project(mobile_implementation_core)

##############################################################################
# Catkin
##############################################################################

find_package(
  catkin REQUIRED
  mobile_implementation_core_build_tools
  mobile_implementation_core_messages
  mobile_implementation_core_bootstrap
  geometry_msgs
  nav_msgs
  mobile_implementation_core_test_msgs
  rosgraph_msgs
  sensor_msgs
  tf2_msgs
)

catkin_mobile_implementation_core_setup(publish installDist)

# make sure messages get built first if genjava is enabled
add_dependencies(gradle-${PROJECT_NAME} ${catkin_EXPORTED_TARGETS})

catkin_package()

##############################################################################
# Installation
##############################################################################

install(
  DIRECTORY ${CATKIN_DEVEL_PREFIX}/${CATKIN_GLOBAL_MAVEN_DESTINATION}/com/github/mobile_implementation_core/
  DESTINATION ${CATKIN_GLOBAL_MAVEN_DESTINATION}/com/github/mobile_implementation_core
)
