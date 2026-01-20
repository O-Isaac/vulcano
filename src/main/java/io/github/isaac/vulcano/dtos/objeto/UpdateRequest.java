package io.github.isaac.vulcano.dtos.objeto;

import java.io.Serializable;

public record UpdateRequest(String nombre,  String desc) implements Serializable {
}
