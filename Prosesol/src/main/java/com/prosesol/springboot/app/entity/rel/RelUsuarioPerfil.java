package com.prosesol.springboot.app.entity.rel;

import com.prosesol.springboot.app.entity.Perfil;
import com.prosesol.springboot.app.entity.Usuario;
import com.prosesol.springboot.app.entity.composite.id.RelUsuarioPerfilId;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "rel_usuarios_perfiles")
@IdClass(RelUsuarioPerfilId.class)
public class RelUsuarioPerfil implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_perfil")
    private Perfil perfil;

    public RelUsuarioPerfil() {
    }

    public RelUsuarioPerfil(Usuario usuario, Perfil perfil) {
        this.usuario = usuario;
        this.perfil = perfil;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
}
