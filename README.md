# Cache System with Eviction Policies

This project implements a **Cache System** with support for dynamic **Eviction Policies**, such as **LRU (Least Recently Used)**, **FIFO (First In First Out)**, and more customizable strategies. It is designed with extensibility, modularity, and clarity in mind, making it easy to add new eviction policies or cache configurations.

---

## Table of Contents
- [Features](#features)
- [Getting Started](#getting-started)
- [Modules](#modules)
- [Eviction Policies](#eviction-policies)
    - [LRU (Least Recently Used)](#lru-least-recently-used)
    - [FIFO (First In First Out)](#fifo-first-in-first-out)
    - [Custom Policies](#custom-policies)
- [Code Structure](#code-structure)
- [Usage](#usage)
- [Example](#example)
- [Extending the System](#extending-the-system)
- [Tests](#tests)

---

## Features

1. **Cache Implementation**
    - Stores key-value pairs with bounded capacity.
    - Manages eviction of keys when the cache reaches its capacity.

2. **Eviction Policies**
    - Policies to decide which key to evict when the cache is full.
    - Currently Supported Policies:
        - **LRU (Least Recently Used)**
        - **FIFO (First In First Out)**

3. **Extensibility**
    - Built using interfaces, making it easy to add custom eviction policies.
    - Decoupled logic for the cache and eviction policies.

4. **Thread-Safe Cache Storage**
    - Utilizes Java’s `ConcurrentHashMap` for thread-safe access to cache entries.

---

## Getting Started

To get started with this project, you'll need to clone the repository, build it, and configure the cache with your desired eviction policy.

### Prerequisites
- Java 8+
- A Java IDE (e.g., IntelliJ IDEA) or a build tool like Maven/Gradle

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/your-repo/cache-system.git
   cd cache-system
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the tests to verify functionality:
   ```bash
   mvn test
   ```

---

## Modules

### 1. Cache (`Cache.java`)
The main cache storage class. Stores key-value pairs with a specified capacity and uses an associated `EvictionPolicy` to manage evictions when the storage exceeds its size.

### 2. Eviction Policies
Eviction policies determine which key(s) should be evicted from the cache when it is full. This is encapsulated in the `EvictionPolicy<K>` interface, which can be implemented for various strategies.

Currently implemented policies:
- **LRU (Least Recently Used)**: Evicts the least used key.
- **FIFO (First In First Out)**: Evicts the oldest key added to the cache.

---

## Eviction Policies

### LRU (Least Recently Used)
The **LRU** eviction policy ensures that when the cache is full, the least recently used key is evicted. It is implemented using a `LinkedHashMap` with access-order enabled for efficient access and eviction operations.

```java
public class LRU<K> implements EvictionPolicy<K> {
    private final Map<K, Boolean> accessOrder;

    public LRU() {
        this.accessOrder = new LinkedHashMap<>(16, 0.75f, true);
    }

    @Override
    public void keyAccessed(K key) {
        accessOrder.put(key, Boolean.TRUE);
    }

    @Override
    public K evictKey() {
        if (accessOrder.isEmpty()) {
            throw new IllegalStateException("Eviction attempted without any keys in cache.");
        }
        return accessOrder.keySet().iterator().next();
    }
}
```

### FIFO (First In First Out)
The **FIFO** eviction policy ensures that when the cache is full, the first key added to the cache (the oldest one) is evicted. It operates using a FIFO-style queue internally.

---

## Code Structure

The project follows modular principles for scalability and readability. Here's a summary of the key components:

```plaintext
├── com/machinecoding/cache/
│   ├── Cache.java           # Main cache implementation
│   ├── ICache.java          # Cache interface
│
├── com/machinecoding/policies/
│   ├── EvictionPolicy.java  # Interface for eviction policies
│   ├── LRU.java             # LRU eviction policy
│   ├── FIFO.java            # FIFO eviction policy
│
└── ExampleTest.java         # Test cases for the cache and policies
```

---

## Usage

### Basic Usage
You can configure the cache to use any eviction policy by passing an implementation of the `EvictionPolicy`:

```java
Cache<Integer, String> cache = new Cache<>(2, new LRU<>());
cache.put(1, "A");
cache.put(2, "B");
System.out.println(cache.get(1)); // Outputs: "A"
```

If you want an alternate policy like FIFO:
```java
Cache<Integer, String> cache = new Cache<>(2, new FIFO<>(new ConcurrentHashMap<>()));
```

---

## Example

An example test case demonstrating **LRU** behavior:

```java
@Test
public void testLRUPolicy() {
    Cache<Integer, String> cache = new Cache<>(2, new LRU<>());

    cache.put(1, "A");
    cache.put(2, "B");
    assertEquals("A", cache.get(1)); // Access key 1
    cache.put(3, "C"); // Evicts key 2
    assertNull(cache.get(2)); // Key 2 should no longer exist
    assertEquals("C", cache.get(3)); // Key 3 should still exist
}
```

---

## Extending the System

### Adding a New Eviction Policy
To add a new eviction policy, implement the `EvictionPolicy<K>` interface:

```java
public class CustomPolicy<K> implements EvictionPolicy<K> {
    @Override
    public void keyAccessed(K key) {
        // Your logic here
    }

    @Override
    public K evictKey() {
        // Your eviction policy here
    }
}
```

Then, simply pass the new policy to the `Cache` constructor.

---

## Tests

Unit tests are provided to validate the behavior of the cache and eviction policies. You can add additional tests in `ExampleTest.java` to verify corner cases.

To run the tests:
```bash
mvn test
```

---

## Contributing

If you’d like to contribute:
1. Fork the repository.
2. Create a feature branch (`git checkout -b feature/new-policy`).
3. Submit a pull request with your changes.

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

--- 

### Happy Caching! 🎉