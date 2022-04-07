package edu.byu.cs.tweeter.util;

public class DataAccessException extends Exception{
    public DataAccessException(String errorMessage){
        super(errorMessage);
    }
}
