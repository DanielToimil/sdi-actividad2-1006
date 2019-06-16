package utilMongoDB;

import java.text.ParseException;
import java.util.Date;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class ControladorBaseDeDatosMongoDB {

	private MongoClient mongoClient;
	private MongoDatabase mongodb;

	public void connectDatabase() {
		try {
			setMongoClient(new MongoClient(new MongoClientURI(
					"mongodb://sdi:sdi@sdicluster-shard-00-00-5ayqw.mongodb.net:27017,sdicluster-shard-00-01-5ayqw.mongodb.net:27017,sdicluster-shard-00-02-5ayqw.mongodb.net:27017/test?ssl=true&replicaSet=sdiCluster-shard-0&authSource=admin&retryWrites=true&w=majority")));
			setMongodb(getMongoClient().getDatabase("test"));
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}

	public void insertarDatos() throws ParseException {
		try {
			MongoCollection<Document> col = getMongodb().getCollection("usuarios");
			col.insertOne(new Document().append("nombre", "Administrador").append("apellido", "administrador")
					.append("email", "admin@gmail.com")
					.append("password", "ebd5359e500475700c6cc3dd4af89cfd0569aa31724a1bf10ed1e3019dcfdb11")
					.append("rol", "admin").append("dinero", 1000));
			col.insertOne(new Document().append("nombre", "Usuario1").append("apellido", "Usuario1")
					.append("email", "usuario1@gmail.com")
					.append("password", "353f9f25a52fbbe951bc1176019b58d9a7dd04b3094bc0334115862118846098")
					.append("rol", "user").append("dinero", 100));
			col.insertOne(new Document().append("nombre", "Usuario2").append("apellido", "Usuario2")
					.append("email", "usuario2@gmail.com")
					.append("password", "353f9f25a52fbbe951bc1176019b58d9a7dd04b3094bc0334115862118846098")
					.append("rol", "user").append("dinero", 20));
			col.insertOne(new Document().append("nombre", "Usuario3").append("apellido", "Usuario3")
					.append("email", "usuario3@gmail.com")
					.append("password", "353f9f25a52fbbe951bc1176019b58d9a7dd04b3094bc0334115862118846098")
					.append("rol", "user").append("dinero", 100));
			col.insertOne(new Document().append("nombre", "Usuario4").append("apellido", "Usuario4")
					.append("email", "usuario4@gmail.com")
					.append("password", "353f9f25a52fbbe951bc1176019b58d9a7dd04b3094bc0334115862118846098")
					.append("rol", "user").append("dinero", 100));
			col.insertOne(new Document().append("nombre", "Usuario5").append("apellido", "Usuario5")
					.append("email", "usuario5@gmail.com")
					.append("password", "353f9f25a52fbbe951bc1176019b58d9a7dd04b3094bc0334115862118846098")
					.append("rol", "user").append("dinero", 19));

			col = getMongodb().getCollection("ofertas");
			/** OFERTAS USER 1**/
			col.insertOne(new Document().append("titulo", "Seat_Ibiza").append("descripcion",
					"Coche de segunda mano con 100k kilometros, gasolina, año 2014 color rojo pasión.")
					.append("precio", "6000").append("creador", "usuario1@gmail.com").append("comprador", "")
					.append("destacada", "false").append("fecha", new Date()));
			col.insertOne(new Document().append("titulo", "Ford_fiesta").append("descripcion",
					"Coche de segunda mano con 150k kilometros, diesel, año 2009 color blanco.")
					.append("precio", "6000").append("creador", "usuario1@gmail.com").append("comprador", "usuario2@gmail.com")
					.append("destacada", "true").append("fecha", new Date()));
			col.insertOne(new Document().append("titulo", "Renault_megane").append("descripcion",
					"Coche de segunda mano con 200k kilometros, diesel, año 2007 color azul.")
					.append("precio", "6000").append("creador", "usuario1@gmail.com").append("comprador", "usuario2@gmail.com")
					.append("destacada", "false").append("fecha", new Date()));
			
			
			/** OFERTAS USER 2**/
			col.insertOne(new Document().append("titulo", "Kit_de_supervivencia_de_montaña").append("descripcion",
					"Inmejorable kit para la supervivencia para situaciones extremas.")
					.append("precio", "80").append("creador", "usuario2@gmail.com").append("comprador", "")
					.append("destacada", "false").append("fecha", new Date()));
			col.insertOne(new Document().append("titulo", "Mochila_de_deporte").append("descripcion",
					"Mochila para pensada para todo tipo de deportes")
					.append("precio", "1000").append("creador", "usuario2@gmail.com").append("comprador", "usuario3@gmail.com")
					.append("destacada", "true").append("fecha", new Date()));
			col.insertOne(new Document().append("titulo", "Pistola").append("descripcion",
					"Pistola de policia")
					.append("precio", "12").append("creador", "usuario2@gmail.com").append("comprador", "usuario3@gmail.com")
					.append("destacada", "false").append("fecha", new Date()));
			
			
			/** OFERTAS USER 3**/
			col.insertOne(new Document().append("titulo", "Tekken_8").append("descripcion",
					"Juego ps4 precintado edición coleccionista.")
					.append("precio", "35").append("creador", "usuario3@gmail.com").append("comprador", "")
					.append("destacada", "false").append("fecha", new Date()));
			col.insertOne(new Document().append("titulo", "God_of_war_4").append("descripcion",
					"El mejor juego de sony del 2018.")
					.append("precio", "69").append("creador", "usuario3@gmail.com").append("comprador", "usuario4@gmail.com")
					.append("destacada", "true").append("fecha", new Date()));
			col.insertOne(new Document().append("titulo", "Fifa_20").append("descripcion",
					"Para que destruyas todos los mandos cuando juegues FutChampions")
					.append("precio", "45").append("creador", "usuario3@gmail.com").append("comprador", "usuario4@gmail.com")
					.append("destacada", "false").append("fecha", new Date()));
			
			
			/** OFERTAS USER 4**/
			col.insertOne(new Document().append("titulo", "Se_vende_cachorro_de_Husky").append("descripcion",
					"Tiene solamente un mes")
					.append("precio", "180").append("creador", "usuario4@gmail.com").append("comprador", "")
					.append("destacada", "false").append("fecha", new Date()));
			col.insertOne(new Document().append("titulo", "Labrador").append("descripcion",
					"Perfecto para cuidar de tu ganado")
					.append("precio", "200").append("creador", "usuario4@gmail.com").append("comprador", "usuario5@gmail.com")
					.append("destacada", "true").append("fecha", new Date()));
			col.insertOne(new Document().append("titulo", "Gato_egipcio").append("descripcion",
					"Para los que les encantan las mascotas y no quieren tener pelos por toda la casa")
					.append("precio", "100").append("creador", "usuario4@gmail.com").append("comprador", "usuario5@gmail.com")
					.append("destacada", "false").append("fecha", new Date()));
			
			
			/** OFERTAS USER 5**/
			col.insertOne(new Document().append("titulo", "El_triciclo_rojo").append("descripcion",
					"Mejor thriller francés en 2017")
					.append("precio", "100").append("creador", "usuario5@gmail.com").append("comprador", "")
					.append("destacada", "false").append("fecha", new Date()));
			col.insertOne(new Document().append("titulo", "Harry_Potter_versión_del_coleccionista").append("descripcion",
					"Incluye varita y capa de la invisibilidad")
					.append("precio", "120").append("creador", "usuario5@gmail.com").append("comprador", "usuario1@gmail.com")
					.append("destacada", "true").append("fecha", new Date()));
			col.insertOne(new Document().append("titulo", "Juego_de_tronos").append("descripcion",
					"Para que veas la lucha contra los caminantes blancos")
					.append("precio", "19").append("creador", "usuario5@gmail.com").append("comprador", "usuario1@gmail.com")
					.append("destacada", "false").append("fecha", new Date()));
			
			
			/**Conversacion  **/
			
			col = getMongodb().getCollection("conversaciones");
			
			col.insertOne(new Document().append("vendedor", "usuario5@gmail.com").append("posibleComprador",
					"usuario1@gmail.com").append("titulo", "Juego_de_tronos"));
			col.insertOne(new Document().append("vendedor", "usuario5@gmail.com").append("posibleComprador",
					"usuario1@gmail.com").append("titulo", "Tekken_8"));
			
			
		
		} catch (Exception ex) {
			System.out.print(ex.toString());
		}

	}

	public void borrarDatos() {
		getMongodb().getCollection("ofertas").drop();
		getMongodb().getCollection("usuarios").drop();
		getMongodb().getCollection("mensajes").drop();
		getMongodb().getCollection("conversaciones").drop();

	}

	public void preparacionBase() throws ParseException {
		ControladorBaseDeDatosMongoDB javaMongodbInsertData = new ControladorBaseDeDatosMongoDB();
		System.out.println("Conectando a la base");
		javaMongodbInsertData.connectDatabase();
		System.out.println("Eliminando la base");
		javaMongodbInsertData.borrarDatos();
		System.out.println("Insertando en la base");
		javaMongodbInsertData.insertarDatos();
	}

	public MongoDatabase getMongodb() {
		return mongodb;
	}

	public void setMongodb(MongoDatabase mongodb) {
		this.mongodb = mongodb;
	}

	public MongoClient getMongoClient() {
		return mongoClient;
	}

	public void setMongoClient(MongoClient mongoClient) {
		this.mongoClient = mongoClient;
	}
}
