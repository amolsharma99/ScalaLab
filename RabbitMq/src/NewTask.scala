package src

import com.rabbitmq.client.{MessageProperties, Channel, Connection, ConnectionFactory}

/**
 * Created with IntelliJ IDEA.
 * User: amol.sharma
 * Date: 29/08/14
 * Time: 7:16 PM
 * To change this template use File | Settings | File Templates.
 */
object NewTask {
  val queue_name = "hello"

  def main(args: Array[String]) {

    //create connection to the server
    val factory: ConnectionFactory = new ConnectionFactory
    factory.setHost("localhost")
    val connection: Connection = factory.newConnection()
    val channel: Channel = connection.createChannel() //API for getting things done resides here.

    //declare queue and publish message
    channel.queueDeclare(queue_name, true, false, false, null) //Declaring a queue is idempotent - it will only be created if it doesn't exist already
    //NOTE: declare queue as durable for durability, RabbitMQ doesn't allow you to redefine an existing queue with different parameters and will return an error to any program that tries to do that


    val message: String = getMessage(args) //The message content is a byte array, so you can encode whatever you like there.
    channel.basicPublish("", queue_name, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes())
    //NOTE: Make message persistent as well for durability. it doesn't fully guarantee persistense, check note here: http://www.rabbitmq.com/tutorials/tutorial-two-java.html
    println("[x] sent '%s'".format(message))

    //close channel and connection
    channel.close()
    connection.close()
  }

  private def getMessage(strings: Array[String]): String = {
    if (strings.length < 1)
      "Hello World !!!"
    else
      strings.mkString(" ")
  }

}
