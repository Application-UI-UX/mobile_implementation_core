cmake_minimum_required(VERSION 3.0.2)
project(rosmobile_core)

##############################################################################
# Catkin
##############################################################################

find_package(
  catkin REQUIRED
  rosmobile_build_tools
  rosmobile_messages
  rosmobile_bootstrap
  geometry_msgs
  nav_msgs
  rosmobile_test_msgs
  rosgraph_msgs
  sensor_msgs
  tf2_msgs
)

catkin_rosmobile_setup(publish installDist)

# make sure messages get built first if genjava is enabled
add_dependencies(gradle-${PROJECT_NAME} ${catkin_EXPORTED_TARGETS})

catkin_package()

##############################################################################
# Installation
##############################################################################

install(
  DIRECTORY ${CATKIN_DEVEL_PREFIX}/${CATKIN_GLOBAL_MAVEN_DESTINATION}/com/github/rosmobile_core/
  DESTINATION ${CATKIN_GLOBAL_MAVEN_DESTINATION}/com/github/rosmobile_core
)
