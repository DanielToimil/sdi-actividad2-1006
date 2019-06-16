package tests;

import static org.junit.Assert.assertTrue;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import tests.pageobjects.PO_HomeView;
import tests.pageobjects.PO_LoginView;
import tests.pageobjects.PO_OfferView;
import tests.pageobjects.PO_PrivateView;
import tests.pageobjects.PO_RegisterView;
import tests.pageobjects.PO_View;
import tests.util.SeleniumUtils;
import utilMongoDB.ControladorBaseDeDatosMongoDB;

public class TestsSDI_actividad2_1006 {

	static String PathFirefox64 = "C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe";
	static String Geckdriver022 = "F:\\Universidad 2018-2019\\SDI\\SDIPractica\\geckodriver024win64.exe";
	static WebDriver driver = getDriver(PathFirefox64, Geckdriver022);
	static String APP_URL = "http://localhost:8081";
	static String URL = "";

	public static WebDriver getDriver(String PathFirefox, String Geckdriver) {
		System.setProperty("webdriver.firefox.bin", PathFirefox);
		System.setProperty("webdriver.gecko.driver", Geckdriver);
		WebDriver driver = new FirefoxDriver();
		return driver;
	}

	// Antes de cada prueba se navega al URL home de la aplicaciónn
	@Before
	public void setUp() throws Exception {
		//ControladorBaseDeDatosMongoDB cMongo = new ControladorBaseDeDatosMongoDB();
		//cMongo.preparacionBase();
		driver.navigate().to(URL);

	}

	// Antes de la primera prueba
	@BeforeClass
	static public void begin() {
		URL = APP_URL;
	}

	// Despu�s de cada prueba se borran las cookies del navegador
	@After
	public void tearDown() {
		//driver.manage().deleteAllCookies();
	}

	// Al finalizar la última prueba
	@AfterClass
	static public void end() {
		// Cerramos el navegador al finalizar las pruebas
		driver.quit();
	}

