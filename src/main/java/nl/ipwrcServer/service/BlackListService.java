package nl.ipwrcServer.service;

import nl.ipwrcServer.model.Token;
import org.apache.commons.jcs.access.CacheAccess;
import org.apache.commons.jcs.JCS;
import org.apache.commons.jcs.access.exception.CacheException;

public class BlackListService {

    private CacheAccess<String, String> blackListCache;
    private LoggerService loggerService;

    public BlackListService(){
        initializeCache();
        this.loggerService = new LoggerService(BlackListService.class);
    }

    private void initializeCache(){
        try{
            blackListCache = JCS.getInstance("default");
        }catch (CacheException initializeException){
            loggerService.getWebLogger().warn("Could not initialize blackList cache");
        }
    }

    public void putInBlackListCache(Token credentials){
        try {
            blackListCache.put(credentials.getCsrfToken() , credentials.getTokenBundle());
        }catch (CacheException putException){
            this.loggerService.getWebLogger().warn("Problem putting token in blacklist");
        }
    }

    public boolean ifCredentialsInBlackList(Token credentials){

        return blackListCache.get(credentials.getCsrfToken()) != null;
    }
}
