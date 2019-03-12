package com.liuzi.util.exception;

public class CustomException extends Throwable{
	
	private static final long serialVersionUID = 1L;

    public CustomException(String message){
        super(message);
    }
}
