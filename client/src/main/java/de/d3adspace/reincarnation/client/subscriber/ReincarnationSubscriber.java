/*
 * Copyright (c) 2017 D3adspace
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package de.d3adspace.reincarnation.client.subscriber;

import de.d3adspace.reincarnation.client.client.ReincarnationClient;
import de.d3adspace.reincarnation.client.subscriber.handler.SubscriptionHandler;
import de.d3adspace.reincarnation.commons.action.ReincarnationNetworkAction;
import de.d3adspace.reincarnation.commons.annotation.Channel;
import de.d3adspace.reincarnation.commons.name.NameCreator;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONObject;

public class ReincarnationSubscriber extends ReincarnationClient {
	
	private static AtomicInteger ID = new AtomicInteger(0);
	private final List<SubscriptionHandler> subscriptionHandlers;
	
	public ReincarnationSubscriber(String host, int port) {
		super(host, port, NameCreator.create("subscriber", ID.getAndIncrement()));
		this.subscriptionHandlers = new ArrayList<>();
	}
	
	@Override
	public void received(JSONObject jsonObject) {
		final String channel = (String) jsonObject.remove("channel");
		
		this.subscriptionHandlers.forEach(handler -> {
			final String currentChannel = this.getChannel(handler.getClass());
			if (Objects.equals(channel, currentChannel)) {
				handler.receivedMessage(jsonObject);
			}
		});
	}
	
	private String getChannel(Class<?> clazz) {
		return clazz.getAnnotation(Channel.class).channelName();
	}
	
	public void subscribe(SubscriptionHandler handler) {
		this.subscriptionHandlers.add(handler);
		
		final JSONObject jsonObject = new JSONObject()
			.put("actionCode", ReincarnationNetworkAction.ACTION_REGISTER_CHANNEL.getActionCode())
			.put("channel", this.getChannel(handler.getClass()));
		
		write(jsonObject);
	}
}