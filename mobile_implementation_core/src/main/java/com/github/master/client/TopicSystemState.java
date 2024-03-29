/*
 * Copyright (C) 2012 Google Inc.
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

package com.github.master.client;

import java.util.Set;

/**
 * Information about a topic.
 * 
 * @author Keith M. Hughes
 */
public class TopicSystemState {

	/**
	 * Name of the topic.
	 */
	private final String topicName;

	/**
	 * Node names of all publishers.
	 */
	private final Set<String> publishers;

	/**
	 * Node names of all subscribers.
	 */
	private final Set<String> subscribers;

	public TopicSystemState(String topicName, Set<String> publishers,
			Set<String> subscribers) {
		this.topicName = topicName;
		this.publishers = publishers;
		this.subscribers = subscribers;
	}

	/**
	 * @return the topicName
	 */
	public String getTopicName() {
		return topicName;
	}

	/**
	 * Get the set of all nodes that publish the topic.
	 * 
	 * @return the set of node names
	 */
	public Set<String> getPublishers() {
		return publishers;
	}

	/**
	 * Get the set of all nodes that subscribe to the topic.
	 * 
	 * @return the set of node names
	 */
	public Set<String> getSubscribers() {
		return subscribers;
	}
}
