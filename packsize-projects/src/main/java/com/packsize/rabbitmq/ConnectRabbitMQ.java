package com.packsize.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
public class ConnectRabbitMQ {

	public static void main(String[] args) throws Exception{
		createQueue();
	}
	
	private static void createQueue() throws Exception{
		ConnectionFactory factory = new ConnectionFactory();
		Connection conn = factory.newConnection(CommonConfigs.AMQP_URL);;
		Channel channel = conn.createChannel();;
		channel.queueDeclare("Queue-2", true, false, false, null);
		
		channel.close();
		conn.close();
	}
}
