package farmacias.AppOchoa.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "usuario_nombre_usuario", nullable = false, length = 50)
    private String nombreUsuarioUsuario;

    @Column(name = "usuario_contrasena_hash" , nullable = false, length = 255)
    private String usuarioContrasenaHash;

    @Column(name = "usuario_nombre", nullable = false, length = 100)
    private String usuarioNombre;

    @Column(name = "usuario_apellido", nullable = false, length = 100)
    private String usuarioApellido;

    @Enumerated(EnumType.STRING)
    @Column(name = "usuario_rol", nullable = false, length = 20)
    private UsuarioRol usuarioRol;


    @Column(name = "usuario_estado" , nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    @Builder.Default
    private Boolean usuarioEstado = true;

    @Column(name = "auditoria_fecha_creacion", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime auditoriaFechaCreacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sucursal_id")
    private Sucursal sucursal;

}
