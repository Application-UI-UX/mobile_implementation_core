# Do not edit. bazel-deps autogenerates this file from dependencies.yaml.
java_library(
    name = "actionlib_msgs",
    exports = [
        "//external:jar/org/ros/mobile_implementation_core_messages/actionlib_msgs"
    ],
    visibility = [
        "//visibility:public"
    ]
)



java_library(
    name = "geometry_msgs",
    exports = [
        "//external:jar/org/ros/mobile_implementation_core_messages/geometry_msgs"
    ],
    visibility = [
        "//visibility:public"
    ]
)



java_library(
    name = "nav_msgs",
    exports = [
        "//external:jar/org/ros/mobile_implementation_core_messages/nav_msgs"
    ],
    runtime_deps = [
        "//3rdparty/jvm/org/ros/mobile_implementation_core_bootstrap:message_generation",
        ":actionlib_msgs",
        ":geometry_msgs",
        ":std_msgs"
    ],
    visibility = [
        "//visibility:public"
    ]
)



java_library(
    name = "rosgraph_msgs",
    exports = [
        "//external:jar/org/ros/mobile_implementation_core_messages/rosgraph_msgs"
    ],
    runtime_deps = [
        "//3rdparty/jvm/org/ros/mobile_implementation_core_bootstrap:message_generation",
        ":std_msgs"
    ],
    visibility = [
        "//visibility:public"
    ]
)



java_library(
    name = "mobile_implementation_core_test_msgs",
    exports = [
        "//external:jar/org/ros/mobile_implementation_core_messages/mobile_implementation_core_test_msgs"
    ],
    runtime_deps = [
        "//3rdparty/jvm/org/ros/mobile_implementation_core_bootstrap:message_generation",
        ":std_msgs"
    ],
    visibility = [
        "//visibility:public"
    ]
)



java_library(
    name = "std_msgs",
    exports = [
        "//external:jar/org/ros/mobile_implementation_core_messages/std_msgs"
    ],
    runtime_deps = [
        "//3rdparty/jvm/org/ros/mobile_implementation_core_bootstrap:message_generation"
    ],
    visibility = [
        "//visibility:public"
    ]
)


