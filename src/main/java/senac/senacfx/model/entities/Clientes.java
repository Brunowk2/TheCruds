package senac.senacfx.model.entities;

import java.io.Serializable;

public class Clientes implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;

    private String name;

    private String cpf;

    private Integer idVendedor;

//    private Integer id;
//        private String name;
//        private String cpf;
//        private String idVendedor;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        Clientes other = (Clientes) o;
        if (id == null){
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Clientes{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
