package com.bank.customer.domain.exceptions;

public class CustomerExceptions {

    public static class CustomerNotFoundException extends RuntimeException {
        public CustomerNotFoundException(String message) {
            super(message);
        }
    }

    public static class CustomerAlreadyExistsException extends RuntimeException {
        public CustomerAlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class CustomerInactiveException extends RuntimeException {
        public CustomerInactiveException(String message) {
            super(message);
        }
    }

    public static class InvalidCustomerDataException extends RuntimeException {
        public InvalidCustomerDataException(String message) {
            super(message);
        }
    }

    public static class CustomerAuthenticationException extends RuntimeException {
        public CustomerAuthenticationException(String message) {
            super(message);
        }
    }
}