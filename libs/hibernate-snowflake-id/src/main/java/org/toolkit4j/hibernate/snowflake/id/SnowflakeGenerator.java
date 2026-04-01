package org.toolkit4j.hibernate.snowflake.id;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.hibernate.annotations.IdGeneratorType;

@IdGeneratorType(HibernateSnowflakeIdGenerator.class)
@Target({METHOD, FIELD})
@Retention(RUNTIME)
@SuppressWarnings("unused")
public @interface SnowflakeGenerator {}
