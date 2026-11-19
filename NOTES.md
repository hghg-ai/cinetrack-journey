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

Tuần 4
1. Back stack là gì? Từ Home vào Detail rồi bấm back, chuyện gì xảy ra với composable Detail?
   Back stack là gì: Là ngăn xếp lưu lịch sử điều hướng các màn hình theo cơ chế LIFO (Last-In, First-Out — Vào sau, Ra trước). Mỗi khi mở màn hình mới, điểm đến đó được push lên đỉnh stack; khi quay lại, điểm đến trên đỉnh bị pop ra.
   Khi từ Home vào Detail rồi bấm back:
   Màn hình Detail bị pop khỏi Back stack.
   Composable Detail bị gỡ bỏ khỏi Composition.
   Các state cục bộ chỉ nằm trong Detail bằng remember (không qua ViewModel hay rememberSaveable) sẽ bị hủy cùng composable đó.
   Màn hình Home bên dưới được hiển thị trở lại.
   
2. Vì sao truyền movieId thay vì truyền cả Movie object?
   Single Source of Truth (Một nguồn dữ liệu duy nhất): Object Movie mang từ Home sang chỉ là bản chụp tại thời điểm trước đó. Dùng movieId giúp Detail (qua DetailViewModel / Repository) luôn lấy dữ liệu mới nhất và đồng nhất.
   Tránh vượt giới hạn kích thước dữ liệu truyền qua Bundle / route (TransactionTooLargeException): Dữ liệu điều hướng/lưu trạng thái có giới hạn dung lượng nhỏ; truyền nguyên object lớn hoặc lồng ghép nhiều trường dễ gây lỗi hoặc crash khi hệ thống lưu/khôi phục trạng thái.Route gọn nhẹ, dễ chuẩn hóa & hỗ trợ Deep Link: Truyền ID (Int hoặc String) trên route rõ ràng, ít lỗi parse và dễ mở trực tiếp màn Detail từ link/thông báo.

 3. Thí nghiệm: Gõ chữ vào Search rồi xoay màn hình
   Thí nghiệm Tuần 4 — Gõ chữ vào Search rồi xoay màn hình
Dự đoán hiện tượng:
  Nếu từ khóa tìm kiếm đang giữ bằng remember { mutableStateOf("") } ngay trong Composable: xoay màn hình sẽ làm mất chữ đã gõ và ô tìm kiếm quay về rỗng "".
  Nếu từ khóa và kết quả đã đưa vào ViewModel (như cách bạn đang làm với SearchViewModel và collectAsStateWithLifecycle()): xoay màn hình sẽ giữ nguyên cả từ khóa lẫn trạng thái/kết quả tìm kiếm.
Dự đoán nguyên nhân:
  Xoay màn hình kích hoạt Configuration Change, khiến Activity bị hủy và tạo lại. Bộ nhớ của remember gắn với vòng đời Composition/Activity cũ nên bị xóa khi Activity hủy, trong khi ViewModel tồn tại xuyên suốt Configuration Change nên giữ được state.  

Tuần 5
1.
[Người dùng gõ phím vào OutlinedTextField]
       │
       ▼ (1. Event lên: onQueryChanged(query))
[SearchScreen (Stateless Composable)]
       │
       ▼ (2. Cập nhật _searchQuery / kích hoạt luồng xử lý)
[SearchViewModel]
       │  ├─ debounce(300ms) -> chờ ngừng gõ 300ms
       │  ├─ distinctUntilChanged() -> bỏ qua nếu query không đổi
       │  └─ flatMapLatest { repository.searchMovies(it) } -> hủy request cũ nếu có query mới
       ▼
[Repository / Data Layer] ──► trả danh sách phim (hoặc lỗi / rỗng)
       │
       ▼ (3. Cập nhật _uiState: Loading -> Success / Error / Empty)
[SearchViewModel (StateFlow<SearchUiState>)]
       │
       ▼ (4. State xuống: collectAsStateWithLifecycle())
[SearchScreen vẽ lại (Recompose) -> LazyColumn hiển thị danh sách]

