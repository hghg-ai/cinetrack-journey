Tuần 1
Val ko thể gán lại giá trị sau khởi tạo lần đâu
Var có thể gán lại giá trị mới nhiều lần
Nên dùng val vì nếu 2 thread cùng truy cập và đổi một biến var thì dễ bị lỗi bất đồng bộ, 
dùng val loại bỏ hoàn toàn dc nguy cơ này

?. và ?: đều là công cụ xử lý Null,nhưng
?. dùng để truy cập an toàn bằng cách kiểm tra giá trị có hợp lệ hay ko,
nếu hợp lệ thì thực thi, ko thì null
val name: String? = getName()
name?.let{
println("Tên này có ${it.name} ký tự")
}
?: dùng để cung cấp giá trị thay thế cho biểu thức A,
nếu A là null thì trả về B, ko thì trả về A
val name: String? = getName()
val displayName: String = name?:"jljlj"

"!!" nguy hiểm vì nó có thể ép Kotlin coi biến có thể null thành null, gây crash chương trình

Tuần 2
1. sealed interface vs interface thường vs enumKhác biệt cốt lõi:
   interface thường: Không giới hạn số lượng class implement và có thể implement ở bất kỳ đâu (kể cả ngoài module). Compiler không biết hết mọi kiểu con tại compile-time.
   enum: Đại diện cho tập hợp các hằng số / đối tượng đơn thể (singleton) cố định. Mọi giá trị đều có cùng cấu trúc thuộc tính và không thể mang dữ liệu riêng biệt khác nhau cho từng case.
   sealed interface: Giới hạn tập hợp các class/object/interface implement nó trong cùng module (hoặc package tùy phiên bản/quy ước). Mỗi nhánh con có thể có dữ liệu và thuộc tính khác nhau (ví dụ data object Loading, data class Success(...), data class Error(...)).
    Khi nào dùng cái nào:
   enum: Khi có tập giá trị cố định, đơn giản, không cần state riêng phức tạp (ví dụ: ngày trong tuần, hướng gió, mức độ ưu tiên).
   interface thường: Khi định nghĩa hành vi/hợp đồng mở rộng cho nhiều thành phần độc lập (ví dụ: Repository, ClickListener, các tính năng plugin).
   sealed interface: Khi biểu diễn tập trạng thái hoặc kết quả khép kín (như UiState, Result) để when được compiler kiểm tra đầy đủ các nhánh.
2. map khác forEach thế nào? filter trả về gì khi không có phần tử nào thỏa?
   map vs forEach:
   map: Biến đổi từng phần tử sang dạng mới và trả về một List mới chứa các phần tử đã biến đổi.
   forEach: Chỉ duyệt qua từng phần tử để thực hiện tác vụ phụ (side-effect như println, log, ghi DB) và trả về Unit (không tạo list mới).
   filter khi không có phần tử nào thỏa mãn:Trả về một danh sách rỗng (emptyList() / List<T> rỗng), không trả về null và không ném exception.
3. Vì sao tách MovieDto và Movie thay vì dùng chung một class?
   Tách biệt trách nhiệm & tầng kiến trúc: MovieDto thuộc tầng Data/Network (khớp cấu trúc JSON/API, ví dụ snake_case, annotation serialize), còn Movie là Domain/UI model (chỉ giữ dữ liệu sạch, chuẩn naming của app như camelCase).
   Chống vỡ code khi API đổi: Nếu backend đổi tên trường, đổi kiểu dữ liệu hoặc thêm/bớt field, bạn chỉ cần sửa Dto và hàm Mapper (toDomain()), không làm ảnh hưởng đến Domain, ViewModel hay UI.
   Tránh rò rỉ chi tiết network lên UI: Tầng giao diện không phải làm quen với các trường dư thừa hoặc quy ước riêng của API.
4. Hàm suspend là gì? Vì sao hàm thường không gọi được hàm suspend?
   Hàm suspend là hàm có thể tạm dừng (suspend) tại một điểm chờ và tiếp tục (resume) sau đó mà không block luồng hiện tại (như Main thread).
   Vì sao hàm thường không gọi được: Bên dưới, compiler biên dịch hàm suspend thành máy trạng thái dùng cơ chế Continuation để lưu trạng thái và điều phối điểm dừng/chạy tiếp. Hàm thường không có ngữ cảnh Coroutine/Continuation này, nên chỉ có thể gọi hàm suspend từ một hàm suspend khác hoặc bên trong Coroutine builder (launch, async, ...).
