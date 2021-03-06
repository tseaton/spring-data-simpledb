package org.springframework.data.simpledb.query;

import java.lang.reflect.Field;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.simpledb.core.SimpleDbOperations;
import org.springframework.data.simpledb.query.executions.AbstractSimpleDbQueryExecution;
import org.springframework.data.simpledb.query.executions.MultipleResultExecution;
import org.springframework.data.simpledb.query.executions.PagedResultExecution;
import org.springframework.data.simpledb.query.executions.SingleResultExecution;
import org.springframework.data.simpledb.reflection.FieldType;
import org.springframework.data.simpledb.reflection.FieldTypeIdentifier;
import org.springframework.data.simpledb.reflection.ReflectionUtils;

/**
 * {@link RepositoryQuery} implementation that inspects a {@link SimpleDbQueryMethod} for the existence of an
 * {@link org.springframework.data.simpledb.annotation.Query} annotation and provides implementations based on query
 * method information.
 */
public class SimpleDbRepositoryQuery implements RepositoryQuery {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleDbRepositoryQuery.class);
	private final SimpleDbQueryMethod method;
	private final SimpleDbOperations simpledbOperations;

	public SimpleDbRepositoryQuery(SimpleDbQueryMethod method, SimpleDbOperations simpledbOperations) {
		this.method = method;
		this.simpledbOperations = simpledbOperations;
	}

	@Override
	public Object execute(Object[] parameters) {
		return getExecution().execute(method, parameters);
	}

	@Override
	public QueryMethod getQueryMethod() {
		return method;
	}

	/**
	 * Creates a {@link RepositoryQuery} from the given {@link org.springframework.data.repository.query.QueryMethod}
	 * that is potentially annotated with {@link org.springframework.data.simpledb.annotation.Query}.
	 * 
	 * @param queryMethod
	 * @return the {@link RepositoryQuery} derived from the annotation or {@code null} if no annotation found.
	 */
	public static RepositoryQuery fromQueryAnnotation(SimpleDbQueryMethod queryMethod,
			SimpleDbOperations simpleDbOperations) {
		LOGGER.debug("Looking up query for method {}", queryMethod.getName());
		return queryMethod.getAnnotatedQuery() == null ? null : new SimpleDbRepositoryQuery(queryMethod,
				simpleDbOperations);
	}

	protected AbstractSimpleDbQueryExecution getExecution() {
		String query = method.getAnnotatedQuery();
		assertNotHavingNestedQueryParameters(query);

		if(method.isPagedQuery()) {
			/*
			 * Paged query must be checked first because the checking is done based on parameter types in the query
			 * method's signature, while the rest of the checks are based on the method's return type
			 */

			return new PagedResultExecution(simpledbOperations);
		}
		if(method.isCollectionQuery()) {
			return new MultipleResultExecution(simpledbOperations);
		} else if(method.isModifyingQuery()) {
			throw new IllegalArgumentException(
					"Modifying query not supported. Please use repository methods for update operations.");
		} else {
			return new SingleResultExecution(simpledbOperations);
		}
	}

	void assertNotHavingNestedQueryParameters(String query) {
		List<String> attributesFromQuery = QueryUtils.getQueryPartialFieldNames(query);
		final Class<?> domainClass = method.getDomainClazz();
		for(String attribute : attributesFromQuery) {
			try {
				Field field = ReflectionUtils.getDeclaredFieldInHierarchy(domainClass, attribute); 
				if(FieldTypeIdentifier.isOfType(field, FieldType.NESTED_ENTITY)) {
					throw new IllegalArgumentException("Invalid query parameter :" + attribute + " is nested object");
				}
			} catch(NoSuchFieldException e) {
				// might be a count or something else
			}
		}
	}
}
