package com.sample.spring.repository;

import com.sample.spring.common.exception.BizException;
import com.sample.spring.common.model.ModelQueryDto;
import com.sample.spring.common.model.PageableDto;
import com.sample.spring.common.model.QueryCondition;
import com.sample.spring.common.model.QueryModelDto;
import com.sample.spring.enums.BizErrorCode;
import lombok.SneakyThrows;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public abstract class BasicMongoRepository<T> implements BasicRepository<T> {

    protected final MongoTemplate template;

    protected BasicMongoRepository(MongoTemplate template) {
        Assert.notNull(template, "Template must not be null");
        this.template = template;
    }

    @Override
    public void create(T entity) {
        this.template.save(entity);
    }

    @SneakyThrows
    @Override
    public void update(T entity) {
        if (ObjectUtils.isEmpty(entity))
            throw new BizException(BizErrorCode.E0002);
        this.create(entity);
    }

    @Override
    public void removeByField(ModelQueryDto modelQuery) {
        Query query = this.generateQuery(modelQuery);
        this.template.remove(query);
    }

    @Override
    public void removeById(Object id) {
        this.removeByField(new ModelQueryDto(List.of(new QueryModelDto(this.getPrimaryKey(), QueryCondition.IS, id))));
    }

    @Override
    public T findByField(ModelQueryDto modelQuery) {
        Query query = this.generateQuery(modelQuery);
        return this.template.findOne(query, this.getEntityClass());
    }

    @Override
    public T findById(Object id) {
        return this.findByField(new ModelQueryDto(List.of(new QueryModelDto(this.getPrimaryKey(), QueryCondition.IS, id))));
    }

    @Override
    public List<T> findMany(ModelQueryDto modelQuery) {
        Query query = this.generateQuery(modelQuery);
        if (ObjectUtils.isNotEmpty(modelQuery.getPageable()))
            query.with(this.generatePageable(modelQuery.getPageable()));
        List<T> list = this.template.find(query, this.getEntityClass());
        if (ObjectUtils.isEmpty(list))
            return Collections.emptyList();
        return this.template.find(query, this.getEntityClass());
    }

    @Override
    public long count(ModelQueryDto modelQuery) {
        Query query = this.generateQuery(modelQuery);
        return this.template.count(query, this.getEntityClass());
    }

    private Query generateQuery(ModelQueryDto modelQuery) {
        Query query = new Query();
        if (ObjectUtils.isNotEmpty(modelQuery.getFields()))
            modelQuery.getFields().forEach(field -> query.fields().include(field));
        else
            this.getStrFields().forEach(field -> query.fields().include(field));
        if (ObjectUtils.anyNull(modelQuery, modelQuery.getQueries())) return query;
        for (QueryModelDto q : modelQuery.getQueries()) {
            Criteria criteria = this.getCriteria(q);
            query.addCriteria(criteria);
        }
        return query;
    }

    private Pageable generatePageable(PageableDto pageable) {
        Sort sort = ObjectUtils.isEmpty(pageable.getSort()) ? Sort.by(Sort.Direction.ASC, this.getStrFields().get(0)) : pageable.getSort();
        return PageRequest.of(pageable.getPage() - 1, pageable.getRpp(), sort);
    }

    private Criteria getCriteria(QueryModelDto modelQuery) {
        Criteria criteria = new Criteria();
        switch (modelQuery.getCondition()) {
            case IS:
                criteria = Criteria.where(modelQuery.getFieldName()).is(modelQuery.getFieldValue());
                break;
            case LIKE:
                criteria = Criteria.where(modelQuery.getFieldName()).regex(String.valueOf(modelQuery.getFieldValue()));
                break;
        }
        return criteria;
    }

    private List<String> getStrFields() {
        return this.getFields().stream().map(field -> field.getName()).collect(Collectors.toList());
    }

    private String getPrimaryKey() {
        Optional<Field> primaryKey = this.getFields().stream().filter(field -> Objects.nonNull(field.getAnnotation(Id.class))).findFirst();
        if (primaryKey.isPresent())
            return primaryKey.get().getName();
        return StringUtils.EMPTY;
    }

    private List<Field> getFields() {
        Set<Field> fields = new HashSet<>();
        Class<?> current = this.getEntityClass();
        while (ObjectUtils.isNotEmpty(current) && !("java.lang.Object".equalsIgnoreCase(current.getName()))) {
            List<Field> declaredFields = List.of(current.getDeclaredFields());
            if (ObjectUtils.isNotEmpty(current.getFields()))
                declaredFields = new ArrayList<>(CollectionUtils.subtract(declaredFields, List.of(current.getFields())));
            if (!declaredFields.isEmpty()) {
                fields.addAll(declaredFields);
            } else {
                fields.addAll(Arrays.asList(current.getDeclaredFields()));
            }
            current = current.getSuperclass();
        }
        return new ArrayList<>(fields);
    }
}
