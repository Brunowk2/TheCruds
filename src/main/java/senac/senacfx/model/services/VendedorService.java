package senac.senacfx.model.services;

import senac.senacfx.model.dao.DaoFactory;
import senac.senacfx.model.dao.SellerDao;
import senac.senacfx.model.dao.VendedorDao;
import senac.senacfx.model.entities.Seller;
import senac.senacfx.model.entities.Vendedor;

import java.util.List;

public class VendedorService {

    //dependencia injetada usando padrao factory
    private VendedorDao dao = DaoFactory.createVendedorDao();

    public List<Vendedor> findAll() {
        return dao.findAll();

        //Dados MOCK (fake) so para testar, sem puxar do banco por hora
//        List<Seller> list = new ArrayList<>();
//        list.add(new Seller(1,"Computadores"));
//        list.add(new Seller(2,"Alimentação"));
//        list.add(new Seller(3,"Financeiro"));
//        return list;

    }
    public void saveOrUpdate(Vendedor obj){
        if (obj.getId() == null){
            dao.insert(obj);
        } else {
            dao.update(obj);
        }
    }

    public void remove(Seller obj){
        dao.deleteById(obj.getId());
    }
}


