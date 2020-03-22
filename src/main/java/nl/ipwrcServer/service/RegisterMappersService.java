package nl.ipwrcServer.service;

import nl.ipwrcServer.model.Account;
import nl.ipwrcServer.model.Product;
import nl.ipwrcServer.model.User;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.BeanMapper;

public class RegisterMappersService {

    private Jdbi jdbi;

    private final Class[] registerRowClasses = new Class[] {
            Account.class,
            User.class,
            Product.class
    };

    public RegisterMappersService(Jdbi jdbi){
        this.jdbi = jdbi;
    }

    public void registerMappersToModels(){
        for(Class registerRowClass : registerRowClasses){
            jdbi.registerRowMapper(BeanMapper.factory(registerRowClass));
        }
    }

}
