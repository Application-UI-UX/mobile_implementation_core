# Do not edit. bazel-deps autogenerates this file from dependencies.yaml.
java_library(
    name = "actionlib_msgs",
    exports = [
        "//external:jar/org/ros/rosmobile_messages/actionlib_msgs"
    ],
    visibility = [
        "//visibility:public"
    ]
)



java_library(
    name = "geometry_msgs",
    exports = [
        "//external:jar/org/ros/rosmobile_messages/geometry_msgs"
    ],
    visibility = [
        "//visibility:public"
    ]
)



java_library(
    name = "nav_msgs",
    exports = [
        "//external:jar/org/ros/rosmobile_messages/nav_msgs"
    ],
    runtime_deps = [
        "//3rdparty/jvm/org/ros/rosmobile_bootstrap:message_generation",
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
        "//external:jar/org/ros/rosmobile_messages/rosgraph_msgs"
    ],
    runtime_deps = [
        "//3rdparty/jvm/org/ros/rosmobile_bootstrap:message_generation",
        ":std_msgs"
    ],
    visibility = [
        "//visibility:public"
    ]
)



java_library(
    name = "rosmobile_test_msgs",
    exports = [
        "//external:jar/org/ros/rosmobile_messages/rosmobile_test_msgs"
    ],
    runtime_deps = [
        "//3rdparty/jvm/org/ros/rosmobile_bootstrap:message_generation",
        ":std_msgs"
    ],
    visibility = [
        "//visibility:public"
    ]
)



java_library(
    name = "std_msgs",
    exports = [
        "//external:jar/org/ros/rosmobile_messages/std_msgs"
    ],
    runtime_deps = [
        "//3rdparty/jvm/org/ros/rosmobile_bootstrap:message_generation"
    ],
    visibility = [
        "//visibility:public"
    ]
)


