package senac.senacfx.model.services;

import senac.senacfx.model.dao.DaoFactory;

import senac.senacfx.model.dao.impl.ClientesDao;
import senac.senacfx.model.entities.Clientes;


import java.util.List;

public class ClientesService {

    //dependencia injetada usando padrao factory
    private ClientesDao dao = DaoFactory.createClientesDao();

    public List<Clientes> findAll() {
        return dao.findAll();

        //Dados MOCK (fake) so para testar, sem puxar do banco por hora
//        List<Clientes> list = new ArrayList<>();
//        list.add(new Clientes(1,"João"));
//        list.add(new Clientes(2,"Maria"));
//        list.add(new Clientes(3,"Venezuela"));
//        return list;

    }
    public void saveOrUpdate(Clientes obj){
        if (obj.getId() == null){
            dao.insert(obj);
        } else {
            dao.update(obj);
        }
    }

    public void remove(Clientes obj){
        dao.deleteById(obj.getId());
    }
}