Các bước chi tiết:
Phát sự kiện (Event Up): Người dùng gõ ký tự vào ô tìm kiếm; SearchScreen không tự giữ hay lọc dữ liệu mà gửi sự kiện (ví dụ onQueryChange(newText)) lên SearchViewModel.
Xử lý luồng & chống nhiễu trong ViewModel: Query đi qua chuỗi toán tử (debounce(300), distinctUntilChanged(), flatMapLatest) để tránh gọi tìm kiếm thừa và hủy tác vụ cũ khi người dùng đang gõ tiếp.
Lấy dữ liệu & đổi UiState: SearchViewModel đặt trạng thái đang tải (Loading), gọi Repository lấy danh sách phim, rồi cập nhật _uiState sang trạng thái tương ứng (Success(movies), danh sách rỗng hoặc Error).  
Phát state xuống & vẽ lại UI (State Down): SearchScreen thu thập uiState qua collectAsStateWithLifecycle() và dùng when để render giao diện tương ứng (LazyColumn khi có kết quả).

2. Vì sao ViewModel không bị hủy khi xoay màn hình còn Activity thì có?
   Activity bị hủy và tạo lại: Xoay màn hình là một Configuration Change (thay đổi cấu hình thiết bị). Hệ thống Android phá hủy (destroy) instance Activity cũ và tạo (recreate) Activity mới để tải lại tài nguyên/layout phù hợp với hướng màn hình mới.
   ViewModel được giữ lại: ViewModel không gắn trực tiếp vào vòng đời ngắn hạn của Activity mà được lưu trữ bên trong ViewModelStore (quản lý bởi ViewModelStoreOwner). Khi xoay màn hình, ViewModelStore tồn tại xuyên suốt quá trình cấu hình thay đổi; Activity mới sau khi tạo lại sẽ lấy lại đúng ViewModel cũ. ViewModel chỉ bị hủy và gọi onCleared() khi Activity kết thúc thực sự (như nhấn Back hoặc finish()).
3. StateFlow khác Flow thường chỗ nào? Vì sao UI state nên dùng StateFlow?
   Khác biệt chính:Flow thường (Cold Flow): Chỉ bắt đầu chạy code bên trong khi có collector lắng nghe; mỗi lần có người collect mới thường kích hoạt lại luồng riêng và không lưu giữ giá trị hiện tại.StateFlow (Hot State-Holder Flow): Luôn giữ một giá trị trạng thái hiện tại (value) (bắt buộc có giá trị khởi tạo), phát giá trị mới nhất cho nhiều collector, và tự bỏ qua nếu giá trị mới trùng (equals) với giá trị cũ.
   Vì sao UI state nên dùng StateFlow:
   UI luôn cần biết "bây giờ màn hình đang ở trạng thái nào" để vẽ đúng ngay khi vừa mở màn hình hoặc vừa tái tạo sau xoay màn hình.
   Nhờ giữ giá trị mới nhất và lọc trùng lặp, collector mới gắn vào sẽ nhận ngay state hiện hành mà không phải gọi lại dữ liệu từ đầu hay recompose vô ích.
4. Vì sao expose ra ngoài là StateFlow (bất biến) còn MutableStateFlow giữ private?
   Đây là kỹ thuật Backing Property (private val _uiState = MutableStateFlow(...) và val uiState: StateFlow<...> = _uiState.asStateFlow()):
   Giữ ViewModel làm nguồn sự thật duy nhất (Single Source of Truth): Ngăn các lớp bên ngoài (như Composable UI) tự gán _uiState.value = ... làm sai lệch state hoặc tạo ra nhiều nơi cùng sửa dữ liệu.
 Đúng nguyên lý một chiều (UDF): UI chỉ đọc (StateFlow) để hiển thị và gửi event lên ViewModel khi có tương tác, giúp code dễ theo dõi và gỡ lỗi khi có bug.
5. Debounce giải quyết vấn đề gì? Tiết kiệm chi phí API như thế nào?
Vấn đề giải quyết: Khi người dùng gõ tìm kiếm liên tục từng ký tự trong thời gian ngắn, sự kiện thay đổi query bắn ra dồn dập. debounce(300) trì hoãn việc xử lý cho đến khi ngừng phát sự kiện mới đủ 300ms, giúp bỏ qua các từ khóa dở dang khi người dùng còn đang gõ.

Tiết kiệm khi API tính tiền theo lượt gọi:

Ví dụ người dùng gõ nhanh từ "batman" (6 ký tự, mỗi ký tự cách nhau < 300ms):

