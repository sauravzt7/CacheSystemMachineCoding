import com.machinecoding.cache.Cache;
import com.machinecoding.policies.FIFO;
import com.machinecoding.policies.LFU;
import com.machinecoding.policies.LRU;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ExampleTest {

    @org.junit.Test
    @Test
    public void testLRUPolicy() {
        Cache<Integer, String> cache = new Cache<>(2, new LRU<>());

        cache.put(1, "A");
        cache.put(2, "B");
        cache.get(1);
        assertEquals("A", cache.get(1)); // Accesses key 1
        cache.put(3, "C"); // Evicts key 2
        assertNull(cache.get(2)); // Key 2 should be evicted
        assertEquals("C", cache.get(3)); // Key 3 should be present
    }
    /*
    put(1, 'A')  # Cache: {1: 'A'}
    put(2, 'B')  # Cache: {1: 'A', 2: 'B'}
    get(1) -> 'A'  # Cache: {1: 'A', 2: 'B'} (1's frequency increased)
    put(3, 'C')  # Evicts key 2 (Least Frequently Used), Cache: {1: 'A', 3: 'C'}
    get(2) -> -1  # Not found
     */
    @org.junit.Test
    @Test
    public void testLFUPolicy() {
        // Implement LFU tests when LFU policy is completed
        Cache<Integer, String> cache = new Cache<>(2, new LFU<>());

        cache.put(1, "A");
        cache.put(2, "B");
        cache.get(1);
        cache.put(3, "C");
        assertEquals(null, cache.get(2));

    }

    @org.junit.Test
    @Test
    public void testFIFOPolicy() {
        // Implement LFU tests when LFU policy is completed
        Cache<Integer, String> cache = new Cache<>(2, new FIFO<>());

        cache.put(1, "A");
        cache.put(2, "B");
        cache.put(3, "C");
        assertEquals(null, cache.get(1)); // should return null as 1 would have evicted first
        cache.put(4, "D");
        assertEquals(null, cache.get(2)); // should return null as 2 evicts next

    }


}
