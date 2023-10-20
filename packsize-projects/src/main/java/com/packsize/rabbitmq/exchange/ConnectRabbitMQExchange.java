package com.packsize.rabbitmq.exchange;

import com.packsize.rabbitmq.CommonConfigs;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
public class ConnectRabbitMQExchange {

	public static void main(String[] args) throws Exception{
		createExchange();
		createQueue();
		createQueueBinding();
	}
	
	private static void createExchange() throws Exception{
		ConnectionFactory factory = new ConnectionFactory();
		Connection conn = factory.newConnection(CommonConfigs.AMQP_URL);;
		Channel channel = conn.createChannel();;
		channel.exchangeDeclare("my-direct-exchange", BuiltinExchangeType.DIRECT, true);
		
		channel.close();
		conn.close();
	}
	
	private static void createQueue() throws Exception{
		ConnectionFactory factory = new ConnectionFactory();
		Connection conn = factory.newConnection(CommonConfigs.AMQP_URL);;
		Channel channel = conn.createChannel();;
		channel.queueDeclare("Queue-1", true, false, false, null);
		channel.queueDeclare("Queue-2", true, false, false, null);
		
		channel.close();
		conn.close();
	}
	
	private static void createQueueBinding() throws Exception{
		ConnectionFactory factory = new ConnectionFactory();
		Connection conn = factory.newConnection(CommonConfigs.AMQP_URL);;
		Channel channel = conn.createChannel();;
		
		channel.queueBind("Queue-1", "my-direct-exchange", "device1");
		channel.queueBind("Queue-2", "my-direct-exchange", "device2");
		
		//channel.close();
		conn.close();
	}
}
