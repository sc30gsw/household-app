package com.example.demo.domain.validate;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = UniqueValidator.class)
@Target({FIELD})
@Retention(RUNTIME)
public @interface Unique {

	// エラーメッセージ
	String message() default "すでに登録済みのメールアドレスです"; 

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({FIELD})
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        Unique[] value();
    }
}
