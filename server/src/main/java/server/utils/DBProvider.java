package server.utils;

import server.constants.DBConstants;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DBProvider {
    Connection c;

    String url;

    private Logger log;

    private void connect(boolean isAutoCommit) throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        c = DriverManager
                .getConnection(url,
                        DBConstants.USERNAME,
                        DBConstants.PASSWORD);
        if(!isAutoCommit)
            c.setAutoCommit(false);

        log.log(Level.INFO, "Successfully connected to database ");
    }

    public DBProvider(String url) {
        log = Logger.getLogger(DBProvider.class.getName());
        this.url = url;
    }

    public List<String> getAllTables () {

        ArrayList<String> tables = new ArrayList<>();

        try {
            connect(true);
            DatabaseMetaData metaData = c.getMetaData();

            String[] types = {"TABLE"};
            ResultSet rs = metaData.getTables(null, null, "%", types);

            while (rs.next())
                tables.add(rs.getString(3));

            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tables;
    }

    public boolean executeRawSQL(String sql) {
        try {
            connect(true);
            Statement statement = c.createStatement();

            statement.execute(sql);
            statement.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public Set<Long> getAllId () {
        TreeSet<Long> selected = new TreeSet<>();

        try{
            connect(false);

            Statement s = c.createStatement();

            Iterator it = getAllTables().iterator();
            while (it.hasNext()) {

                String tmp = it.next() + "";

                if(!(tmp.contains("databasechangelog") || tmp.contains("hibernate") || Arrays.asList(DBConstants.NON_ENTITY_EXTENDED_ELEMENTS).contains(tmp))){
                    String query = "SELECT * FROM " + tmp + ";";

                    String guidName = "guid";

                    if(DBConstants.ELEMENT_COLLECTION_ELEMENTS.containsKey(tmp))
                        guidName = DBConstants.ELEMENT_COLLECTION_ELEMENTS.get(tmp);

                    ResultSet rs = s.executeQuery(query);

                    while ( rs.next() ) {
                        long guid = rs.getLong(guidName);
                        selected.add(guid);
                    }
                    rs.close();

                }
            }

            s.close();
            c.close();

        } catch (Exception e){
            e.printStackTrace();
        }

        return selected;
    }

}
