package logicallayer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import customexceptions.CustomException;
import persistentlayer.Cache;
import redis.clients.jedis.Jedis;

public class RedisCache<K, V> implements Cache<K, V> {

	private final Jedis jedis;

	public RedisCache() {
		this.jedis = new Jedis();
	}

	public RedisCache(int port) {
		this.jedis = new Jedis("localhost", port);
	}

	@Override
	public synchronized void set(K key, V value) throws CustomException {
		byte[] keyArray = serializeObject(key);
		byte[] valueArray = serializeObject(value);
		jedis.set(keyArray, valueArray);
	}

	@Override
	@SuppressWarnings("unchecked")
	public synchronized V get(K key) throws CustomException {
		byte[] keyArray = serializeObject(key);
		byte[] valueArray = jedis.get(keyArray);
		if (valueArray == null) {
			return null;
		}
		V value = (V) deSerializeObject(valueArray);
		return value;
	}

	@Override
	public synchronized void remove(K key) throws CustomException {
		byte[] keyArray = serializeObject(key);
		jedis.del(keyArray);
	}

	public byte[] serializeObject(Object obj) throws CustomException {
		try (ByteArrayOutputStream byteArrOS = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(byteArrOS)) {
			oos.writeObject(obj);
			return byteArrOS.toByteArray();
		} catch (IOException e) {
			throw new CustomException("Cannot serialize object", e);
		}
	}

	public Object deSerializeObject(byte[] array) throws CustomException {
		try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(array))) {
			return ois.readObject();
		} catch (IOException e) {
			throw new CustomException("Cannot deSerialize object", e);
		} catch (ClassNotFoundException e) {
			throw new CustomException("Class Not found!", e);
		}
	}

}