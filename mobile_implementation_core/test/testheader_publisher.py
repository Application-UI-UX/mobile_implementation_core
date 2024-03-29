#!/usr/bin/env python
from ros import rospy
from ros import mobile_implementation_core_test_msgs
import mobile_implementation_core_test_msgs.msg

def publisher():
  rospy.init_node('testheader_publisher')
  pub = rospy.Publisher('test_header_in', mobile_implementation_core_test_msgs.msg.TestHeader)
  r = rospy.Rate(10)
  m = mobile_implementation_core_test_msgs.msg.TestHeader()
  m.caller_id = rospy.get_name()
  m.header.stamp = rospy.get_rostime()
  while not rospy.is_shutdown():
    pub.publish(m)
    r.sleep()

if __name__ == '__main__':
  publisher()
