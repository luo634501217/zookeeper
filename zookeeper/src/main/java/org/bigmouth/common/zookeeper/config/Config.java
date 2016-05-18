package org.bigmouth.common.zookeeper.config;
 
 
public interface Config {
 
    byte[] getConfig(String path) throws Exception;
}