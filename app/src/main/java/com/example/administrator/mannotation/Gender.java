package com.example.administrator.mannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Gender {
    Type value() default Type.Male;

     enum Type {
        Male("男"),
        Female("女"),
        Other("中性");

        private String genderStr;

         Type(String arg0) {
            this.genderStr = arg0;
        }

        @Override
        public String toString() {
            return genderStr;
        }
    }
}
