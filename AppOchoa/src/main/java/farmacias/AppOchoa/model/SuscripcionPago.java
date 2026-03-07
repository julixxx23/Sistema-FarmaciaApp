package farmacias.AppOchoa.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "suscripcion_pagos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SuscripcionPago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pagoId;

}
