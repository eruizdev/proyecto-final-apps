package com.coworking.application.coworking_booking.business.service;

import com.coworking.application.coworking_booking.persistence.entity.UserEntity;

/**
 * Servicio de autenticación y registro de usuarios.
 * Define las operaciones base para login, registro y obtención de información de usuario.
 */
public interface AuthService {

    /**
     * Registra un nuevo usuario en el sistema.
     * @param email correo electrónico del usuario
     * @param pass contraseña sin cifrar (se procesará antes de guardar)
     * @param first primer nombre del usuario
     * @param last apellido del usuario
     * @return la entidad del usuario creado
     */
    UserEntity register(String email, String pass, String first, String last);

    /**
     * Obtiene la información de un usuario por su ID.
     * @param id identificador del usuario
     * @return entidad del usuario
     */
    UserEntity get(Long id);

    /**
     * Inicia sesión validando las credenciales del usuario y generando un JWT.
     * @param email correo electrónico del usuario
     * @param password contraseña ingresada
     * @return token JWT si las credenciales son válidas
     */
    String login(String email, String password);
}
