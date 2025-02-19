import com.machinecoding.backend.SQLiteStorageBackend;
import com.machinecoding.backend.StorageBackend;
import com.machinecoding.cache.Cache;
import com.machinecoding.model.CacheEntry;
import com.machinecoding.policies.FIFO;
import com.machinecoding.policies.LFU;
import com.machinecoding.policies.LRU;

public class StorageBackEndTest {

    public static void main(String[] args) throws InterruptedException {
        // Initialize storage backend

        // Initialize Cache (with in-memory map) and integrate backend
        Cache<Integer, String> cache = new Cache<>(5, new LRU<>());
        System.out.println(cache.toString());
//         Add entries with specific expiration times
        cache.put(10, new CacheEntry<>("Hello", 10000));
        System.out.println(cache.toString());
        cache.put(20, new CacheEntry<>("World", 10000));
        System.out.println(cache.toString());
        cache.put(30, new CacheEntry<>("This", 10000));
        System.out.println(cache.toString());
        cache.put(40, new CacheEntry<>("Saurs", 10000));
        System.out.println(cache.toString());
        cache.put(50, new CacheEntry<>("Sams", 10000));
        System.out.println(cache.toString());
        cache.put(15, new CacheEntry<>("Hi", 10000));
        System.out.println(cache.toString());
        cache.put(16, new CacheEntry<>("Hey", 10000));
//
        // Fetch before expiration
        System.out.println(cache.get(1)); // Outputs: null
//
//        System.out.println(cache.get(1));
//        System.out.println(cache.get(2));
//        cache.put(6, new CacheEntry<>("That", 10000));
//        cache.put(7, new CacheEntry<>("Ping", 10000));
//
//        System.out.println(cache.toString());
//        Thread.sleep(15000);
//        System.out.println(cache.toString());

    }

}
