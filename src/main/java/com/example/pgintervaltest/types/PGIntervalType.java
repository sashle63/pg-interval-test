package com.example.pgintervaltest.types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;
import org.postgresql.util.PGInterval;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class PGIntervalType implements UserType {

    public static final PGIntervalType INSTANCE = new PGIntervalType();

    public Class<?> returnedClass() {
        return Duration.class;
    }


    public int[] sqlTypes() {
        return new int[] {Types.OTHER};
    }


    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException {
        try {
            final PGInterval pgi = (PGInterval) rs.getObject(names[0]);
            final int days = pgi.getDays();
            final int hours = pgi.getHours();
            final int mins = pgi.getMinutes();
            final double secs = pgi.getSeconds();

            return Duration.ofDays(days)
                    .plus(hours, ChronoUnit.HOURS)
                    .plus(mins, ChronoUnit.MINUTES)
                    .plus(Math.round(secs), ChronoUnit.SECONDS);

        }
        catch (Exception e) {
            return null;
        }
    }


    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, Types.OTHER);
        }
        else {
            final Duration period = ((Duration)value);
            final int days = (int) period.toDays();
            final int hours = (int) (period.toHours() - days * 24);
            final int mins = (int) (period.toMinutes() - period.toHours() * 60);
            final int secs = (int) (period.getSeconds() - mins * 60);

            final PGInterval pgi = new PGInterval(0, 0, days, hours, mins, secs);
            st.setObject(index, pgi);
        }
    }


    public boolean equals(Object x, Object y)
            throws HibernateException {

        return x == y;
    }


    public int hashCode(Object x)
            throws HibernateException {
        return x.hashCode();
    }


    public Object deepCopy(Object value)
            throws HibernateException {
        return value;
    }


    public boolean isMutable() {
        return false;
    }


    public Serializable disassemble(Object value)
            throws HibernateException {
        return (Serializable) value;
    }


    public Object assemble(Serializable cached, Object owner)
            throws HibernateException {
        return cached;
    }


    public Object replace(Object original, Object target, Object owner)
            throws HibernateException {
        return original;
    }

}