	/** TESTS APLICACION WEB 
	 * @throws Exception **/
	// [Prueba1] Registro de Usuario con datos válidos.
	@Test
	public void P01() throws Exception {
		ControladorBaseDeDatosMongoDB cMongo = new ControladorBaseDeDatosMongoDB();
		cMongo.preparacionBase();
		// Vamos al formulario de registro
		driver.navigate().to(URL + "/registrarse");
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "prueba@gmail.com", "Prueba", "Prueba", "123456", "123456");
		PO_View.checkElement(driver, "text", "Ofertas disponibles");
	}

	// [Prueba2] Registro de Usuario con datos inválidos (email vacío, repetición de
	// contraseña inválida).
	@Test
	public void P02() {
		// Vamos al formulario de registro
		driver.navigate().to(URL + "/registrarse");
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "", "", "", "123456", "12345612");
		PO_View.checkElement(driver, "text", "Registrate");
	}

	// [Prueba3] Registro de Usuario con email existente.
	@Test
	public void P03() {
		// Vamos al formulario de registro
		driver.navigate().to(URL + "/registrarse");
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "usuario1@gmail.com", "prueba", "prueba", "123456", "123456");
		PO_View.checkElement(driver, "text", "Email ya existente");
	}

	// [Prueba4] Inicio de sesión con datos válidos.
	@Test
	public void P04() {
		// Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "usuario1@gmail.com", "1234");
		// COmprobamos que entramos en la pagina privada del usuario
		PO_View.checkElement(driver, "text", "Ofertas disponibles");
	}

	// [Prueba5] Inicio de sesión con datos inválidos (email existente, pero
	// contraseña incorrecta).
	@Test
	public void P05() {
		// Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "usuario1@gmail.com", "admin");
		// COmprobamos que entramos en la pagina privada del usuario
		PO_View.checkElement(driver, "text", "Email o Contrase�a incorrecta");
	}

	// [Prueba6] Inicio de sesión con datos inválidos (campo email o contraseña
	// vacíos).
	@Test
	public void P06() {
		// Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "", "");
		// COmprobamos que entramos en la pagina privada del usuario
		PO_View.checkElement(driver, "text", "Identificaci�n de usuario");
	}

	// [Prueba7] Inicio de sesión con datos inválidos (email no existente en la
	// aplicación).
	@Test
	public void P07() {
		// Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "noexiste@noexiste.com", "1234");
		// COmprobamos que entramos en la pagina privada del usuario
		PO_View.checkElement(driver, "text", "Email o Contrase�a incorrecta");
	}

	// [Prueba8] Hacer click en la opción de salir de sesión y comprobar que se
	// redirige a la página de inicio de sesión (Login).
	@Test
	public void P08() {
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "usuario1@gmail.com", "1234");
		// COmprobamos que entramos en la pagina privada del usuario
		PO_View.checkElement(driver, "text", "Ofertas disponibles");
		// Ahora nos desconectamos
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");
	}

	// [Prueba9] Comprobar que el botón cerrar sesión no está visible si el usuario
	// no está autenticado.
	@Test
	public void P09() {
		ExpectedConditions.invisibilityOfElementLocated(By.partialLinkText("Desconectarse"));
	}

	// [Prueba10] Mostrar el listado de usuarios y comprobar que se muestran todos
	// los que existen en el sistema.

	@Test
	public void P10() {
		PO_LoginView.fillForm(driver, "admin@gmail.com", "admin");
		PO_View.checkElement(driver, "text", "Ofertas disponibles");
		driver.navigate().to(URL + "/adm/users");
		// Comprobar los 5 usuarios
		PO_View.checkElement(driver, "text", "usuario1@gmail.com");
		PO_View.checkElement(driver, "text", "usuario2@gmail.com");
		PO_View.checkElement(driver, "text", "usuario3@gmail.com");
		PO_View.checkElement(driver, "text", "usuario4@gmail.com");
		PO_View.checkElement(driver, "text", "usuario5@gmail.com");
		// Ahora nos desconectamos
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");
	}
	// [Prueba11] Ir a la lista de usuarios, borrar el primer usuario de la lista,
	// comprobar que la lista se actualiza y dicho usuario desaparece.

	@Test
	public void P11() {
		PO_LoginView.fillForm(driver, "admin@gmail.com", "admin");
		PO_View.checkElement(driver, "text", "Ofertas disponibles");
		driver.navigate().to(URL + "/adm/users");
		// Comprobar los 5 usuarios
		PO_View.checkElement(driver, "text", "usuario1@gmail.com");
		PO_View.checkElement(driver, "text", "usuario2@gmail.com");
		PO_View.checkElement(driver, "text", "usuario3@gmail.com");
		PO_View.checkElement(driver, "text", "usuario4@gmail.com");
		PO_View.checkElement(driver, "text", "usuario5@gmail.com");
		// Marcar el primero y borrarlo
		driver.findElement(By.xpath(
				"(.//*[normalize-space(text()) and normalize-space(.)='usuario1@gmail.com'])[1]/following::input[1]"))
				.click();
		By boton = By.className("btn");
		driver.findElement(boton).click();
		// Comprobar que no esta
		ExpectedConditions.invisibilityOfElementLocated(By.partialLinkText("usuario1"));
		// Ahora nos desconectamos
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");
	}
	// [Prueba12] Ir a la lista de usuarios, borrar el último usuario de la lista,
	// comprobar que la lista se actualiza y dicho usuario desaparece.

	@Test
	public void P12() throws Exception {
		ControladorBaseDeDatosMongoDB cMongo = new ControladorBaseDeDatosMongoDB();
		cMongo.preparacionBase();
		PO_LoginView.fillForm(driver, "admin@gmail.com", "admin");
		PO_View.checkElement(driver, "text", "Ofertas disponibles");
		driver.navigate().to(URL + "/adm/users");
		// Comprobar los 5 usuarios
		PO_View.checkElement(driver, "text", "usuario1@gmail.com");
		PO_View.checkElement(driver, "text", "usuario2@gmail.com");
		PO_View.checkElement(driver, "text", "usuario3@gmail.com");
		PO_View.checkElement(driver, "text", "usuario4@gmail.com");
		PO_View.checkElement(driver, "text", "usuario5@gmail.com");
		// Marcar el primero y borrarlo
		driver.findElement(By
				.xpath("(.//*[normalize-space(text()) and normalize-space(.)='usuario5@gmail.com'])[1]/following::input[1]"))
				.click();
		By boton = By.className("btn");
		driver.findElement(boton).click();
		// Comprobar que no esta
		ExpectedConditions.invisibilityOfElementLocated(By.partialLinkText("usuario5"));
		// Ahora nos desconectamos
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");
	}

	// [Prueba13] Ir a la lista de usuarios, borrar 3 usuarios, comprobar que la
	// lista se actualiza y dichos usuarios desaparecen.
	@Test
	public void P13() throws Exception {
		ControladorBaseDeDatosMongoDB cMongo = new ControladorBaseDeDatosMongoDB();
		cMongo.preparacionBase();
		PO_LoginView.fillForm(driver, "admin@gmail.com", "admin");
		PO_View.checkElement(driver, "text", "Ofertas disponibles");
		driver.navigate().to(URL + "/adm/users");
		// Comprobar los 5 usuarios
		PO_View.checkElement(driver, "text", "usuario1@gmail.com");
		PO_View.checkElement(driver, "text", "usuario2@gmail.com");
		PO_View.checkElement(driver, "text", "usuario3@gmail.com");
		PO_View.checkElement(driver, "text", "usuario4@gmail.com");
		PO_View.checkElement(driver, "text", "usuario5@gmail.com");
		// Marcar el primero y borrarlo
		driver.findElement(By.xpath(
				"(.//*[normalize-space(text()) and normalize-space(.)='usuario3@gmail.com'])[1]/following::input[1]"))
				.click();
		driver.findElement(By.xpath(
				"(.//*[normalize-space(text()) and normalize-space(.)='usuario2@gmail.com'])[1]/following::input[1]"))
				.click();
		driver.findElement(By
				.xpath("(.//*[normalize-space(text()) and normalize-space(.)='usuario4@gmail.com'])[1]/following::input[1]"))
				.click();
		By boton = By.className("btn");
		driver.findElement(boton).click();
		// Comprobar que no esta
		ExpectedConditions.invisibilityOfElementLocated(By.partialLinkText("usuario3"));
		ExpectedConditions.invisibilityOfElementLocated(By.partialLinkText("usuario4"));
		ExpectedConditions.invisibilityOfElementLocated(By.partialLinkText("usuario2"));
		// Ahora nos desconectamos
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");
	}

	// [Prueba14] Ir al formulario de alta de oferta, rellenarla con datos válidos y
	// pulsar el botón Submit. Comprobar que la oferta sale en el listado de ofertas
	// de dicho usuario.
	@Test
	public void P14() throws Exception {
		ControladorBaseDeDatosMongoDB cMongo = new ControladorBaseDeDatosMongoDB();
		cMongo.preparacionBase();
		// Vamos al formulario de registro
		PO_LoginView.fillForm(driver, "usuario1@gmail.com", "1234");
		PO_View.checkElement(driver, "text", "Ofertas");
		driver.navigate().to(URL + "/usr/agregarOferta");
		// Rellenamos el formulario.
		PO_OfferView.fillForm(driver, "Prueba de titulo", "Esto es una descripcion", "12.0", "false");
		PO_View.checkElement(driver, "text", "Oferta insertada con exito.");
		// Ahora nos desconectamos
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");
	}

	// [Prueba15] Ir al formulario de alta de oferta, rellenarla con datos inválidos
	// (campo título vacío) y pulsar el botón Submit. Comprobar que se muestra el
	// mensaje de campo obligatorio.
	@Test
	public void P15() {
		// Vamos al formulario de registro
		PO_LoginView.fillForm(driver, "usuario1@gmail.com", "1234");
		PO_View.checkElement(driver, "text", "Ofertas");
		driver.navigate().to(URL + "/usr/agregarOferta");
		// Rellenamos el formulario.
		PO_OfferView.fillForm(driver, "", "Esto es una descripcion", "12.0", "false");
		PO_View.checkElement(driver, "text", "�Destacar oferta?");
		// Ahora nos desconectamos
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");
	}

	// [Prueba16] Mostrar el listado de ofertas para dicho usuario y comprobar que
	// se muestran todas los que existen para este usuario.
	@Test
	public void P16() {
		PO_LoginView.fillForm(driver, "usuario1@gmail.com", "1234");
		PO_View.checkElement(driver, "text", "Ofertas disponibles");
		driver.navigate().to(URL + "/usr/listarCreadas");
		// Comprobar los 5 usuarios
		PO_View.checkElement(driver, "text", "Seat_Ibiza");
		PO_View.checkElement(driver, "text", "Ford_fiesta");
		PO_View.checkElement(driver, "text", "Renault_megane");
		// Ahora nos desconectamos
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");
	}

	// [Prueba17] Ir a la lista de ofertas, borrar la primera oferta de la lista,
	// comprobar que la lista se actualiza y que la oferta desaparece.
	@Test
	public void P17() {
		PO_LoginView.fillForm(driver, "usuario1@gmail.com", "1234");
		PO_View.checkElement(driver, "text", "Ofertas disponibles");
		driver.navigate().to(URL + "/usr/listarCreadas");
		// Comprobar los 5 usuarios
		PO_View.checkElement(driver, "text", "Seat_Ibiza");
		PO_View.checkElement(driver, "text", "Ford_fiesta");
		PO_View.checkElement(driver, "text", "Renault_megane");

		driver.findElement(
				By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Seat_Ibiza'])[1]/following::td[2]"))
				.click();
		driver.findElement(By.linkText("Eliminar")).click();
		ExpectedConditions.invisibilityOfElementLocated(By.partialLinkText("Seat_Ibiza"));
		PO_View.checkElement(driver, "text", "Oferta eliminada");
		// Ahora nos desconectamos
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");
	}

	// [Prueba18] Ir a la lista de ofertas, borrar la última oferta de la lista,
	// comprobar que la lista se actualiza y que la oferta desaparece
	@Test
	public void P18() throws Exception {
		ControladorBaseDeDatosMongoDB cMongo = new ControladorBaseDeDatosMongoDB();
		cMongo.preparacionBase();
		PO_LoginView.fillForm(driver, "usuario1@gmail.com", "1234");
		PO_View.checkElement(driver, "text", "Ofertas disponibles");
		driver.navigate().to(URL + "/usr/listarCreadas");
		// Comprobar los 5 usuarios
		PO_View.checkElement(driver, "text", "Seat_Ibiza");
		PO_View.checkElement(driver, "text", "Ford_fiesta");
		PO_View.checkElement(driver, "text", "Renault_megane");

		driver.findElement(
				By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Renault_megane'])[1]/following::td[2]"))
				.click();
		driver.findElement(By.linkText("Eliminar")).click();
		ExpectedConditions.invisibilityOfElementLocated(By.partialLinkText("Renault_megane"));
		PO_View.checkElement(driver, "text", "Oferta eliminada");
		// Ahora nos desconectamos
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");
	}

	// [Prueba19] Hacer una búsqueda con el campo vacío y probar que se muestra la
	// página que corresponde con el listado de las ofertas existentes en el sistema
	@Test
	public void P19() throws Exception {
		ControladorBaseDeDatosMongoDB cMongo = new ControladorBaseDeDatosMongoDB();
		cMongo.preparacionBase();
		PO_LoginView.fillForm(driver, "usuario1@gmail.com", "1234");
		PO_View.checkElement(driver, "text", "Ofertas");
		PO_View.checkElement(driver, "text", "Kit_de_supervivencia_de_monta�a");
		PO_View.checkElement(driver, "text", "Pistola");
		PO_View.checkElement(driver, "text", "Tekken_8");
		PO_View.checkElement(driver, "text", "Fifa_20");
		PO_View.checkElement(driver, "text", "Se_vende_cachorro_de_Husky");
		PO_PrivateView.search(driver, "");
		PO_View.checkElement(driver, "text", "Ofertas");
		PO_View.checkElement(driver, "text", "Kit_de_supervivencia_de_monta�a");
		PO_View.checkElement(driver, "text", "Pistola");
		PO_View.checkElement(driver, "text", "Tekken_8");
		PO_View.checkElement(driver, "text", "Fifa_20");
		PO_View.checkElement(driver, "text", "Se_vende_cachorro_de_Husky");

		// Ahora nos desconectamos
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");

	}

	// [Prueba20] Hacer una búsqueda escribiendo en el campo texto que no exista y
	// comprobar que se muestra la página que corresponde, con la lista de ofertas
	// vacía
	@Test
	public void P20() {
		PO_LoginView.fillForm(driver, "usuario1@gmail.com", "1234");
		PO_View.checkElement(driver, "text", "Ofertas");
		PO_View.checkElement(driver, "text", "Kit_de_supervivencia_de_monta�a");
		PO_View.checkElement(driver, "text", "Pistola");
		PO_View.checkElement(driver, "text", "Tekken_8");
		PO_View.checkElement(driver, "text", "Fifa_20");
		PO_View.checkElement(driver, "text", "Se_vende_cachorro_de_Husky");
		PO_PrivateView.search(driver, "LOLOLOLOLO");
		ExpectedConditions.invisibilityOfElementLocated(By.partialLinkText("LOLOLOLO"));
		ExpectedConditions.invisibilityOfElementLocated(By.partialLinkText("Fifa_20"));
		ExpectedConditions.invisibilityOfElementLocated(By.partialLinkText("Kit"));

		// Ahora nos desconectamos
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");

	}

	// [Prueba21] Hacer búsqueda escribiendo en el campo texto en minúscula o
	// mayúscula y comprobar que se muestra la página que corresponde, con la lista
	// de ofertas que contengan dicho texto, independientemente que
	// el título esté almacenado en minúsculas o mayúscula
	@Test
	public void P21() {
		PO_LoginView.fillForm(driver, "usuario1@gmail.com", "1234");
		PO_View.checkElement(driver, "text", "Ofertas");
		PO_View.checkElement(driver, "text", "Kit_de_supervivencia_de_monta�a");
		PO_View.checkElement(driver, "text", "Pistola");
		PO_View.checkElement(driver, "text", "Tekken_8");
		PO_View.checkElement(driver, "text", "Fifa_20");
		PO_View.checkElement(driver, "text", "Se_vende_cachorro_de_Husky");
		PO_PrivateView.search(driver, "FiFa_20");
		PO_View.checkElement(driver, "text", "Fifa_20");
		ExpectedConditions.invisibilityOfElementLocated(By.partialLinkText("Kit"));

		// Ahora nos desconectamos
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");

	}

	// [Prueba22] Sobre búsqueda determinada (a elección desarrollador), comprar una
	// oferta que deja un saldo positivo en el contador del comprobador. Y comprobar
	// que el contador se actualiza correctamente en la vista del comprador
	@Test
	public void P22() {

		PO_LoginView.fillForm(driver, "usuario1@gmail.com", "1234");
		PO_View.checkElement(driver, "text", "Ofertas");

		PO_PrivateView.search(driver, "Kit");

		driver.findElement(By.xpath(
				"(.//*[normalize-space(text()) and normalize-space(.)='Kit_de_supervivencia_de_monta�a'])[1]/following::td[2]"))
				.click();
		driver.findElement(By.linkText("Comprar")).click();

		WebElement money = driver.findElement(By.id("money"));
		assertTrue(money.getText().equals("20�"));

		PO_View.checkElement(driver, "text", "Oferta comprada");
		// Ahora nos desconectamos
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");

	}

	// [Prueba23]Sobre búsqueda determinada (a elección de desarrollador), comprar
	// una oferta que deja un saldo 0 en el contador del comprobador. Y comprobar
	// que el contador se actualiza correctamente en la vista del comprador
	@Test
	public void P23() throws Exception {
		ControladorBaseDeDatosMongoDB cMongo = new ControladorBaseDeDatosMongoDB();
		cMongo.preparacionBase();
		PO_LoginView.fillForm(driver, "usuario1@gmail.com", "1234");
		PO_View.checkElement(driver, "text", "Ofertas");

		PO_PrivateView.search(driver, "El_triciclo_rojo");

		driver.findElement(By.xpath(
				"(.//*[normalize-space(text()) and normalize-space(.)='El_triciclo_rojo'])[1]/following::td[2]"))
				.click();
		driver.findElement(By.linkText("Comprar")).click();

		WebElement money = driver.findElement(By.id("money"));
		assertTrue(money.getText().equals("0�"));

		PO_View.checkElement(driver, "text", "Oferta comprada");
		// Ahora nos desconectamos
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");

	}

	// [Prueba24]Sobre una búsqueda determinada (a elección de desarrollador),
	// intentar comprar una oferta que esté por encima de saldo disponible del
	// comprador. Y comprobar que se muestra el mensaje de saldo no
	// suficiente
	@Test
	public void P24() throws Exception {
		ControladorBaseDeDatosMongoDB cMongo = new ControladorBaseDeDatosMongoDB();
		cMongo.preparacionBase();
		PO_LoginView.fillForm(driver, "usuario1@gmail.com", "1234");
		PO_View.checkElement(driver, "text", "Ofertas");

		PO_PrivateView.search(driver, "Se_vende_cachorro_de_Husky");

		driver.findElement(By.xpath(
				"(.//*[normalize-space(text()) and normalize-space(.)='Se_vende_cachorro_de_Husky'])[1]/following::td[2]"))
				.click();
		driver.findElement(By.linkText("Comprar")).click();

		PO_View.checkElement(driver, "text", "No tienes suficiente dinero para comprar esta oferta");
		// Ahora nos desconectamos
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");

	}

	// [Prueba25]Ir a la opción de ofertas del usuario y mostrar la lista. Comprobar
	// que aparecen las ofertas que deben aparecer
	@Test
	public void P25() {
		PO_LoginView.fillForm(driver, "usuario1@gmail.com", "1234");
		PO_View.checkElement(driver, "text", "Ofertas disponibles");
		driver.navigate().to(URL + "/usr/listarCompradas");
		// Comprobar los 5 usuarios
		PO_View.checkElement(driver, "text", "Harry_Potter_versi�n_del_coleccionista");
		PO_View.checkElement(driver, "text", "Juego_de_tronos");

		// Ahora nos desconectamos
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");
	}

	// [Prueba26]Al crear una oferta marcar dicha oferta como destacada y a
	// continuación comprobar: i) que aparece en el listado de ofertas destacadas
	// para los usuarios y que el saldo del usuario se actualizate en la
	// vista del ofertante (-20).

	@Test
	public void P26() {
		// Vamos al formulario de registro
		PO_LoginView.fillForm(driver, "usuario2@gmail.com", "1234");
		PO_View.checkElement(driver, "text", "Ofertas");
		driver.navigate().to(URL + "/usr/agregarOferta");
		// Rellenamos el formulario.
		PO_OfferView.fillForm(driver, "Esto_es_una_oferta_destacada", "Esto es una descripcion", "12.0", "true");
		PO_View.checkElement(driver, "text", "Oferta insertada");
		WebElement money = driver.findElement(By.id("money"));
		assertTrue(money.getText().equals("0�"));

		// Ahora nos desconectamos
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");

		PO_LoginView.fillForm(driver, "usuario3@gmail.com", "1234");
		PO_View.checkElement(driver, "text", "Ofertas disponibles");
		PO_View.checkElement(driver, "text", "Esto_es_una_oferta_destacada");
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");
	}

	// [Prueba27]Sobre el listado de ofertas de un usuario con más de 20 euros de
	// saldo, char en el enlace Destacada y a continuación comprobar: i) que aparece
	// en el listado de ofertas destacadas para los usuarios y que
	// el listado del usuario se actualiza en la vista del ofertante (-20)
	@Test
	public void P27() throws Exception {
		ControladorBaseDeDatosMongoDB cMongo = new ControladorBaseDeDatosMongoDB();
		cMongo.preparacionBase();
		PO_LoginView.fillForm(driver, "usuario1@gmail.com", "1234");
		PO_View.checkElement(driver, "text", "Ofertas disponibles");
		driver.navigate().to(URL + "/usr/listarCreadas");
		// Comprobar los 5 usuarios
		PO_View.checkElement(driver, "text", "Seat_Ibiza");
		PO_View.checkElement(driver, "text", "Ford_fiesta");
		PO_View.checkElement(driver, "text", "Renault_megane");

		driver.findElement(
				By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Seat_Ibiza'])[1]/following::td[2]"))
				.click();
		driver.findElement(By.linkText("Marcar como destacada")).click();
		PO_View.checkElement(driver, "text", "Oferta destacada");
		driver.navigate().to(URL + "/home");
		WebElement money = driver.findElement(By.id("money"));
		assertTrue(money.getText().equals("80�"));
		// Ahora nos desconectamos
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");

		PO_LoginView.fillForm(driver, "usuario3@gmail.com", "1234");
		PO_View.checkElement(driver, "text", "Ofertas disponibles");
		PO_View.checkElement(driver, "text", "Seat_Ibiza");
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");
	}

	// [Prueba28]Sobre el listado de ofertas de un usuario con menos de 20 euros de
	// saldo, pinchar en el enlace Destacada y a continuación comprobar que se
	// muestra el mensaje de saldo no suficiente.

	@Test
	public void P28() throws Exception {
		ControladorBaseDeDatosMongoDB cMongo = new ControladorBaseDeDatosMongoDB();
		cMongo.preparacionBase();
		PO_LoginView.fillForm(driver, "usuario5@gmail.com", "1234");
		PO_View.checkElement(driver, "text", "Ofertas disponibles");
		driver.navigate().to(URL + "/usr/listarCreadas");

		driver.findElement(
				By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Juego_de_tronos'])[1]/following::td[2]"))
				.click();
		driver.findElement(By.linkText("Marcar como destacada")).click();
		PO_View.checkElement(driver, "text", "Para marcar la oferta como destacada necesitas al menos 20�");
		// Ahora nos desconectamos
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");

	}

	/** TESTS API **/

	// [Prueba29] Inicio de sesión con datos válidos.
	@Test
	public void P29() {
		driver.navigate().to(URL + "/cliente.html?w=login");
		// Rellenamos el formulario
		PO_LoginView.fillFormApi(driver, "usuario1@gmail.com", "1234");
		// COmprobamos que entramos en la pagina privada del usuario
		PO_View.checkElement(driver, "text","T�tulo");
	}
	// [Prueba30] Inicio de sesión con datos inválidos (email existente, pero
	// contraseña incorrecta
	@Test
	public void P30() {
		driver.navigate().to(URL + "/cliente.html?w=login");
		// Rellenamos el formulario
		PO_LoginView.fillFormApi(driver, "usuario1@gmail.com", "123456");
		// COmprobamos que entramos en la pagina privada del usuario
		PO_View.checkElement(driver, "text","El usuario no existe");
	}
	// [Prueba31] Inicio sesión con datos inválidos (campo email o contraseña vacíos
	@Test
	public void P31() {
		driver.navigate().to(URL + "/cliente.html?w=login");
		// Rellenamos el formulario
		PO_LoginView.fillFormApi(driver, "", "");
		// COmprobamos que entramos en la pagina privada del usuario
		PO_View.checkElement(driver, "text","El usuario no existe");

		SeleniumUtils.esperarSegundos(driver, 3);
	}
	// [Prueba32] Mostrar l listado de ofertas disponibles y comprobar que se
	// muestran todas las que existen, menos las del usuario identificado
	@Test
	public void P32() {
		driver.navigate().to(URL + "/cliente.html?w=login");
		// Rellenamos el formulario
		PO_LoginView.fillFormApi(driver, "usuario1@gmail.com", "1234");
		// COmprobamos que entramos en la pagina privada del usuario
		PO_View.checkElement(driver, "text", "Kit_de_supervivencia_de_monta�a");
		PO_View.checkElement(driver, "text", "Pistola");
		PO_View.checkElement(driver, "text", "Tekken_8");
		PO_View.checkElement(driver, "text", "Fifa_20");

		ExpectedConditions.invisibilityOfElementLocated(By.partialLinkText("Seat"));
		ExpectedConditions.invisibilityOfElementLocated(By.partialLinkText("Ford"));
		ExpectedConditions.invisibilityOfElementLocated(By.partialLinkText("Renault"));
		
	}
	
	// [Prueba33] Sobre una búsqueda determinada de ofertas (a elección de
	// desarrollador), enviar un mensaje a una oferta concreta. Se abriría dicha
	// conversación por primera vez. Comprobar que el mensaje aparece en el
	// listado de mensajes
	
	@Test
	public void P33() {
		driver.navigate().to(URL + "/cliente.html?w=login");
		// Rellenamos el formulario
		PO_LoginView.fillFormApi(driver, "usuario1@gmail.com", "1234");
		// COmprobamos que entramos en la pagina privada del usuario
		PO_View.checkElement(driver, "text", "Kit_de_supervivencia_de_monta�a");
		driver.findElement(
				By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Kit_de_supervivencia_de_monta�a'])[1]/following::td[2]"))
				.click();
		driver.findElement(By.linkText("Ver")).click();
		SeleniumUtils.esperarSegundos(driver, 3);
		PO_View.checkElement(driver, "text", "Autor");
		SeleniumUtils.esperarSegundos(driver, 3);
		PO_PrivateView.enviarMensaje(driver, "Hola que tal?");
		PO_PrivateView.enviarMensaje(driver, "Hola que tal?");
		SeleniumUtils.esperarSegundos(driver, 5);
		PO_View.checkElement(driver, "text", "Hola");
		
		
	}
	// [Prueba34] Sobre el listado de conversaciones enviar un mensaje a una
	// conversación ya abierta. Comprobar que el mensaje aparece en el listado de
	// mensajes
	@Test
	public void P34() {
		driver.navigate().to(URL + "/cliente.html?w=login");
		// Rellenamos el formulario
		PO_LoginView.fillFormApi(driver, "usuario1@gmail.com", "1234");
		SeleniumUtils.esperarSegundos(driver, 5);
		// COmprobamos que entramos en la pagina privada del usuario
		PO_View.checkElement(driver, "text", "Gato_egipcio");
		driver.findElement(
				By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Kit_de_supervivencia_de_monta�a'])[1]/following::td[2]"))
				.click();
		driver.findElement(By.linkText("Ver")).click();
		SeleniumUtils.esperarSegundos(driver, 3);
		PO_View.checkElement(driver, "text", "Autor");
		driver.findElement(By.linkText("Conversaciones usuario")).click();
		SeleniumUtils.esperarSegundos(driver, 5);
		driver.findElement(By.linkText("Entrar conversacion")).click();
		SeleniumUtils.esperarSegundos(driver, 3);
		PO_View.checkElement(driver, "text", "Autor");
		SeleniumUtils.esperarSegundos(driver, 3);
		PO_PrivateView.enviarMensaje(driver, "Hola que tal?");
		SeleniumUtils.esperarSegundos(driver, 5);
		PO_View.checkElement(driver, "text", "Hola");		
	}
	// [Prueba35] Mostrar el listado de conversaciones ya abiertas. Comprobar que el
	// listado contiene las conversaciones que deben ser
	@Test
	public void P35() {
		driver.navigate().to(URL + "/cliente.html?w=login");
		// Rellenamos el formulario
		PO_LoginView.fillFormApi(driver, "usuario1@gmail.com", "1234");
		// COmprobamos que entramos en la pagina privada del usuario
		PO_View.checkElement(driver, "text", "Kit_de_supervivencia_de_monta�a");

		driver.findElement(By.linkText("Conversaciones usuario")).click();
		SeleniumUtils.esperarSegundos(driver, 5);
		PO_View.checkElement(driver, "text", "Kit_de_supervivencia_de_monta�a");	
	}
	// [Prueba36] Sobre el listado de conversaciones ya abiertas. Pinchar el enlace
	// Eliminar de la primera y comprobar que el listado se actualiza correctamente.
	@Test
	public void P36() {
		driver.navigate().to(URL + "/cliente.html?w=login");
		// Rellenamos el formulario
		PO_LoginView.fillFormApi(driver, "usuario1@gmail.com", "1234");
		SeleniumUtils.esperarSegundos(driver, 5);

		driver.findElement(By.linkText("Conversaciones usuario")).click();
		SeleniumUtils.esperarSegundos(driver, 5);
		PO_View.checkElement(driver, "text", "Kit_de_supervivencia_de_monta�a");
		driver.findElement(By.linkText("Eliminar conversacion")).click();
		SeleniumUtils.esperarSegundos(driver, 5);
		ExpectedConditions.invisibilityOfElementLocated(By.partialLinkText("Kit"));
	}
	// [Prueba37] Sobre el listado de conversaciones ya abiertas. Pinchar el enlace
	// Eliminar de la última y comprobar que el listado se actualiza correctamente.
	@Test
	public void P37() throws Exception {
		ControladorBaseDeDatosMongoDB cMongo = new ControladorBaseDeDatosMongoDB();
		cMongo.preparacionBase();
		driver.navigate().to(URL + "/cliente.html?w=login");
		// Rellenamos el formulario
		PO_LoginView.fillFormApi(driver, "usuario1@gmail.com", "1234");
		SeleniumUtils.esperarSegundos(driver, 3);
		driver.findElement(By.linkText("Conversaciones usuario")).click();
		SeleniumUtils.esperarSegundos(driver, 3);
		
		driver.findElement(
				By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Tekken_8'])[1]/following::td[2]"))
				.click();
		driver.findElement(By.linkText("Eliminar conversacion")).click();
		ExpectedConditions.invisibilityOfElementLocated(By.partialLinkText("Tekken_8"));
	}
}
