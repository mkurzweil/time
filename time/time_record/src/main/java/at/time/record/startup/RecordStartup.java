package at.time.record.startup;

import java.io.IOException;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import at.time.base.rabbit.RabbitConstants;
import at.time.base.rabbit.RabbitManager;

@ManagedBean(eager = true)
@ApplicationScoped
public class RecordStartup {

	private static Logger logger = LogManager.getLogger();

	public RecordStartup() {
		startRabbitConsumer();
	}

	private void startRabbitConsumer() {
		final Channel channel = RabbitManager.getInstance().getChannel();
		try {
			final Consumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					final String message = new String(body, "UTF-8");
					logger.info(" [x] Received '" + message + "'");
				}
			};
			channel.basicConsume(RabbitConstants.RECORD_QUEUE, true, consumer);
		} catch (final IOException e) {
			logger.error("Fehler beim Initialisieren vom RabbitMQ Consumer", e.getMessage());
		}
	}

}