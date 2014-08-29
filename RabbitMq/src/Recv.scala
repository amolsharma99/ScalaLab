package src

/**
 * Created with IntelliJ IDEA.
 * User: amol.sharma
 * Date: 25/08/14
 * Time: 12:54 AM
 * To change this template use File | Settings | File Templates.
 */

import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.Connection
import com.rabbitmq.client.Channel
import com.rabbitmq.client.QueueingConsumer


object Recv {
  val queue_name = "hello"

  def main(args: Array[String]) {

    //open a connection and a channel, and declare the queue from which we're going to consume
    val factory = new ConnectionFactory
    factory.setHost("localhost")
    val connection: Connection = factory.newConnection
    val channel: Channel = connection.createChannel
    channel.queueDeclare(queue_name, false, false, false, null)
    println(" [*] Waiting for messages. To exit press CTRL+C")

    //extra QueueingConsumer is a class we'll use to buffer the messages pushed to us by the server.
    val consumer: QueueingConsumer = new QueueingConsumer(channel)
    channel.basicConsume(queue_name, true, consumer)

    while (true) {
      val delivery: QueueingConsumer.Delivery = consumer.nextDelivery
      val message: String = new String(delivery.getBody)
      println(" [x] received: '%s'".format(message))
    }
  }
}
