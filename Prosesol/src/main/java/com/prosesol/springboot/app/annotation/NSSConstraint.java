package com.prosesol.springboot.app.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.prosesol.springboot.app.validator.NSSConstraintValidator;

@Documented
@Constraint(validatedBy = NSSConstraintValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NSSConstraint {

	String message() default "No cuenta con la longitud correcta";
	Class<?>[] groups() default{};
	Class<? extends Payload>[] payload() default{};
}
