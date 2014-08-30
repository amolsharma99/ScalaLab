package src

import com.rabbitmq.client.{QueueingConsumer, Channel, Connection, ConnectionFactory}

/**
 * Created with IntelliJ IDEA.
 * User: amol.sharma
 * Date: 30/08/14
 * Time: 11:58 AM
 * To change this template use File | Settings | File Templates.
 */

object Worker {
  val queue_name = "hello"

  def main(args: Array[String]) {

    //open a connection and a channel, and declare the queue from which we're going to consume
    val factory = new ConnectionFactory
    factory.setHost("localhost")
    val connection: Connection = factory.newConnection
    val channel: Channel = connection.createChannel
    channel.queueDeclare(queue_name, false, false, false, null)
    println(" [*] Waiting for messages. To exit press CTRL+C")

    val autoAck: Boolean = false

    //extra QueueingConsumer is a class we'll use to buffer the messages pushed to us by the server.
    val consumer: QueueingConsumer = new QueueingConsumer(channel)
    channel.basicConsume(queue_name, autoAck, consumer)

    while (true) {
      val delivery: QueueingConsumer.Delivery = consumer.nextDelivery
      val message: String = new String(delivery.getBody)
      println(" [x] received: '%s'".format(message))
      doWork(message)
      //NOTE: doing acknowledgement, after making sure that the work is done.
      channel.basicAck(delivery.getEnvelope.getDeliveryTag, false)
      println(" [x] done")
    }
  }

  private def doWork(task: String) {
    task.toCharArray.foreach(ch => {
      if (ch.equals("."))
        Thread.sleep(1000)
    })
  }
}
