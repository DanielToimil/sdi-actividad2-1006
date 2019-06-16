module.exports = function (app, gestorBD) {

    app.post("/api/login/", function (req, res) {
        var seguro = app.get("crypto").createHmac('sha256', app.get('clave'))
            .update(req.body.password).digest('hex');
        var criterio = {
            email: req.body.email,
            password: seguro
        }

        gestorBD.obtUsuarios(criterio, function (usuarios) {
            if (usuarios == null || usuarios.length == 0) {
                res.status(401); // Unauthorized
                res.json({
                    autenticado: false
                })
            } else {
                var token = app.get('jwt').sign(
                    {usuario: criterio.email, tiempo: Date.now() / 1000},
                    "secreto");
                req.session.usuario = req.body.email;
                res.status(200);
                res.json({
                    autenticado: true,
                    token: token
                })
            }

        });
    });


    //Obtener las ofertas para el usuario logeado

    app.get("/api/ofertas", function (req, res) {
        var criterio = {
            "creador": {
                $ne: req.headers.email
            }
        }

        gestorBD.obtOfertas(criterio, function (ofertas) {
            if (ofertas == null) {
                res.status(500);
                res.json({
                    error: "Error al obtener las ofertas"
                })
            } else {
                res.status(200);
                res.send(JSON.stringify(ofertas));
            }
        });
    });


//Escribir mensaje en un oferta

    app.post("/api/crearMensaje", function (req, res) {
        var mensaje = {
            fecha: new Date(),
            mensaje: req.body.mensaje,
            conversacion: req.body.conversacion,
            autorMensaje: req.headers.email,
            titulo : req.body.titulo,
            leido: "false"
        }

        gestorBD.insertMensaje(mensaje, function (id) {
            if (id == null) {
                res.status(500);
                res.json({
                    error: "Error al escribir el mensaje"
                })
            } else {

                res.status(201);
                res.json({
                    mensaje: "Mensaje escrito correctamente",
                    objecto: mensaje,
                    _id: id
                })
            }
        });

    });

    //Obtener mensajes de las conversaciones

    app.get("/api/conversaciones/:id", function (req, res) {
        var owner = "";
        if (req.headers.vendedor == req.headers.email) {
            owner = "true";
        } else {
            owner = "false";
        }
        var criterio = {};

        if (owner == "true") {
            criterio = {
                oferta: req.params.id,
                vendedor: req.headers.email,
                titulo: req.headers.titulo
            }
        } else {
            criterio = {
                oferta: req.params.id,
                posibleComprador: req.headers.email,
                titulo: req.headers.titulo
            }
        }
        gestorBD.obtConversaciones(criterio, function (conversaciones) {
            if (conversaciones == null) {
                res.status(500);
                res.json({
                    error: "Error al obtener las conversaciones"
                })
            } else {
                if (owner == "true") {
                    res.status(200);
                    res.send(JSON.stringify(conversaciones));
                } else {
                    var idCon = "";
                    if (conversaciones[0] != null || conversaciones[0] != undefined) {
                        criterio = {
                            conversacion: conversaciones[0]._id.toString()
                        }
                        idCon = conversaciones[0]._id.toString();

                    } else {

                        var conversacion = {
                            oferta: req.params.id,
                            vendedor: req.headers.vendedor,
                            posibleComprador: req.headers.email,
                            titulo: req.headers.titulo
                        }
                        gestorBD.insertConversacion(conversacion, function (id) {
                            if (id == null) {

                                res.status(500);
                                res.json({
                                    error: "Se ha producido un error creando la conversacion"
                                })
                            } else {
                                idCon = id.toString();
                                criterio = {
                                    conversacion: id
                                }
                            }
                        });
                    }
                    gestorBD.obtMensajes(criterio, function (mensajes) {
                        if (mensajes == null) {
                            res.status(500);
                            res.json({
                                error: "Error al obtener los mensajes"
                            })
                        } else {
                            res.status(200);
                            var resp = {idCon: idCon.toString(), mens: mensajes};

                            res.send(JSON.stringify(resp));

                        }
                    });
                }
            }
        });
    });

    //Marcar mensaje como leido

    app.put("/api/marcarLeido/:id", function (req, res) {
        var criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)};
        var mensaje = {"leido": "true"};
        gestorBD.modMensaje(criterio, mensaje, function (result) {
            if (result == null) {
                res.status(500);
                res.json({
                    error: "Error al modificar el mensaje"
                })
            } else {
                res.status(200);
                res.json({
                    mensaje: "Mensaje modificado",
                    _id: req.params.id
                })
            }
        });
    });

    //Borrar la conversacion
    app.delete("/api/borrarConver/:id", function (req, res) {
        var criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)};
        var criterio2 = {"conversacion": req.params.id};

        gestorBD.deleteConversacion(criterio, function (conversaciones) {
            if (conversaciones == null) {
                res.status(500);
                res.json({
                    error: "Error al borrar la conversacion, la conversacion es null"
                })
            } else {
                gestorBD.deleteMensajes(criterio2, function (mensajes) {
                    if (mensajes == null) {
                        res.status(500);
                        res.json({
                            error: "Error al borrar la conversacion, el mensaje es null"
                        })
                    } else {
                        res.status(200);
                        var resp = {convers: conversaciones, mens: mensajes};
                        res.send(JSON.stringify(resp));
                    }
                });
            }
        });
    });


    //Obtener las conversaciones del usuario
    app.get("/api/conversUsuario", function (req, res) {
        criterio = {
            $or: [{vendedor: req.headers.email}, {posibleComprador: req.headers.email}]
        }

        gestorBD.obtConversaciones(criterio, function (conversaciones) {
            if (conversaciones == null) {
                res.status(500);
                res.json({
                    error: "Error al obtener las conversaciones"
                })
            } else {
                res.status(200);
                res.send(JSON.stringify(conversaciones));
            }
        });
    });

//Obtener mensajes de una conversacion
    app.get("/api/mensajesConversacion/:id", function (req, res) {
       var criterio = {
            conversacion: req.params.id
        }

        gestorBD.obtMensajes(criterio, function (mensajes) {
            if (mensajes == null) {
                res.status(500);
                res.json({
                    error: "Error al obtener los mensajes"
                })
            } else {
                res.status(200);
                var resp = {idCon: req.params.id, mens: mensajes};

                res.send(JSON.stringify(resp));
            }
        });
    });
}