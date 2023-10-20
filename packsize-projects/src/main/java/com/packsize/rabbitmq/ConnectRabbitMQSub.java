package com.packsize.rabbitmq;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
public class ConnectRabbitMQSub {

	public static void main(String[] args) throws Exception {
		subQueue();
		
	}
	
	private static void subQueue() throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		Connection conn = factory.newConnection(CommonConfigs.AMQP_URL);
		Channel channel = conn.createChannel();
		
		DeliverCallback deliverCallback = (s,delivery) -> {
			System.out.println(new String(delivery.getBody(), "UTF-8"));
		};
		
		CancelCallback cancelCallback = s -> {
			System.out.println(s);
		};
			
		channel.basicConsume("Queue-2", true,deliverCallback, cancelCallback);
	}
}
