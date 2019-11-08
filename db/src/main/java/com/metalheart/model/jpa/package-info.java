/**
 * This package was created for JPA model.
 */
@TypeDefs({
    @TypeDef(name = "postgres_enum", typeClass = PostgresEnumType.class)
})
package com.metalheart.model.jpa;

import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;