import kotlinx.serialization.json.Json
import java.io.File

data class Movie(
        val id:Int,
        val title:String,
        val overview:String,
        val rating: Double,
        val releaseDate: String,
        val genreIds: List<Int>
)
data class Genre(val id:Int, val name:String)
fun Movie.prettyPrint() {
    val shortOverview = if (overview.length > 80) {
        overview.take(80) + "..."
    } else {
        overview
    }
    val year = releaseDate.substringBefore("-")
    println("* $rating | $title($year)")
    println("$shortOverview\n")
}
    val sampleGenre = listOf(
            Genre(1, "Sci-fi"),
            Genre(2,"Action"),
            Genre(3,"Comedy"),
            Genre(4,"Mystery"),
            Genre(5,"Adventure")
    )
    val sampleMovies = listOf(
            Movie(1,"Pacific Rim","Bộ phim lấy bối cảnh tương lai gần, khi Trái Đất bất ngờ bị tấn công" +
                    " bởi Kaiju — loài quái vật khổng lồ trồi lên từ một cổng không gian bên dưới lòng Thái Bình Dương." +
                    " Để chống lại mối đe dọa sinh tồn này, các quốc gia đã hợp lực tạo ra Jaeger: những robot chiến đấu" +
                    " khổng lồ đòi hỏi hai phi công phải kết nối thần kinh (Drift) để cùng điều khiển. Khi chiến tranh " +
                    "kéo dài và lực lượng Kaiju ngày càng tiến hóa mạnh mẽ hơn, dự án Jaeger đứng trước nguy cơ bị " +
                    "loại bỏ. Trong nỗ lực cuối cùng, cựu phi công Raleigh Becket — người vẫn mang tổn thương tâm lý " +
                    "sau cái chết của anh trai — được triệu tập lại để phối hợp cùng tân binh Mako Mori. Họ cùng nhau" +
                    " điều khiển cỗ máy huyền thoại cũ kỹ Gipsy Danger, dấn thân vào trận chiến quyết định nhằm san" +
                    " phẳng cổng rãnh nứt, khép lại cuộc xâm lăng và giải cứu nhân loại.",6.9,"2003-7-19", listOf(1,2)),
            Movie(2,"Pirates of the Caribbean:The Curse of the Black Pearl","Người thợ rèn Will Turner liên kết với tên cướp biển " +
                    "lập dị thuyền trưởng Jack Sparrow nhằm cứu người yêu của Turner Elizabeth Swann từ những tên cướp " +
                    "biển bất tử do cựu thuyền phó nổi loạn của Jack thuyền trưởng Barbossa cầm đầu. Jack cũng muốn trả " +
                    "thù Barbossa vì đã bỏ lại hắn mắc kẹt trên một hòn đảo trước khi đánh cắp con tàu Ngọc Trai Đen " +
                    "của hắn cùng 882 thỏi vàng Aztec bị nguyền rủa.",8.1,"2003-7-9", listOf(5,2)),
            Movie(3,"Scary main.kotlin.Movie 1","A group of hapless teens harboring a guilty secret is stalked by" +
                    " an equally bumbling serial killer in this parody of 1990s horror movies.",6.3,"2000-7-7", listOf(3,4)),
            Movie(4,"Scary main.kotlin.Movie 2","A pair of priests, a group of students and a professor find themselves seduced and " +
                    "spooked by a poltergeist wreaking havoc in a haunted mansion.",5.4, "2001-7-4", listOf(3,4)),
            Movie(5,"White chicks","In order to foil a kidnapping, two Black FBI agents disguise " +
                    "themselves as white women to impersonate the heiresses they've been assigned to protect.",6.0,
                    "2004-6-23", listOf(3)),
            Movie(6, "Transformers: The Last Knight","Quintessa brainwashes Optimus Prime and heads " +
                    "to Earth to main.kotlin.search for an ancient staff. Cade, Bumblebee and the Autobots race against time to " +
                    "find it, while also escaping an anti-Transformers force.",5.2,"2017-6-20", listOf(1,2)),
            Movie(7,"Real Steel ","Charlie, a prize fighter, loses his chance to win the title when humans" +
                    " are replaced by heavy, towering robots in the boxing ring. After failing badly, he teams up with " +
                    "his estranged son Max to win.", 7.1,"2011-8-2", listOf(1,2)),
            Movie(8,"Spider-Man: Brand New Day","Peter Parker devotes his life to protecting New York City " +
                    "as a full-time Spider-Man. But as the demands on him intensify, the pressure sparks a surprising physical " +
                    "evolution that threatens his existence, even as a strange new pattern of crimes gives rise to one of the " +
                    "most powerful threats he's ever faced.",8.0,"2026-7-31", listOf(2,5)),
            Movie(9,"Advengers","S.H.I.E.L.D. leader Nick Fury is compelled to launch the Avengers programme " +
                    "when Loki poses a threat to planet Earth. But the superheroes must learn to work together if they" +
                    " are to stop him in time.",8.0,"2012-4-27", listOf(1,2,5)),
            Movie(10, "The Tomorrow War","The world is stunned when a group of time travellers arrive" +
                    " from the year 2051 to deliver an urgent message: thirty years in the future, mankind is " +
                    "losing a global war against a deadly alien species.",6.6,"2021-6-30", listOf(1,2,5)),
            Movie(11,"Pirates of the Caribbean: Dead Man's Chest","Captain Jack Sparrow seeks the heart of Davy Jones," +
                    " a mythical pirate, in order to avoid being enslaved to him. However, others, including his friends Will and Elizabeth," +
                    " want it for their own gain.",7.4,"2006-7-28",listOf(5,2)),
        Movie(12,"Pirates of the Caribbean: At World's End","Will Turner and Elizabeth Swann team up with Barbossa " +
                "to rescue Jack Sparrow from the clutches of Davy Jones. The Flying Dutchman's ghost ship is all " +
                "set to create trouble on the Seven Seas.",7.2,"2007-5-25",listOf(5,2)),
        Movie(13,"Pirates of the Caribbean: On Stranger Tides","Captain Jack Sparrow sets sail in main.kotlin.search of the " +
                "fountain of youth. On his way, he meets a mysterious woman from his past. In order to accomplish his mission, he has " +
                "to face his old enemy, Blackbeard.",6.6,"2011-5-7",listOf(5,2)),
        Movie(14,"Pirates of the Caribbean: Dead Men Tell No Tales","To break the curse of Flying Dutchman, " +
                "Captain Jack Sparrow and Henry Turner embark on a mission to find the Trident of Poseidon. They also try to stop" +
                " Captain Salazar who intends to rule the seas.",6.5,"2017-5-26",listOf(5,2)),
        Movie(15,"Avengers: Age of Ultron","Tony Stark builds an artificial intelligence system " +
                "named Ultron with the help of Bruce Banner. When the sentient Ultron makes plans to " +
                "wipe out the human race, the Avengers set out to stop him.",7.3,"2015-4-24",listOf(1,2,5)),
        Movie(16,"Avengers: Infinity War","As Thanos sets about his quest for finding the infinity stones and " +
                "carrying out his twisted scheme, the Avengers join forces with their allies to " +
                "stop him from causing chaos and destruction.",8.4,"2018-4-23",listOf(1,2,5)),
        Movie(17,"Avengers: Endgame","After Thanos, an intergalactic warlord, disintegrates half of the universe," +
                " the Avengers must reunite and assemble again to reinvigorate their trounced" +
                " allies and restore balance.",8.4,"2019-4-26",listOf(1,2,5)),
        Movie(18,"Transformer","Autobots and Decepticons, races from outer space, engage in a " +
                "humongous and fierce battle on planet Earth. The Autobots enlist the help of a teenager, Sam, to " +
                "win the battle against the Decepticons.",7.1,"2007-8-24",listOf(1,2,5)),
        Movie(19,"Transformers: Revenge of the Fallen","Sam wants to lead a normal life in college " +
                "but he has visions of Cybertronion symbols. Meanwhile, an ancient threat marshals the rest of " +
                "the Decepticons in order to avenge their earlier defeat.",6.0,"2009-6-24",listOf(1,2,5)),
        Movie(20,"Transformers: Dark of the Moon","The Autobots discover their lost leader Sentinel Prime and an" +
                " invention capable of ending the war between them and the Decepticons, only to realize that " +
                "they have been betrayed to their enemies.",6.2,"2011-6-29",listOf(1,2,5)),
        Movie(21,"Transformers: Age of Extinction","The sentient Autobots from the planet Cybertron " +
                "receive help from Cade Yeager, an unsuccessful inventor, and a team of humans as a bounty hunter and" +
                " an elite CIA black ops unit try to apprehend them.",5.6,"2014-7-27",listOf(1,2,5)),
        Movie(22,"Transformers One","Once upon a time, Optimus Prime and Megatron were friends bonded like " +
                "brothers who managed to change the fate of the Cybertron planet forever. This is their untold original story," +
                " before they end up being bitter opponents.",7.6,"2024-8-20",listOf(2,5)),
        Movie(23,"Men in Black","James, an NYC cop, is hired by Agent K of a secret government" +
                " agency that monitors extraterrestrial life on Earth. Together, they must recover an item that has " +
                "been stolen by an intergalactic villain.",7.3,"1997-7-2",listOf(1,2,3,4)),
        Movie(24,"Men in Black 2","Agent J learns about the Light of Zartha and the powerful " +
                "shapeshifting alien looking for it. He must seek the help of his former partner, Agent K, to stop " +
                "the extraterrestrial and save the Earth.",6.2,"2002-7-3",listOf(1,2,3,4)),
        Movie(25,"Men in Black 3 ","After a dangerous alien criminal escapes a fully secure prison," +
                " it is up to Agent J to bring him back. He goes back in time and finds a younger Agent K to " +
                "help him in his quest.",6.8,"2012-5-25",listOf(1,2,3,4)),
        Movie(26,"Ready Player One","When the creator of a virtual reality world called OASIS " +
                "dies, he leaves a challenge behind. Hidden inside the virtual world is an Easter Egg, which grants a " +
                "worthy person total control over OASIS.",7.4,"2018-3-28",listOf(1,2,5,4)),
        Movie(27,"Jurassic World","A theme park showcasing genetically-engineered dinosaurs turns " +
                "into a nightmare for its tourists when one of the dinosaurs escapes its enclosure. An ex-military " +
                "animal expert steps up to save the day.",6.9,"2015-6-10",listOf(1,2,5)),
        Movie(28,"Jurassic World: Fallen Kingdom","After a volcano eruption proves to be a threat " +
                "for the dinosaurs, Owen and Claire reach the defunct Jurassic World, a theme park, to save the animals " +
                "from extinction.",6.2,"2018-6-8",listOf(1,2,5)),
        Movie(29,"Jurassic World Dominion","Four years after the destruction of Isla Nublar," +
                " dinosaurs now live and hunt alongside humans all over the world. This fragile balance will reshape" +
                " the future and determine, once and for all, whether human beings are to remain the apex predators " +
                "on a planet they now share with history's most fearsome creatures.",5.6,"2022-6-3",
            listOf(1,2,5)),
        Movie(30,"Jurassic World Rebirth","Zora Bennett leads a team of skilled operatives to the" +
                " most dangerous place on Earth, an island research facility for the original Jurassic Park. Their" +
                " mission is to secure genetic material from dinosaurs whose DNA can provide life-saving benefits " +
                "to mankind. As the top-secret expedition becomes more and more risky, they soon make a sinister," +
                " shocking discovery that's been hidden from the world for decades.",5.8,"2025-7-2",
            listOf(1,2,5))

    )
