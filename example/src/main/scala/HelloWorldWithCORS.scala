import zhttp.http._
import zhttp.http.middleware.HttpMiddleware.cors
import zhttp.service.Server
import zio._

object HelloWorldWithCORS extends App {

  // Create CORS configuration
  val config: CORSConfig =
    CORSConfig(allowedOrigins = _ == "dev", allowedMethods = Some(Set(Method.PUT, Method.DELETE)))

  // Create HTTP route with CORS enabled
  val app: HttpApp[Any, Nothing] =
    HttpApp.collect {
      case Method.GET -> !! / "text" => Response.text("Hello World!")
      case Method.GET -> !! / "json" => Response.jsonString("""{"greetings": "Hello World!"}""")
    } @@ cors(config)

  // Run it like any simple app
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    Server.start(8090, app.silent).exitCode
}
