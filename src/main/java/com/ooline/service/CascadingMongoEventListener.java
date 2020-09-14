package com.ooline.service;

import com.ooline.OolineApplication;
import com.ooline.annotation.CascadeSave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mapping.MappingException;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;

@Service
public class CascadingMongoEventListener extends AbstractMongoEventListener {
    private static final Logger logger = LoggerFactory.getLogger(OolineApplication.class);
    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public void onBeforeConvert(BeforeConvertEvent event) {
        logger.info("onBeforeConvert method is called");
        Object source = event.getSource();
        ReflectionUtils.doWithFields(source.getClass(), new ReflectionUtils.FieldCallback() {
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                ReflectionUtils.makeAccessible(field);
                if (field.isAnnotationPresent(DBRef.class) && field.isAnnotationPresent(CascadeSave.class)) {
                    final Object fieldValue = field.get(source);
                    if (fieldValue != null) {
                        if (fieldValue instanceof List<?>) {
                            for (Object item : (List<?>) fieldValue) {
                                checkNSave(item);
                            }
                        } else {
                            checkNSave(fieldValue);
                        }
                    }
                }
            }
        });
    }

    public void checkNSave(Object fieldValue) {
        logger.info("Method called checkNSave to save the fieldValue {}", fieldValue);
        DbRefFieldCallback callback = new DbRefFieldCallback();
        ReflectionUtils.doWithFields(fieldValue.getClass(), callback);
        if (!callback.isIdFound()) {
            logger.info("Cannot perform cascade save on child object without id set");
            throw new MappingException("Cannot perform cascade save on child object without id set");
        }
        mongoOperations.save(fieldValue);
    }

    private static class DbRefFieldCallback implements ReflectionUtils.FieldCallback {
        private boolean idFound;

        public void doWith(Field field) throws IllegalArgumentException {
            ReflectionUtils.makeAccessible(field);

            if (field.isAnnotationPresent(Id.class)) {
                idFound = true;
            }
        }

        public boolean isIdFound() {
            return idFound;
        }
    }
}