package com.packsize.rabbitmq.exchange;

import com.packsize.rabbitmq.CommonConfigs;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
public class ConnectRabbitMQPublishExchange {

	public static void main(String[] args) throws Exception {
		publishToExchange();
	}
	
	private static void publishToExchange() throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		Connection conn = factory.newConnection(CommonConfigs.AMQP_URL);
		Channel channel = conn.createChannel();
		
		String message = "Message 1 for Queue 1";
		channel.basicPublish("my-direct-exchange", "device1", null, message.getBytes());
		
		channel.close();
		conn.close();
	}
}
