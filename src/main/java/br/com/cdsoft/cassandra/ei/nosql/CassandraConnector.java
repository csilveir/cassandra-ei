package br.com.cdsoft.cassandra.ei.nosql;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CassandraConnector {

    private Cluster cluster;

    private Session session;

    protected boolean isConnected() {
        return connected;
    }

    private boolean connected = false;



    protected void connect(final String node, final Integer port, final String username, final String password, final String keyspace) {
        Cluster.Builder contactPoint = Cluster.builder().addContactPoint(node);
        if (port != null) {
            contactPoint.withPort(port);
        }
        if (Objects.nonNull(username) && Objects.nonNull(password)) {
            contactPoint.withCredentials(username, password);
        }
        cluster = contactPoint.build();
        session = cluster.connect(keyspace);
        connected = true;
    }

    protected Session getSession() {
        return this.session;
    }

    protected void close() {
        session.close();
        cluster.close();
        connected = false;
    }
}
