package com.thirdpillar.codify.loanpath.lookup;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.thirdpillar.foundation.lookup.Lookup;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Lookup(provider=AttributeChoiceLookupProvider.class)
public @interface AttributeChoiceLookup {

	String key();
	
	String groupMvel() default "";
	
	boolean effective() default true;
	
}
