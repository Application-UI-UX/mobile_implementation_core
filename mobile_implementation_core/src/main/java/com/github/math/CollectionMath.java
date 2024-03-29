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

package com.github.math;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author damonkohler@google.com (Damon Kohler)
 */
public class CollectionMath {
  
  private CollectionMath() {
    // Utility class.
  }
 
  public static <T extends Comparable<? super T>> T median(Collection<T> collection) {
    Preconditions.checkArgument(collection.size() > 0);
    List<T> list = Lists.newArrayList(collection);
    Collections.sort(list);
    return list.get(list.size() / 2);
  }
}
