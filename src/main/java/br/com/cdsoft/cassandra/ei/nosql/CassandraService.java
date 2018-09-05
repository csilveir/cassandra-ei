package br.com.cdsoft.cassandra.ei.nosql;

import com.datastax.driver.core.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CassandraService {

    @Value("${cassandra.host}")
    private String host;

    @Value("${cassandra.port}")
    private int port;

    @Value("${cassandra.username}")
    private String username;

    @Value("${cassandra.password}")
    private String password;

    public String getKeyspaceName() {
        return keyspaceName;
    }

    @Value("${cassandra.keyspace}")
    private String keyspaceName;


    public


    @Autowired
    CassandraConnector cassandraConnector;

    public boolean isConnected() {
        return cassandraConnector.isConnected();
    }

    public Session getSession() {
        if (!cassandraConnector.isConnected()) {
            cassandraConnector.connect(host, port, username, password, keyspaceName);
        }
        return cassandraConnector.getSession();
    }

    public List<String> getMatchedKeyspaces() {


        Session session = getSession();
        var schemaRepository = new KeyspaceRepository(session);

        schemaRepository.createKeyspace(keyspaceName, "SimpleStrategy", 1);

        final var result =
                session.execute("SELECT * FROM system_schema.keyspaces;");

        return result.all()
                .stream()
                .filter(r -> r.getString(0).equals(keyspaceName.toLowerCase()))
                .map(r -> r.getString(0))
                .collect(Collectors.toList());


    }
}