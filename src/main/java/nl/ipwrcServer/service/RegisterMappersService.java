package nl.ipwrcServer.service;

import nl.ipwrcServer.model.Product;
import nl.ipwrcServer.model.User;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.BeanMapper;

public class RegisterMappersService {

    private Jdbi jdbi;

    private final Class[] registerClasses = new Class[] {
            User.class,
            Product.class
    };

    public RegisterMappersService(Jdbi jdbi){
        this.jdbi = jdbi;
    }

    public void registerMappersToModels(){
        for(Class registerClass : registerClasses){
            jdbi.registerRowMapper(BeanMapper.factory(registerClass));
        }
    }

}
