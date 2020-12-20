package im.tony.utils

import io.github.serpro69.kfaker.Faker
import io.github.serpro69.kfaker.provider.ESport
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.logging.Level
import kotlin.random.Random

private val faker: Faker by lazy { Faker() }
private val random: Random by lazy {
  Random(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
}
private val fixedRand: Random by lazy { Random(0) }
private val levels: List<Level> by lazy { listOf(Level.FINER, Level.FINE, Level.FINEST, Level.INFO, Level.WARNING, Level.SEVERE, Level.CONFIG) }

fun getRandomLogLevel(useFixed: Boolean = false): Level = levels.random(if (useFixed) fixedRand else random)

fun getRandomQuote(useFixed: Boolean = false): String = when ((if (useFixed) fixedRand else random).nextInt(0, 120)) {
  in 0..5 -> "His last words were ... '${faker.quote.famousLastWords()}'"
  in 6..10 -> "Matz said ... '${faker.quote.matz()}'"

  in 11..15 -> faker.quote.mostInterestingManInTheWorld()
  in 16..20 -> "Robin said ... '${faker.quote.robin()}'"

  in 21..25 -> "Yoda said ... '${faker.quote.yoda()}'"
  in 26..30 -> "Siegler said ... '${faker.quote.singularSiegler()}'"

  in 31..35 -> "Ancient Hero: ${faker.ancient.hero()}"
  in 36..40 -> "Ancient Primordial: ${faker.ancient.primordial()}"

  in 41..45 -> "Ancient God: ${faker.ancient.god()}"
  in 46..50 -> "Ancient Titan: ${faker.ancient.titan()}"

  in 51..55 -> "Ancient Titan: ${faker.ancient.titan()}"
  in 56..60 -> "Michael said ... '${faker.michaelScott.quotes()}'"

  in 61..65 -> faker.bigBangTheory.quotes()
  in 66..70 -> faker.departed.quotes()

  in 71..75 -> faker.rickAndMorty.quotes()
  in 76..80 -> faker.greekPhilosophers.quotes()

  in 81..85 -> faker.drWho.quotes()
  in 86..90 -> faker.prince.lyric()

  in 91..95 -> faker.witcher.quotes()
  in 96..120 -> faker.eSport.quote()

  else -> "Whoops"
}

fun ESport.quote(): String =
  "Today ${players()} of ${this.teams()} played ${this.teams()} in ${this.games()} during the ${this.events()} in the ${this.leagues()}"
