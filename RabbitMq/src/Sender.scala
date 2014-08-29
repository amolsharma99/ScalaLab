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

object Sender {
  val queue_name = "hello"

  def main(args: Array[String]) {

    //create connection to the server
    val factory: ConnectionFactory = new ConnectionFactory
    factory.setHost("localhost")
    val connection: Connection = factory.newConnection()
    val channel: Channel = connection.createChannel() //API for getting things done resides here.

    //declare queue and publish message
    channel.queueDeclare(queue_name, false, false, false, null) //Declaring a queue is idempotent - it will only be created if it doesn't exist already
    val message: String = "Hello World" //The message content is a byte array, so you can encode whatever you like there.
    channel.basicPublish("", queue_name, null, message.getBytes())
    println("[x] sent '%s'".format(message))

    //close channel and connection
    channel.close()
    connection.close()
  }

}

