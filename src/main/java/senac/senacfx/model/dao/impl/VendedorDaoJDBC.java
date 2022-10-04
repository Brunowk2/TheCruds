package senac.senacfx.model.dao.impl;

import senac.senacfx.db.DB;
import senac.senacfx.db.DbException;
import senac.senacfx.model.entities.Clientes;
import senac.senacfx.model.entities.Department;
import senac.senacfx.model.entities.Vendedor;
import senac.senacfx.model.entities.Vendedor;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VendedorDaoJDBC {

    private Connection conn;

    public VendedorDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Vendedor obj) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement(
                    "insert into Vendedor " +
                            "(Nome, Cpf, email,comissao, ) " +
                            "values (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            st.setString(1, obj.getName());
            st.setString(2, obj.getCpf());
            st.setString(4, obj.getEmail());
            st.setDouble(3, obj.getComissao());


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
    public void update(Vendedor obj) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement(
                    "update Vendedor " +
                            "set Nome = ?, cpf = ?, email = ?, comissao = ?, ClientesId = ? " +
                            "where id = ?");

            st.setString(1, obj.getName());
            st.setString(2, obj.getCpf());
            st.setString(3, obj.getEmail());
            st.setDouble(4, obj.getComissao());
            st.setInt(5, obj.getClientes().getId());
            st.setInt(6, obj.getId());

            st.executeUpdate();

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
            st = conn.prepareStatement("delete from vendedor where Id = ?");

            st.setInt(1, id);

            int rowsAffected = st.executeUpdate();

            if (rowsAffected == 0){
                throw new DbException("Vendedor inexistente!");
            }

        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Vendedor findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement("" +
                    "select vendedor.*, department.Name as DepName " +
                    "from vendedor inner join department " +
                    "on vendedor.DepartmentId = department.Id " +
                    "where vendedor.Id = ?");

            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()){
                Vendedor dep = instantiateVendedor(rs);
                Vendedor obj = instantiateVendedor(rs, dep);
                return obj;

            }
            return null;
        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("DepName"));
        return dep;
    }

    private Vendedor instantiateVendedor(ResultSet rs, Department dep) throws SQLException{
        Vendedor obj = new Vendedor();
        obj.setId(rs.getInt("Id"));
        obj.setName(rs.getString("Name"));
        obj.setCpf(rs.getString("Cpf"));
        obj.setEmail(rs.getString("Email"));
        obj.setComissao(rs.getDouble("Comissao"));
        obj.setClientes(dep);
        return obj;
    }
    @Override
    public List<Vendedor> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement("" +
                    "select vendedor.*, department.Name as DepName " +
                    "from vendedor inner join department " +
                    "on vendedor.DepartmentId = department.Id " +
                    "order by Name");

            rs = st.executeQuery();

            List<Vendedor> list = new ArrayList<>();
            Map<Integer, Clientes> map = new HashMap<>();

            while (rs.next()){

                Clientes dep = map.get(rs.getInt("ClientesId"));

                if (dep == null){
                    dep = instantiateVendedor(rs);
                    map.put(rs.getInt("ClientesId"), dep);
                }

                Vendedor obj = instantiateVendedor(rs, dep);
                list.add(obj);
            }
            return list;
        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Vendedor> findByDepartment(Vendedor department) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement("" +
                    "select vendedor.*, department.Name as DepName " +
                    "from vendedor inner join department " +
                    "on vendedor.ClientesId = department.Id " +
                    "where ClientesId = ? " +
                    "order by Name");

            st.setInt(1, department.getId());

            rs = st.executeQuery();

            List<Clientes> list = new ArrayList<>();
            Map<Integer, Clientes> map = new HashMap<>();

            while (rs.next()){

                Clientes dep = map.get(rs.getInt("ClientesId"));

                if (dep == null){
                    dep = instantiateClientes(rs);
                    map.put(rs.getInt("ClientesId"), dep);
                }

                Clientes obj = instantiateClientes(rs, dep);
                list.add(obj);
            }
            return list;
        } catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    private Clientes instantiateClientes(ResultSet rs) {
    }
}

}
