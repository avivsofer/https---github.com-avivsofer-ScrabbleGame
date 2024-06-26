package test;
import java.util.HashMap;
import java.util.LinkedList;

public class LRU implements CacheReplacementPolicy { //מחזירה את המילה שהיה בה שימוש לפני הכי הרבה זמן

    int capacityCache = CacheManager.maxSize; ;
    private final HashMap<String, Node> cache= new HashMap<>();
    private final LinkedList<Node> keys= new LinkedList<>();

    @Override //הוספת מילה למטמון
    public void add(String word) {
        if (cache.containsKey(word)) { // אם המילה קיימת
            cache.get(word).updateTimestamp(); // עדכון הזמן האחרון בו נעשה שימוש במילה
            moveToHead(word); // המילה שהתקבלה כארגומנט מועברת לראש הרשימה של המטמון 
            return;
        }

        if (cache.size() == capacityCache) { // אם המטמון מלא
            evictLRU();// מוחקת את המילה האחרונה במטמון ומעדכנת את המפתחות
        }

        Node node = new Node(word); // הסופת צומת במטמון
        cache.put(word, node); // השמת המילה החדשה בצומת
        keys.addFirst(node); //מעביר את הצומת הזאת לראש רשימת המפתחות
    }

    @Override
    public String remove() {
        Node node = keys.removeLast();
        cache.remove(node.word);
        return node.word;
    }
    
    //העברת המילה לראש רשימת המפתחות
    private void moveToHead(String word) { 
        Node node = cache.get(word);
        keys.remove(node);
        keys.addFirst(node);
    }

    private void evictLRU() {
        if (!keys.isEmpty()) {
            Node node = keys.removeLast();
            cache.remove(node.word);
        }
    }
    

    private static class Node {
        private final String word;
        private long timestamp;

        public Node(String word) {
            this.word = word;
            this.timestamp = System.currentTimeMillis();
        }

        public void updateTimestamp() {
            this.timestamp = System.currentTimeMillis();
        }
    }
}

