package sd2223.trab2.servers.kafka;


import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class KafkaPublisher {

	static public KafkaPublisher createPublisher(String brokers) {
		Properties props = new Properties();

		// Localização dos servidores kafka (lista de máquinas + porto)
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);

		// Classe para serializar as chaves dos eventos (string)
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

		// Classe para serializar os valores dos eventos (string)
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

		return new KafkaPublisher(new KafkaProducer<String, String>(props));
	}
	
	private final KafkaProducer<String, String> producer;

	private KafkaPublisher( KafkaProducer<String, String> producer) {
		this.producer = producer;
	}

	public void close() {
		this.producer.close();
	}

	public long publish(String topic, String key, String value) {
		try {
			long offset = producer.send(new ProducerRecord<>(topic, key, value)).get().offset();
			return offset;
		} catch (ExecutionException | InterruptedException x) {
			x.printStackTrace();
		}
		return -1;
	}
	
	public long publish(String topic, String value) {
		try {
			long offset = producer.send(new ProducerRecord<>(topic, value)).get().offset();
			return offset;
		} catch (ExecutionException | InterruptedException x) {
			x.printStackTrace();
		}
		return -1;
	}
	
	
	public static void main(String[] args) throws Exception {

		
		
		
	}
}