Không có debounce: Có thể phát sinh tới 6 lượt gọi cho các chuỗi "b", "ba", "bat", "batm", "batma", "batman".

Có debounce(300): Các chuỗi trung gian bị bỏ qua; chỉ khi ngừng gõ đủ 300ms ở "batman" mới gọi 1 lượt (tiết kiệm được 5 lượt trong tình huống đó).

Nhờ vậy, ứng dụng giảm đáng kể số lượng request tính phí trên API, giảm tải cho server và tiết kiệm pin/dữ liệu mạng cho người dùng.

Tuần 6
1. GET khác POST chỗ nào? Status 200, 401, 404, 500 nghĩa là gì? Key sai thì TMDB trả mã nào?
   GET vs POST:
   GET: Dùng để lấy/đọc dữ liệu từ server. Tham số thường truyền qua URL/query string và không có request body.
   POST: Dùng để gửi/tạo dữ liệu mới lên server (hoặc gửi dữ liệu xử lý như form/payload), thường mang dữ liệu trong request body.
   Ý nghĩa các HTTP Status Code:
   200 OK: Yêu cầu thành công, server đã xử lý và trả về dữ liệu phản hồi.
   401 Unauthorized: Chưa xác thực hoặc thông tin xác thực không hợp lệ (ví dụ thiếu/sai API key hoặc token).
   404 Not Found: Không tìm thấy tài nguyên/endpoint được yêu cầu trên server (ví dụ sai URL, sai ID phim, hoặc tài nguyên không tồn tại).
   500 Internal Server Error: Lỗi không mong muốn xảy ra ở phía server khi xử lý yêu cầu.
   Key sai thì TMDB trả mã nào: TMDB trả về 401 (401 Unauthorized).
2. Vì sao đổi Fake sang Remote mà UI và ViewModel hầu như không phải sửa?
   UI và ViewModel chỉ làm việc với interface của Repository và các model domain/UI chứ không phụ thuộc trực tiếp vào cách lấy dữ liệu bên dưới. Khi chuyển từ Fake sang Remote, ta chỉ cần cung cấp implementation dùng Retrofit/API (và map DTO sang domain model) thông qua tầng Data/DI. Nhờ vậy, hợp đồng dữ liệu trả về cho ViewModel và luồng state lên UI giữ nguyên, nên UI và ViewModel gần như không cần sửa.
3. Interceptor là gì? Ngoài logging còn dùng làm gì?
   Interceptor là thành phần nằm trên đường đi của request/response trong client mạng (như OkHttp) cho phép can thiệp, quan sát, chỉnh sửa hoặc chặn request trước khi gửi đi và response trước khi trả về.Ngoài logging còn dùng để làm gì:Tự động gắn api_key hoặc Authorization header cho mọi request ở một chỗ duy nhất thay vì lặp lại ở từng API call.Xử lý chung cho lỗi xác thực/token (ví dụ bắt 401 để refresh token hoặc điều hướng đăng nhập lại).Tùy chỉnh header/cache/timeout (ví dụ thêm Accept, User-Agent, hoặc ép chính sách cache cho từng loại request).
4. Vì sao ghép URL ảnh trong mapper chứ không phải trong composable?
   Giữ Composable sạch và chỉ lo hiển thị: UI chỉ cần nhận URL ảnh hoàn chỉnh đã sẵn sàng để tải/hiển thị, không phải biết quy tắc nối baseImageUrl + posterPath hay kích thước ảnh của API.
   Tránh lặp logic và dễ bảo trì: Nếu đổi domain/base URL ảnh hoặc đổi kích thước ảnh, bạn chỉ cần sửa ở một chỗ duy nhất trong mapper thay vì rà soát nhiều composable khác nhau.

Tuần 8
1. DI là gì? Giải thích bằng chính HomeViewModel trước và sau tuần này
Khái niệm DI (Dependency Injection - Tiêm phụ thuộc): Là nguyên lý thiết kế giúp một class không tự khởi tạo các đối tượng phụ thuộc (dependencies) bên trong nó, mà nhận các đối tượng này từ bên ngoài truyền vào. Cách làm này giúp code tách rời lỏng lẻo (loose coupling), dễ bảo trì và dễ kiểm thử (test).

Trước khi dùng DI:

HomeViewModel phải tự tạo hoặc nhận repository qua constructor có giá trị mặc định thủ công (ví dụ HomeViewModel(private val repository: MovieRepository = FakeMovieRepository())).   

