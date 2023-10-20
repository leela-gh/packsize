package com.packsize.rabbitmq.exchange;

import com.packsize.rabbitmq.CommonConfigs;
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
		
		DeliverCallback deliverCallback = (consumerTag,message) -> {
			System.out.println(consumerTag);
			System.out.println(new String(message.getBody(), "UTF-8"));
		};
		
		CancelCallback cancelCallback = consumerTag -> {
			System.out.println(consumerTag);
		};
			
		channel.basicConsume("Queue-1", true,deliverCallback, cancelCallback);
	}
}
