package br.com.cdsoft.cassandra.ei.business;

import br.com.cdsoft.cassandra.ei.dto.PropertyDTO;
import br.com.cdsoft.cassandra.ei.nosql.CassandraService;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.utils.UUIDs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.datastax.driver.core.querybuilder.QueryBuilder.bindMarker;
import static com.datastax.driver.core.querybuilder.QueryBuilder.select;

@Service
public class PropertyService {


    public static final int LIMIT = 100;
    public static final String PROPERTY = "property";
    public static final String LIMITED = "limited";
    public static final String PROPERTYNAME = "propertyname";
    public static final String DTPROPERTY = "dtproperty";
    @Autowired
    private CassandraService cassandraService;

    public static final String INSERT_INTO_LIBRARY_PROPERTY = "insert into %s.property (id, propertyname, propertyvalue, dtproperty) values (?, ?, ?, ?)";

    public List<PropertyDTO> getProperties() {

        var list = new ArrayList<PropertyDTO>();


        var session = cassandraService.getSession();

        PreparedStatement pst = session.prepare(
                select().all()
                        .from(cassandraService.getKeyspaceName(), PROPERTY)
                        .limit(bindMarker(LIMITED)));

        final List<Row> all = session.execute(pst.bind()
                .setInt("limited", LIMIT)).all();

        return getPropertyDTOS(list, all);

    }

    private List<PropertyDTO> getPropertyDTOS(ArrayList<PropertyDTO> list, List<Row> all) {
        all.forEach(row -> {
            list.add(new PropertyDTO(
                    row.getString("propertyname"),
                    row.getString("propertyvalue"),
                    row.getTimestamp("dtproperty")

            ));
        });
        return list;
    }

    public List<PropertyDTO> getProperty(final String property) {

        var list = new ArrayList<PropertyDTO>();


        var session = cassandraService.getSession();

        PreparedStatement pst = session.prepare(
                select()
                        .all()
                        .from(cassandraService.getKeyspaceName(), PROPERTY)
                        .allowFiltering()
                        .where(QueryBuilder.eq(PROPERTYNAME, Objects.requireNonNull(property)))
                        .orderBy(QueryBuilder.desc(DTPROPERTY))
                        .limit(bindMarker(LIMITED)));

        final List<Row> all = session.execute(pst.bind()
                .setInt("limited", LIMIT)).all();

        return getPropertyDTOS(list, all);

    }

    public PropertyDTO insertProperty(final String key, final String value) {

        var session = cassandraService.getSession();

        var prepared =
                session.prepare(
                        String.format(INSERT_INTO_LIBRARY_PROPERTY, cassandraService.getKeyspaceName()));

        if (Objects.nonNull(key) && Objects.nonNull(value)) {
            PropertyDTO propertyDTO = new PropertyDTO(key, value, new Date());
            BoundStatement bound = prepared.bind(UUIDs.timeBased(), propertyDTO.getKey(),
                    propertyDTO.getValue(), propertyDTO.getDtProperty());
            session.execute(bound);
            return propertyDTO;
        }


        return null;
    }

}
