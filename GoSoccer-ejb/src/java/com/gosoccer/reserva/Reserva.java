/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reserva;

import Cancha.Cancha;
import Usuario.Usuario;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author vgalarza
 */
@Entity
public class Reserva implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //@Column(name = "cancha")
   // @ManyToOne
    private Cancha cancha;
   
    //@Temporal(TemporalType.DATE)
    //@Column(name = "fecha")
    private Date fecha;
    
    //@Column(name = "hora")
    private int hora;
    
    //@Column(name = "usarioCreador")
    private Usuario usarioCreador;
    
    private List<Usuario> usarios;
    
    public Cancha getCancha() {
        return cancha;
    }

    public void setCancha(Cancha cancha) {
        this.cancha = cancha;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public Usuario getUsarioCreador() {
        return usarioCreador;
    }

    public void setUsarioCreador(Usuario usarioCreador) {
        this.usarioCreador = usarioCreador;
    }

    public List<Usuario> getUsarios() {
        return usarios;
    }

    public void setUsarios(List<Usuario> usarios) {
        this.usarios = usarios;
    }
   
    public Long getId() {
        return id;
    }
    public Reserva() {}
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reserva)) {
            return false;
        }
        Reserva other = (Reserva) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Reserva.Reserva[ id=" + id + " ]";
    }
    
}
