// Módulos
var express = require('express');
var app = express();

var mongo = require('mongodb');
var swig = require('swig');
var crypto = require('crypto');
var expressSession = require('express-session');

//API
var jwt = require('jsonwebtoken');
app.set('jwt',jwt);


//JQUERY
app.use(function(req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Credentials", "true");
    res.header("Access-Control-Allow-Methods", "POST, GET, DELETE, UPDATE, PUT");
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, token");
    // Debemos especificar todas las headers que se aceptan. Content-Type , token
    next();
});


app.use(expressSession({
    secret: 'abcdefg',
    resave: true,
    saveUninitialized: true
}));

app.set('db','mongodb://sdi:sdi@sdicluster-shard-00-00-5ayqw.mongodb.net:27017,sdicluster-shard-00-01-5ayqw.mongodb.net:27017,sdicluster-shard-00-02-5ayqw.mongodb.net:27017/test?ssl=true&replicaSet=sdiCluster-shard-0&authSource=admin&retryWrites=true&w=majority');

var fileUpload = require('express-fileupload');
app.use(fileUpload());


var gestorBD = require("./modules/gestorBD.js");
gestorBD.init(app,mongo);
app.set('clave','abcdefg');
app.set('crypto',crypto);

var bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

// routerUsuarioSession
var routerUsuarioSession = express.Router();
routerUsuarioSession.use(function(req, res, next) {
    console.log("routerUsuarioSession");
    if ( req.session.usuario ) {
        // dejamos correr la petición
        next();
    } else {
        res.redirect("/login");
    }
});
//Aplicar routerUsuarioSession
app.use("/home",routerUsuarioSession);
app.use("/usr/",routerUsuarioSession);
app.use("/adm/",routerUsuarioSession);
app.use("/desconectarse",routerUsuarioSession);

var routerAdmin = express.Router();
routerAdmin.use(function(req, res, next) {
    console.log("routerAdmin");
    if ( req.session.usuario  && req.session.rol =="admin") {
        // dejamos correr la petición
        next();
    } else {
        req.session.usuario=null;
        res.redirect("/login");
    }
});

app.use("/adm/",routerAdmin);

//API ENRUTADOR
// routerUsuarioToken
var routerUsuarioToken = express.Router();
routerUsuarioToken.use(function(req, res, next) {
    // obtener el token, vía headers (opcionalmente GET y/o POST).
    var token = req.headers['token'] || req.body.token || req.query.token;
    if (token != null) {// verificar el token
        jwt.verify(token, 'secreto', function(err, infoToken) {
            if (err || (Date.now()/1000 - infoToken.tiempo) > 240 ){
                res.status(403); // Forbidden
                res.json({
                    acceso : false,
                    error: 'Token invalido o caducado'
                });
                // También podríamos comprobar que intoToken.usuario existe
                return;

            } else {
                // dejamos correr la petición
                res.usuario = infoToken.usuario;
                next();
            }
        });

    } else {
        res.status(403); // Forbidden
        res.json({
            acceso : false,
            mensaje: 'No hay Token'
        });
    }
});
// Aplicar routerUsuarioToken
app.use('/api/ofertas', routerUsuarioToken);
app.use('/api/borrarConver/', routerUsuarioToken);
app.use('/api/crearMensaje/', routerUsuarioToken);
app.use('/api/conversaciones/', routerUsuarioToken);
app.use('/api/marcarLeido/', routerUsuarioToken);
app.use('/api/conversUsuario/', routerUsuarioToken);
app.use('/api/mensajesConversacion/', routerUsuarioToken);


app.use(express.static('public'));

// Variables
app.set('port', 8081);

//Rutas/controladores por lógica
require("./routes/rusuarios.js")(app, swig, gestorBD);
require("./routes/rofertas.js")(app, swig, gestorBD);
require("./routes/rapiwallapop.js")(app, gestorBD);

//Página principal
app.get('/', function (req, res) {
    res.redirect('/login');
})
// lanzar el servidor
app.listen(app.get('port'), function () {
    console.log("Servidor activo");
})