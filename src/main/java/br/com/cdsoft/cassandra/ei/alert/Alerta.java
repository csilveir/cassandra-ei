package br.com.cdsoft.cassandra.ei.alert;

public interface Alerta {


    String getPayload(Formmater formatter) throws Exception;
}
