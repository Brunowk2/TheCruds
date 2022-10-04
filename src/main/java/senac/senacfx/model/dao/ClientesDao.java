package senac.senacfx.model.dao;

import senac.senacfx.model.entities.Clientes;

import java.util.List;
public interface ClientesDao {

    void insert(Clientes obj);
    void update(Clientes obj);
    void deleteById(Integer id);
    Clientes findById(Integer id);
    List<Clientes> findAll();
}
