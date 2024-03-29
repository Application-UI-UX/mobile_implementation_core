#!/usr/bin/python
#
# Copyright (C) 2012 Google Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License"); you may not
# use this file except in compliance with the License. You may obtain a copy of
# the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
# WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
# License for the specific language governing permissions and limitations under
# the License.

"""Prints the serialized bytese of a nav_msgs.Odometry message in hex.

This can be modified slightly to print the serialized bytes in hex for
arbitrary ROS messages and is useful for generating test cases for mobile_implementation_core
message serialization.
"""

__author__ = 'damonkohler@google.com (Damon Kohler)'

import StringIO

import roslib; roslib.load_manifest('mobile_implementation_core_test')
import rospy

import nav_msgs.msg as nav_msgs

message = nav_msgs.Odometry()
buf = StringIO.StringIO()
message.serialize(buf)
print ''.join('0x%02x,' % ord(c) for c in buf.getvalue())[:-1]

