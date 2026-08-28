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
            Movie(2,"Pirates of the Caribbean","Người thợ rèn Will Turner liên kết với tên cướp biển " +
                    "lập dị thuyền trưởng Jack Sparrow nhằm cứu người yêu của Turner Elizabeth Swann từ những tên cướp " +
                    "biển bất tử do cựu thuyền phó nổi loạn của Jack thuyền trưởng Barbossa cầm đầu. Jack cũng muốn trả " +
                    "thù Barbossa vì đã bỏ lại hắn mắc kẹt trên một hòn đảo trước khi đánh cắp con tàu Ngọc Trai Đen " +
                    "của hắn cùng 882 thỏi vàng Aztec bị nguyền rủa.",8.1,"2003-7-9", listOf(5,2)),
            Movie(3,"Scary Movie 1","A group of hapless teens harboring a guilty secret is stalked by" +
                    " an equally bumbling serial killer in this parody of 1990s horror movies.",6.3,"2000-7-7", listOf(3,4)),
            Movie(4,"Scary Movie 2","A pair of priests, a group of students and a professor find themselves seduced and " +
                    "spooked by a poltergeist wreaking havoc in a haunted mansion.",5.4, "2001-7-4", listOf(3,4)),
            Movie(5,"White chicks","In order to foil a kidnapping, two Black FBI agents disguise " +
                    "themselves as white women to impersonate the heiresses they've been assigned to protect.",6.0,
                    "2004-6-23", listOf(3)),
            Movie(6, "Transformers: The Last Knight","Quintessa brainwashes Optimus Prime and heads " +
                    "to Earth to search for an ancient staff. Cade, Bumblebee and the Autobots race against time to " +
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
                    "losing a global war against a deadly alien species.",6.6,"2021-6-30", listOf(1,2,5))
    )


fun main(){
    val topMovie = sampleMovies.maxByOrNull { it.rating }
    topMovie ?.let{
        println("PHIM HAY NHẤT:${it.title} | ${it.rating} điểm")
    }
    val validTopMovie = topMovie ?:run{
        println(" Danh sách phim trống")
        return
    }
    println("Danh sách phim\n")
    for( movie in sampleMovies ){
        if(movie == validTopMovie){
            println("Phim hay nhất")
        }
        movie.prettyPrint()
    }

}
