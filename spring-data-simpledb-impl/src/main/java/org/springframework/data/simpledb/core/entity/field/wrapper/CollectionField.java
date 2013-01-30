package org.springframework.data.simpledb.core.entity.field.wrapper;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.simpledb.core.entity.EntityWrapper;
import org.springframework.data.simpledb.util.SimpleDBAttributeConverter;

public class CollectionField<T, ID extends Serializable> extends InstantiableField<T, ID> {

	CollectionField(Field field, EntityWrapper<T, ID> parent, final boolean isNewParent) {
		super(field, parent, isNewParent);
	}


    @Override
    public Map<String, List<String>> serialize(String prefix) {
        final Map<String, List<String>> result = new HashMap<>();

        final List<String> fieldValues = new ArrayList<>();

        fieldValues.add(SimpleDBAttributeConverter.coreTypesToSimpleDBAttributeValues(this.getValue()));

        result.put(prefix.isEmpty() ? getName() : prefix + "." + getName(), fieldValues);

        return result;
    }


    @Override
    public void deserialize(List<String> value) {
    }

}
