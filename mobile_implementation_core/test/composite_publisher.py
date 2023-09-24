#!/usr/bin/env python
from ros import rospy
from ros import mobile_implementation_core_test_msgs
import mobile_implementation_core_test_msgs.msg
import random

def publisher():
  rospy.init_node('composite_publisher')
  pub = rospy.Publisher('composite_in', mobile_implementation_core_test_msgs.msg.Composite)
  r = rospy.Rate(10)
  m = mobile_implementation_core_test_msgs.msg.Composite()
  m.a.x = random.random() * 10000.
  m.a.y = random.random() * 10000.
  m.a.z = random.random() * 10000.
  m.a.w = m.a.x + m.a.y + m.a.z
  
  m.b.x = m.a.x
  m.b.y = m.a.y
  m.b.z = m.a.z
  
  while not rospy.is_shutdown():
    pub.publish(m)
    r.sleep()

if __name__ == '__main__':
  publisher()
