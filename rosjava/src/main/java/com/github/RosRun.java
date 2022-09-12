/*
 * Copyright (C) 2011 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.github;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import com.github.exception.RosRuntimeException;
import com.github.internal.loader.CommandLineLoader;
import com.github.node.DefaultNodeMainExecutor;
import com.github.node.NodeConfiguration;
import com.github.node.NodeMain;
import com.github.node.NodeMainExecutor;

/**
 * This is a main class entry point for executing {@link NodeMain}s.
 * 
 * @author kwc@willowgarage.com (Ken Conley)
 * @author damonkohler@google.com (Damon Kohler)
 */
public class RosRun {

  public static void printUsage() {
    System.err.println("Usage: java -jar my_package.jar com.example.MyNodeMain [args]");
  }

  public static void main(String[] argv) throws Exception {
    if (argv.length == 0) {
      printUsage();
      System.exit(1);
    }

    CommandLineLoader loader = new CommandLineLoader(Lists.newArrayList(argv));
    String nodeClassName = loader.getNodeClassName();
    System.out.println("Loading node class: " + loader.getNodeClassName());
    NodeConfiguration nodeConfiguration = loader.build();

    NodeMain nodeMain = null;
    try {
      nodeMain = loader.loadClass(nodeClassName);
    } catch (ClassNotFoundException e) {
      throw new RosRuntimeException("Unable to locate node: " + nodeClassName, e);
    } catch (InstantiationException e) {
      throw new RosRuntimeException("Unable to instantiate node: " + nodeClassName, e);
    } catch (IllegalAccessException e) {
      throw new RosRuntimeException("Unable to instantiate node: " + nodeClassName, e);
    }

    Preconditions.checkState(nodeMain != null);
    NodeMainExecutor nodeMainExecutor = DefaultNodeMainExecutor.newDefault();
    nodeMainExecutor.execute(nodeMain, nodeConfiguration);
  }
}
