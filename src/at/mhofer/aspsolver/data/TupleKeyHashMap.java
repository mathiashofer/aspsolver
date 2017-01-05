package at.mhofer.aspsolver.data;

import java.util.HashMap;
import java.util.Set;

/**
 * Simply a HashMap with two keys instead of one
 * 
 * @author Mathias
 *
 */
public class TupleKeyHashMap<K1, K2, V> {

	private HashMap<K1, HashMap<K2, V>> _map;

	public TupleKeyHashMap() {
		this._map = new HashMap<K1, HashMap<K2, V>>();
	}

	/**
	 * Associates the specified value with the specified key in this map. If the
	 * map previously contained a mapping for the key, the old value is
	 * replaced.
	 * 
	 * @param key1
	 * @param key2
	 * @param value
	 * @return the previous value associated with key, or null if there was no
	 *         mapping for key. (A null return can also indicate that the map
	 *         previously associated null with key.)
	 */
	public V put(K1 key1, K2 key2, V value) {
		V previous = null;

		HashMap<K2, V> k2Map = this._map.get(key1);
		if (k2Map == null) {
			k2Map = new HashMap<K2, V>();
			this._map.put(key1, k2Map);
		}

		assert (k2Map != null);

		previous = k2Map.put(key2, value);

		return previous;
	}
	
	public Set<K1> getKeys() {
		return this._map.keySet();
	}
	
	public Set<K2> getKeys(K1 key) {
		return this._map.get(key).keySet();
	}

	/**
	 * 
	 * @param key1
	 * @param key2
	 * @return
	 */
	public V get(K1 key1, K2 key2) {
		V value = null;

		HashMap<K2, V> k2Map = this._map.get(key1);
		if (k2Map != null) {
			value = k2Map.get(key2);
		}

		return value;
	}
}
