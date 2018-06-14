package com.example.administrator.mannotation;

import android.support.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@IntDef({Constant.ONE, Constant.FIVE, Constant.TEN})//建议值
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})//可以作用于变量，方法，参数列表
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Level {
    int value();//没有设置默认值，所以使用的时候需要设置值
}