Khi đổi từ FakeMovieRepository sang RemoteMovieRepository (hoặc thêm tham số như apiKey / apiService), bạn phải sửa ở nơi tạo ViewModel hoặc gán mặc định thủ công.

Sau khi dùng DI:

HomeViewModel chỉ khai báo những dependency nó cần qua constructor (ví dụ class HomeViewModel(private val repository: MovieRepository) : ViewModel()).

Container DI (như Koin hoặc Hilt) sẽ tự tạo và tiêm MovieRepository vào cho HomeViewModel, đồng thời ở màn hình Compose có thể lấy HomeViewModel thông qua DI (koinViewModel() hoặc Hilt) mà không tự khởi tạo repo bằng tay.   

2. @Provides khác @Binds? Khi nào dùng cái nào?
@Binds:

Dùng trên một hàm abstract bên trong một abstract class của module DI (như Hilt @Module).   

Dùng để ánh xạ một interface sang một class implementation cụ thể khi implementation đó đã được DI biết cách tạo (ví dụ có @Inject constructor).   

Kiểu trả về của hàm là interface, còn tham số duy nhất là class implementation.   


@Provides:

Dùng trên hàm có thân code bên trong object hoặc class của module.   

Dùng khi bạn phải tự viết logic khởi tạo đối tượng, đặc biệt với các class từ thư viện bên ngoài không gắn @Inject constructor được (như Retrofit, OkHttpClient, RoomDatabase) hoặc cần cấu hình builder.   

Khi nào dùng cái nào:

Dùng @Binds khi bind Interface -> Implementation cho các lớp trong dự án mà DI đã tạo được instance.   

Dùng @Provides khi cần tự tay build() / khởi tạo đối tượng (nhất là thư viện thứ 3 hoặc cấu hình tùy biến).   

3. @Singleton nghĩa là gì? Nếu Database không phải singleton thì chuyện gì xảy ra?
@Singleton nghĩa là chỉ tạo và dùng một instance duy nhất trong phạm vi tương ứng (ví dụ toàn ứng dụng / singleton scope) thay vì tạo mới mỗi lần được yêu cầu.   

Nếu Database không phải singleton thì chuyện gì xảy ra:

Mỗi lần tạo RoomDatabase là một thao tác khá nặng/tốn tài nguyên, việc sinh ra nhiều instance không cần thiết gây lãng phí bộ nhớ và tài nguyên.   

Có thể phát sinh xung đột/mất đồng bộ trạng thái giữa nhiều instance mở cùng lúc trên cơ sở dữ liệu (nhất là khi có tình huống truy cập đồng thời hoặc multi-process, nếu không quản lý đúng sẽ dễ gặp race condition hoặc trạng thái không nhất quán).   

4. cachedIn(viewModelScope) để làm gì? Bỏ đi thì xoay màn hình bị sao?
cachedIn(viewModelScope) để lưu trữ (cache) luồng PagingData trong phạm vi viewModelScope để giữ dữ liệu phân trang khi có thay đổi cấu hình hoặc khi collector mới đăng ký lắng nghe, tránh việc phải tải lại từ đầu một cách không cần thiết.

Bỏ đi thì xoay màn hình bị sao: Khi xoay màn hình (Configuration Change), Activity bị hủy và tạo lại. Nếu không có cachedIn(viewModelScope), luồng PagingData không được giữ cache sẵn trong scope của ViewModel mà có thể bị kích hoạt tải lại từ đầu / mất trạng thái phân trang đã tải trước đó khi UI kết nối lại sau xoay màn hình.

5. Trong PagingSource, nextKey = null mang ý nghĩa gì?
Trong LoadResult.Page(data = ..., prevKey = ..., nextKey = ...), nextKey là key dùng cho lần gọi load() tiếp theo để lấy các trang dữ liệu phía sau trang hiện tại.   

Khi nextKey = null, điều đó báo cho Paging biết là không còn trang dữ liệu nào phía sau nữa (đã chạm tới cuối danh sách/hết dữ liệu để phân trang), nên Paging sẽ không tiếp tục gọi load() để tải thêm trang sau nữa.
   Đảm bảo dữ liệu đầu vào đồng nhất: Model domain/UI nhận được giá trị chuẩn ngay từ tầng chuyển đổi dữ liệu, giúp tái sử dụng model ở nhiều màn hình hoặc test dễ dàng hơn.


