package senac.senacfx.model.dao;

import senac.senacfx.db.DB;
import senac.senacfx.model.dao.impl.DepartmentDaoJDBC;
import senac.senacfx.model.dao.impl.SellerDaoJDBC;
import senac.senacfx.model.entities.Clientes;
import senac.senacfx.model.entities.Seller;
import senac.senacfx.model.entities.Vendedor;

import java.sql.Connection;
import java.util.List;

public class DaoFactory {

    public static VendedorDao createVendedorDao(){
        return new VendedorDao() {
            @Override
            public void insert(Vendedor obj) {

            }

            @Override
            public void update(Vendedor obj) {

            }

            @Override
            public void deleteById(Integer id) {

            }

            @Override
            public Seller findById(Integer id) {
                return null;
            }

            @Override
            public List<Vendedor> findAll() {
                return null;
            }

            @Override
            public List<Vendedor> findByClientes(Clientes department) {
                return null;
            }
        }DaoJDBC(DB.getConnection());
    }

    private static void DaoJDBC(Connection connection) {
    }

    public static DepartmentDao createDepartmentDao(){
        return new DepartmentDaoJDBC(DB.getConnection());
    }

}
