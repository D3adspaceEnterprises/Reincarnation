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

package de.d3adspace.reincarnation.client.network.pipe;

import de.d3adspace.reincarnation.client.network.impl.ReincarnationPubSubClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.io.IOException;
import org.json.JSONObject;

public class ReincarnationPubSubClientChannelHandler extends
	SimpleChannelInboundHandler<JSONObject> {
	
	private final ReincarnationPubSubClient reincarnationPubSubClient;
	
	public ReincarnationPubSubClientChannelHandler(
		ReincarnationPubSubClient reincarnationPubSubClient) {
		this.reincarnationPubSubClient = reincarnationPubSubClient;
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, JSONObject jsonObject)
		throws Exception {
		
		this.reincarnationPubSubClient.received(jsonObject);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (!(cause instanceof IOException)) {
			cause.printStackTrace();
		}
	}
}
