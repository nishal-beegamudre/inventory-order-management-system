package com.productservice.keygenerator;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class CategoryKeyGenerator implements IdentifierGenerator { 

    private static AtomicLong counter = new AtomicLong(0);

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        long nextValue = counter.incrementAndGet();
        
        Long timestamp = System.currentTimeMillis();

        return String.format("CAT" + timestamp.toString() + "%010d", nextValue); 
    }

}

