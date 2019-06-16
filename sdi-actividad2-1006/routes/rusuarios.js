module.exports = function (app, swig, gestorBD) {

    app.get("/login", function (req, res) {
        var respuesta = swig.renderFile('views/login.html', {});
        res.send(respuesta);
    });

    app.post("/identificarse", function (req, res) {
        var seguro = app.get("crypto").createHmac('sha256', app.get('clave'))
            .update(req.body.password).digest('hex');
        var criterio = {
            email: req.body.email,
            password: seguro
        }
        gestorBD.obtUsuarios(criterio, function (usuarios) {
            if (usuarios == null || usuarios.length == 0) {
                req.session.usuario = null;
                res.redirect("/login" +
                    "?mensaje=Email o Contraseña incorrecta" +
                    "&tipoMensaje=alert-danger ");
            } else {
                req.session.usuario = usuarios[0].email;
                req.session.rol = usuarios[0].rol;
                req.session.dinero = usuarios[0].dinero;
                res.redirect("/home");
            }
        });
    });

    app.get("/registrarse", function (req, res) {
        var respuesta = swig.renderFile('views/registro.html', {});
        res.send(respuesta);
    });

    app.post('/registrarUsuario', function (req, res) {
        if (req.body.password == req.body.password2) {
            var criterio = {
                email: req.body.email
            }
            gestorBD.obtUsuarios(criterio, function (usuarios) {
                if (usuarios == null || usuarios.length == 0) {
                    var seguro = app.get("crypto").createHmac('sha256', app.get('clave'))
                        .update(req.body.password).digest('hex');
                    var usuario = {
                        email: req.body.email,
                        nombre: req.body.name,
                        apellidos: req.body.lastname,
                        password: seguro,
                        dinero: 100,
                        rol: "user"
                    }
                    gestorBD.insertarUsuario(usuario, function (id) {
                        if (id == null) {
                            res.redirect("/registrarse" +
                                "?mensaje=Algo ha fallado." +
                                "&tipoMensaje=alert-danger ");
                        } else {
                            req.session.usuario = req.body.email;
                            req.session.rol = usuario.rol;
                            req.session.dinero = usuario.dinero;
                            res.redirect("/home");
                        }
                    });
                } else {
                    req.session.usuario = null;
                    res.redirect("/registrarse" +
                        "?mensaje=Email ya existente" +
                        "&tipoMensaje=alert-danger ");
                }
            });
        } else {
            res.redirect("/registrarse" +
                "?mensaje=Las contraseñas deben coincidir" +
                "&tipoMensaje=alert-danger ");
        }
    })


    app.get('/desconectarse', function (req, res) {
        req.session.usuario = null;
        res.redirect("/login" +
            "?mensaje=Usuario desconectado" +
            "&tipoMensaje=alert-success");
    })


    //Gestionar usuarios

    app.get("/adm/users", function (req, res) {
        var criterio = {
            rol: "user"
        };
        gestorBD.obtUsuarios(criterio, function (usuarios) {
            if (usuarios == null) {
                res.send("Error al listar ");
            } else {
                var respuesta = swig.renderFile('views/listUsers.html', {
                    usuario: req.session.usuario,
                    dinero: req.session.dinero,
                    rol: req.session.rol,
                    usuarios: usuarios
                });
                res.send(respuesta);
            }
        });
    });


    app.post('/adm/eliminarUsuariosSeleccionados/', function (req, res) {
        let ids = req.body.idsSelectedUsers;
        if (!Array.isArray(ids)) {
            let aux = ids;
            ids = [];
            ids.push(aux);
        }
        let criterio = {
            email: {$in: ids}
        };
        gestorBD.deleteUsuario(criterio, function (usuarios) {
            if (usuarios == null) {
                console.log("Error al eliminar usuarios");
            } else {
                let criterio = {
                    creador: {$in: ids}
                };
                gestorBD.deleteOferta(criterio, function (ofertas) {
                    if (ofertas == null) {
                        console.log("Error al eliminar ofertas creadas por los usuarios borrados");
                    } else {
                        res.redirect("/adm/users" + "?mensaje=Usuarios borrados con exito" +
                            "&tipoMensaje=alert-success ");
                    }
                });
            }
        });
    })
};