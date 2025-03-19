# Machine Coding Assignment: Cache System with Multiple Eviction Policies

## Problem Statement

Design and implement a **cache system** that supports **multiple eviction policies**. Your cache should be able to store **key-value pairs** and dynamically support **eviction policies** like:

- **Least Recently Used (LRU)**
- **Least Frequently Used (LFU)**
- **First-In-First-Out (FIFO)**

Additionally, implement a **flexible structure** that allows adding new eviction policies easily.

## Requirements

1. Implement a cache with **configurable eviction policies** (LRU, LFU, FIFO).
2. Support standard cache operations:
    - `put(key, value)`: Inserts or updates a key-value pair.
    - `get(key)`: Retrieves the value of a given key (or returns `-1` if not found).
3. Implement a mechanism to **switch eviction policies dynamically**.
4. Ensure **efficient time complexity** (**preferably O(1)** for `get` and `put` operations).
5. Provide **unit tests** demonstrating the correctness of your implementation.
6. The system should be **easily extendable** to support **additional eviction policies** in the future.

## Example Cases

### Example 1: Using LRU Policy

**Cache Size:** 2

#### Operations:
```python
put(1, 'A')  # Cache: {1: 'A'}
put(2, 'B')  # Cache: {1: 'A', 2: 'B'}
get(1) -> 'A'  # Cache: {2: 'B', 1: 'A'} (1 is most recently used)
put(3, 'C')  # Evicts key 2 (Least Recently Used), Cache: {1: 'A', 3: 'C'}
get(2) -> -1  # Not found
```

### Example 2: Using LFU Policy

**Cache Size:** 2

#### Operations:
```python
put(1, 'A')  # Cache: {1: 'A'}
put(2, 'B')  # Cache: {1: 'A', 2: 'B'}
get(1) -> 'A'  # Cache: {1: 'A', 2: 'B'} (1's frequency increased)
put(3, 'C')  # Evicts key 2 (Least Frequently Used), Cache: {1: 'A', 3: 'C'}
get(2) -> -1  # Not found
```

## Additional Features (Optional Enhancements)

### 1. Persistent Storage Support
- Option to **store cached data on disk** (e.g., using SQLite, LevelDB, or RocksDB).
- Upon restart, cache should **load existing data** from storage.

### 2. Time-to-Live (TTL) for Cache Entries
- Set an expiration time for each entry to auto-remove stale data.

### 3. Multi-threading Support
- Ensure **thread-safe cache operations** for concurrent `get` and `put` requests.
- Use **locks, mutex, or concurrent data structures** to avoid race conditions.

### 4. Cache Persistence via Write-Through & Write-Back
- **Write-through**: Every update to the cache should also update persistent storage.
- **Write-back**: Cache writes updates in batches asynchronously.

### 5. Configurable Cache Size
- Allow users to **set cache size dynamically** at runtime.
- Implement a policy to **prevent cache from growing uncontrollably**.

## Submission Guidelines

- Code should be **clean and well-documented**.
- Implement **proper error handling**.
- Provide **test cases** demonstrating correctness.
- Share the solution on the **Discord group before 20 Feb** for evaluation.

## Problems 
- How to solve the data type issues while reading and writing to the persistent storage
- 