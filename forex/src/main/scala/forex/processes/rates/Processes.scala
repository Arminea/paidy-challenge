package forex.processes.rates

import cats.Monad
import cats.data.EitherT
import forex.domain._
import forex.services._

object Processes {
  def apply[F[_]]: Processes[F] =
    new Processes[F] {}
}

trait Processes[F[_]] {
  import messages._
  import converters._

  def get(
      request: GetRequest
  )(
      implicit
      M: Monad[F],
      OneFrame: OneFrame[F]
  ): F[Error Either Rate] =
    (for {
      result ← EitherT(OneFrame.get(Rate.Pair(request.from, request.to))).leftMap(toProcessError)
    } yield result).value

}
