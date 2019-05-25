package com.prosesol.springboot.app.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.prosesol.springboot.app.annotation.NSSConstraint;

public class NSSConstraintValidator implements ConstraintValidator<NSSConstraint, Long>{

	@Override
	public void initialize(NSSConstraint nssConstraint) {
	}
	
	@Override
	public boolean isValid(Long nss, ConstraintValidatorContext context) {
		
		if(nss == null) {
			return false;
		}else {
			return (nss.toString().length() == 11);
		}	
		
	}

}
