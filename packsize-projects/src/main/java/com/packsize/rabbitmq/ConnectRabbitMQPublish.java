package com.packsize.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
public class ConnectRabbitMQPublish {

	public static void main(String[] args) throws Exception {
		publishQueue();
	}
	
	private static void publishQueue() throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		Connection conn = factory.newConnection(CommonConfigs.AMQP_URL);
		Channel channel = conn.createChannel();
		
		for(int i=1; i<4 ; i++) {
			String message = "Message" + i;
			channel.basicPublish("", "Queue-2", null, message.getBytes());
		}
		
		channel.close();
		conn.close();
	}
}
