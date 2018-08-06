package com.liuzi.util.optimization;

import java.io.FileNotFoundException;

public class ServiceException extends FileNotFoundException{

	private static final long serialVersionUID = 1L;
	
	public ServiceException(){  
        super();  
    }  
    public ServiceException(String msg){  
        super(msg);
    }  
}