fun List<Movie>.filterByGenre(genreId: Int): List<Movie> {
    return this.filter{movie->movie.genreIds.contains(genreId)}
}
fun List<Movie>.search(keyword: String): List<Movie> {
    return this.filter{movie -> movie.title.contains(keyword, ignoreCase = true)}
    return this.filter{movie -> movie.overview.contains(keyword, ignoreCase = true)}
}
fun List<Movie>.top5ByRating(): List<Movie> {
    return this.sortedByDescending { movie -> movie.rating }.take(5)
}
fun List<Movie>.groupByYear(): Map<String,List<Movie>> {
    return this.groupBy { movie -> movie.releaseDate.substringBefore("-") }
}
fun List<Movie>.averageRatingByGenre(genres: List<Genre>): Map<String, Double> {
    return genres.associate { genre ->
        val moviesInGenre = this.filterByGenre(genre.id)
        val avgRating = if( moviesInGenre.isNotEmpty()){
        moviesInGenre.map{it.rating}.average()
    }else{
        0.0
    }
        genre.name to avgRating
    }
}



fun main() {
    val topMovie = sampleMovies.maxByOrNull { it.rating }
    topMovie?.let {
        println("PHIM HAY NHẤT:${it.title} | ${it.rating} điểm")
    }
    val validTopMovie = topMovie ?: run {
        println(" Danh sách phim trống")
        return
    }
    println("Danh sách phim\n")
    for (movie in sampleMovies) {
        if (movie == validTopMovie) {
            println(">>>PHIM HAY NHẤT<<<")
        }
        movie.prettyPrint()
    }
    println("1.Lọc theo genre(Action, ID:2)")
    sampleMovies.filterByGenre(2).forEach { it.prettyPrint() }

    println("2.Tìm phim real steel ")
    sampleMovies.search("real steel").forEach { it.prettyPrint() }

    println("3.Top 5 phim dựa vào rate")
    sampleMovies.top5ByRating().forEach { it.prettyPrint() }

    println("4.Nhóm theo năm")
    sampleMovies.groupByYear().forEach { (year, movies) ->
        println("Năm: $year, ${movies.size} phim")
    }

    println("5.Trung bình rate dựa trên genre")
    sampleMovies.averageRatingByGenre(sampleGenre).forEach { (genrename, avg) ->
        println("$genrename: ${"%.2f".format(avg)}")
    }
    val jsonParser = Json { ignoreUnknownKeys = true }
    val jsonString = File("src/main/resources/movies.json").readText()
    val response = jsonParser.decodeFromString<MovieResponseDto>(jsonString)
    val movieDtos: List<MovieDto> = response.results
    val realMovies: List<Movie> = movieDtos.map { it.toMovie() }
    println("Đã lấy thành công ${realMovies.size} phim từ movie.json")

    println("1.TOP 5 PHIM BÌNH DỰA VÀO RATE")
    realMovies.top5ByRating().forEach { movie ->
        println("- ${movie.title} (${movie.rating}) - Ngày chiếu: ${movie.releaseDate}")
    }
    println("2.LỌC THEO GENRE(28)")
    realMovies.filterByGenre(28).forEach { movie ->
        println("${movie.title} (${movie.genreIds})")
    }
    println("3.TÌM PHIM SPIDER-MAN")
    realMovies.search("SPIDER").forEach { movie ->
        println("${movie.title}")
    }
    println("4.NHÓM THEO NĂM")
    realMovies.groupByYear().forEach { (year, movies) ->
        println("$year: ${movies.size} phim")
    }
    println("5.TRUNG BÌNH RATE DỰA TRÊN GENRE")
    val realGenres = listOf(
        Genre(28, "action"),
        Genre(35, "comedy"),
        Genre(878, "sci-fi"))
    realMovies.averageRatingByGenre(realGenres).forEach { (genrename, avg) ->
        println("$genrename: ${"%.2f".format(avg)}")
    }
}
