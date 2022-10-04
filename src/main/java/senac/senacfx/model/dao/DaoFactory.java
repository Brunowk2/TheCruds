package senac.senacfx.model.dao;

import senac.senacfx.db.DB;
import senac.senacfx.model.dao.impl.ClientesDaoJDBC;
import senac.senacfx.model.dao.impl.VendedorDaoJDBC;
import senac.senacfx.model.dao.impl.SellerDaoJDBC;

public class DaoFactory {
    public static ClientesDao createClientesDao() {
        return new ClientesDaoJDBC(DB.getConnection());
    }

    public static VendedorDao createVendedorDao() { return new VendedorDaoJDBC(DB.getConnection());
    }
}

//    public static SellerDao createSellerDao(){
//        return new SellerDaoJDBC(DB.getConnection());
//    }

//    public static DepartmentDao createDepartmentDao(){
//        return new DepartmentDaoJDBC(DB.getConnection());
//    }

    }
