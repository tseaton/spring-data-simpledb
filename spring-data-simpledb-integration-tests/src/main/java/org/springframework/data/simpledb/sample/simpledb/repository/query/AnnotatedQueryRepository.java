package org.springframework.data.simpledb.sample.simpledb.repository.query;

import org.springframework.data.repository.query.Param;
import org.springframework.data.simpledb.annotation.Query;
import org.springframework.data.simpledb.repository.SimpleDbPagingAndSortingRepository;
import org.springframework.data.simpledb.sample.simpledb.domain.SimpleDbUser;

import java.util.List;

public interface AnnotatedQueryRepository extends SimpleDbPagingAndSortingRepository<SimpleDbUser, String> {

    @Query(value = "select * from simpleDb.User where lastName=:lastName")
    public List<SimpleDbUser> sampleQueryMethodWithDeclaredParams(@Param(value = "lastName") String lastName);

    @Query(value = "select * from simpleDb.User where lastName=?")
    public List<SimpleDbUser> sampleQueryMethodWithPlainQuery(String lastName);
}