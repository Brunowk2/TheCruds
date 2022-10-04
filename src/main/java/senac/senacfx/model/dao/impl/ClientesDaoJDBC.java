package senac.senacfx.model.dao.impl;

import senac.senacfx.db.DB;
import senac.senacfx.db.DbException;
import senac.senacfx.model.dao.ClientesDao;
import senac.senacfx.model.entities.Clientes;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientesDaoJDBC implements ClientesDao {
    private Connection conn;

    public ClientesDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Clientes obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("insert into Clientes " +
                    "(Nome, cpf, codigo vendedor) " +
                    "values (?, ?, ?,) ",
                    Statement.RETURN_GENERATED_KEYS);

            st.setString(1, obj.getName());
            st.setString(2, obj.getName());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0){
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()){
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                DB.closeResultSet(rs);
            } else {
                throw new DbException("Error! No rows affected!");
            }

        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Clientes obj) {

        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("update Clientes " +
                            "set Name = ? " +
                            "where Id = ?");

            st.setString(1, obj.getName());
            st.setInt(2, obj.getId());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected == 0){
                throw new DbException("Error! No rows affected!");
            }

        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement("delete from Clientes where Id = ?");

            st.setInt(1, id);

            int rowsAffected = st.executeUpdate();

            if (rowsAffected == 0){
                throw new DbException("Departamento inexistente!");
            }

        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Clientes findById(Integer id) {

        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement("" +
                    "select * from Clientes " +
                    "where Id = ?");

            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()){
                Clientes dep = instantiateClientes(rs);
                return dep;

            }
            return null;
        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }

    }

    private Clientes instantiateClientes(ResultSet rs) throws SQLException {
        Clientes dep = new Clientes(cpf, idVendedor);
        dep.setId(rs.getInt("Id"));
        dep.setName(rs.getString("Name"));
        return dep;
    }

    @Override
    public List<Clientes> findAll() {

        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement("" +
                    "select * from Clientes "+
                    "order by Name");

            rs = st.executeQuery();

            List<Clientes> list = new ArrayList<>();
            Map<Integer, Clientes> map = new HashMap<>();

            while (rs.next()){

                Clientes dep = map.get(rs.getInt("Id"));

                if (dep == null){
                    dep = instantiateClientes(rs);
                    map.put(rs.getInt("Id"), dep);
                }

                list.add(dep);

            }
            return list;

        } catch (SQLException e){
            throw new DbException(e.getMessage());

        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }


    }
}
