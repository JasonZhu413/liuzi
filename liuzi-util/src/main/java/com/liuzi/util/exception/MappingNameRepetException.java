package com.liuzi.util.exception;


public class MappingNameRepetException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public MappingNameRepetException(){  
        super();  
    }  
    public MappingNameRepetException(String msg){  
        super(msg);
    }  
}