Tuần 9

1. Vì sao ViewModel test được dễ? Kể đúng 2 quyết định kiến trúc từ các tuần trước
   ViewModel test được dễ là nhờ 2 quyết định kiến trúc làm từ các tuần trước:
   Phụ thuộc vào Interface (MovieRepository) thay vì gán cứng implementation cụ thể (như RemoteMovieRepository).
   Tiêm phụ thuộc qua constructor (Dependency Injection — DI), giúp test truyền thẳng FakeMovieRepository vào mà không cần gọi mạng thật hay khởi tạo tầng dữ liệu nặng.
   
2. Fake khác Mock thế nào?
   Trong nhóm Test Doubles (các đối tượng thay thế dependency thật khi test):
   Fake: Là một implementation có logic hoạt động đơn giản thật sự, nhưng rút gọn/giả lập cho môi trường test thay vì dùng hạ tầng thật (ví dụ FakeMovieRepository dùng danh sách có sẵn trong bộ nhớ và cho phép cài dữ liệu hoặc ra lệnh ném lỗi thay vì gọi API/DB thật).
    Mock: Là đối tượng giả lập được tạo ra (thường bằng framework/thư viện mock) để ghi nhận tương tác (được gọi hàm nào, với tham số gì, bao nhiêu lần) và cấu hình sẵn hành vi/giá trị trả về cho từng lời gọi.
   
3. runTest làm gì với delay? Vì sao test debounce không phải đợi 300ms thật?
   runTest làm gì với delay: runTest (từ kotlinx-coroutines-test) chạy test với thời gian ảo (virtual time) thông qua TestCoroutineScheduler. Khi gặp delay(...), thay vì ngủ theo đồng hồ thật của hệ điều hành, coroutine tạm dừng trên đồng hồ ảo và có thể được tua nhanh (advanceTimeBy(...) / advanceUntilIdle()).
   Vì sao test debounce không phải đợi 300ms thật: Vì thời gian 300ms trong debounce(300) nằm trên thời gian ảo của runTest. Bạn chỉ cần điều khiển/tua đồng hồ ảo qua mốc 300ms là logic debounce đã kích hoạt ngay lập tức, giúp bộ test chạy nhanh (toàn bộ unit test chạy gọn trong vài giây, không tốn thời gian chờ thật).
   
 4. Vì sao dùng WorkManager thay vì một coroutine chạy vòng lặp vô hạn?
    Vòng đời & sự bền vững: Coroutine thông thường (như trong scope của app/ViewModel) chỉ sống khi app/scope còn tồn tại — nếu người dùng kill app hoặc khởi động lại máy, coroutine bị hủy và vòng lặp biến mất. Ngược lại, WorkManager lưu trữ công việc bền vững và có thể sống sót qua việc kill app và khởi động lại thiết bị.
    Pin & tài nguyên hệ thống: Một vòng lặp vô hạn tự chạy liên tục dễ đốt pin, tốn CPU/mạng không kiểm soát khi app không dùng tới. WorkManager được Android điều phối và tôn trọng chế độ tiết kiệm pin cũng như các ràng buộc hệ thống (như có mạng, sạc pin...), rất phù hợp cho tác vụ nền định kỳ hoặc chạy ngay cả khi app đã đóng.

5. App bị kill thì PeriodicWork còn chạy không? Máy khởi động lại thì sao?
   Cả hai trường hợp đều CÒN CHẠY (khi điều kiện ràng buộc của công việc được đáp ứng):
   Khi app bị kill (bị đóng / bị hệ thống giết tiến trình): WorkManager lưu thông tin công việc bền vững vào hệ thống (database cục bộ của WorkManager) thay vì chỉ nằm trong bộ nhớ của tiến trình app. Do đó, khi app bị kill, PeriodicWork vẫn được giữ lại và tiếp tục chạy theo lịch khi tới thời điểm và thỏa mãn các ràng buộc (ví dụ có mạng).
   Khi máy khởi động lại (reboot): WorkManager được thiết kế để sống sót qua cả việc khởi động lại thiết bị. Sau khi máy bật lên và các điều kiện ràng buộc phù hợp, hệ thống vẫn tiếp tục lập lịch và chạy PeriodicWork như bình thường.  
