package br.com.cdsoft.cassandra.ei.business;

import br.com.cdsoft.cassandra.ei.dto.PropertyDTO;
import br.com.cdsoft.cassandra.ei.dto.TypeProperty;
import br.com.cdsoft.cassandra.ei.nosql.CassandraService;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.utils.UUIDs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public static final String PROPERTYVALUE = "propertyvalue";
    public static final String ID = "id";
    @Autowired
    private CassandraService cassandraService;

    public static final String INSERT_INTO_LIBRARY_PROPERTY = "insert into %s.property (id, propertyname, propertyvalue, dtproperty, type) values (?, ?, ?, ?, ?)";

    public List<PropertyDTO> getProperties() {

        var list = new ArrayList<PropertyDTO>();


        var session = cassandraService.getSession();

        PreparedStatement pst = session.prepare(
                select().all()
                        .from(cassandraService.getKeyspaceName(), PROPERTY)
                        .limit(bindMarker(LIMITED)));

        final List<Row> all = session.execute(pst.bind()
                .setInt(LIMITED, LIMIT)).all();

        return populateProperty(list, all);

    }

    private List<PropertyDTO> populateProperty(ArrayList<PropertyDTO> list, List<Row> all) {
        all.forEach(row -> {

            PropertyDTO property = new PropertyDTO(
                    row.getUUID(ID),
                    row.getString(PROPERTYNAME),
                    row.getString(PROPERTYVALUE),
                    row.getTimestamp(DTPROPERTY));

            var type = row.getString("type");
            if (Objects.nonNull(type))
                property.setType(TypeProperty.valueOf(type));

            list.add(property);
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

        var all = session.execute(pst.bind()
                .setInt(LIMITED, LIMIT)).all();

        return populateProperty(list, all);

    }

    public PropertyDTO insertProperty(final PropertyDTO propertyDTO) {

        var session = cassandraService.getSession();


        var prepared =
                session.prepare(
                        String.format(INSERT_INTO_LIBRARY_PROPERTY, cassandraService.getKeyspaceName()));

        if (Objects.nonNull(propertyDTO.getKey()) && Objects.nonNull(propertyDTO.getValue())) {
            BoundStatement bound = prepared.bind(UUIDs.timeBased(), propertyDTO.getKey(),
                    propertyDTO.getValue(), propertyDTO.getDtProperty(), Objects.nonNull(propertyDTO.getType()) ? propertyDTO.getType().toString() : null);
            session.executeAsync(bound);
            return propertyDTO;
        }


        return null;
    }

}