5. launch khác async chỗ nào?
   launch:Chạy tác vụ bất đồng bộ theo kiểu "fire-and-forget" (thường dùng cho side-effect như cập nhật UI, gọi hàm suspend không cần lấy giá trị trả về).Trả về Job để quản lý vòng đời (chờ xong với join(), hủy với cancel()).
   async:Chạy tác vụ bất đồng bộ khi cần lấy kết quả trả về (thường dùng khi chạy song song nhiều tác vụ rồi gộp kết quả).Trả về Deferred<T> (kế thừa Job) và dùng .await() để nhận giá trị kiểu T.

   Tuần 3
   1. Recomposition là gì? Cái gì kích hoạt nó?
    Recomposition là gì: Là quá trình Jetpack Compose chạy lại các hàm @Composable khi dữ liệu/state hoặc tham số đầu vào thay đổi để cập nhật lại UI cho khớp với dữ liệu mới. Compose có cơ chế intelligent recomposition nên chỉ gọi lại đúng những composable/lambda có đọc dữ liệu thay đổi và bỏ qua các phần không đổi.
    Cái gì kích hoạt nó:
 State thay đổi (như MutableState / mutableStateOf): Khi giá trị state được cập nhật, hệ thống theo dõi state của Compose sẽ lập lịch recompose cho các composable đang đọc state đó.
 Tham số truyền vào composable thay đổi: Khi composable cha recompose và truyền giá trị mới xuống composable con, composable con cũng có thể được gọi lại.
  2. remember để làm gì? Bỏ remember thì mutableStateOf bị sao?
 remember để lưu trữ một đối tượng hoặc kết quả tính toán trong bộ nhớ của Composition qua các lần recomposition (hoặc lưu kết quả tính toán nặng và chỉ tính lại khi key thay đổi). Khi composable bị gỡ khỏi Composition, remember sẽ quên giá trị đó. (Lưu ý: remember không giữ qua configuration change như xoay màn hình — lúc đó cần rememberSaveable).
 Bỏ remember thì mutableStateOf bị sao: Nếu viết var count = mutableStateOf(0) mà không có remember, mỗi lần hàm composable chạy lại (recompose), câu lệnh mutableStateOf(0) sẽ được thực thi lại từ đầu và reset giá trị về trạng thái ban đầu (ví dụ 0 hoặc ""), khiến state bị mất/không giữ được giá trị qua các lần vẽ lại.   
3. State hoisting là gì? Giải thích bằng chính MovieCard
   State hoisting là mẫu thiết kế đưa state từ bên trong một composable lên hàm cha (nơi gọi nó) để biến composable đó thành Stateless (không tự giữ state). Thường đi kèm mô hình luồng dữ liệu một chiều (UDF) qua cặp tham số: giá trị hiển thị (đi xuống) và sự kiện yêu cầu thay đổi (đi lên).
   Ví dụ với MovieCard: MovieCard có trạng thái yêu thích (isFavorite). Thay vì tự giữ remember { mutableStateOf(false) } bên trong MovieCard:
   
   // Stateless Composable: MovieCard chỉ nhận trạng thái và báo sự kiện click lên trên
@Composable
fun MovieCard(
    movie: Movie,
    isFavorite: Boolean,
    onToggleFavorite: (Movie) -> Unit,
    onMovieClick: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    // Hiển thị thông tin phim + nút tim dựa trên isFavorite
    // Khi bấm nút tim -> gọi onToggleFavorite(movie)
}

// Composable cha giữ state (hoặc sau này chuyển lên ViewModel)
@Composable
fun MovieSection(movies: List<Movie>, onMovieClick: (Movie) -> Unit) {
    var favoriteIds by remember { mutableStateOf(setOf<Int>()) }

    MovieCard(
        movie = movies.first(),
        isFavorite = movies.first().id in favoriteIds,
        onToggleFavorite = { targetMovie ->
            favoriteIds = if (targetMovie.id in favoriteIds) {
                favoriteIds - targetMovie.id
            } else {
                favoriteIds + targetMovie.id
            }
        },
        onMovieClick = onMovieClick
    )
}
Nhờ vậy, MovieCard dễ tái sử dụng ở nhiều màn hình, dễ test, và có một nguồn dữ liệu duy nhất (Single Source of Truth) thay vì mỗi card tự giữ state rời rạc. 

4. LazyColumn khác Column + verticalScroll chỗ nào? Khi nào bắt buộc dùng Lazy?
Khác biệt chính:
Column + Modifier.verticalScroll(...): Compose và đo đạc/bố trí (compose and layout) toàn bộ các item ngay từ đầu, kể cả những item đang nằm ngoài vùng nhìn thấy trên màn hình.   
LazyColumn: Chỉ compose và layout những item đang hiển thị trong viewport (cộng thêm phần đệm), và tái sử dụng khi cuộn — tương tự nguyên lý của RecyclerView.  
Khi nào bắt buộc / nên dùng Lazy: Khi danh sách có nhiều item hoặc không biết trước độ dài/số lượng lớn (như danh sách phim, feed dữ liệu dài), vì dùng Column thường sẽ gây tốn tài nguyên, giật lag hoặc tràn bộ nhớ khi danh sách lớn. Ngược lại, nếu chỉ có vài block cố định ngắn trên màn hình, Column + verticalScroll vẫn đơn giản và ổn.

5. Đổi Modifier.padding(16.dp).background(Color.Red) thành background trước padding thì khác gì?
Trong Jetpack Compose, thứ tự của Modifier rất quan trọng vì mỗi modifier bọc/áp dụng lên kết quả của modifier đứng trước nó theo chuỗi từ ngoài vào trong:

Modifier.padding(16.dp).background(Color.Red): Trước tiên tạo khoảng trống 16.dp xung quanh nội dung, sau đó tô màu đỏ cho vùng còn lại bên trong (tức là vùng padding 16.dp ở ngoài cùng không có màu đỏ).

Modifier.background(Color.Red).padding(16.dp): Trước tiên tô màu đỏ toàn bộ khung (bao gồm cả phần đệm 16.dp), sau đó mới tạo khoảng trống 16.dp đẩy nội dung bên trong vào xa viền màu đỏ (tức là nền đỏ phủ ra tới mép ngoài, tạo thành viền/lớp nền đỏ bao quanh padding).
