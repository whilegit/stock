package yjt.api.v1.Annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import yjt.api.v1.Interceptor.ParamInterceptor;


@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Repeatable(value = ParamAnnotations.class) 
public @interface ParamAnnotation{
	String name();
	ParamInterceptor.Type type();
	boolean must();
	int min() default Integer.MIN_VALUE;
	int max() default Integer.MAX_VALUE;
	double mind() default Double.MIN_VALUE;
	double maxd() default Double.MAX_VALUE;
	int minlen() default 0;
	int maxlen() default Integer.MAX_VALUE;
	String[] allow_list() default {""};
	String chs();
	
	String typeErrTips() default "";
	String mustErrTips() default "";
	String minErrTips() default "";
	String maxErrTips() default "";
	String minlenErrTips() default "";
	String maxlenErrTips() default "";
	String allowListErrTips() default "";
}