package org.springframework.data.simpledb.core.entity;


import org.junit.Test;
import org.springframework.data.simpledb.core.entity.domain.SampleCoreCollection;
import org.springframework.data.simpledb.core.entity.domain.User;
import org.springframework.data.simpledb.core.entity.util.AttributeUtil;
import org.springframework.data.simpledb.repository.support.entityinformation.SimpleDbEntityInformation;
import org.springframework.data.simpledb.repository.support.entityinformation.SimpleDbEntityInformationSupport;

import java.util.*;

import static org.junit.Assert.assertTrue;

public class CollectionWrapperTest {

    @Test
    public void serialize_deserialize_sets_of_core_types() {
        SampleCoreCollection sampleCollection = new SampleCoreCollection();
        sampleCollection.setSetOfIntegers( new HashSet<>(Arrays.asList(Integer.valueOf(20), Integer.valueOf(12))));
        sampleCollection.setHashSetOfFloats(new HashSet<>(Arrays.asList(Float.valueOf(23f), Float.valueOf(32f))));

        EntityWrapper<SampleCoreCollection, String> sdbEntity = new EntityWrapper<>(this.<SampleCoreCollection>readEntityInformation(SampleCoreCollection.class), sampleCollection);
        final Map<String, List<String>> attributes = sdbEntity.serialize();

        /* convert back */
        final EntityWrapper<SampleCoreCollection, String> convertedEntity = new EntityWrapper<>(this.<SampleCoreCollection>readEntityInformation(SampleCoreCollection.class));
        convertedEntity.deserialize(attributes);

        assertTrue(sampleCollection.equals(convertedEntity.getItem()));

    }

    @Test
    public void serialize_deserialize_lists_of_core_types() {
        SampleCoreCollection sampleCollection = new SampleCoreCollection();
        sampleCollection.setListOfBytes(new ArrayList<>(Arrays.asList(Byte.valueOf("123"), Byte.valueOf("23"))));

        EntityWrapper<SampleCoreCollection, String> sdbEntity = new EntityWrapper<>(this.<SampleCoreCollection>readEntityInformation(SampleCoreCollection.class), sampleCollection);
        final Map<String, List<String>> attributes = sdbEntity.serialize();

        /* convert back */
        final EntityWrapper<SampleCoreCollection, String> convertedEntity = new EntityWrapper<>(this.<SampleCoreCollection>readEntityInformation(SampleCoreCollection.class));
        convertedEntity.deserialize(attributes);

        assertTrue(sampleCollection.equals(convertedEntity.getItem()));

    }

    @Test
    public void serialize_deserialize_lists_of_Objects() {
        SampleCoreCollection sampleCollection = new SampleCoreCollection();
        sampleCollection.setListOfObjects(new ArrayList<User>());
        User user = new User();
        user.setName("Simple");
        sampleCollection.getListOfObjects().add(user);

        EntityWrapper<SampleCoreCollection, String> sdbEntity = new EntityWrapper<>(this.<SampleCoreCollection>readEntityInformation(SampleCoreCollection.class), sampleCollection);
        final Map<String, List<String>> attributes = sdbEntity.serialize();

        /* convert back */
        final EntityWrapper<SampleCoreCollection, String> convertedEntity = new EntityWrapper<>(this.<SampleCoreCollection>readEntityInformation(SampleCoreCollection.class));
        convertedEntity.deserialize(attributes);

        assertTrue(sampleCollection.equals(convertedEntity.getItem()));

    }

    @Test
    public void serialize_deserialize_collection_instantiated_as_arrayList_of_core_types() {
        SampleCoreCollection sampleCollection = new SampleCoreCollection();
        sampleCollection.setCollectionOfLongs(new ArrayList<>(Arrays.asList(Long.valueOf("123"), Long.valueOf("23"))));

        EntityWrapper<SampleCoreCollection, String> sdbEntity = new EntityWrapper<>(this.<SampleCoreCollection>readEntityInformation(SampleCoreCollection.class), sampleCollection);
        final Map<String, List<String>> attributes = sdbEntity.serialize();

        /* convert back */
        final EntityWrapper<SampleCoreCollection, String> convertedEntity = new EntityWrapper<>(this.<SampleCoreCollection>readEntityInformation(SampleCoreCollection.class));
        convertedEntity.deserialize(attributes);

        assertTrue(sampleCollection.equals(convertedEntity.getItem()));

    }

    @Test
    public void deserialize_should_return_null_for_not_instantiated_collections() {
        SampleCoreCollection sampleCollection = new SampleCoreCollection();

        EntityWrapper<SampleCoreCollection, String> sdbEntity = new EntityWrapper<>(this.<SampleCoreCollection>readEntityInformation(SampleCoreCollection.class), sampleCollection);
        final Map<String, List<String>> attributes = sdbEntity.serialize();

        /* convert back */
        final EntityWrapper<SampleCoreCollection, String> convertedEntity = new EntityWrapper<>(this.<SampleCoreCollection>readEntityInformation(SampleCoreCollection.class));
        convertedEntity.deserialize(attributes);

        assertTrue(sampleCollection.equals(convertedEntity.getItem()));

    }

    @Test
    public void serialize_should_return_attribute_name_key() {
        SampleCoreCollection collection = new SampleCoreCollection();
        collection.setCollectionOfLongs(new LinkedHashSet<Long>());
        collection.setHashSetOfFloats(new HashSet<Float>());
        collection.setListOfBytes(new ArrayList<Byte>());
        collection.setListOfObjects(new ArrayList<User>());
        collection.setSetOfIntegers(new HashSet<Integer>());

        /* ----------------------- Serialize Representation ------------------------ */
        EntityWrapper<SampleCoreCollection, String> sdbEntity = new EntityWrapper<>(this.<SampleCoreCollection>readEntityInformation(SampleCoreCollection.class), collection);
        final Map<String, List<String>> attributes = sdbEntity.serialize();

        assertTrue(attributes.size() == 5);

        for(String attributeName : AttributeUtil.<SampleCoreCollection>getAttributeNamesThroughReflection(SampleCoreCollection.class)) {
            assertTrue(attributes.containsKey(attributeName));
        }

    }

    private <E> SimpleDbEntityInformation<E, String> readEntityInformation(Class<E> clazz) {
        return (SimpleDbEntityInformation<E, String>) SimpleDbEntityInformationSupport.<E>getMetadata(clazz);
    }
}
