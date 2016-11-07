import akka.actor.{ActorSystem, Props}

object ExamplePersistentActorApp {

  val system = ActorSystem("example")
  val persistentActor = system.actorOf(Props[ExamplePersistentActor], "persistentActor-4-scala")

  persistentActor ! Cmd("foo")
  persistentActor ! Cmd("baz")
  persistentActor ! Cmd("bar")
  persistentActor ! "snap"
  persistentActor ! Cmd("buzz")
  persistentActor ! "print"

  Thread.sleep(10000)
  system.terminate()
}
