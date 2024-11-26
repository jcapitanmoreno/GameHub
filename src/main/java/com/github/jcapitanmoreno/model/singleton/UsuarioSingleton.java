package com.github.jcapitanmoreno.model.singleton;

import com.github.jcapitanmoreno.model.entity.Usuarios;

public class UsuarioSingleton {
    private static UsuarioSingleton _instance;
    private static Usuarios playerLoged;

    public static UsuarioSingleton get_Instance() {
        if (_instance == null) {
            _instance = new UsuarioSingleton();
        }
        return _instance;
    }

    public void login(Usuarios usuarios) {
        playerLoged = usuarios;
    }

    public Usuarios getPlayerLoged() {
        return playerLoged;
    }
}
