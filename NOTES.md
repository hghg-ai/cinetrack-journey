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
tuần 2
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
