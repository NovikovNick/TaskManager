/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package com.metalheart.model.jpa;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.usertype.DynamicParameterizedType;
import org.hibernate.usertype.EnhancedUserType;
import org.hibernate.usertype.LoggableUserType;
import org.postgresql.util.PGobject;


/**
 * Custom implementation of enum type to support postgres enums. Inspired by {@link org.hibernate.type.EnumType}.
 */
@Slf4j
public class PostgresEnumType implements EnhancedUserType, DynamicParameterizedType, LoggableUserType, Serializable {
    public static final String ENUM = "enumClass";
    public static final String TYPE = "type";

    private static final int SQL_TYPE = Types.OTHER;
    private static final long serialVersionUID = -4076945100533184136L;

    private Class<? extends Enum> enumClass;

    @Override
    public void setParameterValues(Properties parameters) {
        ParameterType reader = (ParameterType) parameters.get(PARAMETER_TYPE);

        if (reader != null) {
            enumClass = reader.getReturnedClass().asSubclass(Enum.class);
        } else {
            String enumClassName = (String) parameters.get(ENUM);
            try {
                enumClass = ReflectHelper.classForName(enumClassName, this.getClass()).asSubclass(Enum.class);
            } catch (ClassNotFoundException exception) {
                throw new HibernateException("Enum class not found: " + enumClassName, exception);
            }
        }
    }

    @Override
    public int[] sqlTypes() {
        return new int[]{SQL_TYPE};
    }

    @Override
    public Class<? extends Enum> returnedClass() {
        return enumClass;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return Objects.equals(x, y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x == null ? 0 : x.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
        throws SQLException {

        Object object = rs.getObject(names[0]);

        if (rs.wasNull()) {
            log.trace("Returning null as column [{}]", names[0]);
            return null;
        }

        if (object instanceof PGobject) {
            PGobject pgObject = (PGobject) object;
            Enum enumValue = fromName(pgObject.getValue());
            logEnum(names[0], enumValue);
            return enumValue;
        }

        if (object instanceof String) {
            Enum enumValue = fromName((String) object);
            logEnum(names[0], enumValue);
            return enumValue;
        }
        return null;
    }

    private void logEnum(String name, Enum enumValue) {
        log.trace("Returning [{}] as column [{}]", enumValue, name);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
        throws HibernateException, SQLException {

        if (value == null) {
            log.trace("Binding null to parameter: [{}]", index);
            st.setNull(index, SQL_TYPE);
            return;
        }

        log.trace("Binding [{}] to parameter: [{}]", value, index);
        st.setObject(index, value, SQL_TYPE);
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    @Override
    public String objectToSQLString(Object value) {
        return '\'' + toXMLString(value) + '\'';
    }

    @Override
    public String toXMLString(Object value) {
        return ((Enum) value).name();
    }

    @Override
    public Object fromXMLString(String xmlValue) {
        return fromName(xmlValue);
    }

    @Override
    public String toLoggableString(Object value, SessionFactoryImplementor factory) {
        return toXMLString(value);
    }

    private Enum fromName(String name) {
        try {
            if (name == null) {
                return null;
            }
            return Enum.valueOf(enumClass, name.trim());
        } catch (IllegalArgumentException iae) {
            throw new IllegalArgumentException(String.format("Unknown name value [%s] for enum class [%s]",
                name, enumClass.getName()));
        }
    }
}
